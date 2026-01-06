package viettech.dto;

public class CartCheckoutItemDTO {
    private int productId;
    private int variantId;
    private String productName;
    private String productImage;
    private String variantDisplay;
    private int quantity;
    private double price;
    private double subtotal;
    
    // Constructor, getters và setters
    public CartCheckoutItemDTO() {}
    
    public CartCheckoutItemDTO(int productId, int variantId, String productName,
                               String productImage, String variantDisplay,
                               int quantity, double price) {
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName;
        this.productImage = productImage;
        this.variantDisplay = variantDisplay;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price * quantity;
    }
    
    // Getters và setters cho tất cả fields
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    
    public String getVariantDisplay() { return variantDisplay; }
    public void setVariantDisplay(String variantDisplay) { this.variantDisplay = variantDisplay; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        this.subtotal = this.price * quantity;
    }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { 
        this.price = price;
        this.subtotal = price * this.quantity;
    }
    
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}