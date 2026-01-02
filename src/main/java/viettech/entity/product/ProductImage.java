package viettech.entity.product;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int imageId;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "variant_id")
    private Integer variantId; // có thể null nếu ảnh chung cho product

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "alt_text", length = 255)
    private String altText;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "uploaded_at", nullable = false)
    private Date uploadedAt;

    /* =======================
       MAPPING
       ======================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "variant_id", insertable = false, updatable = false)
    private Variant variant;

    /* =======================
       CONSTRUCTORS
       ======================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public ProductImage() {
        this.url = "";
        this.altText = "";
        this.sortOrder = 0;
        this.isPrimary = false;
        this.uploadedAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có imageId)
    public ProductImage(int productId,
                        Integer variantId,
                        String url,
                        String altText,
                        int sortOrder,
                        boolean isPrimary,
                        Date uploadedAt) {
        this.productId = productId;
        this.variantId = variantId;
        this.url = url;
        this.altText = altText;
        this.sortOrder = sortOrder;
        this.isPrimary = isPrimary;
        this.uploadedAt = uploadedAt;
    }

    /* =======================
       GETTERS & SETTERS
       ======================= */

    public int getImageId() {
        return imageId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
