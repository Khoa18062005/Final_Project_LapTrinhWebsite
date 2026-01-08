package viettech.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dto.ReviewDTO;
import viettech.entity.user.User;
import viettech.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * ReviewServlet - Servlet xử lý các request về đánh giá sản phẩm
 * @author VietTech Team
 */
@WebServlet("/review/*")
public class ReviewServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServlet.class);
    private ReviewService reviewService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        reviewService = new ReviewService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            String productIdParam = request.getParameter("productId");
            if (productIdParam != null) {
                getReviewsByProduct(request, response, Integer.parseInt(productIdParam));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Missing productId parameter");
            }
        } else if (pathInfo.equals("/list")) {
            String productIdParam = request.getParameter("productId");
            if (productIdParam != null) {
                getReviewsByProduct(request, response, Integer.parseInt(productIdParam));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Missing productId parameter");
            }
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/add")) {
            addReview(request, response);
        } else if (pathInfo.equals("/update")) {
            updateReview(request, response);
        } else if (pathInfo.equals("/delete")) {
            deleteReview(request, response);
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    /**
     * Lấy danh sách reviews theo product ID
     */
    private void getReviewsByProduct(HttpServletRequest request, HttpServletResponse response, int productId)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("count", reviews.size());
            jsonResponse.add("reviews", gson.toJsonTree(reviews));

            out.print(jsonResponse.toString());
            logger.info("✓ Returned {} reviews for product ID: {}", reviews.size(), productId);
        } catch (Exception e) {
            logger.error("✗ Error getting reviews for product ID: {}", productId, e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading reviews");
        }
    }

    /**
     * Thêm review mới
     */
    private void addReview(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Vui lòng đăng nhập để đánh giá sản phẩm");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(jsonResponse.toString());
            return;
        }

        try {
            // Lấy dữ liệu từ request
            String productIdParam = request.getParameter("productId");
            String ratingParam = request.getParameter("rating");
            String title = request.getParameter("title");
            String comment = request.getParameter("comment");

            logger.info(">>> Received review data - productId: {}, rating: {}, title: {}, comment: {}",
                       productIdParam, ratingParam, title, comment);

            // Validate dữ liệu
            if (productIdParam == null || ratingParam == null) {
                logger.warn(">>> Missing required params - productId: {}, rating: {}", productIdParam, ratingParam);
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Thiếu thông tin bắt buộc");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(jsonResponse.toString());
                return;
            }

            int productId = Integer.parseInt(productIdParam);
            int rating = Integer.parseInt(ratingParam);
            int customerId = user.getUserId();

            logger.info(">>> Parsed data - productId: {}, rating: {}, customerId: {}", productId, rating, customerId);

            // Validate rating
            if (rating < 1 || rating > 5) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Đánh giá phải từ 1 đến 5 sao");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(jsonResponse.toString());
                return;
            }

            // Kiểm tra xem đã review chưa
            boolean alreadyReviewed = reviewService.hasCustomerReviewedProduct(customerId, productId);
            logger.info(">>> Already reviewed check: {}", alreadyReviewed);

            if (alreadyReviewed) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Bạn đã đánh giá sản phẩm này rồi");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(jsonResponse.toString());
                return;
            }

            // Thêm review
            logger.info(">>> Calling reviewService.addReview...");
            boolean success = reviewService.addReview(productId, customerId, rating, title, comment);
            logger.info(">>> addReview result: {}", success);

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Đánh giá của bạn đã được gửi thành công!");
                logger.info("✓ Customer {} added review for product {}", customerId, productId);
            } else {
                logger.error(">>> addReview returned false for productId: {}, customerId: {}", productId, customerId);
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Không thể thêm đánh giá. Vui lòng thử lại");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            out.print(jsonResponse.toString());
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Dữ liệu không hợp lệ");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(jsonResponse.toString());
        } catch (Exception e) {
            logger.error("✗ Error adding review", e);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi hệ thống. Vui lòng thử lại sau");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(jsonResponse.toString());
        }
    }

    /**
     * Cập nhật review
     */
    private void updateReview(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Vui lòng đăng nhập");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(jsonResponse.toString());
            return;
        }

        try {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String title = request.getParameter("title");
            String comment = request.getParameter("comment");

            ReviewDTO existingReview = reviewService.getReviewById(reviewId);
            if (existingReview == null || existingReview.getCustomerId() != user.getUserId()) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Bạn không có quyền sửa đánh giá này");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print(jsonResponse.toString());
                return;
            }

            boolean success = reviewService.updateReview(reviewId, rating, title, comment);

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Cập nhật đánh giá thành công");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Không thể cập nhật đánh giá");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            logger.error("✗ Error updating review", e);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi hệ thống");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(jsonResponse.toString());
        }
    }

    /**
     * Xóa review
     */
    private void deleteReview(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Vui lòng đăng nhập");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(jsonResponse.toString());
            return;
        }

        try {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));

            ReviewDTO existingReview = reviewService.getReviewById(reviewId);
            if (existingReview == null || existingReview.getCustomerId() != user.getUserId()) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Bạn không có quyền xóa đánh giá này");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print(jsonResponse.toString());
                return;
            }

            boolean success = reviewService.deleteReview(reviewId);

            if (success) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Xóa đánh giá thành công");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Không thể xóa đánh giá");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            out.print(jsonResponse.toString());
        } catch (Exception e) {
            logger.error("✗ Error deleting review", e);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi hệ thống");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(jsonResponse.toString());
        }
    }

    /**
     * Gửi response lỗi
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", false);
        jsonResponse.addProperty("message", message);

        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
    }
}

