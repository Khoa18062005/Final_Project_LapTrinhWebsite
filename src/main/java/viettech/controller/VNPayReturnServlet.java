package viettech.controller;

import dev.langchain4j.agent.tool.P;
import viettech.config.VNPayConfig;
import viettech.dao.OrderDAO;
import viettech.dao.OrderDetailDAO;
import viettech.dao.OrderStatusDAO;
import viettech.dao.PaymentDAO;
import viettech.dto.CartCheckoutItemDTO;
import viettech.dto.CartItemDTO;
import viettech.entity.order.Order;
import viettech.entity.order.OrderDetail;
import viettech.entity.order.OrderStatus;
import viettech.entity.payment.Payment;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "VNPayReturnServlet", urlPatterns = {"/vnpay-return"})
public class VNPayReturnServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private OrderStatusDAO orderStatusDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
        orderStatusDAO = new OrderStatusDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String vnp_Amount = request.getParameter("vnp_Amount");
        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
        String vnp_BankCode = request.getParameter("vnp_BankCode");
        String vnp_PayDate = request.getParameter("vnp_PayDate");

        HttpSession session = request.getSession();

        List<CartCheckoutItemDTO> cartItemDTOs = (List<CartCheckoutItemDTO>) session.getAttribute("selectedCartItems");

        try {
            if (vnp_SecureHash != null && VNPayConfig.verifySignature(fields, vnp_SecureHash)) {

                if ("00".equals(vnp_ResponseCode)) {
                    // THANH TOÁN THÀNH CÔNG
                    Order order = orderDAO.findByOrderNumber(vnp_TxnRef);

                    if (order != null) {
                        // Cập nhật status sang PAID
                        order.setStatus("processing");
                        orderDAO.update(order);


                        // Ghi OrderStatus: CONFIRMED
                        OrderStatus confirmedStatus = new OrderStatus(
                                order.getOrderId(),
                                "processing",
                                "Đơn hàng đã được xác nhận tự động sau thanh toán",
                                null,
                                "SYSTEM",
                                null
                        );
                        orderStatusDAO.insert(confirmedStatus);

                        // Cập nhật status cuối cùng sang CONFIRMED
                        order.setStatus("processing");
                        orderDAO.update(order);

                        session.setAttribute("paymentSuccess", true);
                        session.setAttribute("orderNumber", vnp_TxnRef);
                        session.setAttribute("transactionNo", vnp_TransactionNo);
                        session.setAttribute("amount", Long.parseLong(vnp_Amount) / 100);
                        session.setAttribute("bankCode", vnp_BankCode);
                        session.setAttribute("payDate", vnp_PayDate);

                        System.out.println("cartItemDTOs = " + cartItemDTOs);
                        System.out.println("cartItemDTOs size = " +
                                (cartItemDTOs == null ? "NULL" : cartItemDTOs.size()));

                        for(CartCheckoutItemDTO cartItemDTO : cartItemDTOs) {
                            OrderDetail orderDetail = new OrderDetail();
                            orderDetail.setOrderId(order.getOrderId());
                            orderDetail.setDiscount(order.getDiscount());
                            orderDetail.setQuantity(cartItemDTO.getQuantity());
                            orderDetail.setStatus("active");
                            orderDetail.setProductId(cartItemDTO.getProductId());
                            orderDetail.setProductName(cartItemDTO.getProductName());
                            orderDetail.setSubtotal(cartItemDTO.getSubtotal());
                            orderDetail.setVariantId(cartItemDTO.getVariantId());
                            orderDetail.setUnitPrice(cartItemDTO.getPrice());
                            orderDetail.setVariantInfo(cartItemDTO.getVariantDisplay());
                            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                            orderDetailDAO.insert(orderDetail);
                        }

                        Payment payment = new Payment();
                        payment.setOrderId(order.getOrderId());
                        payment.setAmount(Long.parseLong(vnp_Amount) / 100);
                        payment.setPaymentDate(new Date());
                        payment.setPaidAt(new Date());
                        payment.setMethod("online");
                        payment.setStatus("completed");
                        payment.setProvider("VNPAY");
                        PaymentDAO paymentDAO = new PaymentDAO();
                        paymentDAO.insert(payment);

                        session.removeAttribute("selectedCartItems");
                        session.removeAttribute("pendingOrderNumber");

                        response.sendRedirect(request.getContextPath() + "/payment-success");

                    } else {
                        throw new Exception("Không tìm thấy đơn hàng với mã: " + vnp_TxnRef);
                    }

                } else {
                    // THANH TOÁN THẤT BẠI
                    String errorMessage = getErrorMessage(vnp_ResponseCode);

                    Order order = orderDAO.findByOrderNumber(vnp_TxnRef);
                    if (order != null) {
                        order.setStatus("cancelled");
                        order.setCancelReason("VNPay error: " + errorMessage);
                        orderDAO.update(order);

                        OrderStatus failedStatus = new OrderStatus(
                                order.getOrderId(),
                                "cancelled",
                                "Thanh toán VNPay thất bại. Lý do: " + errorMessage
                                        + " (Mã lỗi: " + vnp_ResponseCode + ")",
                                null,
                                "VNPAY",
                                null
                        );
                        orderStatusDAO.insert(failedStatus);

                        for(CartCheckoutItemDTO cartItemDTO : cartItemDTOs) {
                            OrderDetail orderDetail = new OrderDetail();
                            orderDetail.setOrderId(order.getOrderId());
                            orderDetail.setDiscount(order.getDiscount());
                            orderDetail.setQuantity(cartItemDTO.getQuantity());
                            orderDetail.setStatus("cancelled");
                            orderDetail.setProductId(cartItemDTO.getProductId());
                            orderDetail.setProductName(cartItemDTO.getProductName());
                            orderDetail.setSubtotal(cartItemDTO.getSubtotal());
                            orderDetail.setVariantId(cartItemDTO.getVariantId());
                            orderDetail.setUnitPrice(cartItemDTO.getPrice());
                            orderDetail.setVariantInfo(cartItemDTO.getVariantDisplay());
                            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                            orderDetailDAO.insert(orderDetail);

                            Payment payment = new Payment();
                            payment.setOrderId(order.getOrderId());
                            payment.setAmount(Long.parseLong(vnp_Amount) / 100);
                            payment.setPaymentDate(new Date());
                            payment.setPaidAt(new Date());
                            payment.setMethod("online");
                            payment.setStatus("failed");
                            payment.setProvider("VNPAY");
                            PaymentDAO paymentDAO = new PaymentDAO();
                            paymentDAO.insert(payment);
                        }
                    }

                    session.setAttribute("paymentSuccess", false);
                    session.setAttribute("paymentError", errorMessage);

                    response.sendRedirect(request.getContextPath() + "/payment-failed");
                }

            } else {
                // CHỮ KÝ KHÔNG HỢP LỆ
                Order order = orderDAO.findByOrderNumber(vnp_TxnRef);
                if (order != null) {
                    order.setStatus("cancelled");
                    order.setCancelReason("Chữ ký VNPay không hợp lệ");
                    orderDAO.update(order);

                    OrderStatus securityFailedStatus = new OrderStatus(
                            order.getOrderId(),
                            "cancelled",
                            "Xác thực chữ ký VNPay thất bại. Giao dịch có thể bị giả mạo.",
                            null,
                            "SYSTEM",
                            null
                    );
                    orderStatusDAO.insert(securityFailedStatus);

                    for(CartCheckoutItemDTO cartItemDTO : cartItemDTOs) {
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setOrderId(order.getOrderId());
                        orderDetail.setDiscount(order.getDiscount());
                        orderDetail.setQuantity(cartItemDTO.getQuantity());
                        orderDetail.setStatus("cancelled");
                        orderDetail.setProductId(cartItemDTO.getProductId());
                        orderDetail.setProductName(cartItemDTO.getProductName());
                        orderDetail.setSubtotal(cartItemDTO.getSubtotal());
                        orderDetail.setVariantId(cartItemDTO.getVariantId());
                        orderDetail.setUnitPrice(cartItemDTO.getPrice());
                        orderDetail.setVariantInfo(cartItemDTO.getVariantDisplay());
                        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                        orderDetailDAO.insert(orderDetail);

                        Payment payment = new Payment();
                        payment.setOrderId(order.getOrderId());
                        payment.setAmount(Long.parseLong(vnp_Amount) / 100);
                        payment.setPaymentDate(new Date());
                        payment.setPaidAt(new Date());
                        payment.setMethod("online");
                        payment.setStatus("failed");
                        payment.setProvider("VNPAY");
                        PaymentDAO paymentDAO = new PaymentDAO();
                        paymentDAO.insert(payment);
                    }

                }

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

    private String getErrorMessage(String responseCode) {
        switch (responseCode) {
            case "07": return "Giao dịch bị nghi ngờ gian lận";
            case "09": return "Thẻ/Tài khoản chưa đăng ký dịch vụ Internet Banking";
            case "10": return "Xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11": return "Đã hết hạn chờ thanh toán";
            case "12": return "Thẻ/Tài khoản bị khóa";
            case "13": return "Mật khẩu OTP không chính xác";
            case "24": return "Giao dịch bị hủy";
            case "51": return "Tài khoản không đủ số dư";
            case "65": return "Tài khoản đã vượt quá hạn mức giao dịch trong ngày";
            case "75": return "Ngân hàng thanh toán đang bảo trì";
            case "79": return "Giao dịch vượt quá số lần nhập sai mật khẩu";
            default: return "Giao dịch thất bại. Mã lỗi: " + responseCode;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}