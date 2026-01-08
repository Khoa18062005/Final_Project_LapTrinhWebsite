package viettech.service;

import org.mindrot.jbcrypt.BCrypt;
import viettech.dao.ShipperDAO;
import viettech.dto.Shipper_dto;
import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.order.Order;
import viettech.entity.user.Shipper;

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

        // 1. SÀN ĐƠN HÀNG - Đơn có status = Ready (chờ shipper nhận)
        List<Order> pendingOrders = shipperDAO.getOrdersByStatusReady();
        dto.setPendingOrders(pendingOrders != null ? pendingOrders : new ArrayList<>());

        // 2. ĐƠN ĐANG GIAO - Đơn có status = Shipping (của shipper này)
        List<Order> ongoingOrders = shipperDAO.getOrdersByStatusShipping(shipperId);
        dto.setOngoingOrders(ongoingOrders != null ? ongoingOrders : new ArrayList<>());

        // 3. LỊCH SỬ - Đơn có status = Completed (của shipper này)
        List<Order> historyOrders = shipperDAO.getOrdersByStatusCompleted(shipperId);
        dto.setHistoryOrders(historyOrders != null ? historyOrders : new ArrayList<>());

        // Tính thống kê
        calculateStatisticsFromOrders(shipperId, dto, historyOrders);

        return dto;
    }

    private void calculateStatisticsFromOrders(int shipperId, Shipper_dto dto, List<Order> completedOrders) {
        try {
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

            if (completedOrders != null) {
                for (Order order : completedOrders) {
                    if (order.getCompletedAt() == null) continue;

                    LocalDate completedDate = order.getCompletedAt().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    double income = order.getShippingFee(); // Thu nhập = phí ship

                    String dateStr = completedDate.toString();
                    dailyIncomeMap.merge(dateStr, income, Double::sum);

                    // Tính tổng các mốc thời gian
                    if (completedDate.isEqual(now)) {
                        sumToday += income;
                        countToday++;
                    }
                    if (!completedDate.isBefore(sevenDaysAgo)) {
                        sum7Days += income;
                        count7Days++;
                    }
                    if (!completedDate.isBefore(monthAgo)) {
                        sumMonth += income;
                        countMonth++;
                    }
                }
            }

            // Set dữ liệu vào DTO
            dto.setTodayIncome(sumToday);
            dto.setSuccessOrderCount(countToday);
            dto.setIncome7Days(sum7Days);
            dto.setCount7Days(count7Days);
            dto.setIncomeMonth(sumMonth);
            dto.setCountMonth(countMonth);

            // Tính số đơn được giao hôm nay
            dto.setTodayOrderCount(countToday);

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
                if (weekIndex < 4) weekSum[weekIndex] += val;
            }
            for (int i = 0; i < 4; i++) {
                dto.getChartLabelsMonth().add(weekLabels[i]);
                dto.getChartDataMonth().add(weekSum[i]);
            }

        } catch (Exception e) {
            System.out.println("ERROR calculating statistics: " + e.getMessage());
        }
    }

    public void updateStatus(int orderId, String action, int currentShipperId) {
        Order order = shipperDAO.findOrderById(orderId);

        if (order != null) {
            if ("accept".equals(action)) {
                // Nhận đơn: Chỉ cần status Ready
                if ("Ready".equalsIgnoreCase(order.getStatus())) {
                    order.setStatus("Shipping");
                    shipperDAO.updateOrderStatus(orderId, "Shipping");

                    System.out.println("DEBUG: Shipper " + currentShipperId + " nhận đơn " + orderId);

                    // Gửi thông báo cho shipper khi nhận đơn
                    try {
                        String orderNumber = order.getOrderNumber();
                        String customerName = "";
                        String address = "";
                        int customerId = 0;

                        if (order.getCustomer() != null) {
                            customerName = order.getCustomer().getLastName() + " " + order.getCustomer().getFirstName();
                            customerId = order.getCustomer().getUserId();
                        }
                        if (order.getAddress() != null) {
                            address = order.getAddress().getStreet() + ", " + order.getAddress().getDistrict();
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
                // Hoàn thành đơn
                if ("Shipping".equalsIgnoreCase(order.getStatus())) {
                    order.setStatus("Completed");
                    order.setCompletedAt(new Date());
                    shipperDAO.updateOrderStatus(orderId, "Completed");

                    // Gửi thông báo cho shipper khi hoàn thành đơn
                    try {
                        String orderNumber = order.getOrderNumber();
                        double earnings = order.getShippingFee();
                        int customerId = 0;

                        if (order.getCustomer() != null) {
                            customerId = order.getCustomer().getUserId();
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
                    System.out.println("Lỗi: Đơn hàng không ở trạng thái Shipping");
                }
            }
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