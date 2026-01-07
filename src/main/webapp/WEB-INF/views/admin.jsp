<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - VietTech</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">

    <!-- Google Fonts - Roboto (matching main.css) -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Bootstrap Icons (matching index.jsp) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    <!-- Admin CSS (after Bootstrap to override styles) -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">

    <!-- Admin JavaScript - loaded synchronously for onclick handlers -->
    <script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</head>
<body>
    <!-- Sidebar Navigation -->
    <aside class="sidebar" id="sidebar">
        <div class="logo">
            <h2>Viet<span>Tech</span></h2>
        </div>

        <nav class="nav-menu">
            <!-- Main Section -->
            <div class="nav-menu-section">
                <a href="#dashboard" class="nav-item active" onclick="return showSection('dashboard', this)">
                    <i class="fas fa-chart-pie"></i>
                    <span>Dashboard</span>
                </a>
                <a href="#products" class="nav-item" onclick="return showSection('products', this)">
                    <i class="fas fa-box"></i>
                    <span>Sản phẩm</span>
                </a>
                <a href="#orders" class="nav-item" onclick="return showSection('orders', this)">
                    <i class="fas fa-shopping-cart"></i>
                    <span>Đơn hàng</span>
                    <span class="badge-count">12</span>
                </a>
                <a href="#users" class="nav-item" onclick="return showSection('users', this)">
                    <i class="fas fa-users"></i>
                    <span>Người dùng</span>
                </a>
            </div>

            <!-- Marketing Section -->
            <div class="nav-menu-section">
                <div class="section-title">Marketing</div>
                <a href="#vouchers" class="nav-item" onclick="return showSection('vouchers', this)">
                    <i class="fas fa-ticket-alt"></i>
                    <span>Voucher</span>
                </a>
                <a href="#flash-sales" class="nav-item" onclick="return showSection('flash-sales', this)">
                    <i class="fas fa-bolt"></i>
                    <span>Flash Sale</span>
                </a>
            </div>

            <!-- Support Section -->
            <div class="nav-menu-section">
                <div class="section-title">Hỗ trợ</div>
                <a href="#chatbot" class="nav-item" onclick="return showSection('chatbot', this)">
                    <i class="fas fa-robot"></i>
                    <span>AI Chatbot</span>
                </a>
                <a href="#reviews" class="nav-item" onclick="return showSection('reviews', this)">
                    <i class="fas fa-star"></i>
                    <span>Đánh giá</span>
                </a>
            </div>

            <!-- System Section -->
            <div class="nav-menu-section">
                <div class="section-title">Hệ thống</div>
                <a href="#settings" class="nav-item" onclick="return showSection('settings', this)">
                    <i class="fas fa-cog"></i>
                    <span>Cài đặt</span>
                </a>
            </div>
        </nav>

        <!-- Sidebar Footer -->
        <div class="sidebar-footer">
            <a href="${pageContext.request.contextPath}/logout" class="nav-item">
                <i class="fas fa-sign-out-alt"></i>
                <span>Đăng xuất</span>
            </a>
        </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content" id="main-content">
        <!-- Top Navbar -->
        <header class="top-navbar">
            <div class="navbar-left">
                <button class="menu-toggle" onclick="toggleSidebar()" aria-label="Toggle menu">
                    <i class="fas fa-bars"></i>
                </button>
                <h1 id="page-title">Dashboard</h1>
            </div>

            <div class="navbar-right">
                <!-- Search Box -->
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" placeholder="Tìm kiếm..." aria-label="Search">
                </div>

                <!-- Language Selector -->
                <div class="language-selector">
                    <img src="https://flagcdn.com/w40/vn.png" alt="VN">
                    <span>VN</span>
                    <i class="fas fa-chevron-down"></i>
                </div>

                <!-- Notifications -->
                <div class="notifications" onclick="toggleNotifications()">
                    <i class="fas fa-bell"></i>
                    <span class="badge">5</span>
                </div>

                <!-- User Profile -->
                <div class="user-profile">
                    <img src="https://ui-avatars.com/api/?name=Admin&background=2563EB&color=fff" alt="Admin">
                    <div class="user-profile-info">
                        <div class="name">${sessionScope.admin != null ? sessionScope.admin.fullName : 'Admin'}</div>
                        <div class="role">Quản trị viên</div>
                    </div>
                    <i class="fas fa-chevron-down"></i>
                </div>
            </div>
        </header>

        <!-- Dashboard Section -->
        <section id="dashboard" class="content-section active">
            <jsp:include page="/WEB-INF/views/admin_pages/dashboard.jsp"/>
        </section>

        <!-- Products Section -->
        <section id="products" class="content-section">
            <jsp:include page="/WEB-INF/views/admin_pages/products.jsp"/>
        </section>

        <!-- Orders Section -->
        <section id="orders" class="content-section">
            <div class="section-header">
                <h2>Quản lý đơn hàng</h2>
            </div>
            <p>Đang phát triển...</p>
        </section>

        <!-- Users Section -->
        <section id="users" class="content-section">
            <jsp:include page="/WEB-INF/views/admin_pages/users.jsp"/>
        </section>

        <!-- Vouchers Section -->
        <section id="vouchers" class="content-section">
            <jsp:include page="/WEB-INF/views/admin_pages/voucher.jsp"/>
        </section>

        <!-- Flash Sales Section -->
        <section id="flash-sales" class="content-section">
            <div class="section-header">
                <h2>Quản lý Flash Sale</h2>
                <button class="btn btn-primary" onclick="openAddFlashSaleModal()">
                    <i class="fas fa-plus"></i> Tạo Flash Sale
                </button>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-card-header">
                        <div class="stat-icon orange">
                            <i class="fas fa-bolt"></i>
                        </div>
                    </div>
                    <div class="stat-details">
                        <h3>Flash Sale đang diễn ra</h3>
                        <p class="stat-number">${activeFlashSales != null ? activeFlashSales : 0}</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-card-header">
                        <div class="stat-icon green">
                            <i class="fas fa-check-circle"></i>
                        </div>
                    </div>
                    <div class="stat-details">
                        <h3>Đã hoàn thành</h3>
                        <p class="stat-number">${completedFlashSales != null ? completedFlashSales : 0}</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-card-header">
                        <div class="stat-icon blue">
                            <i class="fas fa-calendar-alt"></i>
                        </div>
                    </div>
                    <div class="stat-details">
                        <h3>Sắp diễn ra</h3>
                        <p class="stat-number">${upcomingFlashSales != null ? upcomingFlashSales : 0}</p>
                    </div>
                </div>
            </div>

            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Tên chiến dịch</th>
                            <th>Thời gian</th>
                            <th>Sản phẩm</th>
                            <th>Giảm giá</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${empty flashSaleList}">
                            <tr>
                                <td colspan="6" style="text-align: center; padding: 48px;">
                                    <i class="fas fa-bolt" style="font-size: 48px; color: var(--text-muted); margin-bottom: 16px; display: block;"></i>
                                    <p style="color: var(--text-muted);">Chưa có Flash Sale nào. Tạo chiến dịch đầu tiên!</p>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>

        <!-- Chatbot Section -->
        <section id="chatbot" class="content-section">
            <jsp:include page="/WEB-INF/views/admin_pages/Chatbot.jsp"/>
        </section>

        <!-- Reviews Section -->
        <section id="reviews" class="content-section">
            <div class="section-header">
                <h2>Quản lý đánh giá</h2>
                <div class="filter-group">
                    <select id="reviewRatingFilter">
                        <option value="">Tất cả đánh giá</option>
                        <option value="5">5 sao</option>
                        <option value="4">4 sao</option>
                        <option value="3">3 sao</option>
                        <option value="2">2 sao</option>
                        <option value="1">1 sao</option>
                    </select>
                </div>
            </div>

            <div class="reviews-list" id="reviewsList">
                <c:forEach var="review" items="${reviewList}">
                    <div class="review-card">
                        <div class="review-header">
                            <div class="review-user">
                                <img src="https://ui-avatars.com/api/?name=${review.customerName}&background=random" alt="${review.customerName}">
                                <div class="review-user-info">
                                    <h4>${review.customerName}</h4>
                                    <small style="color: var(--text-muted);">${review.productName}</small>
                                </div>
                            </div>
                            <div class="review-rating">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="fas fa-star ${i <= review.rating ? '' : 'far'}"></i>
                                </c:forEach>
                            </div>
                        </div>
                        <p class="review-content">${review.comment}</p>
                        <div style="margin-top: 16px; display: flex; gap: 8px;">
                            <button class="btn btn-sm btn-secondary">
                                <i class="fas fa-reply"></i> Phản hồi
                            </button>
                            <button class="btn btn-sm btn-danger">
                                <i class="fas fa-flag"></i> Báo cáo
                            </button>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty reviewList}">
                    <div class="review-card" style="text-align: center; padding: 48px;">
                        <i class="fas fa-star" style="font-size: 48px; color: var(--text-muted); margin-bottom: 16px;"></i>
                        <p style="color: var(--text-muted);">Chưa có đánh giá nào</p>
                    </div>
                </c:if>
            </div>
        </section>

        <!-- Settings Section -->
        <section id="settings" class="content-section">
            <div class="section-header">
                <h2>Cài đặt hệ thống</h2>
            </div>

            <div class="settings-container">
                <div class="settings-card">
                    <h3>Thông tin cửa hàng</h3>
                    <form id="storeSettingsForm">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="storeName">Tên cửa hàng</label>
                                <input type="text" id="storeName" class="form-control" value="VietTech Shop">
                            </div>
                            <div class="form-group">
                                <label for="storePhone">Số điện thoại</label>
                                <input type="tel" id="storePhone" class="form-control" value="1900 1234">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="storeEmail">Email liên hệ</label>
                            <input type="email" id="storeEmail" class="form-control" value="contact@viettech.com">
                        </div>
                        <div class="form-group">
                            <label for="storeAddress">Địa chỉ</label>
                            <textarea id="storeAddress" class="form-control" rows="2">123 Nguyễn Văn Linh, Quận 7, TP.HCM</textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Lưu thay đổi
                        </button>
                    </form>
                </div>

                <div class="settings-card">
                    <h3>Cấu hình email</h3>
                    <form id="emailSettingsForm">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="smtpHost">SMTP Host</label>
                                <input type="text" id="smtpHost" class="form-control" placeholder="smtp.gmail.com">
                            </div>
                            <div class="form-group">
                                <label for="smtpPort">SMTP Port</label>
                                <input type="number" id="smtpPort" class="form-control" placeholder="587">
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="smtpUsername">Username</label>
                                <input type="text" id="smtpUsername" class="form-control" placeholder="your-email@gmail.com">
                            </div>
                            <div class="form-group">
                                <label for="smtpPassword">Password</label>
                                <input type="password" id="smtpPassword" class="form-control" placeholder="••••••••">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Lưu cấu hình
                        </button>
                    </form>
                </div>
            </div>
        </section>
    </main>

    <!-- Add Product Modal -->
    <div id="productModal" class="modal">
        <div class="modal-content modal-lg">
            <div class="modal-header">
                <h3>Thêm sản phẩm mới</h3>
                <button class="modal-close" onclick="closeModal('productModal')">&times;</button>
            </div>
            <div class="modal-body">
                <form id="productForm" action="${pageContext.request.contextPath}/admin" method="POST">
                    <input type="hidden" name="action" value="add_product">

                    <div class="form-row">
                        <div class="form-group">
                            <label for="productName">Tên sản phẩm <span class="required">*</span></label>
                            <input type="text" id="productName" name="name" class="form-control" required placeholder="Nhập tên sản phẩm">
                        </div>
                        <div class="form-group">
                            <label for="productBrand">Thương hiệu <span class="required">*</span></label>
                            <input type="text" id="productBrand" name="brand" class="form-control" required placeholder="Nhập thương hiệu">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="productCategory">Danh mục <span class="required">*</span></label>
                            <select id="productCategory" name="categoryId" class="form-control" required>
                                <option value="">-- Chọn danh mục --</option>
                                <option value="1">Điện thoại</option>
                                <option value="3">Laptop</option>
                                <option value="4">Tablet</option>
                                <option value="5">Tai nghe / Phụ kiện</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="productPrice">Giá (₫) <span class="required">*</span></label>
                            <input type="number" id="productPrice" name="basePrice" class="form-control" required placeholder="0" min="0">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="productDescription">Mô tả</label>
                        <textarea id="productDescription" name="description" class="form-control" rows="4" placeholder="Nhập mô tả sản phẩm..."></textarea>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" onclick="closeModal('productModal')">Hủy</button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Thêm sản phẩm
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</body>
</html>

