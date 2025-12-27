package viettech.service;

import viettech.dto.Shipper_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.review.Review;
import viettech.entity.user.Shipper;
import viettech.config.JPAConfig;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShipperService {

    /**
     * Lấy toàn bộ dữ liệu cần thiết cho trang Dashboard của Shipper
     */
    public Shipper_dto getDashboardData(int shipperId) {
        EntityManager em = JPAConfig.getEntityManager();
        Shipper_dto dto = new Shipper_dto();

        try {
            // 1. Lấy thông tin chi tiết của Shipper (bao gồm cả dữ liệu từ bảng users)
            Shipper shipper = em.find(Shipper.class, shipperId);
            dto.setShipper(shipper);

            // Xác định mốc thời gian bắt đầu ngày hôm nay (00:00:00) để thống kê
            Date startOfDay = getStartOfDay();

            // 2. Thống kê số đơn hàng được phân công hôm nay
            TypedQuery<Long> countTodayQuery = em.createQuery(
                    "SELECT COUNT(da) FROM DeliveryAssignment da " +
                            "WHERE da.shipperId = :id AND da.assignedAt >= :start", Long.class);
            countTodayQuery.setParameter("id", shipperId);
            countTodayQuery.setParameter("start", startOfDay);
            dto.setTodayOrdersCount(countTodayQuery.getSingleResult());

            // 3. Thống kê số đơn thành công & Tổng thu nhập hôm nay
            TypedQuery<Object[]> statsQuery = em.createQuery(
                    "SELECT COUNT(da), SUM(da.earnings) FROM DeliveryAssignment da " +
                            "WHERE da.shipperId = :id AND da.status = 'Completed' AND da.completedAt >= :start", Object[].class);
            statsQuery.setParameter("id", shipperId);
            statsQuery.setParameter("start", startOfDay);

            Object[] stats = statsQuery.getSingleResult();
            dto.setSuccessOrdersCount(stats[0] != null ? (Long) stats[0] : 0L);
            dto.setTodayIncome(stats[1] != null ? (Double) stats[1] : 0.0);

            // 4. Lấy đơn hàng ĐANG GIAO (Ongoing) - Thường chỉ có 1 đơn tại 1 thời điểm
            TypedQuery<DeliveryAssignment> activeQuery = em.createQuery(
                    "SELECT da FROM DeliveryAssignment da " +
                            "JOIN FETCH da.delivery d JOIN FETCH d.order o " +
                            "WHERE da.shipperId = :id AND da.status = 'Ongoing'", DeliveryAssignment.class);
            activeQuery.setParameter("id", shipperId);
            activeQuery.setMaxResults(1);
            List<DeliveryAssignment> activeResults = activeQuery.getResultList();
            if (!activeResults.isEmpty()) {
                dto.setActiveDelivery(activeResults.get(0));
            }

            // 5. Lấy danh sách ĐƠN HÀNG CHỜ NHẬN (Assigned)
            TypedQuery<DeliveryAssignment> pendingQuery = em.createQuery(
                    "SELECT da FROM DeliveryAssignment da " +
                            "JOIN FETCH da.delivery d JOIN FETCH d.order o " +
                            "WHERE da.shipperId = :id AND da.status = 'Assigned' " +
                            "ORDER BY da.assignedAt DESC", DeliveryAssignment.class);
            pendingQuery.setParameter("id", shipperId);
            pendingQuery.setMaxResults(5); // Lấy 5 đơn mới nhất
            dto.setPendingOrders(pendingQuery.getResultList());

            // 6. Lấy các đánh giá gần đây của Shipper
            TypedQuery<Review> reviewQuery = em.createQuery(
                    "SELECT r FROM Review r WHERE r.productId IN " +
                            "(SELECT od.productId FROM OrderDetail od WHERE od.orderId IN " +
                            "(SELECT d.orderId FROM Delivery d JOIN DeliveryAssignment da ON d.deliveryId = da.deliveryId " +
                            "WHERE da.shipperId = :id)) ORDER BY r.reviewDate DESC", Review.class);
            // Lưu ý: Query review có thể phức tạp hơn tùy vào cách bạn liên kết bảng đánh giá shipper
            // Ở đây tạm lấy review liên quan đến các đơn shipper này từng giao
            reviewQuery.setParameter("id", shipperId);
            reviewQuery.setMaxResults(3);
            dto.setRecentReviews(reviewQuery.getResultList());

            // Giả lập thời gian trung bình (Tú có thể viết logic tính toán từ lịch sử)
            dto.setAvgTime("28 phút");

            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Hàm hỗ trợ lấy mốc 00:00:00 của ngày hiện tại
     */
    private Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}