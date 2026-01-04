package viettech.entity.user;

import viettech.entity.cart.Cart;
import viettech.entity.order.Order;
import viettech.entity.review.Review;
import viettech.entity.voucher.VoucherUsage;
import viettech.entity.wishlist.Wishlist;
import viettech.entity.Address;
import viettech.entity.Notification;
import viettech.entity.search.SearchHistory;
import viettech.entity.search.ProductView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {

    @Column(name = "loyalty_points", nullable = false)
    private int loyaltyPoints;

    @Column(name = "membership_tier", nullable = false, length = 50)
    private String membershipTier;

    @Column(name = "total_spent", nullable = false)
    private double totalSpent;

    /* =========================
       RELATIONSHIPS
       ========================= */

    // Customer 1 — 0..1 Cart
    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
    private Cart cart;

    // Customer 1 — 0..* Wishlist
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Wishlist> wishlists = new ArrayList<>();

    // Customer 1 — 0..* Address
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    // Customer 1 — 0..* Order
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    // Customer 1 — 0..* Review
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    // Customer 1 — 0..* Notification
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    // Customer 1 — 0..* SearchHistory
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<SearchHistory> searchHistories = new ArrayList<>();

    // Customer 1 — 0..* ProductView
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<ProductView> productViews = new ArrayList<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<VoucherUsage> voucherUsages = new ArrayList<>();

    /* =========================
       CONSTRUCTORS
       ========================= */

    public Customer() {
        super();
        this.roleID = 4;
        this.loyaltyPoints = 0;
        this.membershipTier = "Bronze";
        this.totalSpent = 0.0;
    }

    public Customer(String firstName,
                    String lastName,
                    String username,
                    String password,
                    String email,
                    String phone,
                    String avatar,
                    String gender) {

        super(firstName, lastName, username, password, email, phone, avatar, gender);
        this.roleID = 4;
        this.loyaltyPoints = 0;
        this.membershipTier = "Bronze";
        this.totalSpent = 0.0;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getMembershipTier() {
        return membershipTier;
    }

    public void setMembershipTier(String membershipTier) {
        this.membershipTier = membershipTier;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Cart getCart() {
        return cart;
    }

    public List<Wishlist> getWishlists() {
        return wishlists;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<SearchHistory> getSearchHistories() {
        return searchHistories;
    }

    public List<ProductView> getProductViews() {
        return productViews;
    }

    public void setWishlists(List<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setSearchHistories(List<SearchHistory> searchHistories) {
        this.searchHistories = searchHistories;
    }

    public void setProductViews(List<ProductView> productViews) {
        this.productViews = productViews;
    }

    public List<VoucherUsage> getVoucherUsages() {
        return voucherUsages;
    }

    public void setVoucherUsages(List<VoucherUsage> voucherUsages) {
        this.voucherUsages = voucherUsages;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getReferralCode() {
        if (this.username == null || !this.username.startsWith("user_")) {
            return "";
        }
        return this.username.substring(5);
    }
}
