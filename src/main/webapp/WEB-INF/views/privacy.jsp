<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Chính Sách Bảo Mật - VietTech</title>
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
    .policy-section h4 {
      color: #1a1a1a;
      font-size: 1.2rem;
      font-weight: 600;
      margin-top: 1.5rem;
      margin-bottom: 1rem;
    }
    .policy-section p {
      line-height: 1.8;
      color: #495057;
      margin-bottom: 1rem;
    }
    .policy-section ul, .policy-section ol {
      line-height: 1.8;
      color: #495057;
    }
    .policy-section ul li, .policy-section ol li {
      margin-bottom: 0.5rem;
    }
    .security-badge {
      background: linear-gradient(135deg, #28a745, #20c997);
      color: white;
      padding: 1.5rem;
      border-radius: 12px;
      text-align: center;
      margin: 2rem 0;
    }
    .security-badge i {
      font-size: 3rem;
      margin-bottom: 1rem;
    }
    .highlight-box {
      background: #e7f3ff;
      border-left: 4px solid #0d6efd;
      padding: 1rem 1.5rem;
      border-radius: 8px;
      margin: 1.5rem 0;
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
    .update-date {
      text-align: center;
      color: #6c757d;
      font-style: italic;
      margin-bottom: 2rem;
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
    <h1><i class="bi bi-shield-lock"></i> Chính Sách Bảo Mật</h1>
    <p class="lead">Cam kết bảo vệ thông tin cá nhân của bạn</p>
  </div>
</div>

<div class="policy-content">
  <div class="container">

    <p class="update-date">Cập nhật lần cuối: 06/01/2025</p>

    <div class="security-badge">
      <i class="bi bi-shield-check"></i>
      <h4>Bảo Mật Thông Tin 100%</h4>
      <p class="mb-0">VietTech cam kết bảo vệ thông tin cá nhân của bạn với công nghệ mã hóa SSL 256-bit và tuân thủ nghiêm ngặt các quy định về bảo vệ dữ liệu cá nhân.</p>
    </div>

    <!-- 1. Thu Thập Thông Tin -->
    <div class="policy-section">
      <h3><i class="bi bi-collection"></i>1. Thông Tin Chúng Tôi Thu Thập</h3>

      <h4>1.1. Thông tin bạn cung cấp trực tiếp</h4>
      <p>Khi sử dụng dịch vụ VietTech, chúng tôi có thể thu thập các thông tin sau:</p>
      <ul>
        <li><strong>Thông tin tài khoản:</strong> Họ tên, email, số điện thoại, mật khẩu</li>
        <li><strong>Thông tin giao hàng:</strong> Địa chỉ, thành phố, quận/huyện, phường/xã</li>
        <li><strong>Thông tin thanh toán:</strong> Thông tin thẻ (được mã hóa bởi cổng thanh toán), phương thức thanh toán</li>
        <li><strong>Thông tin liên hệ:</strong> Nội dung tin nhắn, phản hồi, đánh giá sản phẩm</li>
      </ul>

      <h4>1.2. Thông tin thu thập tự động</h4>
      <p>Khi bạn truy cập website, chúng tôi có thể tự động thu thập:</p>
      <ul>
        <li><strong>Thông tin thiết bị:</strong> IP address, loại trình duyệt, hệ điều hành</li>
        <li><strong>Thông tin sử dụng:</strong> Trang đã xem, thời gian truy cập, lượt click</li>
        <li><strong>Cookies:</strong> Dữ liệu lưu trữ trên trình duyệt để cải thiện trải nghiệm</li>
        <li><strong>Location data:</strong> Vị trí địa lý (nếu bạn cho phép)</li>
      </ul>
    </div>

    <!-- 2. Mục Đích Sử Dụng -->
    <div class="policy-section">
      <h3><i class="bi bi-bullseye"></i>2. Mục Đích Sử Dụng Thông Tin</h3>
      <p>VietTech sử dụng thông tin của bạn cho các mục đích sau:</p>

      <h4>2.1. Cung cấp dịch vụ</h4>
      <ul>
        <li>Xử lý đơn hàng, thanh toán và giao hàng</li>
        <li>Xác nhận thông tin đặt hàng</li>
        <li>Cung cấp dịch vụ khách hàng, hỗ trợ kỹ thuật</li>
        <li>Xử lý bảo hành, đổi trả sản phẩm</li>
      </ul>

      <h4>2.2. Cải thiện dịch vụ</h4>
      <ul>
        <li>Phân tích hành vi người dùng để cải thiện website</li>
        <li>Cá nhân hóa trải nghiệm mua sắm</li>
        <li>Nghiên cứu và phát triển sản phẩm/dịch vụ mới</li>
      </ul>

      <h4>2.3. Marketing và truyền thông</h4>
      <ul>
        <li>Gửi thông báo về đơn hàng, khuyến mãi, sản phẩm mới</li>
        <li>Gửi email marketing (chỉ khi bạn đồng ý)</li>
        <li>Chương trình khách hàng thân thiết</li>
      </ul>

      <h4>2.4. Bảo mật và tuân thủ pháp luật</h4>
      <ul>
        <li>Phát hiện, ngăn chặn gian lận và lạm dụng</li>
        <li>Tuân thủ nghĩa vụ pháp lý</li>
        <li>Giải quyết tranh chấp</li>
      </ul>
    </div>

    <!-- 3. Chia Sẻ Thông Tin -->
    <div class="policy-section">
      <h3><i class="bi bi-people"></i>3. Chia Sẻ Thông Tin</h3>

      <div class="highlight-box">
        <strong><i class="bi bi-info-circle"></i> Cam kết:</strong>
        <p class="mb-0">VietTech <strong>KHÔNG BÁN, CHO THUÊ hoặc CHIA SẺ</strong> thông tin cá nhân của bạn cho bên thứ ba vì mục đích thương mại.</p>
      </div>

      <p>Chúng tôi chỉ chia sẻ thông tin trong các trường hợp sau:</p>

      <h4>3.1. Đối tác dịch vụ</h4>
      <ul>
        <li><strong>Đơn vị vận chuyển:</strong> Chia sẻ thông tin giao hàng (tên, SĐT, địa chỉ)</li>
        <li><strong>Cổng thanh toán:</strong> VNPay, MoMo, ZaloPay (thông tin thanh toán được mã hóa)</li>
        <li><strong>Dịch vụ email:</strong> Gửi thông báo đơn hàng, xác nhận</li>
      </ul>

      <h4>3.2. Yêu cầu pháp lý</h4>
      <p>Khi có yêu cầu từ cơ quan nhà nước có thẩm quyền theo quy định pháp luật.</p>

      <h4>3.3. Bảo vệ quyền lợi</h4>
      <p>Khi cần thiết để bảo vệ quyền, tài sản, an toàn của VietTech, khách hàng hoặc công chúng.</p>
    </div>

    <!-- 4. Bảo Mật Thông Tin -->
    <div class="policy-section">
      <h3><i class="bi bi-lock"></i>4. Biện Pháp Bảo Mật</h3>
      <p>VietTech áp dụng các biện pháp bảo mật tiên tiến để bảo vệ thông tin của bạn:</p>

      <h4>4.1. Bảo mật kỹ thuật</h4>
      <ul>
        <li><strong>Mã hóa SSL 256-bit:</strong> Tất cả dữ liệu truyền tải được mã hóa</li>
        <li><strong>Firewall:</strong> Bảo vệ hệ thống khỏi tấn công</li>
        <li><strong>Xác thực 2 lớp (2FA):</strong> Bảo vệ tài khoản khỏi truy cập trái phép</li>
        <li><strong>Mã hóa mật khẩu:</strong> Sử dụng thuật toán BCrypt</li>
      </ul>

      <h4>4.2. Bảo mật tổ chức</h4>
      <ul>
        <li>Chỉ nhân viên được ủy quyền mới có quyền truy cập thông tin</li>
        <li>Đào tạo nhân viên về bảo mật thông tin</li>
        <li>Kiểm tra, đánh giá bảo mật định kỳ</li>
        <li>Sao lưu dữ liệu thường xuyên</li>
      </ul>

      <div class="note-box">
        <strong><i class="bi bi-exclamation-triangle"></i> Lưu ý:</strong>
        <p class="mb-0">Không có hệ thống nào an toàn 100%. Bạn nên bảo mật thông tin đăng nhập và không chia sẻ với người khác. Nếu phát hiện tài khoản bị xâm nhập, vui lòng thông báo ngay cho chúng tôi.</p>
      </div>
    </div>

    <!-- 5. Quyền Của Bạn -->
    <div class="policy-section">
      <h3><i class="bi bi-person-check"></i>5. Quyền Của Bạn</h3>
      <p>Bạn có các quyền sau đối với thông tin cá nhân của mình:</p>

      <h4>5.1. Quyền truy cập</h4>
      <p>Bạn có quyền yêu cầu biết thông tin cá nhân nào của bạn mà chúng tôi đang lưu trữ.</p>

      <h4>5.2. Quyền chỉnh sửa</h4>
      <p>Bạn có thể cập nhật, chỉnh sửa thông tin cá nhân trên trang "Tài khoản của tôi".</p>

      <h4>5.3. Quyền xóa</h4>
      <p>Bạn có quyền yêu cầu xóa thông tin cá nhân (trừ khi chúng tôi cần giữ lại vì lý do pháp lý).</p>

      <h4>5.4. Quyền phản đối</h4>
      <p>Bạn có quyền từ chối nhận email marketing bằng cách click "Hủy đăng ký" trong email.</p>

      <h4>5.5. Quyền khiếu nại</h4>
      <p>Nếu không hài lòng với cách chúng tôi xử lý thông tin, bạn có quyền khiếu nại đến cơ quan chức năng.</p>

      <div class="highlight-box">
        <strong>Cách thực hiện quyền:</strong>
        <p class="mb-0">Liên hệ qua email: <a href="mailto:support@viettech.vn">support@viettech.vn</a> hoặc Hotline: <strong>0866 448 892</strong></p>
      </div>
    </div>

    <!-- 6. Cookies -->
    <div class="policy-section">
      <h3><i class="bi bi-cookie"></i>6. Chính Sách Cookies</h3>
      <p>VietTech sử dụng cookies để cải thiện trải nghiệm người dùng.</p>

      <h4>6.1. Cookies là gì?</h4>
      <p>Cookies là các tệp văn bản nhỏ được lưu trữ trên trình duyệt của bạn khi truy cập website.</p>

      <h4>6.2. Loại cookies chúng tôi sử dụng</h4>
      <ul>
        <li><strong>Cookies cần thiết:</strong> Giúp website hoạt động (đăng nhập, giỏ hàng)</li>
        <li><strong>Cookies phân tích:</strong> Theo dõi hành vi người dùng để cải thiện dịch vụ</li>
        <li><strong>Cookies marketing:</strong> Hiển thị quảng cáo phù hợp</li>
      </ul>

      <h4>6.3. Quản lý cookies</h4>
      <p>Bạn có thể xóa hoặc chặn cookies thông qua cài đặt trình duyệt. Tuy nhiên, điều này có thể ảnh hưởng đến trải nghiệm sử dụng website.</p>
    </div>

    <!-- 7. Lưu Trữ Dữ Liệu -->
    <div class="policy-section">
      <h3><i class="bi bi-archive"></i>7. Lưu Trữ Và Xóa Dữ Liệu</h3>

      <h4>7.1. Thời gian lưu trữ</h4>
      <p>Chúng tôi lưu trữ thông tin của bạn trong thời gian:</p>
      <ul>
        <li>Bạn còn sử dụng tài khoản VietTech</li>
        <li>Cần thiết để cung cấp dịch vụ (lịch sử đơn hàng, bảo hành)</li>
        <li>Theo yêu cầu pháp luật (hóa đơn, giao dịch)</li>
      </ul>

      <h4>7.2. Xóa dữ liệu</h4>
      <p>Khi bạn yêu cầu xóa tài khoản, chúng tôi sẽ:</p>
      <ul>
        <li>Xóa thông tin cá nhân không còn cần thiết</li>
        <li>Giữ lại một số thông tin tối thiểu theo yêu cầu pháp luật</li>
        <li>Ẩn danh hóa dữ liệu phân tích</li>
      </ul>
    </div>

    <!-- 8. Trẻ Em -->
    <div class="policy-section">
      <h3><i class="bi bi-people"></i>8. Bảo Vệ Trẻ Em</h3>
      <p>Dịch vụ VietTech không dành cho trẻ em dưới 18 tuổi. Chúng tôi không cố ý thu thập thông tin của trẻ em. Nếu phát hiện, chúng tôi sẽ xóa ngay.</p>
    </div>

    <!-- 9. Thay Đổi Chính Sách -->
    <div class="policy-section">
      <h3><i class="bi bi-arrow-repeat"></i>9. Thay Đổi Chính Sách Bảo Mật</h3>
      <p>VietTech có thể cập nhật chính sách này theo thời gian. Chúng tôi sẽ thông báo các thay đổi quan trọng qua:</p>
      <ul>
        <li>Email đến địa chỉ bạn đã đăng ký</li>
        <li>Thông báo trên website</li>
      </ul>
      <p>Phiên bản mới có hiệu lực ngay khi được đăng tải.</p>
    </div>

    <!-- 10. Liên Hệ -->
    <div class="policy-section">
      <h3><i class="bi bi-envelope"></i>10. Liên Hệ</h3>
      <p>Nếu bạn có thắc mắc về Chính sách bảo mật, vui lòng liên hệ:</p>
      <div class="highlight-box">
        <p><strong>CÔNG TY TNHH VIETTECH</strong></p>
        <p><i class="bi bi-geo-alt-fill"></i> Địa chỉ: 01 Đ. Võ Văn Ngân, Linh Chiểu, Thủ Đức, TP. Hồ Chí Minh</p>
        <p><i class="bi bi-telephone-fill"></i> Hotline: 0866 448 892</p>
        <p><i class="bi bi-envelope-fill"></i> Email: support@viettech.vn</p>
        <p class="mb-0"><i class="bi bi-globe"></i> Website: viettechstore.online</p>
      </div>
    </div>

  </div>
</div>

<jsp:include page="../../footer.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
</body>
</html>