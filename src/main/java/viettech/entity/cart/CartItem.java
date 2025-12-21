package viettech.entity.cart;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @Column(name = "variant_id", nullable = false)
    private String variantId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "price_at_add", nullable = false)
    private double priceAtAdd;

    @Column(nullable = false)
    private double subtotal;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_at", nullable = false)
    private Date addedAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public CartItem() {
        this.cartId = 0L;
        this.variantId = "";
        this.quantity = 0;
        this.priceAtAdd = 0.0;
        this.subtotal = 0.0;
        this.addedAt = new Date();
    }

    // Constructor dùng khi tạo CartItem mới
    public CartItem(Long cartId,
                    String variantId,
                    int quantity,
                    double priceAtAdd) {

        this.cartId = cartId != null ? cartId : 0L;
        this.variantId = variantId != null ? variantId : "";
        this.quantity = quantity;
        this.priceAtAdd = priceAtAdd;
        this.subtotal = quantity * priceAtAdd;
        this.addedAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public Long getCartItemId() {
        return cartItemId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId != null ? cartId : 0L;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId != null ? variantId : "";
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        recalculateSubtotal();
    }

    public double getPriceAtAdd() {
        return priceAtAdd;
    }

    public void setPriceAtAdd(double priceAtAdd) {
        this.priceAtAdd = priceAtAdd;
        recalculateSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt != null ? addedAt : new Date();
    }

    /* =========================
       BUSINESS METHOD
       ========================= */

    private void recalculateSubtotal() {
        this.subtotal = this.quantity * this.priceAtAdd;
    }
}
