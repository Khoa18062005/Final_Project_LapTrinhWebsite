package viettech.entity.review;

import viettech.entity.order.OrderDetail;
import viettech.entity.product.Product;
import viettech.entity.product.Variant;
import viettech.entity.user.Customer;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "variant_id")
    private Integer variantId;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    @Column(name = "order_detail_id")
    private Integer orderDetailId;

    /* =========================
       RELATIONSHIP MAPPINGS
       ========================= */

    // Review * -- 1 Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    // Review * -- 0..1 Variant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", insertable = false, updatable = false)
    private Variant variant;

    // Review * -- 0..1 OrderDetail
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id", insertable = false, updatable = false)
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    // Review * -- 0..1 ReviewResponse
    @OneToOne(mappedBy = "review", fetch = FetchType.LAZY)
    private ReviewResponse response;

    // Review 1 -- 0..* ReviewVote
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewVote> votes;


    /* =========================
       REVIEW DATA
       ========================= */

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "review_date", nullable = false)
    private Date reviewDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "likes", nullable = false)
    private int likes;

    @Column(name = "dislikes", nullable = false)
    private int dislikes;

    @Column(name = "helpful_count", nullable = false)
    private int helpfulCount;

    @Column(name = "is_verified_purchase", nullable = false)
    private boolean isVerifiedPurchase;

    @Column(name = "status", nullable = false)
    private String status;

    /* =========================
       CONSTRUCTOR
       ========================= */

    public Review() {
        this.reviewDate = new Date();
        this.updatedAt = new Date();
    }

    /* =========================
       GETTERS
       ========================= */

    public int getReviewId() {
        return reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public Product getProduct() {
        return product;
    }

    public Variant getVariant() {
        return variant;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ReviewResponse getResponse() {
        return response;
    }

    public List<ReviewVote> getVotes() {
        return votes;
    }

    public int getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public int getHelpfulCount() {
        return helpfulCount;
    }

    public boolean isVerifiedPurchase() {
        return isVerifiedPurchase;
    }

    public String getStatus() {
        return status;
    }

    /* =========================
       SETTERS
       ========================= */

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public void setHelpfulCount(int helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public void setVerifiedPurchase(boolean verifiedPurchase) {
        isVerifiedPurchase = verifiedPurchase;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
