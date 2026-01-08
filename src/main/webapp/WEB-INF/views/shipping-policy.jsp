<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Chính Sách Vận Chuyển - VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shipping-policy.css">
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
    <h1><i class="bi bi-truck"></i> Chính Sách Vận Chuyển</h1>
    <p class="lead">Giao hàng nhanh chóng, an toàn trên toàn quốc</p>
  </div>
</div>

<div class="policy-content">
  <div class="container">

    <!-- Phạm Vi Vận Chuyển -->
    <div class="policy-section">
      <h3><i class="bi bi-geo-alt"></i>Phạm Vi Vận Chuyển</h3>
      <p>VietTech hỗ trợ giao hàng <strong>toàn quốc 63 tỉnh thành</strong> với đội ngũ đối tác vận chuyển uy tín:</p>
      <ul class="check-list">
        <li><strong>Nội thành TP.HCM:</strong> 1-2 ngày làm việc</li>
        <li><strong>Ngoại thành TP.HCM & các tỉnh Miền Nam:</strong> 2-3 ngày làm việc</li>
        <li><strong>Miền Trung & Miền Bắc:</strong> 3-5 ngày làm việc</li>
        <li><strong>Khu vực xa, miền núi:</strong> 5-7 ngày làm việc</li>
      </ul>

      <div class="note-box">
        <strong><i class="bi bi-exclamation-triangle"></i> Lưu ý:</strong>
        <p class="mb-0">Thời gian giao hàng có thể kéo dài hơn trong dịp lễ, Tết hoặc do điều kiện thời tiết, giao thông. VietTech sẽ thông báo kịp thời nếu có thay đổi.</p>
      </div>

      <p class="mt-3"><strong>Đối tác vận chuyển:</strong></p>
      <div class="partner-logo">
        <span class="partner-badge">Giao Hàng Nhanh</span>
        <span class="partner-badge">Giao Hàng Tiết Kiệm</span>
        <span class="partner-badge">J&T Express</span>
        <span class="partner-badge">Viettel Post</span>
        <span class="partner-badge">VNPost</span>
      </div>
    </div>

    <!-- Phí Vận Chuyển -->
    <div class="policy-section">
      <h3><i class="bi bi-cash-coin"></i>Phí Vận Chuyển</h3>
      <table class="shipping-fee-table">
        <thead>
        <tr>
          <th>Khu Vực</th>
          <th>Phí Vận Chuyển</th>
          <th>Thời Gian Dự Kiến</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td><strong>Nội thành TP.HCM</strong></td>
          <td>30.000đ</td>
          <td>1-2 ngày</td>
        </tr>
        <tr>
          <td><strong>Ngoại thành TP.HCM</strong></td>
          <td>40.000đ</td>
          <td>2-3 ngày</td>
        </tr>
        <tr>
          <td><strong>Các tỉnh Miền Nam</strong></td>
          <td>50.000đ</td>
          <td>2-3 ngày</td>
        </tr>
        <tr>
          <td><strong>Miền Trung</strong></td>
          <td>70.000đ</td>
          <td>3-5 ngày</td>
        </tr>
        <tr>
          <td><strong>Miền Bắc</strong></td>
          <td>70.000đ</td>
          <td>3-5 ngày</td>
        </tr>
        <tr>
          <td><strong>Khu vực xa, miền núi</strong></td>
          <td>100.000đ</td>
          <td>5-7 ngày</td>
        </tr>
        </tbody>
      </table>

      <div class="free-ship-box">
        <i class="bi bi-gift"></i>
        <div>MIỄN PHÍ GIAO HÀNG</div>
        <div>Cho đơn hàng từ 100.000.000đ trở lên!</div>
      </div>
    </div>

    <!-- Kiểm Tra Hàng Khi Nhận -->
    <div class="policy-section">
      <h3><i class="bi bi-box-seam"></i>Kiểm Tra Hàng Khi Nhận</h3>
      <p>Khách hàng có quyền <strong>kiểm tra sản phẩm trước khi thanh toán</strong> và ký nhận với shipper:</p>

      <p><strong>Bước 1: Kiểm tra bên ngoài</strong></p>
      <ul class="check-list">
        <li>Kiểm tra hộp đựng: nguyên vẹn, không rách, móp méo, dính băng keo lạ</li>
        <li>Kiểm tra tem niêm phong: còn nguyên vẹn, chưa bị mở</li>
      </ul>

      <p><strong>Bước 2: Mở hộp kiểm tra sản phẩm</strong></p>
      <ul class="check-list">
        <li>Đúng mẫu mã, màu sắc, cấu hình như đã đặt</li>
        <li>Đúng số lượng sản phẩm và phụ kiện đi kèm</li>
        <li>Kiểm tra tình trạng sản phẩm: không trầy xước, vỡ, hư hỏng</li>
        <li>Kiểm tra phụ kiện: sạc, cáp, tai nghe, sách hướng dẫn, phiếu bảo hành</li>
      </ul>

      <p><strong>Bước 3: Bật máy kiểm tra (nếu có thể)</strong></p>
      <ul class="check-list">
        <li>Máy bật nguồn bình thường</li>
        <li>Màn hình không bị lỗi, chết điểm</li>
        <li>Các phím bấm, cổng kết nối hoạt động tốt</li>
      </ul>

      <div class="note-box mt-3">
        <strong><i class="bi bi-shield-check"></i> Quyền của khách hàng:</strong>
        <p class="mb-0">Nếu phát hiện sản phẩm không đúng, bị hư hỏng hoặc thiếu phụ kiện, khách hàng có quyền <strong>TỪ CHỐI NHẬN HÀNG</strong> mà không mất bất kỳ chi phí nào. VietTech sẽ xử lý và gửi lại sản phẩm mới.</p>
      </div>
    </div>

    <!-- Chính Sách Giao Hàng -->
    <div class="policy-section">
      <h3><i class="bi bi-calendar-check"></i>Lưu Ý Khi Giao Hàng</h3>
      <ul class="check-list">
        <li><strong>Xác nhận đơn hàng:</strong> VietTech sẽ gọi điện xác nhận đơn hàng trong vòng 2-4 giờ sau khi đặt</li>
        <li><strong>Đóng gói:</strong> Sản phẩm được đóng gói cẩn thận, chống sốc, chống nước</li>
        <li><strong>Mã vận đơn:</strong> Khách hàng nhận mã vận đơn để tra cứu tình trạng đơn hàng</li>
        <li><strong>Giao hàng 2 lần:</strong> Nếu lần 1 không giao được, shipper sẽ liên hệ giao lại lần 2</li>
        <li><strong>Thông báo trước:</strong> Shipper sẽ gọi điện trước khi giao hàng 15-30 phút</li>
      </ul>

      <div class="note-box mt-3">
        <strong><i class="bi bi-telephone"></i> Không liên lạc được:</strong>
        <p class="mb-0">Nếu sau 2 lần giao hàng không liên lạc được với khách hàng, đơn hàng sẽ được hoàn về kho. Khách hàng cần liên hệ VietTech để sắp xếp giao lại.</p>
      </div>
    </div>

    <!-- Chính Sách Hoàn/Hủy Đơn -->
    <div class="policy-section">
      <h3><i class="bi bi-arrow-return-left"></i>Hoàn Tiền & Hủy Đơn Hàng</h3>

      <p><strong>Hủy đơn hàng trước khi giao:</strong></p>
      <ul class="check-list">
        <li>Khách hàng có thể hủy đơn hàng miễn phí nếu đơn hàng chưa được giao cho đơn vị vận chuyển</li>
        <li>Sau khi đơn hàng đã giao cho shipper, khách hàng phải chịu phí vận chuyển 2 chiều nếu hủy</li>
      </ul>

      <p><strong>Từ chối nhận hàng:</strong></p>
      <ul class="check-list">
        <li>Khách hàng từ chối nhận hàng do sản phẩm lỗi: <strong>MIỄN PHÍ</strong></li>
        <li>Khách hàng từ chối nhận hàng do thay đổi ý định: <strong>chịu phí vận chuyển 2 chiều</strong></li>
      </ul>
    </div>

    <!-- Liên Hệ -->
    <div class="contact-box">
      <h4><i class="bi bi-headset"></i> Hỗ Trợ Vận Chuyển 24/7</h4>
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