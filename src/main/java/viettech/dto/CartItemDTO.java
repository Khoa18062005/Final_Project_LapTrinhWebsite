package viettech.dto;

import java.util.List;

public class CartItemDTO {
    private int productId;
    private int variantId;
    private String productName;
    private String imageUrl;
    private double price;
    private int quantity;
    private VariantDTO variantInfo;
    private boolean selected;
    private String variantDisplay;
    
    public CartItemDTO() {
        this.selected = true; // Mặc định chọn
    }

    public CartItemDTO(int productId, boolean selected, VariantDTO variantInfo, double price, int variantId, String imageUrl, String productName, int quantity, String variantDisplay) {
        this.productId = productId;
        this.selected = selected;
        this.variantInfo = variantInfo;
        this.price = price;
        this.variantId = variantId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.quantity = quantity;
        this.variantDisplay = variantDisplay;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getVariantId() {
        return variantId;
    }
    
    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public VariantDTO getVariantInfo() {
        return variantInfo;
    }
    
    public void setVariantInfo(VariantDTO variantInfo) {
        this.variantInfo = variantInfo;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    // Helper methods
    public double getSubtotal() {
        return price * quantity;
    }

    public String getVariantDisplay() {
        return variantDisplay;
    }

    public void setVariantDisplay(String variantDisplay) {
        this.variantDisplay = variantDisplay;
    }
}