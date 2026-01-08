package viettech.dto;

import java.io.Serializable;

public class ProductCardDTO implements Serializable {
    private int id;
    private String name;
    private double price;
    private double oldPrice;
    private int discountPercent;
    private String primaryImage;
    private double rating;
    private int memberDiscount;

    // === ĐÃ THÊM THÀNH CÔNG: Trường brand để hỗ trợ lọc theo hãng ===
    // Trường này sẽ được sử dụng trong SearchServlet và JSP để hiển thị nút lọc hãng
    private String brand;

    public ProductCardDTO() {
        this.id = 0;
        this.rating = 0;
        this.primaryImage = "";
        this.price = 0;
        this.name = "";
        this.oldPrice = 0;
        this.discountPercent = 0;
        this.memberDiscount = 0;
        this.brand = ""; // khởi tạo mặc định là chuỗi rỗng
    }

    public ProductCardDTO(int id, double rating, String primaryImage, double price, String name, double oldPrice, int discountPercent, int memberDiscount) {
        this.id = id;
        this.rating = rating;
        this.primaryImage = primaryImage;
        this.price = price;
        this.name = name;
        this.oldPrice = oldPrice;
        this.discountPercent = discountPercent;
        this.memberDiscount = memberDiscount;
        this.brand = ""; // mặc định khi dùng constructor cũ (không có brand)
    }

    // === GETTER & SETTER CHO BRAND - ĐÃ HOÀN CHỈNH ===
    // Getter trả về brand (an toàn nếu null)
    public String getBrand() {
        return brand;
    }

    // Setter tự động trim và xử lý null → trả về chuỗi rỗng nếu brand = null
    public void setBrand(String brand) {
        this.brand = brand != null ? brand.trim() : "";
    }

    // Các getter/setter cũ giữ nguyên (không thay đổi)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getMemberDiscount() {
        return memberDiscount;
    }

    public void setMemberDiscount(int memberDiscount) {
        this.memberDiscount = memberDiscount;
    }
}