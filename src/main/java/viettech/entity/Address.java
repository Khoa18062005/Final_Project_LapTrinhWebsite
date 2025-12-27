package viettech.entity;

import viettech.entity.order.Order;
import viettech.entity.user.Customer;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int addressId;

    /* =========================
       RELATIONSHIPS
       ========================= */

    // Address * — 1 Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Address 1 — 0..* Order
    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    /* =========================
       ADDRESS FIELDS
       ========================= */

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "ward", nullable = false)
    private String ward;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // BẮT BUỘC cho JPA
    public Address() {
        this.receiverName = "";
        this.phone = "";
        this.street = "";
        this.ward = "";
        this.district = "";
        this.city = "";
        this.isDefault = false;
    }

    public Address(Customer customer,
                   String receiverName,
                   String phone,
                   String street,
                   String ward,
                   String district,
                   String city,
                   boolean isDefault) {

        this.customer = customer;
        this.receiverName = receiverName;
        this.phone = phone;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.isDefault = isDefault;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getAddressId() {
        return addressId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName != null ? receiverName : "";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone : "";
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street != null ? street : "";
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward != null ? ward : "";
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district != null ? district : "";
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city != null ? city : "";
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
