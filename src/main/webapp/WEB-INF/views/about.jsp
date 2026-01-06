<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giới Thiệu - VietTech</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        .about-hero {
            background: linear-gradient(135deg, #0d6efd 0%, #0b5ed7 100%);
            color: white;
            padding: 80px 0;
            text-align: center;
        }
        .about-hero h1 {
            font-size: 3rem;
            font-weight: 800;
            margin-bottom: 1rem;
        }
        .about-section {
            padding: 60px 0;
        }
        .about-section:nth-child(even) {
            background-color: #f8f9fa;
        }
        .section-title {
            font-size: 2.5rem;
            font-weight: 700;
            color: #0d6efd;
            margin-bottom: 3rem;
            text-align: center;
            position: relative;
            padding-bottom: 1rem;
        }
        .section-title::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 4px;
            background: #0d6efd;
        }
        .team-member {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            text-align: center;
            margin-bottom: 2rem;
            height: 100%;
        }
        .team-member:hover {
            transform: translateY(-10px);
            box-shadow: 0 8px 24px rgba(0,0,0,0.15);
        }
        .team-member-avatar {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            background: linear-gradient(135deg, #0d6efd, #0b5ed7);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2.5rem;
            font-weight: 700;
            margin: 0 auto 1.5rem;
            box-shadow: 0 4px 12px rgba(13, 110, 253, 0.3);
        }
        .team-member-name {
            font-size: 1.2rem;
            font-weight: 700;
            color: #1a1a1a;
            margin-bottom: 0.5rem;
        }
        .team-member-id {
            font-size: 0.95rem;
            color: #0d6efd;
            font-weight: 600;
            margin-bottom: 0.5rem;
        }
        .tech-badge {
            display: inline-block;
            padding: 0.6rem 1.2rem;
            background: white;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            margin: 0.5rem;
            font-weight: 600;
            color: #495057;
            transition: all 0.3s ease;
        }
        .tech-badge:hover {
            background: #0d6efd;
            border-color: #0d6efd;
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(13, 110, 253, 0.3);
        }
        .stats-box {
            background: linear-gradient(135deg, #0d6efd, #0b5ed7);
            color: white;
            border-radius: 12px;
            padding: 2.5rem;
            text-align: center;
            margin-bottom: 2rem;
            box-shadow: 0 4px 12px rgba(13, 110, 253, 0.3);
        }
        .stats-number {
            font-size: 3.5rem;
            font-weight: 800;
            margin-bottom: 0.5rem;
        }
        .stats-label {
            font-size: 1.1rem;
            opacity: 0.9;
        }
        .feature-card {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            text-align: center;
            height: 100%;
            transition: all 0.3s ease;
        }
        .feature-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.12);
        }
        .feature-card i {
            font-size: 3rem;
            color: #0d6efd;
            margin-bottom: 1rem;
        }
        .feature-card h4 {
            color: #1a1a1a;
            font-weight: 700;
            margin-bottom: 1rem;
        }
        .university-logo {
            max-width: 120px;
            margin: 0 auto 1rem;
        }
    </style>
</head>
<body>
<%@ include file="../../header.jsp" %>

<div class="about-hero">
    <div class="container">
        <h1>Về Chúng Tôi</h1>
        <p class="lead">Đồ Án Thương Mại Điện Tử - VietTech Store</p>
    </div>
</div>

<!-- Giới Thiệu Dự Án -->
<section class="about-section">
    <div class="container">
        <h2 class="section-title">Giới Thiệu Dự Án</h2>
        <div class="row align-items-center">
            <div class="col-lg-7">
                <p class="lead mb-4">VietTech Store là nền tảng thương mại điện tử chuyên cung cấp các sản phẩm công nghệ chính hãng với giá tốt nhất.</p>
                <p>Dự án được phát triển bởi nhóm <strong>6 sinh viên năm 3</strong> ngành <strong>Công Nghệ Thông Tin</strong> tại <strong>Trường Đại Học Công Nghệ Kỹ Thuật Thành Phố Hồ Chí Minh (HCM-UTE)</strong> - <em>Ho Chi Minh City University of Technology and Engineering</em> (trước đây là Đại học Sư phạm Kỹ thuật TP.HCM - HCMUTE).</p>
                <p>Đây là <strong>đồ án môn Lập Trình Web</strong> với mục tiêu xây dựng một hệ thống thương mại điện tử hoàn chỉnh, áp dụng các công nghệ hiện đại và thực tiễn trong phát triển web.</p>
            </div>
            <div class="col-lg-5">
                <div class="row g-3">
                    <div class="col-6">
                        <div class="stats-box">
                            <div class="stats-number">6</div>
                            <div class="stats-label">Thành Viên</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="stats-box">
                            <div class="stats-number">2025</div>
                            <div class="stats-label">Một tầm nhìn</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Đội Ngũ Phát Triển -->
