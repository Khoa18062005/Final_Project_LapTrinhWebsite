package viettech.dto;

import java.util.ArrayList;
import java.util.List;

public class VariantDTO {
    private int variantId;

    private double finalPrice;

    private boolean isActive;

    private List<AttributeDTO> attributes;

    public VariantDTO() {
        variantId = 0;
        finalPrice = 0.0;
        isActive = false;
        attributes = new ArrayList<AttributeDTO>();
    }

    public VariantDTO(int variantId, double finalPrice, boolean isActive, List<AttributeDTO> attributes) {
        this.variantId = variantId;
        this.finalPrice = finalPrice;
        this.isActive = isActive;
        this.attributes = attributes;
    }

    public List<AttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDTO> attributes) {
        this.attributes = attributes;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }
}
