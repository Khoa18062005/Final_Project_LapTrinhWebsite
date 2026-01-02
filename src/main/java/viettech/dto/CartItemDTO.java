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
    
    public CartItemDTO() {
        this.selected = true; // Mặc định chọn
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
        if (variantInfo != null && variantInfo.getAttributes() != null) {
            StringBuilder sb = new StringBuilder();
            List<AttributeDTO> attributes = variantInfo.getAttributes();
            for (int i = 0; i < attributes.size(); i++) {
                sb.append(attributes.get(i).getAttributeValue());
                if (i < attributes.size() - 1) {
                    sb.append(" - ");
                }
            }
            return sb.toString();
        }
        return "";
    }
}