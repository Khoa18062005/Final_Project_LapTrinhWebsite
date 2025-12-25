package viettech.entity.review;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "review_responses")
public class ReviewResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private int responseId;

    @Column(name = "review_id", nullable = false)
    private int reviewId;

    @Column(name = "vendor_id", nullable = false)
    private int vendorId;

    @Column(name = "response", columnDefinition = "TEXT")
    private String response;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "response_date", nullable = false)
    private Date responseDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /* =========================
       MAPPING
       ========================= */

    @OneToOne
    @JoinColumn(name = "review_id", nullable = false, unique = true, insertable = false, updatable = false)
    private Review review;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public ReviewResponse() {
        this.reviewId = 0;
        this.vendorId = 0;

        this.response = "";

        this.responseDate = new Date();
        this.updatedAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có responseId)
    public ReviewResponse(int reviewId,
                          int vendorId,
                          String response,
                          Date responseDate) {

        this.reviewId = reviewId;
        this.vendorId = vendorId;
        this.response = response != null ? response : "";
        this.responseDate = responseDate;
        this.updatedAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getResponseId() {
        return responseId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response != null ? response : "";
        this.updatedAt = new Date();
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}