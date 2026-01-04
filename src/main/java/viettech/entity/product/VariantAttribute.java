package viettech.entity.product;

import javax.persistence.*;

@Entity
@Table(name = "variant_attributes")
public class VariantAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_id")
    private int attributeId;

    @Column(name = "variant_id", nullable = false)
    private int variantId;

    @Column(name = "attribute_name", nullable = false, length = 100)
    private String attributeName;

    @Column(name = "attribute_value", nullable = false, length = 255)
    private String attributeValue;

    @Column(name = "price_adjustment", nullable = false)
    private double priceAdjustment;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", insertable = false, updatable = false)
    private Variant variant;

    /* =======================
       CONSTRUCTORS
       ======================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public VariantAttribute() {
        this.attributeName = "";
        this.attributeValue = "";
        this.priceAdjustment = 0.0;
        this.sortOrder = 0;
    }

    // Constructor đầy đủ tham số (KHÔNG có attributeId)
    public VariantAttribute(int variantId,
                            String attributeName,
                            String attributeValue,
                            double priceAdjustment,
                            int sortOrder) {
        this.variantId = variantId;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.priceAdjustment = priceAdjustment;
        this.sortOrder = sortOrder;
    }

    /* =======================
       GETTERS & SETTERS
       ======================= */

    public int getAttributeId() {
        return attributeId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public double getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(double priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
