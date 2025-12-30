package viettech.dto;

public class AttributeDTO {
    private int attributeId;

    private String attributeName;

    private String attributeValue;

    public AttributeDTO() {
        attributeId = 0;
        attributeName = "";
        attributeValue = "";
    }
     public AttributeDTO(int attributeId, String attributeName, String attributeValue) {
        this.attributeId = attributeId;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
     }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
