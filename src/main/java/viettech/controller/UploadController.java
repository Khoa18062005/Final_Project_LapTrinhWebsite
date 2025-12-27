package viettech.controller;

import viettech.util.CloudinaryUtil;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
            throws ServletException, IOException { // Thêm throws để đúng chuẩn Servlet

        try {
            // 1. Lấy Part file từ request (name="imageFile" trong form JSP)
            Part filePart = request.getPart("imageFile");
            if (filePart == null || filePart.getSize() == 0) {
                response.getWriter().print("Vui lòng chọn một file để upload!");
                return;
            }

            // 2. Tạo file tạm thời trên server
            //getAbsolutePath() và delete() sẽ tự động hết lỗi sau khi import java.io.File
            File tempFile = File.createTempFile("upload_", ".tmp");
            filePart.write(tempFile.getAbsolutePath());

            // 3. Upload lên Cloudinary bằng lớp Util bạn đã viết
            String imageUrl = CloudinaryUtil.uploadImage(tempFile);

            // 4. Lưu URL này vào MySQL (Ví dụ mẫu)
            System.out.println("Upload thành công! Link ảnh: " + imageUrl);
            request.setAttribute("imageUrl", imageUrl);

            // 5. Xóa file tạm để giải phóng bộ nhớ server
            if (tempFile.exists()) {
                tempFile.delete();
            }

            // Chuyển hướng hoặc thông báo kết quả
            request.getRequestDispatcher("/admin/result.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("Lỗi upload: " + e.getMessage());
        }
    }
}