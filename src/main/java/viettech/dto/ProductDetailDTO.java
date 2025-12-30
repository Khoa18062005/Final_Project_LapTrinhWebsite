package viettech.dto;

import java.util.Date;

public abstract class ProductDetailDTO {
    private int productId;
    private int vendorId;
    private int categoryId;
    private String name;
    private String slug;
    private double basePrice;
    private String description;
    private String brand;
    private String specifications;
    private String status;
    private String conditions;
    private double weight;
    private String dimensions;
    private Date createdAt;
    private Date updatedAt;
    private double averageRating;
    private int totalReviews;
    private int totalSold;
    private int viewCount;
    private boolean isFeatured;
    private String primaryImageUrl;

    // Constructor mặc định
    public ProductDetailDTO() {
        this.productId = 0;
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
        this.createdAt = null;
        this.updatedAt = null;
        this.averageRating = 0.0;
        this.totalReviews = 0;
        this.totalSold = 0;
        this.viewCount = 0;
        this.isFeatured = false;
        this.primaryImageUrl = "";
    }

    // Constructor all-arg
    public ProductDetailDTO(int productId, int vendorId, int categoryId, String name, String slug,
                            double basePrice, String description, String brand, String specifications,
                            String status, String conditions, double weight, String dimensions,
                            Date createdAt, Date updatedAt, double averageRating, int totalReviews,
                            int totalSold, int viewCount, boolean isFeatured, String imageUrl) {
        this.productId = productId;
        this.vendorId = vendorId;
        this.categoryId = categoryId;
        this.name = name;
        this.slug = slug;
        this.basePrice = basePrice;
        this.description = description;
        this.brand = brand;
        this.specifications = specifications;
        this.status = status;
        this.conditions = conditions;
        this.weight = weight;
        this.dimensions = dimensions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.totalSold = totalSold;
        this.viewCount = viewCount;
        this.isFeatured = isFeatured;
        this.primaryImageUrl = imageUrl;
    }

    // Getters và Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
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
        this.dimensions = dimensions;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
    }
}