<section class="about-section">
    <div class="container">
        <h2 class="section-title">Đội Ngũ Phát Triển</h2>
        <div class="row g-4">
            <div class="col-lg-4 col-md-6">
                <div class="team-member">
                    <div class="team-member-avatar">NK</div>
                    <div class="team-member-name">Nguyễn Quốc Khoa</div>
                    <div class="team-member-id">MSSV: 23110116</div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="team-member">
                    <div class="team-member-avatar">HT</div>
                    <div class="team-member-name">Hoàng Thanh Tú</div>
                    <div class="team-member-id">MSSV: 23110167</div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="team-member">
                    <div class="team-member-avatar">DH</div>
                    <div class="team-member-name">Đinh Xuân Huy</div>
                    <div class="team-member-id">MSSV: 23110102</div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="team-member">
                    <div class="team-member-avatar">NQ</div>
                    <div class="team-member-name">Nghiêm Phú Đăng Quân</div>
                    <div class="team-member-id">MSSV: 23110144</div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="team-member">
                    <div class="team-member-avatar">NT</div>
                    <div class="team-member-name">Nguyễn Ngọc Thiện</div>
                    <div class="team-member-id">MSSV: 23110154</div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="team-member">
                    <div class="team-member-avatar">PA</div>
                    <div class="team-member-name">Phạm Thị Vân Ánh</div>
                    <div class="team-member-id">MSSV: 23110076</div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Công Nghệ Sử Dụng -->
<section class="about-section">
    <div class="container">
        <h2 class="section-title">Công Nghệ Sử Dụng</h2>

        <h4 class="text-center mb-4"><i class="bi bi-code-slash text-primary"></i> Frontend</h4>
        <div class="text-center mb-5">
            <span class="tech-badge">HTML5/CSS3</span>
            <span class="tech-badge">JavaScript ES6+</span>
            <span class="tech-badge">Bootstrap 5</span>
            <span class="tech-badge">AJAX</span>
        </div>

        <h4 class="text-center mb-4"><i class="bi bi-server text-primary"></i> Backend</h4>
        <div class="text-center mb-5">
            <span class="tech-badge">Java 17+</span>
            <span class="tech-badge">Java Servlet 6.0</span>
            <span class="tech-badge">JSP/JSTL</span>
            <span class="tech-badge">JPA (Hibernate)</span>
            <span class="tech-badge">MySQL 8.0</span>
            <span class="tech-badge">HikariCP</span>
            <span class="tech-badge">Jackson</span>
            <span class="tech-badge">BCrypt</span>
        </div>

        <h4 class="text-center mb-4"><i class="bi bi-cloud text-primary"></i> DevOps & Deployment</h4>
        <div class="text-center mb-5">
            <span class="tech-badge">Aiven (Database)</span>
            <span class="tech-badge">Render (Deploy)</span>
            <span class="tech-badge">Git/GitHub</span>
            <span class="tech-badge">Maven</span>
            <span class="tech-badge">Tomcat 10</span>
        </div>

        <h4 class="text-center mb-4"><i class="bi bi-credit-card text-primary"></i> Payment Integration</h4>
        <div class="text-center">
            <span class="tech-badge">VNPay API</span>
            <span class="tech-badge">MoMo API</span>
            <span class="tech-badge">ZaloPay API</span>
            <span class="tech-badge">Brevo Email API</span>
        </div>
    </div>
</section>

<!-- Tính Năng Nổi Bật -->
<section class="about-section">
    <div class="container">
        <h2 class="section-title">Tính Năng Nổi Bật</h2>
        <div class="row g-4">
            <div class="col-lg-3 col-md-6">
                <div class="feature-card">
                    <i class="bi bi-search"></i>
                    <h4>Fuzzy Search</h4>
                    <p>Tìm kiếm thông minh với thuật toán fuzzy search</p>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="feature-card">
                    <i class="bi bi-robot"></i>
                    <h4>AI Chatbot</h4>
                    <p>Hỗ trợ khách hàng 24/7 với chatbot AI</p>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="feature-card">
                    <i class="bi bi-cloud-upload"></i>
                    <h4>CDN Integration</h4>
                    <p>Lưu trữ hình ảnh trên CDN tốc độ cao</p>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="feature-card">
                    <i class="bi bi-credit-card"></i>
                    <h4>Payment Gateway</h4>
                    <p>Đa dạng cổng thanh toán VNPay, MoMo, ZaloPay</p>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Domain & Deployment -->
<section class="about-section">
    <div class="container text-center">
        <h2 class="section-title">Thông Tin Deployment</h2>
        <div class="row justify-content-center">
            <div class="col-lg-6">
                <div class="feature-card">
                    <i class="bi bi-globe"></i>
                    <h4>Domain</h4>
                    <p class="mb-2">Hệ thống được deploy với domain riêng:</p>
                    <a href="https://viettechstore.online" target="_blank" class="text-primary fw-bold fs-5">
                        viettechstore.online
                    </a>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../../footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>