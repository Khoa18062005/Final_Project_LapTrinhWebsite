<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Câu Hỏi Thường Gặp - VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/faq.css">
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
    <h1><i class="bi bi-question-circle"></i> Câu Hỏi Thường Gặp</h1>
    <p class="lead">Giải đáp mọi thắc mắc của bạn</p>
  </div>
</div>

<div class="faq-content">
  <div class="container">

    <!-- Search Box -->
    <div class="search-box">
      <h4 class="mb-3">Tìm kiếm câu hỏi</h4>
      <input type="text" id="faqSearch" class="form-control" placeholder="Nhập từ khóa tìm kiếm...">
    </div>

    <!-- Đặt Hàng & Thanh Toán -->
    <div class="faq-category">
      <h3><i class="bi bi-cart"></i>Đặt Hàng & Thanh Toán</h3>

      <div class="faq-item">
        <div class="faq-question">
          <span>Làm thế nào để đặt hàng trên VietTech?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Bạn có thể đặt hàng dễ dàng qua 3 bước:</p>
          <ol>
            <li>Chọn sản phẩm và thêm vào giỏ hàng</li>
            <li>Điền thông tin giao hàng và chọn phương thức thanh toán</li>
            <li>Xác nhận đơn hàng và chờ VietTech liên hệ xác nhận</li>
          </ol>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>VietTech hỗ trợ những phương thức thanh toán nào?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Chúng tôi hỗ trợ đa dạng phương thức thanh toán:</p>
          <ul>
            <li>Thanh toán khi nhận hàng (COD)</li>
            <li>Chuyển khoản ngân hàng</li>
            <li>Ví điện tử: MoMo, ZaloPay</li>
            <li>Cổng thanh toán VNPay</li>
            <li>Thẻ tín dụng/ghi nợ quốc tế</li>
          </ul>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Tôi có thể hủy đơn hàng không?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Có, bạn có thể hủy đơn hàng miễn phí nếu đơn hàng chưa được giao cho đơn vị vận chuyển. Sau khi đơn hàng đã giao cho shipper, bạn có thể từ chối nhận hàng nhưng phải chịu phí vận chuyển 2 chiều.</p>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Thanh toán online có an toàn không?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Hoàn toàn an toàn! Tất cả giao dịch được bảo vệ bởi:</p>
          <ul>
            <li>Mã hóa SSL 256-bit chuẩn quốc tế</li>
            <li>Xác thực 2 lớp (OTP)</li>
            <li>Tuân thủ chuẩn bảo mật PCI DSS</li>
            <li>VietTech không lưu trữ thông tin thẻ của khách hàng</li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Vận Chuyển & Giao Hàng -->
    <div class="faq-category">
      <h3><i class="bi bi-truck"></i>Vận Chuyển & Giao Hàng</h3>

      <div class="faq-item">
        <div class="faq-question">
          <span>Bao lâu thì tôi nhận được hàng?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Thời gian giao hàng phụ thuộc vào khu vực:</p>
          <ul>
            <li>Nội thành TP.HCM: 1-2 ngày</li>
            <li>Ngoại thành & Miền Nam: 2-3 ngày</li>
            <li>Miền Trung & Miền Bắc: 3-5 ngày</li>
            <li>Khu vực xa: 5-7 ngày</li>
          </ul>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Phí vận chuyển là bao nhiêu?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Phí vận chuyển từ 30.000đ - 100.000đ tùy khu vực. <strong>MIỄN PHÍ SHIP</strong> cho đơn hàng từ 2.000.000đ trở lên!</p>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Tôi có thể kiểm tra hàng trước khi thanh toán không?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Có! Bạn hoàn toàn có quyền kiểm tra sản phẩm trước khi thanh toán cho shipper. Nếu phát hiện sản phẩm không đúng hoặc bị hư hỏng, bạn có quyền từ chối nhận hàng.</p>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Nếu tôi không ở nhà khi shipper giao hàng thì sao?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Shipper sẽ gọi điện trước khi giao hàng. Nếu bạn không có mặt, shipper sẽ liên hệ để sắp xếp giao lại lần 2. Sau 2 lần giao không thành công, đơn hàng sẽ được hoàn về kho.</p>
        </div>
      </div>
    </div>

    <!-- Bảo Hành & Đổi Trả -->
    <div class="faq-category">
      <h3><i class="bi bi-arrow-repeat"></i>Bảo Hành & Đổi Trả</h3>

      <div class="faq-item">
        <div class="faq-question">
          <span>Sản phẩm được bảo hành bao lâu?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Thời gian bảo hành tùy loại sản phẩm:</p>
          <ul>
            <li>Điện thoại: 12 tháng</li>
            <li>Laptop: 12-24 tháng</li>
            <li>Máy tính bảng: 12 tháng</li>
            <li>Tai nghe: 6-12 tháng</li>
            <li>Phụ kiện: 3-12 tháng</li>
          </ul>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Tôi có thể đổi trả sản phẩm không?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Có! VietTech chấp nhận đổi trả trong các trường hợp:</p>
          <ul>
            <li>Lỗi do nhà sản xuất: 30 ngày</li>
            <li>Giao sai sản phẩm: 7 ngày</li>
            <li>Hư hỏng khi nhận: 7 ngày</li>
            <li>Đổi ý, muốn đổi sản phẩm khác: 7 ngày (sản phẩm chưa sử dụng)</li>
          </ul>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Quy trình bảo hành như thế nào?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Quy trình bảo hành gồm 4 bước đơn giản:</p>
          <ol>
            <li>Mang sản phẩm và phiếu bảo hành đến trung tâm bảo hành</li>
            <li>Nhân viên kiểm tra và lập phiếu tiếp nhận</li>
            <li>Chờ xử lý (7-15 ngày)</li>
            <li>Nhận sản phẩm sau khi hoàn thành</li>
          </ol>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Chi phí bảo hành là bao nhiêu?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Bảo hành hoàn toàn MIỄN PHÍ trong thời gian bảo hành nếu lỗi do nhà sản xuất. Chi phí chỉ phát sinh khi sản phẩm hết bảo hành hoặc lỗi do người dùng.</p>
        </div>
      </div>
    </div>

    <!-- Tài Khoản -->
    <div class="faq-category">
      <h3><i class="bi bi-person"></i>Tài Khoản</h3>

      <div class="faq-item">
        <div class="faq-question">
          <span>Làm thế nào để đăng ký tài khoản?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Bạn có thể đăng ký tài khoản miễn phí qua 3 bước:</p>
          <ol>
            <li>Click "Đăng ký" ở góc trên cùng</li>
            <li>Điền thông tin: email, mật khẩu, số điện thoại</li>
            <li>Xác thực OTP và hoàn tất đăng ký</li>
          </ol>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Tôi quên mật khẩu, phải làm sao?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Click vào "Quên mật khẩu" ở trang đăng nhập, nhập email đã đăng ký. VietTech sẽ gửi mã OTP để bạn đặt lại mật khẩu mới.</p>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Làm thế nào để thay đổi thông tin cá nhân?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Sau khi đăng nhập, vào "Tài khoản cá nhân" > "Chỉnh sửa thông tin" để cập nhật họ tên, số điện thoại, địa chỉ.</p>
        </div>
      </div>
    </div>

    <!-- Sản Phẩm -->
    <div class="faq-category">
      <h3><i class="bi bi-box-seam"></i>Sản Phẩm</h3>

      <div class="faq-item">
        <div class="faq-question">
          <span>Sản phẩm có chính hãng không?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>VietTech cam kết 100% sản phẩm chính hãng, có đầy đủ tem nhãn, hóa đơn và phiếu bảo hành từ nhà sản xuất. Nếu phát hiện hàng giả, VietTech hoàn tiền gấp 10 lần.</p>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Làm sao để biết sản phẩm còn hàng không?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Trên trang sản phẩm sẽ hiển thị trạng thái "Còn hàng" hoặc "Hết hàng". Bạn cũng có thể liên hệ Hotline để kiểm tra tình trạng kho.</p>
        </div>
      </div>

      <div class="faq-item">
        <div class="faq-question">
          <span>Tôi có thể xem sản phẩm trực tiếp không?</span>
          <i class="bi bi-chevron-down"></i>
        </div>
        <div class="faq-answer">
          <p>Có! Bạn có thể đến cửa hàng VietTech tại: 01 Đ. Võ Văn Ngân, Linh Chiểu, Thủ Đức, TP.HCM để xem và trải nghiệm sản phẩm trực tiếp.</p>
        </div>
      </div>
    </div>

    <!-- Liên Hệ -->
    <div class="contact-box">
      <h4><i class="bi bi-headset"></i> Không tìm thấy câu trả lời?</h4>
      <p><i class="bi bi-telephone-fill"></i> <strong>Hotline:</strong> 0866 448 892</p>
      <p><i class="bi bi-envelope-fill"></i> <strong>Email:</strong> support@viettech.vn</p>
      <p class="mb-0">Chúng tôi luôn sẵn sàng hỗ trợ bạn!</p>
    </div>

  </div>
</div>

<jsp:include page="../../footer.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/faq.js"></script>
</body>
</html>