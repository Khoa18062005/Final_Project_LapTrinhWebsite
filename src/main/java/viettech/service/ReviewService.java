package viettech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.ReviewDAO;
import viettech.dao.CustomerDAO;
import viettech.dto.ReviewDTO;
import viettech.entity.review.Review;
import viettech.entity.user.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ReviewService - Service layer for Review operations
 * @author VietTech Team
 */
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewDAO reviewDAO;
    private final CustomerDAO customerDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Lấy danh sách reviews của sản phẩm
     */
    public List<ReviewDTO> getReviewsByProductId(int productId) {
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        try {
            List<Review> reviews = reviewDAO.findByProductId(productId);

            for (Review review : reviews) {
                ReviewDTO dto = convertToDTO(review);
                reviewDTOs.add(dto);
            }

            logger.info("✓ Loaded {} reviews for product ID: {}", reviewDTOs.size(), productId);
        } catch (Exception e) {
            logger.error("✗ Error loading reviews for product ID: {}", productId, e);
        }
        return reviewDTOs;
    }

    /**
     * Lấy thông tin review theo ID
     */
    public ReviewDTO getReviewById(int reviewId) {
        try {
            Review review = reviewDAO.findById(reviewId);
            if (review != null) {
                return convertToDTO(review);
            }
        } catch (Exception e) {
            logger.error("✗ Error getting review by ID: {}", reviewId, e);
        }
        return null;
    }

    /**
     * Thêm review mới
     */
    public boolean addReview(int productId, int customerId, int rating, String title, String comment) {
        logger.info(">>> Starting addReview - productId: {}, customerId: {}, rating: {}", productId, customerId, rating);
        logger.info(">>> Title: {}, Comment: {}", title, comment);

        try {
            Review review = new Review();
            review.setProductId(productId);
            review.setCustomerId(customerId);
            review.setRating(rating);
            review.setTitle(title != null ? title : "");
            review.setComment(comment != null ? comment : "");
            review.setReviewDate(new Date());
            review.setLikes(0);
            review.setDislikes(0);
            review.setHelpfulCount(0);
            review.setVerifiedPurchase(false);
            review.setStatus("Approved");
            // Set null vì không bắt buộc phải có order_detail khi review
            review.setOrderDetailId(null);
            review.setVariantId(null);

            logger.info(">>> Review object created, calling DAO insert...");
            reviewDAO.insert(review);
            logger.info("✓ Added new review for product ID: {} by customer ID: {}", productId, customerId);
            return true;
        } catch (Exception e) {
            logger.error("✗ Error adding review for product ID: {} - Exception: {}", productId, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật review
     */
    public boolean updateReview(int reviewId, int rating, String title, String comment) {
        try {
            Review review = reviewDAO.findById(reviewId);
            if (review != null) {
                review.setRating(rating);
                review.setTitle(title);
                review.setComment(comment);
                review.setUpdatedAt(new Date());
                reviewDAO.update(review);
                logger.info("✓ Updated review ID: {}", reviewId);
                return true;
            }
        } catch (Exception e) {
            logger.error("✗ Error updating review ID: {}", reviewId, e);
        }
        return false;
    }

    /**
     * Xóa review
     */
    public boolean deleteReview(int reviewId) {
        try {
            reviewDAO.delete(reviewId);
            logger.info("✓ Deleted review ID: {}", reviewId);
            return true;
        } catch (Exception e) {
            logger.error("✗ Error deleting review ID: {}", reviewId, e);
            return false;
        }
    }

    /**
     * Lấy rating trung bình của sản phẩm
     */
    public double getAverageRating(int productId) {
        try {
            Double avg = reviewDAO.getAverageRatingByProductId(productId);
            return avg != null ? avg : 0.0;
        } catch (Exception e) {
            logger.error("✗ Error getting average rating for product ID: {}", productId, e);
            return 0.0;
        }
    }

    /**
     * Đếm số reviews của sản phẩm
     */
    public long countReviewsByProductId(int productId) {
        try {
            return reviewDAO.countByProductId(productId);
        } catch (Exception e) {
            logger.error("✗ Error counting reviews for product ID: {}", productId, e);
            return 0;
        }
    }

    /**
     * Kiểm tra xem customer đã review sản phẩm chưa
     */
    public boolean hasCustomerReviewedProduct(int customerId, int productId) {
        try {
            List<Review> customerReviews = reviewDAO.findByCustomerId(customerId);
            for (Review review : customerReviews) {
                if (review.getProductId() == productId) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("✗ Error checking if customer {} reviewed product {}", customerId, productId, e);
        }
        return false;
    }

    /**
     * Convert Review entity sang ReviewDTO
     */
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setProductId(review.getProductId());
        dto.setCustomerId(review.getCustomerId());
        dto.setRating(review.getRating());
        dto.setTitle(review.getTitle());
        dto.setComment(review.getComment());
        dto.setReviewDate(review.getReviewDate());
        dto.setLikes(review.getLikes());
        dto.setDislikes(review.getDislikes());
        dto.setHelpfulCount(review.getHelpfulCount());
        dto.setVerifiedPurchase(review.isVerifiedPurchase());
        dto.setStatus(review.getStatus());

        // Lấy thông tin customer
        try {
            Customer customer = review.getCustomer();
            if (customer != null) {
                dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
                dto.setCustomerAvatar(customer.getAvatar());
            } else {
                // Fallback: load customer từ DAO
                Customer customerFromDao = customerDAO.findById(review.getCustomerId());
                if (customerFromDao != null) {
                    dto.setCustomerName(customerFromDao.getFirstName() + " " + customerFromDao.getLastName());
                    dto.setCustomerAvatar(customerFromDao.getAvatar());
                } else {
                    dto.setCustomerName("Khách hàng");
                    dto.setCustomerAvatar(null);
                }
            }
        } catch (Exception e) {
            dto.setCustomerName("Khách hàng");
            dto.setCustomerAvatar(null);
        }

        // Lấy response nếu có
        try {
            if (review.getResponse() != null) {
                dto.setResponseContent(review.getResponse().getResponse());
                dto.setResponseDate(review.getResponse().getResponseDate());
            }
        } catch (Exception e) {
            // Ignore nếu không có response
        }

        return dto;
    }
}

