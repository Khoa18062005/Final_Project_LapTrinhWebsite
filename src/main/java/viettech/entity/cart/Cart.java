package viettech.entity.cart;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int cartId;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

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

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Cart() {
        this.customerId = 0;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.expiresAt = null;
    }

    // Constructor dùng khi tạo Cart mới
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt != null ? createdAt : new Date();
    }

    public Date getUpdatedAt() {
        return updatedAt;
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
