package viettech.entity.wishlist;

import viettech.entity.user.Customer;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private int wishlistId;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /* ==========================
       MAPPING
       ===========================*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @OneToMany(mappedBy = "wishlist", fetch = FetchType.LAZY)
    private List<WishlistItem> wishlistItems;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Wishlist() {
        this.customerId = 0;
        this.isDefault = false;
        this.isPublic = false;

        this.name = "";

        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có wishlistId)
    public Wishlist(int customerId,
                    String name,
                    boolean isDefault,
                    boolean isPublic) {

        this.customerId = customerId;
        this.name = name != null ? name : "";
        this.isDefault = isDefault;
        this.isPublic = isPublic;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getWishlistId() {
        return wishlistId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
        this.updatedAt = new Date();
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
        this.updatedAt = new Date();
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
        this.updatedAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}