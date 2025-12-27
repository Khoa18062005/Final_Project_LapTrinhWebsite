package viettech.service;

import viettech.config.JPAConfig;
import viettech.dto.Shipper_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ShipperService {

    public Shipper_dto getDashboardData(int shipperId) {
        EntityManager em = JPAConfig.getEntityManager();
        Shipper_dto dto = new Shipper_dto();

        try {
            // 1. Lấy thông tin Shipper
            Shipper shipper = em.find(Shipper.class, shipperId);
            if (shipper == null) return null;
            dto.setShipperInfo(shipper);

            // 2. Lấy danh sách nhiệm vụ giao hàng (Assignments)
            // Lấy tất cả đơn hàng được phân công cho shipper này
            String jpql = "SELECT da FROM DeliveryAssignment da " +
                    "JOIN FETCH da.delivery d " +
                    "JOIN FETCH d.order o " +
                    "JOIN FETCH o.customer c " +
                    "JOIN FETCH d.warehouse w " +
                    "WHERE da.shipperId = :sid ORDER BY da.assignedAt DESC";

            TypedQuery<DeliveryAssignment> query = em.createQuery(jpql, DeliveryAssignment.class);
            query.setParameter("sid", shipperId);
            List<DeliveryAssignment> allAssignments = query.getResultList();

            // 3. Phân loại đơn hàng & Tính toán
            // Lấy ngày hôm nay (để tính thu nhập trong ngày)
            LocalDate today = LocalDate.now();
            Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

            double dailyIncome = 0;
            long todayCount = 0;
            long successCount = 0;

            // Stream API để lọc danh sách (Java 8+)
            // a. Đơn chờ nhận (Pending)
            dto.setPendingOrders(allAssignments.stream()
                    .filter(a -> "Assigned".equalsIgnoreCase(a.getStatus()) || "Pending".equalsIgnoreCase(a.getStatus()))
                    .toList()); // Hoặc .collect(Collectors.toList()) nếu Java < 16

            // b. Đơn đang giao (Accepted/In Transit)
            dto.setOngoingOrders(allAssignments.stream()
                    .filter(a -> "Accepted".equalsIgnoreCase(a.getStatus()) || "In Transit".equalsIgnoreCase(a.getStatus()) || "Picking Up".equalsIgnoreCase(a.getStatus()))
                    .toList());

            // c. Lịch sử / Hoàn thành
            dto.setHistoryOrders(allAssignments.stream()
                    .filter(a -> "Completed".equalsIgnoreCase(a.getStatus()) || "Delivered".equalsIgnoreCase(a.getStatus()))
                    .toList());

            // 4. Tính toán thống kê
            for (DeliveryAssignment da : allAssignments) {
                // Nếu đơn được phân công hôm nay
                if (da.getAssignedAt() != null && da.getAssignedAt().after(startOfDay)) {
                    todayCount++;
                }

                // Nếu hoàn thành hôm nay
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