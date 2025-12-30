package viettech.dto;

public class BuyNowDTO {
    private int productId;
    private int quantity;
    private double unitPrice;
    private String productName;
    private String productImage;
    
    // Constructors
    public BuyNowDTO() {}
    
    public BuyNowDTO(int productId, double unitPrice,
                    String productName, String productImage) {
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.productName = productName;
        this.productImage = productImage;
    }
    
    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    
    // Helper method
    public double getTotalPrice() {
        return unitPrice * quantity;
    }
}