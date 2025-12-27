package viettech.controller;

import viettech.dto.Shipper_dto;
import viettech.entity.user.User;
import viettech.service.ShipperService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/shipper")
public class ShipperServlet extends HttpServlet {

    private final ShipperService service = new ShipperService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        // 1. Lấy Session hiện tại (false: không tạo mới nếu chưa có)
        HttpSession session = request.getSession(false);

        // 2. Kiểm tra đăng nhập
        // LƯU Ý QUAN TRỌNG: Phải dùng key "user" khớp với LoginServlet
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Chưa đăng nhập -> Về trang Login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 3. Kiểm tra lại quyền (Optional: Đề phòng user nhập URL trực tiếp)
        // Nếu không phải Shipper (Role ID 3) thì đá về trang chủ
        if (user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 4. Lấy dữ liệu Dashboard
        int shipperId = user.getUserId();
        System.out.println("Fetching dashboard for Shipper ID: " + shipperId); // Debug log

        Shipper_dto data = service.getDashboardData(shipperId);

        // 5. Xử lý trường hợp Shipper mới tinh chưa có dữ liệu (tránh lỗi null bên JSP)
        if (data == null) {
            data = new Shipper_dto(); // Tạo đối tượng rỗng để hiển thị số 0
            System.out.println("No data found for Shipper ID: "); // Debug log

        }

        // 6. Đẩy dữ liệu và chuyển hướng
        request.setAttribute("data", data);

        // Đảm bảo đường dẫn file JSP của bạn đúng (ví dụ: nằm trong WEB-INF để bảo mật)
        request.getRequestDispatcher("/WEB-INF/views/shipper.jsp").forward(request, response);
    }
}