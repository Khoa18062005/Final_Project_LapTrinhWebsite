package viettech.entity.cart;

import viettech.entity.user.Customer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int cartId;

    // GIỮ customerId
    @Column(name = "customer_id", nullable = false)
    private int customerId;

    // ➕ Cart 1 — 1 Customer
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            insertable = false,
            updatable = false
    )
    private Customer customer;

    // ➕ Cart 1 — 0..* CartItem
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<CartItem> items = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at")
    private Date expiresAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    public Cart() {
        this.customerId = 0;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.expiresAt = null;
    }

    public Cart(int customerId, Date expiresAt) {
        this.customerId = customerId;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.expiresAt = expiresAt;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getCartId() {
        return cartId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt != null ? updatedAt : new Date();
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
