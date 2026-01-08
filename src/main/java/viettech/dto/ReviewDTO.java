package viettech.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * ReviewDTO - Data Transfer Object for Review
 * @author VietTech Team
 */
public class ReviewDTO implements Serializable {

    private int reviewId;
    private int productId;
    private int customerId;
    private String customerName;
    private String customerAvatar;
    private int rating;
    private String title;
    private String comment;
    private Date reviewDate;
    private int likes;
    private int dislikes;
    private int helpfulCount;
    private boolean verifiedPurchase;
    private String status;
    private String responseContent;
    private Date responseDate;

    public ReviewDTO() {
    }

    public ReviewDTO(int reviewId, int productId, int customerId, String customerName,
                     String customerAvatar, int rating, String title, String comment,
                     Date reviewDate, int likes, int dislikes, int helpfulCount,
                     boolean verifiedPurchase, String status) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAvatar = customerAvatar;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.likes = likes;
        this.dislikes = dislikes;
        this.helpfulCount = helpfulCount;
        this.verifiedPurchase = verifiedPurchase;
        this.status = status;
    }

    // Getters and Setters

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAvatar() {
        return customerAvatar;
    }

    public void setCustomerAvatar(String customerAvatar) {
        this.customerAvatar = customerAvatar;
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
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
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
        return verifiedPurchase;
    }

    public void setVerifiedPurchase(boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }
}

