package viettech.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {

    @Column(name = "loyalty_points", nullable = false)
    private int loyaltyPoints;

    @Column(name = "membership_tier", nullable = false, length = 50)
    private String membershipTier;

    @Column(name = "total_spent", nullable = false)
    private double totalSpent;

    // ===== Constructor mặc định =====
    public Customer() {
        super(); // gọi constructor User

        this.loyaltyPoints = 0;
        this.membershipTier = "";
        this.totalSpent = 0.0;
    }

    // ===== Constructor đầy đủ =====
    public Customer(String firstName,
                    String lastName,
                    String username,
                    String password,
                    String email,
                    String phone,
                    String avatar,
                    String gender) {

        super(firstName, lastName, username, password, email, phone, avatar, gender);

        this.loyaltyPoints = 0;
        this.membershipTier = "";
        this.totalSpent = 0.0;
    }

    // ===== Getter & Setter =====
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getMembershipTier() {
        return membershipTier;
    }

    public void setMembershipTier(String membershipTier) {
        this.membershipTier = membershipTier;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
}