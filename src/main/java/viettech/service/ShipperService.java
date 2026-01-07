package viettech.service;

import org.mindrot.jbcrypt.BCrypt;
import viettech.config.JPAConfig;
import viettech.dto.Shipper_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;
import viettech.entity.user.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
            if (shipper == null) {
                System.out.println("DEBUG: Không tìm thấy Shipper với ID: " + shipperId);
                return null;
            }
            dto.setShipperInfo(shipper);

            // 2. Lấy danh sách nhiệm vụ giao hàng
            // Sử dụng JOIN FETCH để lấy luôn dữ liệu liên quan, tránh lỗi Lazy Loading
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

            // --- LOG DEBUG QUAN TRỌNG ---
            System.out.println("================ SHIPPER DASHBOARD DEBUG ================");
            System.out.println("Shipper ID: " + shipperId);
            System.out.println("Tổng số đơn tìm thấy trong DB: " + allAssignments.size());

            // 3. Phân loại đơn hàng
            List<DeliveryAssignment> pending = new ArrayList<>();
            List<DeliveryAssignment> ongoing = new ArrayList<>();
            List<DeliveryAssignment> history = new ArrayList<>();

            for (DeliveryAssignment da : allAssignments) {
                // Kiểm tra an toàn để tránh NullPointerException
                if (da.getDelivery() == null || da.getDelivery().getOrder() == null) {
                    System.out.println("WARNING: Đơn Assignment ID " + da.getAssignmentId() + " bị lỗi dữ liệu (thiếu Delivery hoặc Order). Bỏ qua.");
                    continue;
                }

                String status = (da.getStatus() != null) ? da.getStatus().trim() : "";
                String orderStatus = (da.getDelivery().getOrder().getStatus() != null) ? da.getDelivery().getOrder().getStatus().trim() : "";

                System.out.println("-> Đơn #" + da.getDelivery().getOrder().getOrderNumber() +
                        " | AssignStatus: " + status +
                        " | OrderStatus: " + orderStatus);

                // --- NHÓM 1: CHỜ NHẬN (Pending) ---
                if (containsIgnoreCase(status, "Assigned", "Pending", "Processing", "Ready", "Created")) {
                    pending.add(da);
                }
                // --- NHÓM 2: ĐANG THỰC HIỆN (Ongoing) ---
                else if (containsIgnoreCase(status, "Accepted", "Picking Up", "In Transit", "Shipping", "On Delivery")) {
                    ongoing.add(da);
                }
                // --- NHÓM 3: LỊCH SỬ (History) ---
                else if (containsIgnoreCase(status, "Completed", "Delivered", "Cancelled", "Returned", "Fail", "Success")) {
                    history.add(da);
                }
                // --- TRƯỜNG HỢP LẠ (Fallback) ---
                else {
                    // Nếu trạng thái Shipper lạ, thử check theo trạng thái Order
                    if (containsIgnoreCase(orderStatus, "Shipping", "In Transit")) {
                        ongoing.add(da);
                    } else if (containsIgnoreCase(orderStatus, "Completed", "Delivered")) {
                        history.add(da);
                    } else {
                        // Mặc định ném vào pending để Shipper thấy
                        System.out.println("   (Trạng thái lạ -> Chuyển vào Pending)");
                        pending.add(da);
                    }
                }
            }

            System.out.println("Kết quả phân loại: Pending=" + pending.size() + ", Ongoing=" + ongoing.size() + ", History=" + history.size());
            System.out.println("=========================================================");

            dto.setPendingOrders(pending);
            dto.setOngoingOrders(ongoing);
            dto.setHistoryOrders(history);

            // 4. Tính toán thống kê & Biểu đồ
            calculateStatistics(em, shipperId, dto, allAssignments);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return dto;
    }

    private boolean containsIgnoreCase(String source, String... keywords) {
        if (source == null) return false;
        for (String keyword : keywords) {
            if (source.trim().equalsIgnoreCase(keyword)) return true;
        }
        return false;
    }

    private void calculateStatistics(EntityManager em, int shipperId, Shipper_dto dto, List<DeliveryAssignment> allAssignments) {
        try {
            // Query lấy dữ liệu tổng hợp theo ngày
            String sqlStats = "SELECT DATE(completed_at), SUM(earnings), COUNT(assignment_id) " +
                    "FROM delivery_assignments " +
                    "WHERE shipper_id = ? AND (status = 'Completed' OR status = 'Delivered') " +
                    "AND completed_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
                    "GROUP BY DATE(completed_at)";

            Query qStats = em.createNativeQuery(sqlStats);
            qStats.setParameter(1, shipperId);
            List<Object[]> results = qStats.getResultList();

            Map<String, Double> dailyIncomeMap = new HashMap<>();
            double sumToday = 0; long countToday = 0;
            double sum7Days = 0; long count7Days = 0;
            double sumMonth = 0; long countMonth = 0;

            LocalDate now = LocalDate.now();
            LocalDate sevenDaysAgo = now.minusDays(6);
            LocalDate monthAgo = now.minusDays(29);

            for (Object[] row : results) {
                if (row[0] == null) continue;
                String dateStr = row[0].toString();
                double money = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
                long count = row[2] != null ? ((Number) row[2]).longValue() : 0;

                LocalDate rowDate = LocalDate.parse(dateStr);
                dailyIncomeMap.put(dateStr, money);

                if (rowDate.isEqual(now)) {
                    sumToday += money;
                    countToday += count;
                }
                if (!rowDate.isBefore(sevenDaysAgo)) {
                    sum7Days += money;
                    count7Days += count;
                }
                if (!rowDate.isBefore(monthAgo)) {
                    sumMonth += money;
                    countMonth += count;
                }
            }

            dto.setTodayIncome(sumToday);
            dto.setSuccessOrderCount(countToday);
            dto.setIncome7Days(sum7Days);
            dto.setCount7Days(count7Days);
            dto.setIncomeMonth(sumMonth);
            dto.setCountMonth(countMonth);

            long assignedToday = allAssignments.stream()
                    .filter(a -> a.getAssignedAt() != null &&
                            a.getAssignedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(now))
                    .count();
            dto.setTodayOrderCount(assignedToday);

            // A. Biểu đồ 7 Ngày
            for (int i = 6; i >= 0; i--) {
                LocalDate d = now.minusDays(i);
                String key = d.toString();
                dto.getChartLabels7Days().add(d.getDayOfMonth() + "/" + d.getMonthValue());
                dto.getChartData7Days().add(dailyIncomeMap.getOrDefault(key, 0.0));
            }

            // B. Biểu đồ Tháng (4 Tuần)
            double[] weekSum = new double[4];
            String[] weekLabels = {"Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4"};

            for (int i = 0; i < 28; i++) {
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
            System.out.println("ERROR calculating statistics: " + e.getMessage());
        }
    }

    public void updateStatus(int assignmentId, String action) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            DeliveryAssignment da = em.find(DeliveryAssignment.class, assignmentId);
            if (da != null) {
                if ("accept".equals(action)) {
                    da.setStatus("In Transit"); // Đổi thành In Transit cho khớp logic
                    da.setAcceptedAt(new Date());
                    da.setStartedAt(new Date()); // Ghi nhận bắt đầu luôn
                    if (da.getDelivery() != null) da.getDelivery().setStatus("In Transit");
                } else if ("complete".equals(action)) {
                    da.setStatus("Completed");
                    da.setCompletedAt(new Date());
                    if (da.getDelivery() != null) {
                        da.getDelivery().setStatus("Delivered");
                        da.getDelivery().setActualDelivery(new Date());
                        if (da.getDelivery().getOrder() != null) {
                            da.getDelivery().getOrder().setStatus("Completed");
                            da.getDelivery().getOrder().setCompletedAt(new Date());
                        }
                    }
                }
                em.merge(da);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void updateProfile(int userId, String firstName, String lastName, String phone, String password, String vehiclePlate, String licenseNumber, String avatarUrl) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();

            // 1. Cập nhật bảng USERS (Thông tin chung)
            User user = em.find(User.class, userId);
            if (user != null) {
                if (firstName != null) user.setFirstName(firstName.trim());
                if (lastName != null) user.setLastName(lastName.trim());
                if (phone != null) user.setPhone(phone.trim());

                // Xử lý Avatar:
                // - Nếu null: Không làm gì (giữ ảnh cũ)
                // - Nếu rỗng "": Xóa ảnh (set null)
                // - Nếu có link: Cập nhật ảnh mới
                if (avatarUrl != null) {
                    if (avatarUrl.trim().isEmpty()) {
                        user.setAvatar(null);
                    } else {
                        user.setAvatar(avatarUrl);
                    }
                }

                if (password != null && !password.trim().isEmpty()) {
                    String pwd = password.trim();
                    if (pwd.startsWith("$2a$") || pwd.startsWith("$2b$")) {
                        user.setPassword(pwd);
                    } else {
                        user.setPassword(BCrypt.hashpw(pwd, BCrypt.gensalt(12)));
                    }
                }
                em.merge(user);
            }

            // 2. Cập nhật bảng SHIPPERS bằng SQL THUẦN
            String sql = "UPDATE shippers SET vehicle_plate = ?, license_number = ? WHERE user_id = ?";
            Query query = em.createNativeQuery(sql);

            String vPlate = (vehiclePlate != null) ? vehiclePlate.trim() : "";
            String lNum = (licenseNumber != null) ? licenseNumber.trim() : "";

            query.setParameter(1, vPlate);
            query.setParameter(2, lNum);
            query.setParameter(3, userId);

            int rows = query.executeUpdate();
            System.out.println("DEBUG: Updated Shipper Info. Rows affected: " + rows);

            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}