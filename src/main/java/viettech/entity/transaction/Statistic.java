package viettech.entity.transaction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "statistics")
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private int statId;

    @Column(name = "vendor_id", nullable = false)
    private int vendorId;

    @Column(name = "period", length = 50, nullable = false)
    private String period;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "revenue", precision = 15, scale = 2, nullable = false)
    private double revenue;

    @Column(name = "sales_count", nullable = false)
    private int salesCount;

    @Column(name = "order_count", nullable = false)
    private int orderCount;

    @Column(name = "average_order_value", precision = 15, scale = 2, nullable = false)
    private double averageOrderValue;

    @Column(name = "conversion_rate", precision = 8, scale = 4)
    private double conversionRate;

    @Column(name = "return_rate", precision = 8, scale = 4)
    private double returnRate;

    @Column(name = "top_products", columnDefinition = "TEXT")
    private String topProducts;

    @Column(name = "top_categories", columnDefinition = "TEXT")
    private String topCategories;

    @Column(name = "new_customers", nullable = false)
    private int newCustomers;

    @Column(name = "returning_customers", nullable = false)
    private int returningCustomers;

    @Column(name = "cancelled_orders", nullable = false)
    private int cancelledOrders;

    @Column(name = "completed_orders", nullable = false)
    private int completedOrders;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "generated_at", nullable = false)
    private Date generatedAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Statistic() {
        this.vendorId = 0;
        this.revenue = 0.0;
        this.salesCount = 0;
        this.orderCount = 0;
        this.averageOrderValue = 0.0;
        this.conversionRate = 0.0;
        this.returnRate = 0.0;
        this.newCustomers = 0;
        this.returningCustomers = 0;
        this.cancelledOrders = 0;
        this.completedOrders = 0;

        this.period = "";
        this.topProducts = "";
        this.topCategories = "";

        this.startDate = new Date();
        this.endDate = new Date();
        this.generatedAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có statId)
    public Statistic(int vendorId,
                     String period,
                     Date startDate,
                     Date endDate,
                     double revenue,
                     int salesCount,
                     int orderCount,
                     double averageOrderValue,
                     double conversionRate,
                     double returnRate,
                     String topProducts,
                     String topCategories,
                     int newCustomers,
                     int returningCustomers,
                     int cancelledOrders,
                     int completedOrders) {

        this.vendorId = vendorId;
        this.period = period != null ? period : "";
        this.startDate = startDate;
        this.endDate = endDate;
        this.revenue = revenue;
        this.salesCount = salesCount;
        this.orderCount = orderCount;
        this.averageOrderValue = averageOrderValue;
        this.conversionRate = conversionRate;
        this.returnRate = returnRate;
        this.topProducts = topProducts != null ? topProducts : "";
        this.topCategories = topCategories != null ? topCategories : "";
        this.newCustomers = newCustomers;
        this.returningCustomers = returningCustomers;
        this.cancelledOrders = cancelledOrders;
        this.completedOrders = completedOrders;
        this.generatedAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getStatId() {
        return statId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period != null ? period : "";
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public double getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(double averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(double returnRate) {
        this.returnRate = returnRate;
    }

    public String getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(String topProducts) {
        this.topProducts = topProducts != null ? topProducts : "";
    }

    public String getTopCategories() {
        return topCategories;
    }

    public void setTopCategories(String topCategories) {
        this.topCategories = topCategories != null ? topCategories : "";
    }

    public int getNewCustomers() {
        return newCustomers;
    }

    public void setNewCustomers(int newCustomers) {
        this.newCustomers = newCustomers;
    }

    public int getReturningCustomers() {
        return returningCustomers;
    }

    public void setReturningCustomers(int returningCustomers) {
        this.returningCustomers = returningCustomers;
    }

    public int getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(int cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }
}