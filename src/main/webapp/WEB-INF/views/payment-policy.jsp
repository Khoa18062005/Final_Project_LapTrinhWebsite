<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Phương Thức Thanh Toán - VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
  <style>
    .policy-hero {
      background: linear-gradient(135deg, #0d6efd 0%, #0b5ed7 100%);
      color: white;
      padding: 60px 0;
      text-align: center;
    }
    .policy-hero h1 {
      font-size: 2.5rem;
      font-weight: 800;
      margin-bottom: 0.5rem;
    }
    .policy-content {
      padding: 60px 0;
      background-color: #f8f9fa;
    }
    .policy-section {
      background: white;
      border-radius: 12px;
      padding: 2rem;
      margin-bottom: 2rem;
      box-shadow: 0 4px 12px rgba(0,0,0,0.08);
    }
    .policy-section h3 {
      color: #0d6efd;
      font-size: 1.5rem;
      font-weight: 700;
      margin-bottom: 1.5rem;
      padding-bottom: 0.75rem;
      border-bottom: 3px solid #0d6efd;
    }
    .policy-section h3 i {
      margin-right: 0.5rem;
    }
    .payment-method-card {
      background: white;
      border: 2px solid #e9ecef;
      border-radius: 12px;
      padding: 2rem;
      margin-bottom: 1.5rem;
      transition: all 0.3s ease;
    }
    .payment-method-card:hover {
      border-color: #0d6efd;
      box-shadow: 0 8px 20px rgba(13, 110, 253, 0.15);
      transform: translateY(-4px);
    }
    .payment-method-card h4 {
      color: #0d6efd;
      font-size: 1.3rem;
      font-weight: 700;
      margin-bottom: 1rem;
    }
    .payment-method-card .icon {
      font-size: 3rem;
      color: #0d6efd;
      margin-bottom: 1rem;
    }
    .check-list {
      list-style: none;
      padding: 0;
    }
    .check-list li {
      padding: 0.5rem 0;
      padding-left: 2rem;
      position: relative;
      line-height: 1.6;
    }
    .check-list li::before {
      content: '✓';
      position: absolute;
      left: 0;
      color: #28a745;
      font-weight: 700;
      font-size: 1.2rem;
    }
    .note-box {
      background: #fff3cd;
      border-left: 4px solid #ffc107;
      padding: 1rem 1.5rem;
      border-radius: 8px;
      margin: 1.5rem 0;
    }
    .note-box strong {
      color: #856404;
    }
    .security-box {
      background: linear-gradient(135deg, #28a745, #20c997);
      color: white;
      padding: 2rem;
      border-radius: 12px;
      text-align: center;
      margin: 2rem 0;
    }
    .security-box i {
      font-size: 3rem;
      margin-bottom: 1rem;
    }
    .security-box h4 {
      color: white;
      margin-bottom: 1rem;
    }
    .contact-box {
      background: linear-gradient(135deg, #0d6efd, #0b5ed7);
      color: white;
      padding: 2rem;
      border-radius: 12px;
      margin-top: 2rem;
    }
    .contact-box h4 {
      color: white;
      margin-bottom: 1rem;
    }
    .contact-box p {
      margin-bottom: 0.5rem;
    }
    .contact-box i {
      margin-right: 0.5rem;
    }
  </style>
</head>
<body>
<%@ include file="../../header.jsp" %>
<script>
  // Biến toàn cục cho JavaScript
  const contextPath = "${pageContext.request.contextPath}";
  const isLoggedIn = ${not empty sessionScope.user};
</script>
<div class="policy-hero">
  <div class="container">
    <h1><i class="bi bi-credit-card"></i> Phương Thức Thanh Toán</h1>
    <p class="lead">Đa dạng, tiện lợi và an toàn tuyệt đối</p>
  </div>
</div>

<div class="policy-content">
  <div class="container">

    <!-- Tổng Quan -->
    <div class="policy-section">
      <h3><i class="bi bi-info-circle"></i>Tổng Quan</h3>
      <p>VietTech hỗ trợ <strong>đa dạng phương thức thanh toán</strong> để khách hàng có thể lựa chọn cách thanh toán phù hợp và thuận tiện nhất:</p>
      <ul class="check-list">
        <li>Thanh toán khi nhận hàng (COD)</li>
        <li>Chuyển khoản ngân hàng</li>
        <li>Ví điện tử: MoMo, ZaloPay</li>
        <li>Cổng thanh toán VNPay</li>
        <li>Thẻ tín dụng/ghi nợ quốc tế (Visa, Mastercard)</li>
      </ul>
    </div>

    <!-- 1. COD -->
    <div class="payment-method-card">
      <div class="icon">
        <i class="bi bi-cash-coin"></i>
      </div>
      <h4>1. Thanh Toán Khi Nhận Hàng (COD)</h4>
      <p>Khách hàng thanh toán bằng tiền mặt trực tiếp cho nhân viên giao hàng khi nhận sản phẩm.</p>

      <p><strong>Ưu điểm:</strong></p>
      <ul class="check-list">
        <li>Không cần tài khoản ngân hàng hoặc thẻ</li>
        <li>Kiểm tra hàng trước khi thanh toán</li>
        <li>An toàn, đơn giản, dễ sử dụng</li>
      </ul>

      <p><strong>Lưu ý:</strong></p>
      <ul class="check-list">
        <li>Phí COD: 0đ (Miễn phí)</li>
        <li>Áp dụng cho tất cả đơn hàng trong phạm vi giao hàng</li>
        <li>Vui lòng chuẩn bị đủ tiền mặt khi nhận hàng</li>
      </ul>
    </div>

    <!-- 2. Chuyển Khoản -->
    <div class="payment-method-card">
      <div class="icon">
        <i class="bi bi-bank"></i>
      </div>
      <h4>2. Chuyển Khoản Ngân Hàng</h4>
      <p>Khách hàng chuyển khoản trực tiếp vào tài khoản ngân hàng của VietTech.</p>

      <p><strong>Thông tin tài khoản:</strong></p>
      <div style="background: #f8f9fa; padding: 1rem; border-radius: 8px; margin: 1rem 0;">
        <p class="mb-2"><strong>Ngân hàng:</strong> Vietcombank</p>
        <p class="mb-2"><strong>Số tài khoản:</strong> 1234567890</p>
        <p class="mb-2"><strong>Chủ tài khoản:</strong> CÔNG TY VIETTECH</p>
        <p class="mb-0"><strong>Nội dung chuyển khoản:</strong> [Mã đơn hàng] [Số điện thoại]</p>
      </div>

      <p><strong>Hướng dẫn:</strong></p>
      <ul class="check-list">
        <li>Chuyển khoản đúng số tiền và nội dung như hướng dẫn</li>
        <li>Chụp ảnh biên lai và gửi cho VietTech qua Zalo/Email</li>
        <li>VietTech xác nhận và xử lý đơn hàng trong 1-2 giờ</li>
      </ul>
    </div>

    <!-- 3. Ví Điện Tử -->
    <div class="payment-method-card">
      <div class="icon">
        <i class="bi bi-phone"></i>
      </div>
      <h4>3. Ví Điện Tử (MoMo, ZaloPay)</h4>
      <p>Thanh toán nhanh chóng qua ví điện tử MoMo hoặc ZaloPay.</p>

      <p><strong>Hướng dẫn:</strong></p>
      <ul class="check-list">
        <li>Chọn "Thanh toán qua MoMo" hoặc "ZaloPay" khi đặt hàng</li>
        <li>Quét mã QR hoặc nhập số điện thoại ví</li>
        <li>Xác nhận thanh toán trên ứng dụng</li>
        <li>Đơn hàng được xử lý ngay sau khi thanh toán thành công</li>
      </ul>

      <p><strong>Ưu điểm:</strong></p>
      <ul class="check-list">
        <li>Thanh toán nhanh chóng chỉ trong vài giây</li>
        <li>Bảo mật cao với xác thực 2 lớp</li>
        <li>Nhận ưu đãi và hoàn tiền từ ví điện tử</li>
      </ul>
    </div>

    <!-- 4. VNPay -->
    <div class="payment-method-card">
      <div class="icon">
        <i class="bi bi-credit-card-2-front"></i>
      </div>
      <h4>4. Cổng Thanh Toán VNPay</h4>
      <p>Thanh toán qua cổng VNPay với nhiều phương thức: thẻ ATM, thẻ tín dụng, QR Code.</p>

      <p><strong>Hỗ trợ các loại thẻ:</strong></p>
      <ul class="check-list">
        <li>Thẻ ATM nội địa (tất cả ngân hàng)</li>
        <li>Thẻ tín dụng/ghi nợ quốc tế (Visa, Mastercard, JCB)</li>
        <li>Thẻ nội địa có tích hợp VNPay-QR</li>
      </ul>

      <p><strong>Hướng dẫn:</strong></p>
      <ul class="check-list">
        <li>Chọn "Thanh toán qua VNPay" khi đặt hàng</li>
        <li>Chọn ngân hàng và loại thẻ</li>
        <li>Nhập thông tin thẻ và xác thực OTP</li>
        <li>Hoàn tất thanh toán</li>
      </ul>
    </div>

    <!-- 5. Thẻ Quốc Tế -->
    <div class="payment-method-card">
      <div class="icon">
        <i class="bi bi-credit-card"></i>
      </div>
      <h4>5. Thẻ Tín Dụng/Ghi Nợ Quốc Tế</h4>
      <p>Thanh toán trực tiếp bằng thẻ Visa, Mastercard, JCB.</p>

      <p><strong>Hướng dẫn:</strong></p>
      <ul class="check-list">
        <li>Chọn "Thanh toán bằng thẻ quốc tế"</li>
        <li>Nhập thông tin thẻ: số thẻ, ngày hết hạn, CVV</li>
        <li>Xác thực qua mã OTP từ ngân hàng</li>
        <li>Hoàn tất giao dịch</li>
      </ul>

      <div class="note-box">
        <strong><i class="bi bi-shield-check"></i> Bảo mật:</strong>
        <p class="mb-0">Thông tin thẻ được mã hóa SSL 256-bit. VietTech không lưu trữ thông tin thẻ của khách hàng.</p>
      </div>
    </div>

    <!-- Bảo Mật -->
    <div class="security-box">
      <i class="bi bi-shield-lock-fill"></i>
      <h4>An Toàn & Bảo Mật 100%</h4>
      <p>Tất cả giao dịch thanh toán trên VietTech được bảo vệ bởi:</p>
      <ul class="check-list" style="text-align: left; max-width: 600px; margin: 0 auto;">
        <li>Mã hóa SSL 256-bit chuẩn quốc tế</li>
        <li>Xác thực 2 lớp (OTP, Face ID, Fingerprint)</li>
        <li>Tuân thủ chuẩn bảo mật PCI DSS</li>
        <li>Không lưu trữ thông tin thẻ/tài khoản</li>
      </ul>
    </div>

    <!-- Câu Hỏi Thường Gặp -->
    <div class="policy-section">
      <h3><i class="bi bi-question-circle"></i>Câu Hỏi Thường Gặp</h3>

      <p><strong>Q: Tôi có thể đổi phương thức thanh toán sau khi đặt hàng không?</strong></p>
      <p>A: Có, bạn có thể liên hệ VietTech trong vòng 1 giờ sau khi đặt hàng để thay đổi phương thức thanh toán.</p>

      <p><strong>Q: Thanh toán online có mất phí không?</strong></p>
      <p>A: Không, VietTech hoàn toàn miễn phí mọi phương thức thanh toán.</p>

      <p><strong>Q: Tôi thanh toán rồi nhưng chưa nhận được xác nhận?</strong></p>
      <p>A: Vui lòng chờ 5-10 phút để hệ thống xử lý. Nếu vẫn chưa nhận được, liên hệ Hotline: 0866 448 892.</p>

      <p><strong>Q: Tôi có thể hủy đơn hàng sau khi thanh toán online không?</strong></p>
      <p>A: Có, bạn có thể hủy đơn trước khi giao hàng. Tiền sẽ được hoàn lại trong 3-7 ngày làm việc.</p>
    </div>

    <!-- Liên Hệ -->
    <div class="contact-box">
      <h4><i class="bi bi-headset"></i> Hỗ Trợ Thanh Toán 24/7</h4>
      <p><i class="bi bi-telephone-fill"></i> <strong>Hotline:</strong> 0866 448 892</p>
      <p><i class="bi bi-envelope-fill"></i> <strong>Email:</strong> support@viettech.vn</p>
      <p><i class="bi bi-clock-fill"></i> <strong>Giờ làm việc:</strong> 8:00 - 22:00 (Thứ 2 - Chủ nhật)</p>
      <p class="mb-0"><i class="bi bi-geo-alt-fill"></i> <strong>Địa chỉ:</strong> 01 Đ. Võ Văn Ngân, Linh Chiểu, Thủ Đức, TP.HCM</p>
    </div>

  </div>
</div>

<jsp:include page="../../footer.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
</body>
</html>