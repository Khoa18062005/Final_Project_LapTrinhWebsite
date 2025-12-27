package viettech.dto;

import java.io.Serializable;

/**
 * Base DTO class cho tất cả các loại sản phẩm
 * Chứa các thuộc tính chung để chuẩn bị dữ liệu cho DAO
 */
public class Product_dto implements Serializable {
    private static final long serialVersionUID = 1L;

    // ===== Common Product Properties =====
    protected String vendorId;
    protected String categoryId;
    protected String name;
    protected String slug;
    protected String basePrice;
    protected String description;
    protected String brand;
    protected String specifications;
    protected String status;
    protected String conditions;
    protected String weight;
    protected String dimensions;
    protected String isFeatured;

    /* =========================
       CONSTRUCTORS
       ========================= */

    public Product_dto() {
    }

    public Product_dto(String vendorId, String categoryId, String name, String slug,
                       String basePrice, String description, String brand, String specifications,
                       String status, String conditions, String weight, String dimensions,
                       String isFeatured) {
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
        this.isFeatured = isFeatured;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getBasePrice() { return basePrice; }
    public void setBasePrice(String basePrice) { this.basePrice = basePrice; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getSpecifications() { return specifications; }
    public void setSpecifications(String specifications) { this.specifications = specifications; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    public String getIsFeatured() { return isFeatured; }
    public void setIsFeatured(String isFeatured) { this.isFeatured = isFeatured; }

    /* =========================
       UTILITY METHODS
       ========================= */

    /**
     * Parse String to int, return default value if parse fails
     */
    protected int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parse String to double, return default value if parse fails
     */
    protected double parseDoubleOrDefault(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parse String to boolean
     */
    protected boolean parseBoolean(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return "true".equalsIgnoreCase(value.trim()) ||
               "1".equals(value.trim()) ||
               "yes".equalsIgnoreCase(value.trim());
    }

    /**
     * Validate if string is not null or empty
     */
    protected boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Get safe string value (return empty string if null)
     */
    protected String getSafeString(String value) {
        return value != null ? value.trim() : "";
    }

    @Override
    public String toString() {
        return "Product_dto{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", basePrice='" + basePrice + '\'' +
                '}';
    }
}
