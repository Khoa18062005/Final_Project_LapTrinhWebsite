package viettech.service;

import viettech.config.JPAConfig;
import viettech.dto.Shipper_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction; // Thêm import này
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShipperService {

    // ... (Giữ nguyên hàm getDashboardData cũ của bạn ở đây) ...
    public Shipper_dto getDashboardData(int shipperId) {
        // Code cũ giữ nguyên
        // ...
        EntityManager em = JPAConfig.getEntityManager();
        Shipper_dto dto = new Shipper_dto();
        try {
            // ... (Logic lấy dữ liệu cũ) ...
            // Copy lại toàn bộ logic trong hàm getDashboardData ở tin nhắn trước

            // CODE TÓM TẮT ĐỂ BẠN DỄ NHÌN (Đừng copy dòng tóm tắt này, hãy giữ code cũ):
            Shipper shipper = em.find(Shipper.class, shipperId);
            dto.setShipperInfo(shipper);

            String jpql = "SELECT da FROM DeliveryAssignment da " +
                    "LEFT JOIN FETCH da.delivery d " +
                    "LEFT JOIN FETCH d.order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +
                    "LEFT JOIN FETCH d.warehouse w " +
                    "WHERE da.shipperId = :sid ORDER BY da.assignedAt DESC";

            TypedQuery<DeliveryAssignment> query = em.createQuery(jpql, DeliveryAssignment.class);
            query.setParameter("sid", shipperId);
            List<DeliveryAssignment> allAssignments = query.getResultList();

            // ... (Phân loại list pending/ongoing/history) ...
            List<DeliveryAssignment> pending = new ArrayList<>();
            List<DeliveryAssignment> ongoing = new ArrayList<>();
            List<DeliveryAssignment> history = new ArrayList<>();

            for (DeliveryAssignment da : allAssignments) {
                String status = da.getStatus() != null ? da.getStatus().trim() : "";
                if (status.equalsIgnoreCase("Assigned") || status.equalsIgnoreCase("Pending")) {
                    pending.add(da);
                } else if (status.equalsIgnoreCase("In Transit") || status.equalsIgnoreCase("Accepted") || status.equalsIgnoreCase("Picking Up")) {
                    ongoing.add(da);
                } else if (status.equalsIgnoreCase("Completed") || status.equalsIgnoreCase("Delivered")) {
                    history.add(da);
                }
            }
            dto.setPendingOrders(pending);
            dto.setOngoingOrders(ongoing);
            dto.setHistoryOrders(history);

            // ... (Tính toán thống kê) ...
            double dailyIncome = 0;
            long todayCount = 0;
            long successCount = 0;
            Date startOfDay = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            for (DeliveryAssignment da : allAssignments) {
                if (da.getAssignedAt() != null && da.getAssignedAt().after(startOfDay)) todayCount++;
                if ("Completed".equalsIgnoreCase(da.getStatus()) && da.getCompletedAt() != null && da.getCompletedAt().after(startOfDay)) {
                    successCount++;
                    dailyIncome += da.getEarnings();
                }
            }
            dto.setTodayOrderCount(todayCount);
            dto.setSuccessOrderCount(successCount);
            dto.setTodayIncome(dailyIncome);

        } catch (Exception e) { e.printStackTrace(); } finally { em.close(); }
        return dto;
    }

    // --- THÊM HÀM MỚI NÀY ---
    public void updateStatus(int assignmentId, String action) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            DeliveryAssignment da = em.find(DeliveryAssignment.class, assignmentId);

            if (da != null) {
                if ("accept".equals(action)) {
                    // 1. NHẬN ĐƠN
                    da.setStatus("Accepted");
                    da.setAcceptedAt(new Date());

                    // Cập nhật bảng Delivery -> Đang đi lấy hàng
                    if (da.getDelivery() != null) {
                        da.getDelivery().setStatus("Picking Up");
                    }

                } else if ("complete".equals(action)) {
                    // 2. HOÀN THÀNH
                    da.setStatus("Completed");
                    da.setCompletedAt(new Date());

                    // Cập nhật Delivery -> Đã giao
                    if (da.getDelivery() != null) {
                        da.getDelivery().setStatus("Delivered");
                        da.getDelivery().setActualDelivery(new Date()); // Ghi nhận thời gian giao thực tế

                        // Cập nhật Order -> Hoàn tất đơn hàng
                        if (da.getDelivery().getOrder() != null) {
                            da.getDelivery().getOrder().setStatus("Completed");
                            da.getDelivery().getOrder().setCompletedAt(new Date());
                        }
                    }
                }
                // Lưu thay đổi
                em.merge(da);
            }
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}