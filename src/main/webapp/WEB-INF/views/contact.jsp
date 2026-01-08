<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Liên Hệ - VietTech</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/contact.css">
</head>
<body>
<%@ include file="../../header.jsp" %>
<script>
    // Biến toàn cục cho JavaScript
    const contextPath = "${pageContext.request.contextPath}";
    const isLoggedIn = ${not empty sessionScope.user};
</script>
<div class="contact-hero">
    <div class="container">
        <h1><i class="bi bi-headset"></i> Liên Hệ Với Chúng Tôi</h1>
        <p class="lead">Chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7</p>
    </div>
</div>

<div class="contact-content">
    <div class="container">

        <!-- Contact Info Cards -->
        <div class="row g-4 mb-4">

            <!-- Hotline -->
            <div class="col-lg-3 col-md-6">
                <div class="contact-info-card">
                    <div class="icon">
                        <i class="bi bi-telephone-fill"></i>
                    </div>
                    <h4>Hotline</h4>
                    <p><a href="tel:0866448892">0866 448 892</a></p>
                    <p class="mt-2 small">Hỗ trợ 24/7</p>
                </div>
            </div>

            <!-- Email -->
            <div class="col-lg-3 col-md-6">
                <div class="contact-info-card">
                    <div class="icon">
                        <i class="bi bi-envelope-fill"></i>
                    </div>
                    <h4>Email</h4>
                    <p><a href="mailto:support@viettech.vn">support@viettech.vn</a></p>
                    <p class="mt-2 small">Phản hồi trong 24h</p>
                </div>
            </div>

            <!-- Address -->
            <div class="col-lg-3 col-md-6">
                <div class="contact-info-card">
                    <div class="icon">
                        <i class="bi bi-geo-alt-fill"></i>
                    </div>
                    <h4>Địa Chỉ</h4>
                    <p>01 Đ. Võ Văn Ngân<br>Linh Chiểu, Thủ Đức<br>TP. Hồ Chí Minh</p>
                </div>
            </div>

            <!-- Working Hours -->
            <div class="col-lg-3 col-md-6">
                <div class="contact-info-card">
                    <div class="icon">
                        <i class="bi bi-clock-fill"></i>
                    </div>
                    <h4>Giờ Làm Việc</h4>
                    <p>Thứ 2 - Chủ nhật<br>8:00 - 22:00</p>
                    <p class="mt-2 small text-success">Đang mở cửa</p>
                </div>
            </div>

        </div>

        <!-- Contact Form -->
        <div class="contact-form-section">
            <h3><i class="bi bi-chat-dots"></i> Gửi Tin Nhắn Cho Chúng Tôi</h3>

            <!-- ✅ THÊM NOTE BOX -->
            <div class="note-box mb-4">
                <strong><i class="bi bi-info-circle"></i> Lưu ý:</strong>
                <p class="mb-0">Chúng tôi sẽ phản hồi tin nhắn của bạn trong vòng 24 giờ làm việc. Vui lòng kiểm tra email để nhận thông báo.</p>
            </div>

            <form id="contactForm" method="post" action="${pageContext.request.contextPath}/contact">
                <div class="row g-3">
                    <!-- ✅ AUTO-FILL: Họ và tên -->
                    <div class="col-md-6">
                        <label for="fullName" class="form-label">Họ và tên <span class="text-danger">*</span></label>
                        <input type="text"
                               class="form-control"
                               id="fullName"
                               name="fullName"
                               value="${not empty sessionScope.user ? sessionScope.user.firstName.concat(' ').concat(sessionScope.user.lastName) : ''}"
                               required>
                    </div>

                    <!-- ✅ AUTO-FILL: Email (READONLY nếu đã đăng nhập) -->
                    <div class="col-md-6">
                        <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                        <input type="email"
                               class="form-control"
                               id="email"
                               name="email"
                               value="${not empty sessionScope.user ? sessionScope.user.email : ''}"
                        ${not empty sessionScope.user ? 'readonly' : ''}
                               required>
                        <c:if test="${not empty sessionScope.user}">
                            <small class="text-muted">Email không thể thay đổi</small>
                        </c:if>
                    </div>

                    <!-- ✅ AUTO-FILL: Số điện thoại -->
                    <!-- Trường số điện thoại trong contact.jsp -->
                    <div class="col-md-6">
                        <label for="phone" class="form-label">Số điện thoại <span class="text-danger">*</span></label>
                        <input type="tel"
                               class="form-control"
                               id="phone"
                               name="phone"
                               value="${not empty sessionScope.user ? sessionScope.user.phone : ''}"
                               pattern="[0-9]{10}"
                               maxlength="10"
                               oninput="this.value = this.value.replace(/[^0-9]/g, '');"
                               required>
                    </div>

                    <!-- Chủ đề -->
                    <div class="col-md-6">
                        <label for="subject" class="form-label">Chủ đề <span class="text-danger">*</span></label>
                        <select class="form-select" id="subject" name="subject" required>
                            <option value="">-- Chọn chủ đề --</option>
                            <option value="Hỏi về đơn hàng">Hỏi về đơn hàng</option>
                            <option value="Hỏi về sản phẩm">Hỏi về sản phẩm</option>
                            <option value="Bảo hành">Bảo hành</option>
                            <option value="Đổi trả">Đổi trả</option>
                            <option value="Khiếu nại">Khiếu nại</option>
                            <option value="Khác">Khác</option>
                        </select>
                    </div>

                    <!-- Nội dung -->
                    <div class="col-12">
                        <label for="message" class="form-label">Nội dung <span class="text-danger">*</span></label>
                        <textarea class="form-control"
                                  id="message"
                                  name="message"
                                  rows="5"
                                  placeholder="Nhập nội dung tin nhắn của bạn..."
                                  required></textarea>
                    </div>

                    <!-- Submit Button -->
                    <div class="col-12">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="bi bi-send me-2"></i>Gửi Tin Nhắn
                        </button>
                    </div>
                </div>
            </form>
        </div>

        <!-- Map -->
        <div class="map-container">
            <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3586.8427541979263!2d106.76933281012806!3d10.85063765777681!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752763f23816ab%3A0x282f711441b6916f!2zVHLGsOG7nW5nIMSQ4bqhaSBo4buNYyBTxrAgcGjhuqFtIEvhu7kgdGh14bqtdCBUaMOgbmggcGjhu5EgSOG7kyBDaMOtIE1pbmg!5e1!3m2!1svi!2s!4v1767670040922!5m2!1svi!2s"
                    width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy"
                    referrerpolicy="no-referrer-when-downgrade"></iframe>
        </div>

    </div>
</div>

<jsp:include page="../../footer.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
</body>
</html>