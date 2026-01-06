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
    <style>
        .contact-hero {
            background: linear-gradient(135deg, #0d6efd 0%, #0b5ed7 100%);
            color: white;
            padding: 60px 0;
            text-align: center;
        }

        .contact-hero h1 {
            font-size: 2.5rem;
            font-weight: 800;
            margin-bottom: 0.5rem;
        }

        .contact-content {
            padding: 60px 0;
            background-color: #f8f9fa;
        }

        .contact-info-card {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            height: 100%;
            text-align: center;
            transition: all 0.3s ease;
        }

        .contact-info-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
        }

        .contact-info-card .icon {
            font-size: 3rem;
            color: #0d6efd;
            margin-bottom: 1rem;
        }

        .contact-info-card h4 {
            color: #1a1a1a;
            font-weight: 700;
            margin-bottom: 1rem;
        }

        .contact-info-card p {
            color: #495057;
            margin-bottom: 0;
        }

        .contact-info-card a {
            color: #0d6efd;
            text-decoration: none;
            font-weight: 600;
        }

        .contact-info-card a:hover {
            color: #0b5ed7;
            text-decoration: underline;
        }

        .contact-form-section {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            margin-top: 2rem;
        }

        .contact-form-section h3 {
            color: #0d6efd;
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 1.5rem;
            padding-bottom: 0.75rem;
            border-bottom: 3px solid #0d6efd;
        }

        .map-container {
            width: 100%;
            height: 400px;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            margin-top: 2rem;
        }

        .map-container iframe {
            width: 100%;
            height: 100%;
            border: 0;
        }
    </style>
</head>
<body>
<%@ include file="../../header.jsp" %>

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
            <form id="contactForm" method="post" action="${pageContext.request.contextPath}/contact">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label for="fullName" class="form-label">Họ và tên <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="fullName" name="fullName" required>
                    </div>
                    <div class="col-md-6">
                        <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="col-md-6">
                        <label for="phone" class="form-label">Số điện thoại <span class="text-danger">*</span></label>
                        <input type="tel" class="form-control" id="phone" name="phone" required>
                    </div>
                    <div class="col-md-6">
                        <label for="subject" class="form-label">Chủ đề <span class="text-danger">*</span></label>
                        <select class="form-select" id="subject" name="subject" required>
                            <option value="">-- Chọn chủ đề --</option>
                            <option value="order">Hỏi về đơn hàng</option>
                            <option value="product">Hỏi về sản phẩm</option>
                            <option value="warranty">Bảo hành</option>
                            <option value="return">Đổi trả</option>
                            <option value="complaint">Khiếu nại</option>
                            <option value="other">Khác</option>
                        </select>
                    </div>
                    <div class="col-12">
                        <label for="message" class="form-label">Nội dung <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="message" name="message" rows="5" required></textarea>
                    </div>
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

<jsp:include page="../../footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<script>
    // Contact Form Validation
    document.getElementById('contactForm').addEventListener('submit', function (e) {
        e.preventDefault();

        // Get form data
        const fullName = document.getElementById('fullName').value.trim();
        const email = document.getElementById('email').value.trim();
        const phone = document.getElementById('phone').value.trim();
        const subject = document.getElementById('subject').value;
        const message = document.getElementById('message').value.trim();

        // Basic validation
        if (!fullName || !email || !phone || !subject || !message) {
            alert('Vui lòng điền đầy đủ thông tin!');
            return;
        }

        // Phone validation
        const phoneRegex = /^[0-9]{10}$/;
        if (!phoneRegex.test(phone)) {
            alert('Số điện thoại không hợp lệ!');
            return;
        }

        // Success message (in real app, submit to server)
        alert('Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi trong vòng 24 giờ.');
        this.reset();
    });
</script>
</body>
</html>