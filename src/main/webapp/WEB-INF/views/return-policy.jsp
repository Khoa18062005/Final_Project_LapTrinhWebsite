<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Chính Sách Đổi Trả - VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
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
    .timeline {
      position: relative;
      padding-left: 2rem;
    }
    .timeline-item {
      position: relative;
      padding-bottom: 2rem;
    }
    .timeline-item::before {
      content: '';
      position: absolute;
      left: -2rem;
      top: 0;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #0d6efd;
      border: 3px solid white;
      box-shadow: 0 0 0 3px #0d6efd;
    }
    .timeline-item::after {
      content: '';
      position: absolute;
      left: -1.7rem;
      top: 12px;
      width: 2px;
      height: calc(100% - 12px);
      background: #e9ecef;
    }
    .timeline-item:last-child::after {
      display: none;
    }
    .check-list {
      list-style: none;
      padding: 0;
    }
    .check-list li {
      padding: 0.75rem 0;
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
    .x-list {
      list-style: none;
      padding: 0;
    }
    .x-list li {
      padding: 0.75rem 0;
      padding-left: 2rem;
      position: relative;
      line-height: 1.6;
    }
    .x-list li::before {
      content: '✗';
      position: absolute;
      left: 0;
      color: #dc3545;
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
    .fee-table {
      width: 100%;
      margin: 1.5rem 0;
      border-collapse: collapse;
    }
    .fee-table th {
      background: linear-gradient(135deg, #0d6efd, #0b5ed7);
      color: white;
      padding: 1rem;
      font-weight: 600;
      text-align: left;
    }
    .fee-table td {
      padding: 1rem;
      border: 1px solid #e9ecef;
    }
    .fee-table tbody tr:nth-child(even) {
      background-color: #f8f9fa;
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

<div class="policy-hero">
  <div class="container">
    <h1><i class="bi bi-arrow-repeat"></i> Chính Sách Đổi Trả</h1>
    <p class="lead">Đổi trả dễ dàng, minh bạch và nhanh chóng</p>
  </div>
</div>

<div class="policy-content">
  <div class="container">

    <!-- Tổng Quan -->
    <div class="policy-section">
      <h3><i class="bi bi-info-circle"></i>Tổng Quan</h3>
      <p>VietTech cam kết mang đến trải nghiệm mua sắm tốt nhất cho khách hàng với chính sách đổi trả linh hoạt, minh bạch và thuận tiện.</p>
      <p>Chúng tôi chấp nhận đổi trả sản phẩm trong các trường hợp sau:</p>
      <ul class="check-list">
        <li>Sản phẩm bị lỗi do nhà sản xuất</li>
        <li>Sản phẩm bị hư hỏng trong quá trình vận chuyển</li>
        <li>Giao sai mẫu mã, màu sắc, cấu hình</li>
        <li>Sản phẩm không đúng như mô tả</li>
        <li>Khách hàng muốn đổi sang sản phẩm khác</li>
      </ul>
    </div>

    <!-- Thời Hạn Đổi Trả -->
    <div class="policy-section">
      <h3><i class="bi bi-clock-history"></i>Thời Hạn Đổi Trả</h3>
      <table class="fee-table">
        <thead>
        <tr>
          <th>Trường Hợp</th>
          <th>Thời Hạn</th>
          <th>Điều Kiện</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td><strong>Lỗi do nhà sản xuất</strong></td>
          <td>30 ngày</td>
          <td>Đổi mới hoặc hoàn tiền 100%</td>
        </tr>
        <tr>
          <td><strong>Giao sai sản phẩm</strong></td>
          <td>7 ngày</td>
          <td>Đổi đúng sản phẩm, miễn phí vận chuyển</td>
        </tr>
        <tr>
          <td><strong>Sản phẩm bị hư hỏng khi nhận</strong></td>
          <td>7 ngày</td>
          <td>Đổi mới hoặc hoàn tiền 100%</td>
        </tr>
        <tr>
          <td><strong>Đổi ý, muốn đổi sản phẩm khác</strong></td>
          <td>7 ngày</td>
          <td>Sản phẩm chưa sử dụng, còn nguyên tem</td>
        </tr>
        </tbody>
      </table>

      <div class="note-box">
        <strong><i class="bi bi-exclamation-triangle"></i> Lưu ý:</strong>
        <p class="mb-0">Thời hạn đổi trả được tính từ ngày khách hàng nhận hàng. Vui lòng giữ hóa đơn và phiếu bảo hành để được hỗ trợ nhanh chóng.</p>
      </div>
    </div>

    <!-- Điều Kiện Đổi Trả -->
    <div class="policy-section">
      <h3><i class="bi bi-check-circle"></i>Điều Kiện Đổi Trả</h3>
      <p>Sản phẩm được chấp nhận đổi trả khi đáp ứng <strong>TẤT CẢ</strong> các điều kiện sau:</p>
      <ul class="check-list">
        <li>Sản phẩm còn trong thời hạn đổi trả (7-30 ngày tùy trường hợp)</li>
        <li>Có hóa đơn mua hàng hợp lệ từ VietTech</li>
        <li>Sản phẩm còn nguyên vẹn, chưa qua sử dụng (trừ trường hợp lỗi)</li>
        <li>Tem niêm phong, tem bảo hành còn nguyên vẹn</li>
        <li>Hộp đựng, phụ kiện, sách hướng dẫn còn đầy đủ</li>
        <li>Không có dấu hiệu va đập, trầy xước do người dùng</li>
      </ul>
    </div>

    <!-- Trường Hợp Không Đổi Trả -->
    <div class="policy-section">
      <h3><i class="bi bi-x-circle"></i>Trường Hợp Không Chấp Nhận Đổi Trả</h3>
      <p>VietTech <strong>KHÔNG</strong> chấp nhận đổi trả trong các trường hợp sau:</p>
      <ul class="x-list">
        <li>Sản phẩm đã qua thời hạn đổi trả</li>
        <li>Không có hóa đơn mua hàng hoặc chứng từ hợp lệ</li>
        <li>Sản phẩm đã qua sử dụng, có dấu hiệu đã kích hoạt</li>
        <li>Tem niêm phong, tem bảo hành bị rách, mờ hoặc tẩy xóa</li>
        <li>Thiếu phụ kiện, sách hướng dẫn, hộp đựng</li>
        <li>Sản phẩm bị hư hỏng do lỗi người dùng: rơi vỡ, va đập, ngấm nước</li>
        <li>Sản phẩm đã được sửa chữa bởi bên thứ ba</li>
        <li>Sản phẩm khuyến mãi, giảm giá đặc biệt (trừ lỗi kỹ thuật)</li>
      </ul>
    </div>

    <!-- Quy Trình Đổi Trả -->
    <div class="policy-section">
      <h3><i class="bi bi-arrow-clockwise"></i>Quy Trình Đổi Trả</h3>
      <div class="timeline">
        <div class="timeline-item">
          <h5><strong>Bước 1: Liên hệ VietTech</strong></h5>
          <ul class="check-list">
            <li>Gọi Hotline: 0866 448 892</li>
            <li>Hoặc gửi email: support@viettech.vn</li>
            <li>Cung cấp mã đơn hàng, thông tin sản phẩm và lý do đổi trả</li>
          </ul>
        </div>

        <div class="timeline-item">
          <h5><strong>Bước 2: Xác nhận yêu cầu</strong></h5>
          <ul class="check-list">
            <li>VietTech kiểm tra thông tin và xác nhận điều kiện đổi trả</li>
            <li>Hướng dẫn khách hàng đóng gói sản phẩm</li>
            <li>Cung cấp địa chỉ gửi hoặc đặt lịch nhân viên đến lấy hàng</li>
          </ul>
        </div>

        <div class="timeline-item">
          <h5><strong>Bước 3: Gửi sản phẩm về</strong></h5>
          <ul class="check-list">
            <li>Đóng gói cẩn thận, đầy đủ phụ kiện, hóa đơn</li>
            <li>Gửi về địa chỉ VietTech hoặc giao cho nhân viên đến lấy</li>
            <li>Giữ mã vận đơn để tra cứu</li>
          </ul>
        </div>

        <div class="timeline-item">
          <h5><strong>Bước 4: Kiểm tra sản phẩm</strong></h5>
          <ul class="check-list">
            <li>VietTech nhận và kiểm tra sản phẩm (1-2 ngày làm việc)</li>
            <li>Xác nhận tình trạng sản phẩm và điều kiện đổi trả</li>
            <li>Thông báo kết quả cho khách hàng</li>
          </ul>
        </div>

        <div class="timeline-item">
          <h5><strong>Bước 5: Đổi sản phẩm hoặc hoàn tiền</strong></h5>
          <ul class="check-list">
            <li>Đổi sản phẩm mới: Giao hàng trong 2-5 ngày</li>
            <li>Hoàn tiền: Chuyển khoản trong 3-7 ngày làm việc</li>
            <li>Thông báo khi hoàn tất</li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Phí Đổi Trả -->
    <div class="policy-section">
      <h3><i class="bi bi-cash-stack"></i>Phí Đổi Trả</h3>
      <table class="fee-table">
        <thead>
        <tr>
          <th>Trường Hợp</th>
          <th>Phí Vận Chuyển</th>
          <th>Phí Xử Lý</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td><strong>Lỗi do VietTech/Nhà sản xuất</strong></td>
          <td>MIỄN PHÍ</td>
          <td>MIỄN PHÍ</td>
        </tr>
        <tr>
          <td><strong>Giao sai sản phẩm</strong></td>
          <td>MIỄN PHÍ</td>
          <td>MIỄN PHÍ</td>
        </tr>
        <tr>
          <td><strong>Đổi ý (trong 7 ngày)</strong></td>
          <td>Khách hàng chịu phí 2 chiều</td>
          <td>Không</td>
        </tr>
        <tr>
          <td><strong>Đổi sang sản phẩm khác</strong></td>
          <td>Khách hàng chịu phí 2 chiều</td>
          <td>Không</td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Hoàn Tiền -->
    <div class="policy-section">
      <h3><i class="bi bi-cash-coin"></i>Chính Sách Hoàn Tiền</h3>
      <p><strong>Thời gian hoàn tiền:</strong> 3-7 ngày làm việc sau khi VietTech xác nhận đổi trả</p>

      <p><strong>Hình thức hoàn tiền:</strong></p>
      <ul class="check-list">
        <li><strong>Thanh toán chuyển khoản:</strong> Hoàn về tài khoản ngân hàng</li>
        <li><strong>Thanh toán COD:</strong> Chuyển khoản về tài khoản khách hàng cung cấp</li>
        <li><strong>Thanh toán qua ví điện tử:</strong> Hoàn về ví (MoMo, ZaloPay)</li>
        <li><strong>Thanh toán qua cổng VNPay:</strong> Hoàn về thẻ/tài khoản gốc</li>
      </ul>

      <div class="note-box">
        <strong><i class="bi bi-info-circle"></i> Lưu ý:</strong>
        <p class="mb-0">Số tiền hoàn lại bao gồm giá trị sản phẩm và phí vận chuyển ban đầu (nếu lỗi thuộc VietTech). Phí giao hàng khi đổi trả do khách hàng chịu (trừ trường hợp lỗi).</p>
      </div>
    </div>

    <!-- Liên Hệ -->
    <div class="contact-box">
      <h4><i class="bi bi-headset"></i> Hỗ Trợ Đổi Trả 24/7</h4>
      <p><i class="bi bi-telephone-fill"></i> <strong>Hotline:</strong> 0866 448 892</p>
      <p><i class="bi bi-envelope-fill"></i> <strong>Email:</strong> support@viettech.vn</p>
      <p><i class="bi bi-clock-fill"></i> <strong>Giờ làm việc:</strong> 8:00 - 22:00 (Thứ 2 - Chủ nhật)</p>
      <p class="mb-0"><i class="bi bi-geo-alt-fill"></i> <strong>Địa chỉ:</strong> 01 Đ. Võ Văn Ngân, Linh Chiểu, Thủ Đức, TP.HCM</p>
    </div>

  </div>
</div>

<jsp:include page="../../footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>