package viettech.dto;

import viettech.entity.delivery.DeliveryAssignment;
import viettech.entity.review.Review;
import viettech.entity.user.Shipper;
import java.util.List;

public class Shipper_dto {
    private Shipper shipper;
    private long todayOrdersCount;
    private long successOrdersCount;
    private double todayIncome;
    private String avgTime; // Ví dụ: "28 phút"
    private DeliveryAssignment activeDelivery; // Đơn đang đi giao
    private List<DeliveryAssignment> pendingOrders; // Đơn chờ xác nhận nhận đơn
    private List<Review> recentReviews;

    // Constructors
    public Shipper_dto() {}

    // Getters & Setters
    public Shipper getShipper() { return shipper; }
    public void setShipper(Shipper shipper) { this.shipper = shipper; }

    public long getTodayOrdersCount() { return todayOrdersCount; }
    public void setTodayOrdersCount(long todayOrdersCount) { this.todayOrdersCount = todayOrdersCount; }

    public long getSuccessOrdersCount() { return successOrdersCount; }
    public void setSuccessOrdersCount(long successOrdersCount) { this.successOrdersCount = successOrdersCount; }

    public double getTodayIncome() { return todayIncome; }
    public void setTodayIncome(double todayIncome) { this.todayIncome = todayIncome; }

    public String getAvgTime() { return avgTime; }
    public void setAvgTime(String avgTime) { this.avgTime = avgTime; }

    public DeliveryAssignment getActiveDelivery() { return activeDelivery; }
    public void setActiveDelivery(DeliveryAssignment activeDelivery) { this.activeDelivery = activeDelivery; }

    public List<DeliveryAssignment> getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(List<DeliveryAssignment> pendingOrders) { this.pendingOrders = pendingOrders; }

    public List<Review> getRecentReviews() { return recentReviews; }
    public void setRecentReviews(List<Review> recentReviews) { this.recentReviews = recentReviews; }
}