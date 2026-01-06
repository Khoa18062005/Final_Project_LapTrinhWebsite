package viettech.controller;

import viettech.config.VNPayConfig;
import viettech.dao.OrderDAO;
import viettech.entity.order.Order;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "VNPayReturnServlet", urlPatterns = {"/vnpay-return"})
public class VNPayReturnServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy tất cả parameters từ VNPay trả về
        Map<String, String> fields = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue()[0];
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef"); // Order Number
        String vnp_Amount = request.getParameter("vnp_Amount");
        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
        String vnp_BankCode = request.getParameter("vnp_BankCode");
        String vnp_PayDate = request.getParameter("vnp_PayDate");

        HttpSession session = request.getSession();



        try {
            // Xác thực chữ ký
            if (vnp_SecureHash != null && VNPayConfig.verifySignature(fields, vnp_SecureHash)) {
                
                // Kiểm tra mã phản hồi
                if ("00".equals(vnp_ResponseCode)) {
                    // Thanh toán thành công
                    
                    // Cập nhật trạng thái đơn hàng
                    Order order = orderDAO.findByOrderNumber(vnp_TxnRef);
                    
                    if (order != null) {
                        order.setStatus("PAID"); // Đã thanh toán
                        orderDAO.update(order);
                        
                        // Lưu thông tin thanh toán vào session để hiển thị
                        session.setAttribute("paymentSuccess", true);
                        session.setAttribute("orderNumber", vnp_TxnRef);
                        session.setAttribute("transactionNo", vnp_TransactionNo);
                        session.setAttribute("amount", Long.parseLong(vnp_Amount) / 100); // Chia 100 vì VNPay nhân 100
                        session.setAttribute("bankCode", vnp_BankCode);
                        session.setAttribute("payDate", vnp_PayDate);
                        
                        // Xóa giỏ hàng đã thanh toán
                        session.removeAttribute("selectedCartItems");
                        session.removeAttribute("pendingOrderNumber");

                        // Chuyển hướng đến trang thành công
                        response.sendRedirect(request.getContextPath() + "/payment-success");
                        
                    } else {
                        throw new Exception("Không tìm thấy đơn hàng với mã: " + vnp_TxnRef);
                    }
                    
                } else {
                    // Thanh toán thất bại
                    String errorMessage = getErrorMessage(vnp_ResponseCode);
                    
                    // Cập nhật trạng thái đơn hàng nếu cần
                    Order order = orderDAO.findByOrderNumber(vnp_TxnRef);
                    if (order != null) {
                        order.setStatus("PAYMENT_FAILED");
                        order.setCancelReason("VNPay error: " + errorMessage);
                        orderDAO.update(order);
                    }
                    
                    session.setAttribute("paymentSuccess", false);
                    session.setAttribute("paymentError", errorMessage);
                    
                    // Chuyển hướng đến trang thất bại
                    response.sendRedirect(request.getContextPath() + "/payment-failed");
                }
                
            } else {
                // Chữ ký không hợp lệ
                session.setAttribute("paymentSuccess", false);
                session.setAttribute("paymentError", "Chữ ký không hợp lệ. Giao dịch có thể bị giả mạo.");
                response.sendRedirect(request.getContextPath() + "/payment-failed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("paymentSuccess", false);
            session.setAttribute("paymentError", "Lỗi xử lý thanh toán: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/payment-failed");
        }
    }

    /**
     * Lấy thông báo lỗi dựa vào mã phản hồi của VNPay
     */
    private String getErrorMessage(String responseCode) {
        switch (responseCode) {
            case "07":
                return "Giao dịch bị nghi ngờ gian lận";
            case "09":
                return "Thẻ/Tài khoản chưa đăng ký dịch vụ Internet Banking";
            case "10":
                return "Xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11":
                return "Đã hết hạn chờ thanh toán";
            case "12":
                return "Thẻ/Tài khoản bị khóa";
            case "13":
                return "Mật khẩu OTP không chính xác";
            case "24":
                return "Giao dịch bị hủy";
            case "51":
                return "Tài khoản không đủ số dư";
            case "65":
                return "Tài khoản đã vượt quá hạn mức giao dịch trong ngày";
            case "75":
                return "Ngân hàng thanh toán đang bảo trì";
            case "79":
                return "Giao dịch vượt quá số lần nhập sai mật khẩu";
            default:
                return "Giao dịch thất bại. Mã lỗi: " + responseCode;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}