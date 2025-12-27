package viettech.service;

import viettech.config.JPAConfig;
import viettech.dto.Shipper_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ShipperService {

    public Shipper_dto getDashboardData(int shipperId) {
        EntityManager em = JPAConfig.getEntityManager();
        Shipper_dto dto = new Shipper_dto();

        try {
            // 1. Lấy thông tin Shipper
            Shipper shipper = em.find(Shipper.class, shipperId);
            if (shipper == null) return null;
            dto.setShipperInfo(shipper);

            // 2. QUERY QUAN TRỌNG: Dùng LEFT JOIN FETCH để lấy đầy đủ thông tin liên quan
            // Lưu ý: Đã thêm 'o.address' vào để JSP hiển thị được địa chỉ
            String jpql = "SELECT da FROM DeliveryAssignment da " +
                    "LEFT JOIN FETCH da.delivery d " +
                    "LEFT JOIN FETCH d.order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.address a " +  // <-- THÊM DÒNG NÀY
                    "LEFT JOIN FETCH d.warehouse w " +
                    "WHERE da.shipperId = :sid " +
                    "ORDER BY da.assignedAt DESC";

            TypedQuery<DeliveryAssignment> query = em.createQuery(jpql, DeliveryAssignment.class);
            query.setParameter("sid", shipperId);

            List<DeliveryAssignment> allAssignments = query.getResultList();

            // Debug: In ra log để kiểm tra xem có lấy được dữ liệu không
            System.out.println("Shipper ID: " + shipperId + " - Tổng đơn tìm thấy: " + allAssignments.size());

            // 3. Phân loại đơn hàng (Dùng String check đơn giản cho chắc chắn)
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

            // 4. Tính toán thống kê đơn giản
            double dailyIncome = 0;
            long todayCount = 0;
            long successCount = 0;

            // Lấy mốc thời gian bắt đầu ngày hôm nay
            Date startOfDay = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            for (DeliveryAssignment da : allAssignments) {
                // Đếm đơn hôm nay
                if (da.getAssignedAt() != null && da.getAssignedAt().after(startOfDay)) {
                    todayCount++;
                }
                // Tính tiền đơn hoàn thành hôm nay
                if ("Completed".equalsIgnoreCase(da.getStatus()) && da.getCompletedAt() != null && da.getCompletedAt().after(startOfDay)) {
                    successCount++;
                    dailyIncome += da.getEarnings();
                }
            }

            dto.setTodayOrderCount(todayCount);
            dto.setSuccessOrderCount(successCount);
            dto.setTodayIncome(dailyIncome);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return dto;
    }
}