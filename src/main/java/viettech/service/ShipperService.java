package viettech.service;

import org.mindrot.jbcrypt.BCrypt;
import viettech.dao.ShipperDAO;
import viettech.dto.Shipper_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;
import viettech.entity.user.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ShipperService {

    // Khởi tạo DAO
    private final ShipperDAO shipperDAO = new ShipperDAO();
    private final ShipperNotificationService shipperNotificationService = new ShipperNotificationService();

    public Shipper_dto getDashboardData(int shipperId) {
        Shipper_dto dto = new Shipper_dto();

        Shipper shipper = shipperDAO.findById(shipperId);
        if (shipper == null) return null;
        dto.setShipperInfo(shipper);

        List<DeliveryAssignment> pending = new ArrayList<>();
        List<DeliveryAssignment> ongoing = new ArrayList<>();
        List<DeliveryAssignment> history = new ArrayList<>();

        // 1. SÀN ĐƠN HÀNG
        List<DeliveryAssignment> marketAssignments = shipperDAO.getAvailableAssignments();
        if (marketAssignments != null) {
            for (DeliveryAssignment da : marketAssignments) {
                if (da.getDelivery() != null && da.getDelivery().getOrder() != null) {
                    // NẾU MUỐN HIỆN TẤT CẢ (KỂ CẢ CỦA MÌNH):
                    pending.add(da);
                }
            }
        }

        // 2. ĐƠN CỦA TÔI
        List<DeliveryAssignment> myAssignments = shipperDAO.getAssignmentsByShipperId(shipperId);
        if (myAssignments != null) {
            for (DeliveryAssignment da : myAssignments) {
                if (da.getDelivery() == null || da.getDelivery().getOrder() == null) continue;

                String status = (da.getStatus() != null) ? da.getStatus().trim() : "";

                if (containsIgnoreCase(status, "Accepted", "Picking Up", "In Transit", "Shipping", "On Delivery")) {
                    ongoing.add(da);
                } else if (containsIgnoreCase(status, "Completed", "Delivered", "Cancelled", "Fail", "Success")) {
                    history.add(da);
                }
            }
        }

        dto.setPendingOrders(pending);
        dto.setOngoingOrders(ongoing);
        dto.setHistoryOrders(history);

        calculateStatistics(shipperId, dto, myAssignments);
        return dto;
    }

    private boolean containsIgnoreCase(String source, String... keywords) {
        if (source == null) return false;
        for (String keyword : keywords) {
            if (source.trim().equalsIgnoreCase(keyword)) return true;
        }
        return false;
    }

    private void calculateStatistics(int shipperId, Shipper_dto dto, List<DeliveryAssignment> allAssignments) {
        try {
            // Lấy dữ liệu thô từ DAO (SQL Native)
            List<Object[]> results = shipperDAO.getIncomeStatistics(shipperId);

            Map<String, Double> dailyIncomeMap = new HashMap<>();
            double sumToday = 0;
            long countToday = 0;
            double sum7Days = 0;
            long count7Days = 0;
            double sumMonth = 0;
            long countMonth = 0;

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

                // Tính tổng các mốc thời gian
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

            // Set dữ liệu vào DTO
            dto.setTodayIncome(sumToday);
            dto.setSuccessOrderCount(countToday);
            dto.setIncome7Days(sum7Days);
            dto.setCount7Days(count7Days);
            dto.setIncomeMonth(sumMonth);
            dto.setCountMonth(countMonth);

            // Tính số đơn được giao hôm nay (Assigned Today)
            long assignedToday = allAssignments.stream()
                    .filter(a -> a.getAssignedAt() != null &&
                            a.getAssignedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(now))
                    .count();
            dto.setTodayOrderCount(assignedToday);

            // Chuẩn bị dữ liệu cho Biểu đồ
            // 1. Biểu đồ 7 ngày
            for (int i = 6; i >= 0; i--) {
                LocalDate d = now.minusDays(i);
                dto.getChartLabels7Days().add(d.getDayOfMonth() + "/" + d.getMonthValue());
                dto.getChartData7Days().add(dailyIncomeMap.getOrDefault(d.toString(), 0.0));
            }

            // 2. Biểu đồ Tháng (4 tuần)
            double[] weekSum = new double[4];
            String[] weekLabels = {"Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4"};
            for (int i = 0; i < 28; i++) {
                LocalDate d = now.minusDays(27 - i);
                double val = dailyIncomeMap.getOrDefault(d.toString(), 0.0);
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

    public void updateStatus(int assignmentId, String action, int currentShipperId) {
        DeliveryAssignment da = shipperDAO.findAssignmentById(assignmentId);

        if (da != null) {
            if ("accept".equals(action)) {
                // Nhận đơn: Chỉ cần status Ready
                if ("Ready".equalsIgnoreCase(da.getStatus())) {

                    // SỬA LỖI: Gán trực tiếp int
                    da.setShipperId(currentShipperId);
                    da.setStatus("In Transit");

                    da.setAcceptedAt(new Date());
                    da.setAssignedAt(new Date());
                    da.setStartedAt(new Date());

                    if (da.getDelivery() != null) {
                        da.getDelivery().setStatus("In Transit");
                    }
                    System.out.println("DEBUG: Shipper " + currentShipperId + " nhận đơn " + assignmentId);

                    // Gửi thông báo cho shipper khi nhận đơn
                    try {
                        String orderNumber = da.getDelivery() != null && da.getDelivery().getOrder() != null
                            ? da.getDelivery().getOrder().getOrderNumber() : "N/A";
                        String customerName = "";
                        String address = "";
                        int customerId = 0;

                        if (da.getDelivery() != null && da.getDelivery().getOrder() != null) {
                            if (da.getDelivery().getOrder().getCustomer() != null) {
                                customerName = da.getDelivery().getOrder().getCustomer().getLastName() + " "
                                    + da.getDelivery().getOrder().getCustomer().getFirstName();
                                customerId = da.getDelivery().getOrder().getCustomer().getUserId();
                            }
                            if (da.getDelivery().getOrder().getAddress() != null) {
                                address = da.getDelivery().getOrder().getAddress().getStreet() + ", "
                                    + da.getDelivery().getOrder().getAddress().getDistrict();
                            }
                        }

                        // Thông báo cho shipper
                        shipperNotificationService.notifyOrderAccepted(currentShipperId, orderNumber, customerName, address);

                        // Thông báo cho khách hàng
                        if (customerId > 0) {
                            Shipper shipper = shipperDAO.findById(currentShipperId);
                            String shipperName = shipper != null
                                ? shipper.getLastName() + " " + shipper.getFirstName()
                                : "Shipper";
                            shipperNotificationService.notifyCustomerOrderPickedUp(customerId, orderNumber, shipperName);
                        }
                    } catch (Exception e) {
                        System.out.println("Lỗi gửi thông báo nhận đơn: " + e.getMessage());
                    }
                }

            } else if ("complete".equals(action)) {
                // SỬA LỖI: So sánh int dùng == (không dùng .equals)
                if (da.getShipperId() == currentShipperId) {
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

                    // Gửi thông báo cho shipper khi hoàn thành đơn
                    try {
                        String orderNumber = da.getDelivery() != null && da.getDelivery().getOrder() != null
                            ? da.getDelivery().getOrder().getOrderNumber() : "N/A";
                        double earnings = da.getEarnings();
                        int customerId = 0;

                        if (da.getDelivery() != null && da.getDelivery().getOrder() != null
                            && da.getDelivery().getOrder().getCustomer() != null) {
                            customerId = da.getDelivery().getOrder().getCustomer().getUserId();
                        }

                        // Thông báo cho shipper
                        shipperNotificationService.notifyOrderCompleted(currentShipperId, orderNumber, earnings);

                        // Thông báo cho khách hàng
                        if (customerId > 0) {
                            shipperNotificationService.notifyCustomerOrderDelivered(customerId, orderNumber);
                        }
                    } catch (Exception e) {
                        System.out.println("Lỗi gửi thông báo hoàn thành: " + e.getMessage());
                    }
                } else {
                    System.out.println("Lỗi: Shipper ID không khớp (" + da.getShipperId() + " != " + currentShipperId + ")");
                }
            }
            shipperDAO.updateAssignment(da);
        }
    }

    public void updateProfile(int userId, String firstName, String lastName, String phone, String password, String vehiclePlate, String licenseNumber, String avatarUrl) {
        // 1. Tìm User hiện tại
        Shipper shipper = shipperDAO.findById(userId);

        if (shipper != null) {
            // Cập nhật thông tin cơ bản
            if (firstName != null && !firstName.isEmpty()) shipper.setFirstName(firstName.trim());
            if (lastName != null && !lastName.isEmpty()) shipper.setLastName(lastName.trim());
            if (phone != null && !phone.isEmpty()) shipper.setPhone(phone.trim());

            // Xử lý Avatar:
            // - null: giữ nguyên
            // - "": xóa ảnh (set null)
            // - "url": cập nhật
            if (avatarUrl != null) {
                if (avatarUrl.trim().isEmpty()) {
                    shipper.setAvatar(null);
                } else {
                    shipper.setAvatar(avatarUrl);
                }
            }

            // Xử lý Password (Mã hóa nếu có thay đổi)
            if (password != null && !password.trim().isEmpty()) {
                String pwd = password.trim();
                // Kiểm tra xem pass đã mã hóa chưa để tránh mã hóa 2 lần
                if (pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$")) {
                    shipper.setPassword(pwd);
                } else {
                    shipper.setPassword(BCrypt.hashpw(pwd, BCrypt.gensalt(12)));
                }
            }

            // Lưu User/Shipper info qua JPA
            shipperDAO.update(shipper);

            // 2. Cập nhật thông tin xe cộ (Biển số, GPLX) bằng Native Query trong DAO
            String vPlate = (vehiclePlate != null) ? vehiclePlate.trim() : "";
            String lNum = (licenseNumber != null) ? licenseNumber.trim() : "";

            shipperDAO.updateVehicleInfo(userId, vPlate, lNum);
        }
    }

    // Thêm hàm updateLocation để phục vụ Servlet
    public void updateLocation(int shipperId, double lat, double lon) {
        shipperDAO.updateCurrentLocation(shipperId, lat, lon);
    }
}