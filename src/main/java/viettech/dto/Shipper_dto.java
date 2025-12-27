package viettech.dto;

import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.user.Shipper;
import java.util.List;

public class Shipper_dto {
    // Thông tin Shipper
    private Shipper shipperInfo;

    // Thống kê hôm nay
    private long todayOrderCount;
    private long successOrderCount;
    private double todayIncome;

    // Danh sách đơn hàng
    private List<DeliveryAssignment> pendingOrders;   // Chờ nhận
    private List<DeliveryAssignment> ongoingOrders;   // Đang giao
    private List<DeliveryAssignment> historyOrders;   // Lịch sử hoàn thành

    // Getters & Setters
    public Shipper getShipperInfo() { return shipperInfo; }
    public void setShipperInfo(Shipper shipperInfo) { this.shipperInfo = shipperInfo; }

    public long getTodayOrderCount() { return todayOrderCount; }
    public void setTodayOrderCount(long todayOrderCount) { this.todayOrderCount = todayOrderCount; }

    public long getSuccessOrderCount() { return successOrderCount; }
    public void setSuccessOrderCount(long successOrderCount) { this.successOrderCount = successOrderCount; }

    public double getTodayIncome() { return todayIncome; }
    public void setTodayIncome(double todayIncome) { this.todayIncome = todayIncome; }

    public List<DeliveryAssignment> getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(List<DeliveryAssignment> pendingOrders) { this.pendingOrders = pendingOrders; }

    public List<DeliveryAssignment> getOngoingOrders() { return ongoingOrders; }
    public void setOngoingOrders(List<DeliveryAssignment> ongoingOrders) { this.ongoingOrders = ongoingOrders; }

    public List<DeliveryAssignment> getHistoryOrders() { return historyOrders; }
    public void setHistoryOrders(List<DeliveryAssignment> historyOrders) { this.historyOrders = historyOrders; }
}