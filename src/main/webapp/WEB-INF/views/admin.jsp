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
            </div>

            <!-- Support Section -->
            <div class="nav-menu-section">
                <div class="section-title">Hỗ trợ</div>
                <a href="#contact-messages" class="nav-item" onclick="return showSection('contact-messages', this)">
                    <i class="fas fa-envelope"></i>
                    <span>Tin nhắn KH</span>
                    <c:if test="${unreadContactMessages != null && unreadContactMessages > 0}">
                        <span class="badge-count">${unreadContactMessages}</span>
                    </c:if>
                </a>
                <a href="#chatbot" class="nav-item" onclick="return showSection('chatbot', this)">
                    <i class="fas fa-robot"></i>
                    <span>AI Chatbot</span>
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
                <div class="notifications dropdown" id="adminNotificationBell">
                    <button class="notification-bell-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-bell"></i>
                        <span class="notification-badge" style="display: none;">0</span>
                    </button>

                    <!-- Notification Dropdown -->
                    <div class="dropdown-menu dropdown-menu-end notification-dropdown">
                        <div class="notification-dropdown-header">
                            <h6>Thông báo</h6>
                            <a href="${pageContext.request.contextPath}/admin/notifications" class="view-all-link">Xem tất cả</a>
                        </div>
                        <div class="notification-dropdown-body">
                            <!-- Notifications will be loaded here via JavaScript -->
                            <div class="notification-loading text-center py-4">
                                <div class="spinner-border spinner-border-sm text-primary" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                                <p class="mt-2 text-muted small">Đang tải thông báo...</p>
                            </div>
                        </div>
                        <div class="notification-dropdown-footer">
                            <a href="${pageContext.request.contextPath}/admin/notifications/mark-all-read" class="mark-all-read">
                                <i class="fas fa-check-double"></i> Đánh dấu tất cả đã đọc
                            </a>
                        </div>
                    </div>
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

        <!-- Users Section -->
        <section id="users" class="content-section">
            <jsp:include page="/WEB-INF/views/admin_pages/users.jsp"/>
        </section>

        <!-- Vouchers Section -->
        <section id="vouchers" class="content-section">
            <jsp:include page="/WEB-INF/views/admin_pages/voucher.jsp"/>
        </section>

        <!-- Chatbot Section -->
        <section id="chatbot" class="content-section">
            <jsp:include page="/WEB-INF/views/admin_pages/Chatbot.jsp"/>
        </section>

        <!-- Contact Messages Section -->
        <section id="contact-messages" class="content-section">
            <jsp:include page="/WEB-INF/views/admin_pages/contact-messages.jsp"/>
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

    <!-- Notification Variables -->
    <script>
        const contextPath = "${pageContext.request.contextPath}";
        const isLoggedIn = true; // Admin luôn đăng nhập
    </script>

    <!-- Admin Notification JS -->
    <script src="${pageContext.request.contextPath}/assets/js/admin-notification.js"></script>
</body>
</html>

