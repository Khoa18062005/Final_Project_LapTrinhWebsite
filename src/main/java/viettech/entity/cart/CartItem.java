package viettech.entity.cart;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"cart_id", "variant_id"})
        }
)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private int cartItemId;

    // GIỮ cartId
    @Column(name = "cart_id", nullable = false)
    private int cartId;

    // ➕ CartItem * — 1 Cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "cart_id",
            insertable = false,
            updatable = false
    )
    private Cart cart;

    @Column(name = "variant_id", nullable = false)
    private int variantId;

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

    public CartItem() {
        this.cartId = 0;
        this.variantId = 0;
        this.quantity = 0;
        this.priceAtAdd = 0.0;
        this.subtotal = 0.0;
        this.addedAt = new Date();
    }

    public CartItem(int cartId,
                    int variantId,
                    int quantity,
                    double priceAtAdd) {

        this.cartId = cartId;
        this.variantId = variantId;
        this.quantity = quantity;
        this.priceAtAdd = priceAtAdd;
        this.subtotal = quantity * priceAtAdd;
        this.addedAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getCartItemId() {
        return cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public Cart getCart() {
        return cart;
    }

    public int getVariantId() {
        return variantId;
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

    /* =========================
       BUSINESS METHOD
       ========================= */

    private void recalculateSubtotal() {
        this.subtotal = this.quantity * this.priceAtAdd;
    }
}
