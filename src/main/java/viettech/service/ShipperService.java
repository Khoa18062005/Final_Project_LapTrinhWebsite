package viettech.service;

import viettech.config.JPAConfig;
import viettech.dto.Shipper_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
            // Sử dụng LEFT JOIN FETCH để lấy đầy đủ thông tin liên quan
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

            // 3. Phân loại đơn hàng (Cập nhật logic trạng thái đầy đủ)
            List<DeliveryAssignment> pending = new ArrayList<>();
            List<DeliveryAssignment> ongoing = new ArrayList<>();
            List<DeliveryAssignment> history = new ArrayList<>();

            for (DeliveryAssignment da : allAssignments) {
                String status = da.getStatus() != null ? da.getStatus().trim() : "";

                // Nhóm Chờ nhận: Bao gồm cả Processing (Đang xử lý) và Assigned (Đã gán)
                if (status.equalsIgnoreCase("Assigned") || status.equalsIgnoreCase("Pending") || status.equalsIgnoreCase("Processing")) {
                    pending.add(da);
                }
                // Nhóm Đang thực hiện: Bao gồm Shipping (Đang giao), In Transit (Trên đường), Picking Up (Đang lấy)
                else if (status.equalsIgnoreCase("In Transit") || status.equalsIgnoreCase("Accepted")
                        || status.equalsIgnoreCase("Picking Up") || status.equalsIgnoreCase("Shipping")) {
                    ongoing.add(da);
                }
                // Nhóm Lịch sử: Đã xong
                else if (status.equalsIgnoreCase("Completed") || status.equalsIgnoreCase("Delivered")) {
                    history.add(da);
                }
            }

            dto.setPendingOrders(pending);
            dto.setOngoingOrders(ongoing);
            dto.setHistoryOrders(history);

            // ==========================================
            // 4. TÍNH TOÁN THỐNG KÊ & BIỂU ĐỒ
            // ==========================================

            // Query lấy dữ liệu tổng hợp theo ngày (Native Query)
            String sqlStats = "SELECT DATE(completed_at), SUM(earnings), COUNT(assignment_id) " +
                    "FROM delivery_assignments " +
                    "WHERE shipper_id = ? AND status = 'Completed' " +
                    "AND completed_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
                    "GROUP BY DATE(completed_at)";

            Query qStats = em.createNativeQuery(sqlStats);
            qStats.setParameter(1, shipperId);
            List<Object[]> results = qStats.getResultList();

            // Map dữ liệu vào HashMap để dễ xử lý
            Map<String, Double> dailyIncomeMap = new HashMap<>();

            double sumToday = 0; long countToday = 0;
            double sum7Days = 0; long count7Days = 0;
            double sumMonth = 0; long countMonth = 0;

            LocalDate now = LocalDate.now();
            LocalDate sevenDaysAgo = now.minusDays(6);
            LocalDate monthAgo = now.minusDays(29);

            for (Object[] row : results) {
                String dateStr = row[0].toString(); // yyyy-MM-dd
                double money = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
                long count = row[2] != null ? ((Number) row[2]).longValue() : 0;

                LocalDate rowDate = LocalDate.parse(dateStr);
                dailyIncomeMap.put(dateStr, money);

                // Cộng dồn các chỉ số
                if (rowDate.isEqual(now)) {
                    sumToday += money;
                    countToday += count;
                }
                if (!rowDate.isBefore(sevenDaysAgo)) { // Trong 7 ngày qua
                    sum7Days += money;
                    count7Days += count;
                }
                if (!rowDate.isBefore(monthAgo)) { // Trong 30 ngày qua
                    sumMonth += money;
                    countMonth += count;
                }
            }

            // Gán dữ liệu tổng hợp vào DTO
            dto.setTodayIncome(sumToday);
            dto.setSuccessOrderCount(countToday); // Dùng lại trường cũ cho Today

            dto.setIncome7Days(sum7Days);
            dto.setCount7Days(count7Days);

            dto.setIncomeMonth(sumMonth);
            dto.setCountMonth(countMonth);

            // Đếm số lượng đơn phân công hôm nay (bao gồm chưa xong)
            long assignedToday = allAssignments.stream()
                    .filter(a -> a.getAssignedAt() != null &&
                            a.getAssignedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(now))
                    .count();
            dto.setTodayOrderCount(assignedToday);


            // 5. CHUẨN BỊ DỮ LIỆU BIỂU ĐỒ (CHART DATA)

            // --- A. Biểu đồ 7 Ngày ---
            for (int i = 6; i >= 0; i--) {
                LocalDate d = now.minusDays(i);
                String key = d.toString();
                // Label: 27/12
                dto.getChartLabels7Days().add(d.getDayOfMonth() + "/" + d.getMonthValue());
                dto.getChartData7Days().add(dailyIncomeMap.getOrDefault(key, 0.0));
            }

            // --- B. Biểu đồ Tháng (4 Tuần) ---
            double[] weekSum = new double[4];
            String[] weekLabels = {"Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4"};

            for (int i = 0; i < 28; i++) { // Lấy 28 ngày gần nhất
                LocalDate d = now.minusDays(27 - i);
                String key = d.toString();
                double val = dailyIncomeMap.getOrDefault(key, 0.0);
                int weekIndex = i / 7;
                if(weekIndex < 4) weekSum[weekIndex] += val;
            }

            for(int i=0; i<4; i++) {
                dto.getChartLabelsMonth().add(weekLabels[i]);
                dto.getChartDataMonth().add(weekSum[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return dto;
    }

    /**
     * Xử lý nút bấm: Nhận đơn (accept) hoặc Hoàn thành (complete)
     */
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
                        da.getDelivery().setActualDelivery(new Date());

                        // Cập nhật Order -> Hoàn tất đơn hàng
                        if (da.getDelivery().getOrder() != null) {
                            da.getDelivery().getOrder().setStatus("Completed");
                            da.getDelivery().getOrder().setCompletedAt(new Date()); // Set ngày hoàn thành cho Order
                        }
                    }
                }
                // Lưu thay đổi vào DB
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