package viettech.service;

import viettech.dao.*;
import viettech.entity.order.OrderDetail;
import viettech.entity.product.Category;
import viettech.entity.product.Product;
import viettech.entity.product.Variant;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final VariantDAO variantDAO = new VariantDAO();

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
     * (Giữ nguyên vì lấy từ OrderDAO)
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
     * 2. THỐNG KÊ TỶ TRỌNG DANH MỤC
     * (Sửa lại: OrderDetail -> Variant -> Product -> Category)
     */
    public ChartData getCategorySalesStatistics() {
        List<OrderDetail> details = orderDetailDAO.findAll();
        Map<String, Integer> categoryMap = new HashMap<>();

        // Cache danh mục
        List<Category> categories = categoryDAO.findAll();
        Map<Integer, String> categoryNames = categories.stream()
                .collect(Collectors.toMap(Category::getCategoryId, Category::getName));

        for (OrderDetail detail : details) {
            // [FIX] Lấy Variant trước
            Variant variant = variantDAO.findById(detail.getVariantId());
            if (variant != null) {
                // [FIX] Lấy Product từ Variant
                Product p = variant.getProduct();
                if (p != null) {
                    String catName = categoryNames.getOrDefault(p.getCategoryId(), "Khác");
                    categoryMap.put(catName, categoryMap.getOrDefault(catName, 0) + detail.getQuantity());
                }
            }
        }

        return convertGenericMapToChartData(categoryMap);
    }

    /**
     * 3. TOP 5 SẢN PHẨM BÁN CHẠY NHẤT
     * (Sửa lại: OrderDetail -> Variant -> Product)
     */
    public List<TopProductDTO> getTopSellingProducts(int topN) {
        List<OrderDetail> details = orderDetailDAO.findAll();
        Map<String, TopProductDTO> productMap = new HashMap<>();

        for (OrderDetail detail : details) {
            // [FIX] Lấy Variant trước
            Variant variant = variantDAO.findById(detail.getVariantId());
            if (variant != null) {
                // [FIX] Lấy Product từ Variant
                Product p = variant.getProduct();
                if (p != null) {
                    // Gom nhóm theo tên sản phẩm (bỏ qua khác biệt màu sắc/cấu hình của variant)
                    String pName = p.getName();
                    double amount = detail.getUnitPrice() * detail.getQuantity();

                    TopProductDTO dto = productMap.getOrDefault(pName, new TopProductDTO(pName, 0, 0.0));
                    productMap.put(pName, new TopProductDTO(pName, dto.quantity + detail.getQuantity(), dto.revenue + amount));
                }
            }
        }

        return productMap.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getQuantity(), p1.getQuantity()))
                .limit(topN)
                .collect(Collectors.toList());
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