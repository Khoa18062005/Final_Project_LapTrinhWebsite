<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Điều Khoản Sử Dụng - VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/terms.css">
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
    <h1><i class="bi bi-file-text"></i> Điều Khoản Sử Dụng</h1>
    <p class="lead">Quy định và điều khoản khi sử dụng dịch vụ VietTech</p>
  </div>
</div>

<div class="policy-content">
  <div class="container">

    <p class="update-date">Cập nhật lần cuối: 06/01/2025</p>

    <!-- 1. Giới Thiệu -->
    <div class="policy-section">
      <h3><i class="bi bi-info-circle"></i>1. Giới Thiệu</h3>
      <p>Chào mừng bạn đến với <strong>VietTech</strong> (viettechstore.online). Khi truy cập và sử dụng website của chúng tôi, bạn đồng ý tuân thủ và chịu sự ràng buộc bởi các điều khoản và điều kiện sử dụng sau đây.</p>
      <p>Vui lòng đọc kỹ các điều khoản này trước khi sử dụng dịch vụ. Nếu bạn không đồng ý với bất kỳ phần nào của các điều khoản này, vui lòng không sử dụng website của chúng tôi.</p>

      <div class="highlight-box">
        <strong>Định nghĩa:</strong>
        <ul class="mb-0">
          <li><strong>"VietTech"</strong> hoặc <strong>"Chúng tôi"</strong>: Công ty TNHH VietTech, chủ sở hữu và vận hành website viettechstore.online</li>
          <li><strong>"Khách hàng"</strong> hoặc <strong>"Bạn"</strong>: Người dùng truy cập và sử dụng dịch vụ của VietTech</li>
          <li><strong>"Dịch vụ"</strong>: Tất cả các dịch vụ mua bán, tư vấn, bảo hành và hỗ trợ khách hàng của VietTech</li>
        </ul>
      </div>
    </div>

    <!-- 2. Đăng Ký Tài Khoản -->
    <div class="policy-section">
      <h3><i class="bi bi-person-plus"></i>2. Đăng Ký Và Quản Lý Tài Khoản</h3>

      <h4>2.1. Điều kiện đăng ký</h4>
      <ul>
        <li>Bạn phải từ đủ 18 tuổi trở lên hoặc có sự đồng ý của cha mẹ/người giám hộ hợp pháp</li>
        <li>Cung cấp thông tin chính xác, đầy đủ và cập nhật</li>
        <li>Chỉ được tạo một tài khoản cho mỗi người dùng</li>
      </ul>

      <h4>2.2. Bảo mật tài khoản</h4>
      <p>Bạn có trách nhiệm:</p>
      <ul>
        <li>Bảo mật thông tin đăng nhập (email, mật khẩu)</li>
        <li>Không chia sẻ tài khoản với người khác</li>
        <li>Thông báo ngay cho VietTech nếu phát hiện tài khoản bị truy cập trái phép</li>
        <li>Chịu trách nhiệm với mọi hoạt động diễn ra dưới tài khoản của mình</li>
      </ul>

      <h4>2.3. Quyền đình chỉ/khóa tài khoản</h4>
      <p>VietTech có quyền đình chỉ hoặc khóa tài khoản nếu phát hiện:</p>
      <ul>
        <li>Vi phạm điều khoản sử dụng</li>
        <li>Cung cấp thông tin sai lệch, giả mạo</li>
        <li>Có hành vi gian lận, lừa đảo</li>
        <li>Lạm dụng chương trình khuyến mãi</li>
      </ul>
    </div>

    <!-- 3. Sử Dụng Dịch Vụ -->
    <div class="policy-section">
      <h3><i class="bi bi-cart-check"></i>3. Sử Dụng Dịch Vụ</h3>

      <h4>3.1. Đặt hàng và thanh toán</h4>
      <ul>
        <li>Đơn hàng chỉ được xác nhận sau khi VietTech liên hệ xác nhận qua điện thoại/email</li>
        <li>Giá sản phẩm có thể thay đổi mà không cần thông báo trước</li>
        <li>VietTech có quyền từ chối hoặc hủy đơn hàng nếu phát hiện bất thường</li>
        <li>Khách hàng chịu trách nhiệm cung cấp địa chỉ giao hàng chính xác</li>
      </ul>

      <h4>3.2. Quyền và nghĩa vụ của khách hàng</h4>
      <p><strong>Quyền:</strong></p>
      <ul>
        <li>Được cung cấp thông tin đầy đủ, chính xác về sản phẩm</li>
        <li>Được kiểm tra hàng trước khi thanh toán</li>
        <li>Được đổi trả, bảo hành theo chính sách</li>
        <li>Được bảo vệ thông tin cá nhân</li>
      </ul>

      <p><strong>Nghĩa vụ:</strong></p>
      <ul>
        <li>Thanh toán đầy đủ theo phương thức đã chọn</li>
        <li>Cung cấp thông tin chính xác khi đặt hàng</li>
        <li>Tuân thủ quy định về đổi trả, bảo hành</li>
        <li>Không sử dụng dịch vụ cho mục đích trái pháp luật</li>
      </ul>
    </div>

    <!-- 4. Sở Hữu Trí Tuệ -->
    <div class="policy-section">
      <h3><i class="bi bi-shield-check"></i>4. Quyền Sở Hữu Trí Tuệ</h3>
      <p>Tất cả nội dung trên website VietTech bao gồm nhưng không giới hạn ở:</p>
      <ul>
        <li>Logo, tên thương hiệu, slogan</li>
        <li>Văn bản, hình ảnh, video</li>
        <li>Giao diện, thiết kế website</li>
        <li>Mã nguồn, phần mềm</li>
      </ul>
      <p>đều thuộc quyền sở hữu của VietTech hoặc được cấp phép hợp pháp. Bạn không được:</p>
      <ul>
        <li>Sao chép, sửa đổi, phân phối nội dung mà không có sự cho phép</li>
        <li>Sử dụng logo, thương hiệu VietTech cho mục đích thương mại</li>
        <li>Đăng tải nội dung vi phạm bản quyền</li>
      </ul>
    </div>

    <!-- 5. Hành Vi Bị Cấm -->
    <div class="policy-section">
      <h3><i class="bi bi-x-circle"></i>5. Hành Vi Bị Cấm</h3>
      <p>Khi sử dụng dịch vụ VietTech, bạn <strong>KHÔNG ĐƯỢC</strong>:</p>
      <ul>
        <li>Đăng tải, truyền tải nội dung bất hợp pháp, độc hại, xúc phạm</li>
        <li>Giả mạo danh tính, thông tin</li>
        <li>Can thiệp, phá hoại hệ thống website</li>
        <li>Sử dụng robot, crawler, script tự động không được phép</li>
        <li>Lừa đảo, chiếm đoạt thông tin người khác</li>
        <li>Spam, gửi email quảng cáo không mong muốn</li>
        <li>Lạm dụng chương trình khuyến mãi, ưu đãi</li>
        <li>Đăng tải virus, malware, mã độc</li>
      </ul>

      <div class="note-box">
        <strong><i class="bi bi-exclamation-triangle"></i> Hậu quả:</strong>
        <p class="mb-0">Vi phạm các hành vi trên có thể dẫn đến việc khóa tài khoản vĩnh viễn và chịu trách nhiệm trước pháp luật.</p>
      </div>
    </div>

    <!-- 6. Giới Hạn Trách Nhiệm -->
    <div class="policy-section">
      <h3><i class="bi bi-exclamation-octagon"></i>6. Giới Hạn Trách Nhiệm</h3>

      <h4>6.1. Thông tin sản phẩm</h4>
      <p>VietTech cố gắng cung cấp thông tin chính xác nhất về sản phẩm. Tuy nhiên:</p>
      <ul>
        <li>Hình ảnh sản phẩm có thể khác biệt nhỏ so với thực tế</li>
        <li>Thông số kỹ thuật có thể thay đổi theo nhà sản xuất</li>
        <li>Giá cả có thể thay đổi mà không cần thông báo trước</li>
      </ul>

      <h4>6.2. Gián đoạn dịch vụ</h4>
      <p>VietTech không chịu trách nhiệm khi dịch vụ bị gián đoạn do:</p>
      <ul>
        <li>Bảo trì hệ thống định kỳ</li>
        <li>Sự cố kỹ thuật ngoài tầm kiểm soát</li>
        <li>Thiên tai, hỏa hoạn, chiến tranh</li>
        <li>Lỗi từ nhà cung cấp dịch vụ bên thứ ba</li>
      </ul>

      <h4>6.3. Giới hạn bồi thường</h4>
      <p>Trách nhiệm tối đa của VietTech đối với khách hàng không vượt quá giá trị đơn hàng mà khách hàng đã thanh toán.</p>
    </div>

    <!-- 7. Liên Kết Bên Thứ Ba -->
    <div class="policy-section">
      <h3><i class="bi bi-link-45deg"></i>7. Liên Kết Bên Thứ Ba</h3>
      <p>Website VietTech có thể chứa liên kết đến các website bên thứ ba. VietTech không chịu trách nhiệm về:</p>
      <ul>
        <li>Nội dung, chính sách bảo mật của các website này</li>
        <li>Bất kỳ thiệt hại nào phát sinh từ việc sử dụng các website này</li>
      </ul>
      <p>Việc truy cập các liên kết này là quyết định của bạn và bạn tự chịu rủi ro.</p>
    </div>

    <!-- 8. Thay Đổi Điều Khoản -->
    <div class="policy-section">
      <h3><i class="bi bi-arrow-repeat"></i>8. Thay Đổi Điều Khoản</h3>
      <p>VietTech có quyền thay đổi, bổ sung các điều khoản này bất kỳ lúc nào mà không cần thông báo trước. Phiên bản mới nhất sẽ được cập nhật trên website với ngày hiệu lực.</p>
      <p>Việc bạn tiếp tục sử dụng dịch vụ sau khi có thay đổi đồng nghĩa với việc bạn chấp nhận các điều khoản mới.</p>
    </div>

    <!-- 9. Luật Áp Dụng -->
    <div class="policy-section">
      <h3><i class="bi bi-scale"></i>9. Luật Áp Dụng Và Giải Quyết Tranh Chấp</h3>
      <p>Các điều khoản này được điều chỉnh bởi <strong>luật pháp Việt Nam</strong>.</p>
      <p>Mọi tranh chấp phát sinh liên quan đến việc sử dụng dịch vụ sẽ được giải quyết theo thứ tự:</p>
      <ol>
        <li>Thương lượng, hòa giải giữa hai bên</li>
        <li>Trọng tài thương mại (nếu hai bên đồng ý)</li>
        <li>Tòa án có thẩm quyền tại TP. Hồ Chí Minh</li>
      </ol>
    </div>

    <!-- 10. Thông Tin Liên Hệ -->
    <div class="policy-section">
      <h3><i class="bi bi-envelope"></i>10. Thông Tin Liên Hệ</h3>
      <p>Nếu bạn có bất kỳ câu hỏi nào về Điều khoản sử dụng này, vui lòng liên hệ:</p>
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