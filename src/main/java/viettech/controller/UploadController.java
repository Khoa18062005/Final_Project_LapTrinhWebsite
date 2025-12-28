package viettech.controller;

import viettech.util.CloudinaryUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/admin/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class UploadController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // 1. Lấy Part file từ request (name="imageFile" trong form JSP)
            Part filePart = request.getPart("imageFile");

            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("error", "Vui lòng chọn một file để upload!");
                request.getRequestDispatcher("/admin/result.jsp").forward(request, response);
                return;
            }

            // 2. Lấy loại upload (avatar, product, general)
            String uploadType = request.getParameter("uploadType");

            String imageUrl;

            // 3. Upload lên Cloudinary dựa vào loại
            if ("avatar".equalsIgnoreCase(uploadType)) {
                imageUrl = CloudinaryUtil.uploadAvatar(filePart);
            } else if ("product".equalsIgnoreCase(uploadType)) {
                imageUrl = CloudinaryUtil.uploadProductImage(filePart);
            } else {
                // Mặc định: upload vào folder general
                imageUrl = CloudinaryUtil.uploadImage(filePart, "viettech/general");
            }

            // 4. Lưu URL vào attribute để JSP hiển thị
            System.out.println("✅ Upload thành công! Link ảnh: " + imageUrl);
            request.setAttribute("success", true);
            request.setAttribute("imageUrl", imageUrl);
            request.setAttribute("message", "Upload thành công!");

            // 5. TODO: Lưu URL vào MySQL
            // Example:
            // if ("avatar".equals(uploadType)) {
            //     int userId = Integer.parseInt(request.getParameter("userId"));
            //     userDAO.updateAvatar(userId, imageUrl);
            // } else if ("product".equals(uploadType)) {
            //     int productId = Integer.parseInt(request.getParameter("productId"));
            //     productDAO.updateImage(productId, imageUrl);
            // }

            // 6. Chuyển hướng đến trang kết quả
            request.getRequestDispatcher("/admin/result.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            // Lỗi validation (file không hợp lệ, quá dung lượng, ...)
            System.err.println("❌ Validation error: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/admin/result.jsp").forward(request, response);

        } catch (IOException e) {
            // Lỗi upload Cloudinary
            System.err.println("❌ Upload error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Lỗi upload: " + e.getMessage());
            request.getRequestDispatcher("/admin/result.jsp").forward(request, response);

        } catch (Exception e) {
            // Lỗi khác
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/admin/result.jsp").forward(request, response);
        }
    }
}