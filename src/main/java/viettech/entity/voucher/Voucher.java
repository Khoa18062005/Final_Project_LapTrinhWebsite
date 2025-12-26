package viettech.entity.voucher;

import viettech.entity.order.Order;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vouchers")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private int voucherId;

    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "scope", length = 50, nullable = false)
    private String scope;

    @Column(name = "discount_percent", precision = 5, scale = 2)
    private double discountPercent;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private double discountAmount;

    @Column(name = "max_discount", precision = 12, scale = 2)
    private double maxDiscount;

    @Column(name = "min_order_value", precision = 12, scale = 2)
    private double minOrderValue;

    @Column(name = "applicable_products")
    private String applicableProducts;

    @Column(name = "applicable_categories")
    private String applicableCategories;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @Column(name = "usage_limit", nullable = false)
    private int usageLimit;

    @Column(name = "usage_limit_per_user", nullable = false)
    private int usageLimitPerUser;

    @Column(name = "usage_count", nullable = false)
    private int usageCount;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    /* =========================
       MAPPING
       ========================= */
    @OneToMany(mappedBy = "voucher")
    private List<Order> order;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Voucher() {
        this.discountPercent = 0.0;
        this.discountAmount = 0.0;
        this.maxDiscount = 0.0;
        this.minOrderValue = 0.0;
        this.usageLimit = 0;
        this.usageLimitPerUser = 0;
        this.usageCount = 0;
        this.isActive = false;
        this.isPublic = false;

        this.code = "";
        this.name = "";
        this.description = "";
        this.type = "";
        this.scope = "";
        this.applicableProducts = "";
        this.applicableCategories = "";
        this.createdBy = "";

        this.startDate = new Date();
        this.expiryDate = new Date();
        this.createdAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có voucherId)
    public Voucher(String code,
                   String name,
                   String description,
                   String type,
                   String scope,
                   double discountPercent,
                   double discountAmount,
                   double maxDiscount,
                   double minOrderValue,
                   String applicableProducts,
                   String applicableCategories,
                   Date startDate,
                   Date expiryDate,
                   int usageLimit,
                   int usageLimitPerUser,
                   int usageCount,
                   boolean isActive,
                   boolean isPublic,
                   String createdBy) {

        this.code = code != null ? code : "";
        this.name = name != null ? name : "";
        this.description = description != null ? description : "";
        this.type = type != null ? type : "";
        this.scope = scope != null ? scope : "";
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.maxDiscount = maxDiscount;
        this.minOrderValue = minOrderValue;
        this.applicableProducts = applicableProducts != null ? applicableProducts : "";
        this.applicableCategories = applicableCategories != null ? applicableCategories : "";
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.usageLimit = usageLimit;
        this.usageLimitPerUser = usageLimitPerUser;
        this.usageCount = usageCount;
        this.isActive = isActive;
        this.isPublic = isPublic;
        this.createdBy = createdBy != null ? createdBy : "";
        this.createdAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getVoucherId() {
        return voucherId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code != null ? code : "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null ? type : "";
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope != null ? scope : "";
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(double minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public String getApplicableProducts() {
        return applicableProducts;
    }

    public void setApplicableProducts(String applicableProducts) {
        this.applicableProducts = applicableProducts != null ? applicableProducts : "";
    }

    public String getApplicableCategories() {
        return applicableCategories;
    }

    public void setApplicableCategories(String applicableCategories) {
        this.applicableCategories = applicableCategories != null ? applicableCategories : "";
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public int getUsageLimitPerUser() {
        return usageLimitPerUser;
    }

    public void setUsageLimitPerUser(int usageLimitPerUser) {
        this.usageLimitPerUser = usageLimitPerUser;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy != null ? createdBy : "";
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }
}