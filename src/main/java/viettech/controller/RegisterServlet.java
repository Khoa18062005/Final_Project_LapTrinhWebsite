package viettech.controller;

import viettech.dto.Register_dto;
import viettech.entity.user.Customer;
import viettech.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")  // Quan trọng: chỉ map /register
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    // GET: truy cập /register → hiển thị form
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/Register/register.jsp").forward(req, resp);
    }

    // POST: submit form → xử lý đăng ký
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Register_dto regist_dto = new Register_dto();
        regist_dto.setFirstName(req.getParameter("firstName"));
        regist_dto.setLastName(req.getParameter("lastName"));
        regist_dto.setEmail(req.getParameter("email"));
        regist_dto.setPassword(req.getParameter("password"));
        regist_dto.setPhone(req.getParameter("phone"));
        regist_dto.setDateOfBirth(req.getParameter("dateOfBirth"));
        regist_dto.setGender(req.getParameter("gender"));

        int checkUser = userService.register(regist_dto);

        if (checkUser == 1) {
            // Thành công → lấy customer mới đăng ký
            Customer newCustomer = userService.findCustomerByEmail(regist_dto.getEmail());

            HttpSession session = req.getSession();
            session.setAttribute("auth", newCustomer);
            session.setAttribute("successMessage", "Đăng ký thành công!");

            // Cookie
            Cookie emailCookie = new Cookie("userEmail", regist_dto.getEmail());
            emailCookie.setMaxAge(30 * 24 * 60 * 60);
            emailCookie.setPath("/");
            resp.addCookie(emailCookie);

            String fullName = (regist_dto.getFirstName() + " " + regist_dto.getLastName()).trim();
            Cookie nameCookie = new Cookie("userName", fullName);
            nameCookie.setMaxAge(30 * 24 * 60 * 60);
            nameCookie.setPath("/");
            resp.addCookie(nameCookie);

            resp.sendRedirect(req.getContextPath() + "/index.jsp");

        } else if (checkUser == 2) {
            // Email đã tồn tại
            req.setAttribute("errorMessage", "Email này đã được sử dụng. Vui lòng đăng nhập.");
            req.setAttribute("email", regist_dto.getEmail());
            req.getRequestDispatcher("/Login_account/login.jsp").forward(req, resp);

        } else {
            // Lỗi → quay lại form đăng ký
            req.setAttribute("dto", regist_dto);
            req.setAttribute("errorMessage", "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin.");
            req.getRequestDispatcher("/Register/register.jsp").forward(req, resp);
        }
    }
}