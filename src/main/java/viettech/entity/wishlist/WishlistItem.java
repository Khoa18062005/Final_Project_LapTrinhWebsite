package viettech.entity.wishlist;

import viettech.entity.product.Product;
import viettech.entity.product.Variant;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wishlist_items")
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;

    @Column(name = "wishlist_id", nullable = false)
    private int wishlistId;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "variant_id")
    private int variantId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_date", nullable = false)
    private Date addedDate;

    @Column(name = "notify_on_discount", nullable = false)
    private boolean notifyOnDiscount;

    @Column(name = "notify_on_restock", nullable = false)
    private boolean notifyOnRestock;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "notes")
    private String notes;

    /* =========================
       MAPPING
       ========================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", insertable = false, updatable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", insertable = false, updatable = false)
    private Variant variant;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public WishlistItem() {
        this.wishlistId = 0;
        this.productId = 0;
        this.variantId = 0;
        this.priority = 0;
        this.notifyOnDiscount = false;
        this.notifyOnRestock = false;

        this.notes = "";

        this.addedDate = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có itemId)
    public WishlistItem(int wishlistId,
                        int productId,
                        int variantId,
                        boolean notifyOnDiscount,
                        boolean notifyOnRestock,
                        int priority,
                        String notes) {

        this.wishlistId = wishlistId;
        this.productId = productId;
        this.variantId = variantId;
        this.notifyOnDiscount = notifyOnDiscount;
        this.notifyOnRestock = notifyOnRestock;
        this.priority = priority;
        this.notes = notes != null ? notes : "";
        this.addedDate = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getItemId() {
        return itemId;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

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

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public boolean isNotifyOnDiscount() {
        return notifyOnDiscount;
    }

    public void setNotifyOnDiscount(boolean notifyOnDiscount) {
        this.notifyOnDiscount = notifyOnDiscount;
    }

    public boolean isNotifyOnRestock() {
        return notifyOnRestock;
    }

    public void setNotifyOnRestock(boolean notifyOnRestock) {
        this.notifyOnRestock = notifyOnRestock;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
    }
}