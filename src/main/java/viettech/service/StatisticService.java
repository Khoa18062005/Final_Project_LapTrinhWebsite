package viettech.service;

import viettech.dao.OrderDAO;
import viettech.dao.OrderDetailDAO;

import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

    // Đã xóa CategoryDAO và VariantDAO vì không còn cần dùng ở đây nữa

    // DTO để trả dữ liệu cho biểu đồ (Chart.js)
    public static class ChartData {
        private String labels;
        private String data;
        private double totalValue;

        public ChartData(String labels, String data, double totalValue) {
            this.labels = labels;
            this.data = data;
            this.totalValue = totalValue;
        }

        public String getLabels() { return labels; }
        public String getData() { return data; }
        public double getTotalValue() { return totalValue; }
    }

    // DTO cho Top Sản phẩm
    public static class TopProductDTO {
        String name;
        int quantity;
        double revenue;

        public TopProductDTO(String name, int quantity, double revenue) {
            this.name = name;
            this.quantity = quantity;
            this.revenue = revenue;
        }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public double getRevenue() { return revenue; }
    }

    /**
     * 1. THỐNG KÊ DOANH THU THEO THỜI GIAN
     * (Giữ nguyên logic cũ)
     */
    public ChartData getRevenueStatistics(int days) {
        List<Object[]> rawOrders = orderDAO.findAllForStatistics();

        double totalRevenueInRange = 0;
        Map<String, Double> revenueMap = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

        Calendar cal = Calendar.getInstance();
        for (int i = days - 1; i >= 0; i--) {
            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -i);
            revenueMap.put(sdf.format(cal.getTime()), 0.0);
        }

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        Date startDate = cal.getTime();

        if (rawOrders != null) {
            for (Object[] row : rawOrders) {
                try {
                    String status = (String) row[1];
                    Double price = (Double) row[2];
                    Date date = (Date) row[3];

                    if (isValidOrder(status) && date != null && date.after(startDate)) {
                        double amount = (price != null) ? price : 0.0;
                        String dateKey = sdf.format(date);

                        if (revenueMap.containsKey(dateKey)) {
                            revenueMap.put(dateKey, revenueMap.get(dateKey) + amount);
                            totalRevenueInRange += amount;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return convertMapToChartData(revenueMap, totalRevenueInRange);
    }

    /**
     * 2. THỐNG KÊ TỶ TRỌNG DANH MỤC (Đã sửa lỗi Lazy & Tối ưu)
     * Sử dụng phương thức HQL mới từ OrderDetailDAO
     */
    public ChartData getCategorySalesStatistics() {
        try {
            // Gọi xuống DB để lấy thống kê trực tiếp
            List<Object[]> results = orderDetailDAO.getCategoryStatistics();
            Map<String, Integer> categoryMap = new LinkedHashMap<>();

            if (results != null && !results.isEmpty()) {
                for (Object[] row : results) {
                    String catName = (String) row[0];
                    if (catName == null || catName.trim().isEmpty()) catName = "Khác";

                    // HQL SUM thường trả về Long, cần ép kiểu an toàn
                    Number quantity = (Number) row[1];

                    categoryMap.put(catName, quantity != null ? quantity.intValue() : 0);
                }
            }

            // Nếu không có dữ liệu, trả về dữ liệu mặc định
            if (categoryMap.isEmpty()) {
                categoryMap.put("Chưa có dữ liệu", 0);
            }

            return convertGenericMapToChartData(categoryMap);
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về dữ liệu mặc định khi có lỗi
            Map<String, Integer> defaultMap = new LinkedHashMap<>();
            defaultMap.put("Lỗi tải dữ liệu", 0);
            return convertGenericMapToChartData(defaultMap);
        }
    }

    /**
     * 3. TOP 5 SẢN PHẨM BÁN CHẠY NHẤT (Đã sửa lỗi Lazy & Tối ưu)
     * Sử dụng phương thức HQL mới từ OrderDetailDAO
     */
    public List<TopProductDTO> getTopSellingProducts(int topN) {
        List<TopProductDTO> topProducts = new ArrayList<>();
        try {
            // Gọi xuống DB, DB đã sort sẵn và limit sẵn
            List<Object[]> results = orderDetailDAO.getTopSellingProducts(topN);

            if (results != null && !results.isEmpty()) {
                for (Object[] row : results) {
                    String name = (String) row[0];
                    Number quantity = (Number) row[1];
                    Number revenue = (Number) row[2];

                    topProducts.add(new TopProductDTO(
                            name != null ? name : "Không xác định",
                            quantity != null ? quantity.intValue() : 0,
                            revenue != null ? revenue.doubleValue() : 0.0
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về danh sách rỗng khi có lỗi
        }

        return topProducts;
    }

    // --- CÁC HÀM PHỤ TRỢ ---

    private boolean isValidOrder(String status) {
        return status != null && (
                status.equalsIgnoreCase("Delivered") ||
                        status.equalsIgnoreCase("Completed") ||
                        status.equalsIgnoreCase("Success") ||
                        status.equalsIgnoreCase("Thành công"));
    }

    private ChartData convertMapToChartData(Map<String, Double> map, double total) {
        StringBuilder labels = new StringBuilder("[");
        StringBuilder data = new StringBuilder("[");
        int i = 0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (i > 0) { labels.append(","); data.append(","); }
            labels.append("'").append(entry.getKey()).append("'");
            data.append(entry.getValue().longValue());
            i++;
        }
        labels.append("]");
        data.append("]");
        return new ChartData(labels.toString(), data.toString(), total);
    }

    private ChartData convertGenericMapToChartData(Map<String, Integer> map) {
        StringBuilder labels = new StringBuilder("[");
        StringBuilder data = new StringBuilder("[");
        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (i > 0) { labels.append(","); data.append(","); }
            labels.append("'").append(entry.getKey()).append("'");
            data.append(entry.getValue());
            i++;
        }
        labels.append("]");
        data.append("]");
        return new ChartData(labels.toString(), data.toString(), 0);
    }
}