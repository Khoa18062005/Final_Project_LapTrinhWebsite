package viettech.entity.review;

import javax.persistence.*;
import java.util.Date;

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
    private int variantId;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    @Column(name = "order_detail_id", nullable = false)
    private int orderDetailId;

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

    @Column(name = "images", columnDefinition = "TEXT")
    private String images;

    @Column(name = "videos", columnDefinition = "TEXT")
    private String videos;

    @Column(name = "likes", nullable = false)
    private int likes;

    @Column(name = "dislikes", nullable = false)
    private int dislikes;

    @Column(name = "helpful_count", nullable = false)
    private int helpfulCount;

    @Column(name = "is_verified_purchase", nullable = false)
    private boolean isVerifiedPurchase;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "moderated_by", length = 100)
    private String moderatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "moderated_at")
    private Date moderatedAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Review() {
        this.productId = 0;
        this.variantId = 0;
        this.customerId = 0;
        this.orderDetailId = 0;
        this.rating = 0;
        this.likes = 0;
        this.dislikes = 0;
        this.helpfulCount = 0;
        this.isVerifiedPurchase = false;

        this.title = "";
        this.comment = "";
        this.images = "";
        this.videos = "";
        this.status = "";
        this.moderatedBy = "";

        this.reviewDate = new Date();
        this.updatedAt = new Date();
        this.moderatedAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có reviewId)
    public Review(int productId,
                  int variantId,
                  int customerId,
                  int orderDetailId,
                  int rating,
                  String title,
                  String comment,
                  Date reviewDate,
                  String images,
                  String videos,
                  int likes,
                  int dislikes,
                  int helpfulCount,
                  boolean isVerifiedPurchase,
                  String status,
                  String moderatedBy,
                  Date moderatedAt) {

        this.productId = productId;
        this.variantId = variantId;
        this.customerId = customerId;
        this.orderDetailId = orderDetailId;
        this.rating = rating;
        this.title = title != null ? title : "";
        this.comment = comment != null ? comment : "";
        this.reviewDate = reviewDate;
        this.images = images != null ? images : "";
        this.videos = videos != null ? videos : "";
        this.likes = likes;
        this.dislikes = dislikes;
        this.helpfulCount = helpfulCount;
        this.isVerifiedPurchase = isVerifiedPurchase;
        this.status = status != null ? status : "";
        this.moderatedBy = moderatedBy != null ? moderatedBy : "";
        this.moderatedAt = moderatedAt;
        this.updatedAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getReviewId() {
        return reviewId;
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment != null ? comment : "";
        this.updatedAt = new Date();
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images != null ? images : "";
        this.updatedAt = new Date();
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos != null ? videos : "";
        this.updatedAt = new Date();
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getHelpfulCount() {
        return helpfulCount;
    }

    public void setHelpfulCount(int helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public boolean isVerifiedPurchase() {
        return isVerifiedPurchase;
    }

    public void setVerifiedPurchase(boolean verifiedPurchase) {
        isVerifiedPurchase = verifiedPurchase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status : "";
    }

    public String getModeratedBy() {
        return moderatedBy;
    }

    public void setModeratedBy(String moderatedBy) {
        this.moderatedBy = moderatedBy != null ? moderatedBy : "";
    }

    public Date getModeratedAt() {
        return moderatedAt;
    }

    public void setModeratedAt(Date moderatedAt) {
        this.moderatedAt = moderatedAt;
    }
}