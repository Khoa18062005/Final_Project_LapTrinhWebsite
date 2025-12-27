package viettech.entity.product;

import viettech.entity.order.OrderDetail;
import viettech.entity.review.Review;
import viettech.entity.search.ProductView;
import viettech.entity.user.Vendor;
import viettech.entity.voucher.FlashSale;
import viettech.entity.wishlist.WishlistItem;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    protected int productId;

    @Column(name = "vendor_id", nullable = false)
    protected int vendorId;

    @Column(name = "category_id", nullable = false)
    protected int categoryId;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false, unique = true)
    protected String slug;

    @Column(name = "base_price", nullable = false)
    protected double basePrice;

    @Column(columnDefinition = "TEXT")
    protected String description;

    protected String brand;

    @Column(columnDefinition = "TEXT")
    protected String specifications;

    protected String status;
    protected String conditions;

    protected double weight;
    protected String dimensions;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    protected Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    protected Date updatedAt;

    @Column(name = "average_rating")
    protected double averageRating;

    @Column(name = "total_reviews")
    protected int totalReviews;

    @Column(name = "total_sold")
    protected int totalSold;

    @Column(name = "view_count")
    protected int viewCount;

    @Column(name = "is_featured")
    protected boolean isFeatured;
    /* =========================
       RELATIONSHIP MAPPINGS
       ========================= */

    // Product * -- 1 Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    protected Category category;

    // Product * -- 1 Vendor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", insertable = false, updatable = false)
    protected Vendor vendor;

    // Product 1 -- 0..* Variant
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    protected List<Variant> variants;

    // Product 1 -- 0..* Review
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    protected List<Review> reviews;

    // Product 1 -- 0..* ProductImage
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    protected List<ProductImage> images;

    // Product 1 -- 0..* ProductView
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    protected List<ProductView> views;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<FlashSale> flashSales;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<WishlistItem> wishlistItems;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    protected Product() {
        this.vendorId = 0;
        this.categoryId = 0;
        this.name = "";
        this.slug = "";
        this.basePrice = 0.0;
        this.description = "";
        this.brand = "";
        this.specifications = "";
        this.status = "";
        this.conditions = "";
        this.weight = 0.0;
        this.dimensions = "";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.averageRating = 0.0;
        this.totalReviews = 0;
        this.totalSold = 0;
        this.viewCount = 0;
        this.isFeatured = false;
    }

    // Constructor dùng khi tạo Product (cho class con gọi super)
    protected Product(int vendorId,
                      int categoryId,
                      String name,
                      String slug,
                      double basePrice,
                      String description,
                      String brand,
                      String specifications,
                      String status,
                      String conditions,
                      double weight,
                      String dimensions,
                      boolean isFeatured) {

        this.vendorId = vendorId;
        this.categoryId = categoryId;
        this.name = name != null ? name : "";
        this.slug = slug != null ? slug : "";
        this.basePrice = basePrice;
        this.description = description != null ? description : "";
        this.brand = brand != null ? brand : "";
        this.specifications = specifications != null ? specifications : "";
        this.status = status != null ? status : "";
        this.conditions = conditions != null ? conditions : "";
        this.weight = weight;
        this.dimensions = dimensions != null ? dimensions : "";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.averageRating = 0.0;
        this.totalReviews = 0;
        this.totalSold = 0;
        this.viewCount = 0;
        this.isFeatured = isFeatured;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getProductId() {
        return productId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug != null ? slug : "";
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand != null ? brand : "";
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications != null ? specifications : "";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status : "";
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions != null ? conditions : "";
    }
    

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions != null ? dimensions : "";
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public List<ProductView> getViews() {
        return views;
    }

    public void setViews(List<ProductView> views) {
        this.views = views;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<FlashSale> getFlashSales() {
        return flashSales;
    }

    public void setFlashSales(List<FlashSale> flashSales) {
        this.flashSales = flashSales;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
