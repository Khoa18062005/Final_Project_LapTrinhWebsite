package viettech.entity.product;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String slug;

    @Column(length = 500)
    private String description;

    @Column(name = "parent_category_id")
    private Integer parentCategoryId; // null nếu là root category

    @Column(length = 255)
    private String icon;

    @Column(length = 255)
    private String banner;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Category() {
        this.name = "";
        this.slug = "";
        this.description = "";
        this.icon = "";
        this.banner = "";
        this.sortOrder = 0;
        this.isActive = true;
        this.createdAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có categoryId)
    public Category(String name,
                    String slug,
                    String description,
                    Integer parentCategoryId,
                    String icon,
                    String banner,
                    int sortOrder,
                    boolean isActive) {
        this.name = name != null ? name : "";
        this.slug = slug != null ? slug : "";
        this.description = description != null ? description : "";
        this.parentCategoryId = parentCategoryId;
        this.icon = icon;
        this.banner = banner;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.createdAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getCategoryId() {
        return categoryId;
    }

    // ❌ Không setter cho ID (auto increment)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug != null ? slug : "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
