<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="logo">
            <i class="fas fa-store"></i>
            <h2>Admin Panel</h2>
        </div>
        <nav class="nav-menu">
            <a href="#dashboard" class="nav-item active" onclick="showSection('dashboard')">
                <i class="fas fa-chart-line"></i>
                <span>Dashboard</span>
            </a>
            <a href="#products" class="nav-item" onclick="showSection('products')">
                <i class="fas fa-box"></i>
                <span>Sản phẩm</span>
            </a>
            <a href="#users" class="nav-item" onclick="showSection('users')">
                <i class="fas fa-users"></i>
                <span>Người dùng</span>
            </a>
            <a href="#orders" class="nav-item" onclick="showSection('orders')">
                <i class="fas fa-shopping-cart"></i>
                <span>Đơn hàng</span>
            </a>
            <a href="#revenue" class="nav-item" onclick="showSection('revenue')">
                <i class="fas fa-dollar-sign"></i>
                <span>Doanh thu</span>
            </a>
            <a href="#categories" class="nav-item" onclick="showSection('categories')">
                <i class="fas fa-tags"></i>
                <span>Danh mục</span>
            </a>
            <a href="#reviews" class="nav-item" onclick="showSection('reviews')">
                <i class="fas fa-star"></i>
                <span>Đánh giá</span>
            </a>
            <a href="#settings" class="nav-item" onclick="showSection('settings')">
                <i class="fas fa-cog"></i>
                <span>Cài đặt</span>
            </a>
        </nav>
        <div class="sidebar-footer">
            <a href="#" class="nav-item">
                <i class="fas fa-sign-out-alt"></i>
                <span>Đăng xuất</span>
            </a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Navbar -->
        <div class="top-navbar">
            <div class="navbar-left">
                <button class="menu-toggle" onclick="toggleSidebar()">
                    <i class="fas fa-bars"></i>
                </button>
                <h1 id="page-title">Dashboard</h1>
            </div>
            <div class="navbar-right">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" placeholder="Tìm kiếm...">
                </div>
                <div class="notifications">
                    <i class="fas fa-bell"></i>
                    <span class="badge">3</span>
                </div>
                <div class="user-profile">
                    <img src="https://via.placeholder.com/40" alt="Admin">
                    <span>Admin</span>
                </div>
            </div>
        </div>

        <!-- Dashboard Section -->
        <div id="dashboard" class="content-section active">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon blue">
                        <i class="fas fa-box"></i>
                    </div>
                    <div class="stat-details">
                        <h3>Tổng sản phẩm</h3>
                        <p class="stat-number" id="total-products">0</p>
                        <span class="stat-change positive">+12% so với tháng trước</span>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon green">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="stat-details">
                        <h3>Người dùng</h3>
                        <p class="stat-number" id="total-users">0</p>
                        <span class="stat-change positive">+8% so với tháng trước</span>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon orange">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                    <div class="stat-details">
                        <h3>Đơn hàng</h3>
                        <p class="stat-number" id="total-orders">0</p>
                        <span class="stat-change positive">+23% so với tháng trước</span>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon purple">
                        <i class="fas fa-dollar-sign"></i>
                    </div>
                    <div class="stat-details">
                        <h3>Doanh thu</h3>
                        <p class="stat-number" id="total-revenue">0</p>
                        <span class="stat-change positive">+15% so với tháng trước</span>
                    </div>
                </div>
            </div>

            <div class="charts-grid">
                <div class="chart-card">
                    <h3>Doanh thu theo tháng</h3>
                    <canvas id="revenueChart"></canvas>
                </div>
                <div class="chart-card">
                    <h3>Top sản phẩm bán chạy</h3>
                    <div id="topProducts"></div>
                </div>
            </div>
        </div>

        <!-- Products Section -->
        <div id="products" class="content-section">
            <div class="section-header">
                <h2>Quản lý sản phẩm</h2>
                <button class="btn btn-primary" onclick="openAddProductModal()">
                    <i class="fas fa-plus"></i> Thêm sản phẩm
                </button>
            </div>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Hình ảnh</th>
                            <th>Tên sản phẩm</th>
                            <th>Danh mục</th>
                            <th>Giá</th>
                            <th>Kho</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="productsTable"></tbody>
                </table>
            </div>
        </div>

        <!-- Users Section -->
        <div id="users" class="content-section">
            <div class="section-header">
                <h2>Quản lý người dùng</h2>
                <button class="btn btn-primary" onclick="openAddUserModal()">
                    <i class="fas fa-plus"></i> Thêm người dùng
                </button>
            </div>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Avatar</th>
                            <th>Tên</th>
                            <th>Email</th>
                            <th>Số điện thoại</th>
                            <th>Vai trò</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="usersTable"></tbody>
                </table>
            </div>
        </div>

        <!-- Orders Section -->
        <div id="orders" class="content-section">
            <div class="section-header">
                <h2>Quản lý đơn hàng</h2>
            </div>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Mã đơn</th>
                            <th>Khách hàng</th>
                            <th>Ngày đặt</th>
                            <th>Tổng tiền</th>
                            <th>Trạng thái</th>
                            <th>Thanh toán</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="ordersTable"></tbody>
                </table>
            </div>
        </div>

        <!-- Revenue Section -->
        <div id="revenue" class="content-section">
            <div class="section-header">
                <h2>Báo cáo doanh thu</h2>
                <div class="filter-group">
                    <select id="revenueFilter" onchange="filterRevenue()">
                        <option value="week">Tuần này</option>
                        <option value="month" selected>Tháng này</option>
                        <option value="year">Năm này</option>
                    </select>
                </div>
            </div>
            <div class="stats-grid">
                <div class="stat-card">
                    <h3>Doanh thu tháng này</h3>
                    <p class="stat-number large" id="monthRevenue">0 ₫</p>
                </div>
                <div class="stat-card">
                    <h3>Đơn hàng hoàn thành</h3>
                    <p class="stat-number large" id="completedOrders">0</p>
                </div>
                <div class="stat-card">
                    <h3>Giá trị đơn trung bình</h3>
                    <p class="stat-number large" id="avgOrderValue">0 ₫</p>
                </div>
            </div>
            <div class="chart-card">
                <canvas id="revenueDetailChart"></canvas>
            </div>
        </div>

        <!-- Categories Section -->
        <div id="categories" class="content-section">
            <div class="section-header">
                <h2>Quản lý danh mục</h2>
                <button class="btn btn-primary" onclick="openAddCategoryModal()">
                    <i class="fas fa-plus"></i> Thêm danh mục
                </button>
            </div>
            <div class="categories-grid" id="categoriesGrid"></div>
        </div>

        <!-- Reviews Section -->
        <div id="reviews" class="content-section">
            <div class="section-header">
                <h2>Quản lý đánh giá</h2>
            </div>
            <div class="reviews-list" id="reviewsList"></div>
        </div>

        <!-- Settings Section -->
        <div id="settings" class="content-section">
            <div class="section-header">
                <h2>Cài đặt hệ thống</h2>
            </div>
            <div class="settings-container">
                <div class="settings-card">
                    <h3>Thông tin cửa hàng</h3>
                    <form>
                        <div class="form-group">
                            <label>Tên cửa hàng</label>
                            <input type="text" value="VietTech Shop" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>Email liên hệ</label>
                            <input type="email" value="contact@viettech.com" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>Số điện thoại</label>
                            <input type="tel" value="0123456789" class="form-control">
                        </div>
                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Modals -->
    <div id="productModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Thêm sản phẩm mới</h2>
                <span class="close" onclick="closeModal('productModal')">&times;</span>
            </div>
            <div class="modal-body">
                <form id="productForm">
                    <div class="form-group">
                        <label>Tên sản phẩm</label>
                        <input type="text" id="productName" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Danh mục</label>
                        <select id="productCategory" class="form-control" required>
                            <option value="Điện thoại">Điện thoại</option>
                            <option value="Laptop">Laptop</option>
                            <option value="Phụ kiện">Phụ kiện</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Giá (₫)</label>
                        <input type="number" id="productPrice" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Số lượng</label>
                        <input type="number" id="productStock" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Mô tả</label>
                        <textarea id="productDescription" class="form-control" rows="3"></textarea>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" onclick="closeModal('productModal')">Hủy</button>
                        <button type="submit" class="btn btn-primary">Thêm sản phẩm</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</body>
</html>
