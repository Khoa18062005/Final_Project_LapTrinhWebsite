package viettech.dto;

import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;
import java.util.ArrayList;
import java.util.List;

public class Shipper_dto {
    // 1. Thông tin Shipper
    private Shipper shipperInfo;

    // 2. Thống kê Hôm nay (Mặc định)
    private long todayOrderCount;     // Số đơn được phân công hôm nay
    private long successOrderCount;   // Số đơn hoàn thành hôm nay
    private double todayIncome;       // Thu nhập hôm nay

    // 3. Thống kê 7 Ngày qua (Mới)
    private double income7Days;
    private long count7Days;

    // 4. Thống kê Tháng này (Mới)
    private double incomeMonth;
    private long countMonth;

    // 5. Danh sách đơn hàng (Để hiển thị bảng)
    private List<DeliveryAssignment> pendingOrders;   // Chờ nhận
    private List<DeliveryAssignment> ongoingOrders;   // Đang giao
    private List<DeliveryAssignment> historyOrders;   // Lịch sử hoàn thành

    // 6. Dữ liệu Biểu đồ (Chart.js)
    // Biểu đồ 7 ngày: Ngày (20/12) -> Tiền
    private List<String> chartLabels7Days = new ArrayList<>();
    private List<Double> chartData7Days = new ArrayList<>();

    // Biểu đồ Tháng: Tuần (Tuần 1, Tuần 2...) -> Tiền
    private List<String> chartLabelsMonth = new ArrayList<>();
    private List<Double> chartDataMonth = new ArrayList<>();

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================

    public Shipper getShipperInfo() { return shipperInfo; }
    public void setShipperInfo(Shipper shipperInfo) { this.shipperInfo = shipperInfo; }

    // --- Hôm nay ---
    public long getTodayOrderCount() { return todayOrderCount; }
    public void setTodayOrderCount(long todayOrderCount) { this.todayOrderCount = todayOrderCount; }

    public long getSuccessOrderCount() { return successOrderCount; }
    public void setSuccessOrderCount(long successOrderCount) { this.successOrderCount = successOrderCount; }

    public double getTodayIncome() { return todayIncome; }
    public void setTodayIncome(double todayIncome) { this.todayIncome = todayIncome; }

    // --- 7 Ngày ---
    public double getIncome7Days() { return income7Days; }
    public void setIncome7Days(double income7Days) { this.income7Days = income7Days; }

    public long getCount7Days() { return count7Days; }
    public void setCount7Days(long count7Days) { this.count7Days = count7Days; }

    // --- Tháng ---
    public double getIncomeMonth() { return incomeMonth; }
    public void setIncomeMonth(double incomeMonth) { this.incomeMonth = incomeMonth; }

    public long getCountMonth() { return countMonth; }
    public void setCountMonth(long countMonth) { this.countMonth = countMonth; }

    // --- Danh sách đơn ---
    public List<DeliveryAssignment> getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(List<DeliveryAssignment> pendingOrders) { this.pendingOrders = pendingOrders; }

    public List<DeliveryAssignment> getOngoingOrders() { return ongoingOrders; }
    public void setOngoingOrders(List<DeliveryAssignment> ongoingOrders) { this.ongoingOrders = ongoingOrders; }

    public List<DeliveryAssignment> getHistoryOrders() { return historyOrders; }
    public void setHistoryOrders(List<DeliveryAssignment> historyOrders) { this.historyOrders = historyOrders; }

    // --- Dữ liệu Biểu đồ ---
    public List<String> getChartLabels7Days() { return chartLabels7Days; }
    public void setChartLabels7Days(List<String> chartLabels7Days) { this.chartLabels7Days = chartLabels7Days; }

    public List<Double> getChartData7Days() { return chartData7Days; }
    public void setChartData7Days(List<Double> chartData7Days) { this.chartData7Days = chartData7Days; }

    public List<String> getChartLabelsMonth() { return chartLabelsMonth; }
    public void setChartLabelsMonth(List<String> chartLabelsMonth) { this.chartLabelsMonth = chartLabelsMonth; }

    public List<Double> getChartDataMonth() { return chartDataMonth; }
    public void setChartDataMonth(List<Double> chartDataMonth) { this.chartDataMonth = chartDataMonth; }
}