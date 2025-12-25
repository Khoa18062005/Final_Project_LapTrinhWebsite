<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vendor Dashboard - VietTech</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor.css">
</head>
<body>
    <!-- Header / Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="fas fa-store"></i> VietTech Vendor
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="#"><i class="fas fa-bell"></i> Thông báo <span class="badge bg-danger">3</span></a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-circle"></i> Vendor Name
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="#"><i class="fas fa-user"></i> Hồ sơ</a></li>
                            <li><a class="dropdown-item" href="#"><i class="fas fa-cog"></i> Cài đặt</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="#"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav id="sidebar" class="col-md-3 col-lg-2 d-md-block sidebar collapse">
                <div class="position-sticky pt-3">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link active" href="#dashboard" data-section="dashboard">
                                <i class="fas fa-tachometer-alt"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#products" data-section="products">
                                <i class="fas fa-box"></i> Quản lý sản phẩm
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#orders" data-section="orders">
                                <i class="fas fa-shopping-cart"></i> Đơn hàng
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#delivery" data-section="delivery">
                                <i class="fas fa-shipping-fast"></i> Phân phối hàng
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#approvals" data-section="approvals">
                                <i class="fas fa-clock"></i> Chờ phê duyệt
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#statistics" data-section="statistics">
                                <i class="fas fa-chart-bar"></i> Thống kê
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main Content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <!-- Dashboard Section -->
                <div id="dashboard" class="content-section active">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2"><i class="fas fa-tachometer-alt"></i> Dashboard</h1>
                    </div>

                    <!-- Statistics Cards -->
                    <div class="row g-3 mb-4">
                        <div class="col-md-3">
                            <div class="stat-card">
                                <div class="stat-icon bg-primary">
                                    <i class="fas fa-box"></i>
                                </div>
                                <div class="stat-info">
                                    <h3>125</h3>
                                    <p>Tổng sản phẩm</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card">
                                <div class="stat-icon bg-success">
                                    <i class="fas fa-shopping-cart"></i>
                                </div>
                                <div class="stat-info">
                                    <h3>45</h3>
                                    <p>Đơn hàng mới</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card">
                                <div class="stat-icon bg-warning">
                                    <i class="fas fa-clock"></i>
                                </div>
                                <div class="stat-info">
                                    <h3>8</h3>
                                    <p>Chờ phê duyệt</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card">
                                <div class="stat-icon bg-info">
                                    <i class="fas fa-dollar-sign"></i>
                                </div>
                                <div class="stat-info">
                                    <h3>125M</h3>
                                    <p>Doanh thu tháng</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Recent Orders -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5><i class="fas fa-shopping-cart"></i> Đơn hàng gần đây</h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã đơn</th>
                                            <th>Khách hàng</th>
                                            <th>Sản phẩm</th>
                                            <th>Tổng tiền</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>#ORD-001</td>
                                            <td>Nguyễn Văn A</td>
                                            <td>iPhone 15 Pro Max</td>
                                            <td>35.000.000đ</td>
                                            <td><span class="badge bg-warning">Chờ xử lý</span></td>
                                            <td><button class="btn btn-sm btn-primary">Xem</button></td>
                                        </tr>
                                        <tr>
                                            <td>#ORD-002</td>
                                            <td>Trần Thị B</td>
                                            <td>Samsung Galaxy S24</td>
                                            <td>25.000.000đ</td>
                                            <td><span class="badge bg-info">Đang giao</span></td>
                                            <td><button class="btn btn-sm btn-primary">Xem</button></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Products Section -->
                <div id="products" class="content-section">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2"><i class="fas fa-box"></i> Quản lý sản phẩm</h1>
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addProductModal">
                            <i class="fas fa-plus"></i> Thêm sản phẩm mới
                        </button>
                    </div>

                    <!-- Filter -->
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="Tìm kiếm sản phẩm...">
                                </div>
                                <div class="col-md-3">
                                    <select class="form-select">
                                        <option>Tất cả danh mục</option>
                                        <option>Điện thoại</option>
                                        <option>Laptop</option>
                                        <option>Phụ kiện</option>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <select class="form-select">
                                        <option>Tất cả trạng thái</option>
                                        <option>Đang bán</option>
                                        <option>Hết hàng</option>
                                        <option>Chờ duyệt</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <button class="btn btn-secondary w-100"><i class="fas fa-search"></i> Tìm</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Products Table -->
                    <div class="card">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Hình ảnh</th>
                                            <th>Tên sản phẩm</th>
                                            <th>SKU</th>
                                            <th>Giá</th>
                                            <th>Tồn kho</th>
                                            <th>Trạng thái</th>
                                            <th>Phê duyệt</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><img src="https://via.placeholder.com/50" alt="Product" class="img-thumbnail"></td>
                                            <td>iPhone 15 Pro Max</td>
                                            <td>IP15PM-001</td>
                                            <td>35.000.000đ</td>
                                            <td>50</td>
                                            <td><span class="badge bg-success">Đang bán</span></td>
                                            <td><span class="badge bg-success"><i class="fas fa-check"></i> Đã duyệt</span></td>
                                            <td>
                                                <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editProductModal">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteProductModal">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><img src="https://via.placeholder.com/50" alt="Product" class="img-thumbnail"></td>
                                            <td>Samsung Galaxy S24 Ultra</td>
                                            <td>SGS24U-001</td>
                                            <td>28.000.000đ</td>
                                            <td>30</td>
                                            <td><span class="badge bg-success">Đang bán</span></td>
                                            <td><span class="badge bg-warning"><i class="fas fa-clock"></i> Chờ duyệt</span></td>
                                            <td>
                                                <button class="btn btn-sm btn-warning" disabled>
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button class="btn btn-sm btn-danger" disabled>
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><img src="https://via.placeholder.com/50" alt="Product" class="img-thumbnail"></td>
                                            <td>MacBook Pro M3</td>
                                            <td>MBP-M3-001</td>
                                            <td>45.000.000đ</td>
                                            <td>0</td>
                                            <td><span class="badge bg-danger">Hết hàng</span></td>
                                            <td><span class="badge bg-success"><i class="fas fa-check"></i> Đã duyệt</span></td>
                                            <td>
                                                <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editProductModal">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteProductModal">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!-- Pagination -->
                            <nav>
                                <ul class="pagination justify-content-center">
                                    <li class="page-item disabled"><a class="page-link" href="#">Trước</a></li>
                                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                                    <li class="page-item"><a class="page-link" href="#">Sau</a></li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>

                <!-- Orders Section -->
                <div id="orders" class="content-section">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2"><i class="fas fa-shopping-cart"></i> Quản lý đơn hàng</h1>
                    </div>

                    <!-- Order Filters -->
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="Tìm mã đơn hàng...">
                                </div>
                                <div class="col-md-3">
                                    <select class="form-select">
                                        <option>Tất cả trạng thái</option>
                                        <option>Chờ xử lý</option>
                                        <option>Đã xác nhận</option>
                                        <option>Đang giao</option>
                                        <option>Hoàn thành</option>
                                        <option>Đã hủy</option>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <input type="date" class="form-control">
                                </div>
                                <div class="col-md-2">
                                    <button class="btn btn-secondary w-100"><i class="fas fa-search"></i> Tìm</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Orders Table -->
                    <div class="card">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã đơn</th>
                                            <th>Khách hàng</th>
                                            <th>Ngày đặt</th>
                                            <th>Sản phẩm</th>
                                            <th>Tổng tiền</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>#ORD-001</td>
                                            <td>Nguyễn Văn A<br><small>0912345678</small></td>
                                            <td>25/12/2025</td>
                                            <td>iPhone 15 Pro Max x1</td>
                                            <td>35.000.000đ</td>
                                            <td>
                                                <select class="form-select form-select-sm">
                                                    <option>Chờ xử lý</option>
                                                    <option>Đã xác nhận</option>
                                                    <option>Đang giao</option>
                                                    <option>Hoàn thành</option>
                                                    <option>Đã hủy</option>
                                                </select>
                                            </td>
                                            <td>
                                                <button class="btn btn-sm btn-success" title="Cập nhật trạng thái">
                                                    <i class="fas fa-check"></i>
                                                </button>
                                                <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#orderDetailModal">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>#ORD-002</td>
                                            <td>Trần Thị B<br><small>0987654321</small></td>
                                            <td>24/12/2025</td>
                                            <td>Samsung Galaxy S24 x1</td>
                                            <td>25.000.000đ</td>
                                            <td>
                                                <select class="form-select form-select-sm">
                                                    <option>Chờ xử lý</option>
                                                    <option selected>Đã xác nhận</option>
                                                    <option>Đang giao</option>
                                                    <option>Hoàn thành</option>
                                                    <option>Đã hủy</option>
                                                </select>
                                            </td>
                                            <td>
                                                <button class="btn btn-sm btn-success" title="Cập nhật trạng thái">
                                                    <i class="fas fa-check"></i>
                                                </button>
                                                <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#orderDetailModal">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>#ORD-003</td>
                                            <td>Lê Văn C<br><small>0901234567</small></td>
                                            <td>23/12/2025</td>
                                            <td>MacBook Pro M3 x1</td>
                                            <td>45.000.000đ</td>
                                            <td><span class="badge bg-success">Hoàn thành</span></td>
                                            <td>
                                                <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#orderDetailModal">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Delivery Section -->
                <div id="delivery" class="content-section">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2"><i class="fas fa-shipping-fast"></i> Phân phối hàng cho Shipper</h1>
                    </div>

                    <!-- Pending Deliveries -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5>Đơn hàng chờ phân shipper</h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã đơn</th>
                                            <th>Khách hàng</th>
                                            <th>Địa chỉ giao</th>
                                            <th>Khu vực</th>
                                            <th>Giá trị</th>
                                            <th>Shipper</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>#ORD-001</td>
                                            <td>Nguyễn Văn A<br><small>0912345678</small></td>
                                            <td>123 Lê Lợi, Q.1, TP.HCM</td>
                                            <td><span class="badge bg-info">Quận 1</span></td>
                                            <td>35.000.000đ</td>
                                            <td>
                                                <select class="form-select form-select-sm">
                                                    <option value="">-- Chọn shipper --</option>
                                                    <option value="1">Shipper 1 (Q.1, Q.3)</option>
                                                    <option value="2">Shipper 2 (Q.1, Q.5)</option>
                                                    <option value="3">Shipper 3 (Q.2, Q.4)</option>
                                                </select>
                                            </td>
                                            <td>
                                                <button class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#assignShipperModal">
                                                    <i class="fas fa-user-plus"></i> Gán
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>#ORD-002</td>
                                            <td>Trần Thị B<br><small>0987654321</small></td>
                                            <td>456 Nguyễn Huệ, Q.3, TP.HCM</td>
                                            <td><span class="badge bg-info">Quận 3</span></td>
                                            <td>25.000.000đ</td>
                                            <td>
                                                <select class="form-select form-select-sm">
                                                    <option value="">-- Chọn shipper --</option>
                                                    <option value="1">Shipper 1 (Q.1, Q.3)</option>
                                                    <option value="2">Shipper 2 (Q.1, Q.5)</option>
                                                    <option value="3">Shipper 3 (Q.2, Q.4)</option>
                                                </select>
                                            </td>
                                            <td>
                                                <button class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#assignShipperModal">
                                                    <i class="fas fa-user-plus"></i> Gán
                                                </button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- Assigned Deliveries -->
                    <div class="card">
                        <div class="card-header">
                            <h5>Đơn hàng đã phân shipper</h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã đơn</th>
                                            <th>Khách hàng</th>
                                            <th>Địa chỉ giao</th>
                                            <th>Shipper</th>
                                            <th>SĐT Shipper</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>#ORD-003</td>
                                            <td>Lê Văn C<br><small>0901234567</small></td>
                                            <td>789 Võ Văn Tần, Q.3, TP.HCM</td>
                                            <td>Shipper 1</td>
                                            <td>0911111111</td>
                                            <td><span class="badge bg-info">Đang giao</span></td>
                                            <td>
                                                <button class="btn btn-sm btn-warning" title="Thay đổi shipper">
                                                    <i class="fas fa-exchange-alt"></i>
                                                </button>
                                                <button class="btn btn-sm btn-primary" title="Chi tiết">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>#ORD-004</td>
                                            <td>Phạm Thị D<br><small>0909876543</small></td>
                                            <td>321 Điện Biên Phủ, Q.1, TP.HCM</td>
                                            <td>Shipper 2</td>
                                            <td>0922222222</td>
                                            <td><span class="badge bg-success">Đã giao</span></td>
                                            <td>
                                                <button class="btn btn-sm btn-primary" title="Chi tiết">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Approvals Section -->
                <div id="approvals" class="content-section">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2"><i class="fas fa-clock"></i> Yêu cầu chờ phê duyệt</h1>
                    </div>

                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i> Các thao tác thêm, sửa, xóa sản phẩm cần được Admin phê duyệt trước khi có hiệu lực.
                    </div>

                    <div class="card">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Loại yêu cầu</th>
                                            <th>Sản phẩm</th>
                                            <th>Nội dung</th>
                                            <th>Ngày gửi</th>
                                            <th>Trạng thái</th>
                                            <th>Ghi chú</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><span class="badge bg-success">Thêm mới</span></td>
                                            <td>Samsung Galaxy S24 Ultra</td>
                                            <td>Thêm sản phẩm mới</td>
                                            <td>24/12/2025</td>
                                            <td><span class="badge bg-warning">Chờ duyệt</span></td>
                                            <td>-</td>
                                        </tr>
                                        <tr>
                                            <td><span class="badge bg-primary">Cập nhật</span></td>
                                            <td>iPhone 15 Pro Max</td>
                                            <td>Thay đổi giá: 35.000.000đ → 34.000.000đ</td>
                                            <td>23/12/2025</td>
                                            <td><span class="badge bg-success">Đã duyệt</span></td>
                                            <td>Admin đã phê duyệt</td>
                                        </tr>
                                        <tr>
                                            <td><span class="badge bg-danger">Xóa</span></td>
                                            <td>iPad Pro 12.9</td>
                                            <td>Yêu cầu xóa sản phẩm</td>
                                            <td>22/12/2025</td>
                                            <td><span class="badge bg-danger">Từ chối</span></td>
                                            <td>Sản phẩm còn trong đơn hàng</td>
                                        </tr>
                                        <tr>
                                            <td><span class="badge bg-primary">Cập nhật</span></td>
                                            <td>MacBook Pro M3</td>
                                            <td>Cập nhật mô tả sản phẩm</td>
                                            <td>25/12/2025</td>
                                            <td><span class="badge bg-warning">Chờ duyệt</span></td>
                                            <td>-</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Statistics Section -->
                <div id="statistics" class="content-section">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2"><i class="fas fa-chart-bar"></i> Thống kê</h1>
                    </div>

                    <div class="row g-3">
                        <div class="col-md-6">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Doanh thu theo tháng</h5>
                                </div>
                                <div class="card-body">
                                    <canvas id="revenueChart" height="200"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Đơn hàng theo trạng thái</h5>
                                </div>
                                <div class="card-body">
                                    <canvas id="orderStatusChart" height="200"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Top 10 sản phẩm bán chạy</h5>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Sản phẩm</th>
                                                    <th>Đã bán</th>
                                                    <th>Doanh thu</th>
                                                    <th>Đánh giá</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td>1</td>
                                                    <td>iPhone 15 Pro Max</td>
                                                    <td>120</td>
                                                    <td>4.200.000.000đ</td>
                                                    <td><i class="fas fa-star text-warning"></i> 4.8</td>
                                                </tr>
                                                <tr>
                                                    <td>2</td>
                                                    <td>Samsung Galaxy S24</td>
                                                    <td>95</td>
                                                    <td>2.375.000.000đ</td>
                                                    <td><i class="fas fa-star text-warning"></i> 4.6</td>
                                                </tr>
                                                <tr>
                                                    <td>3</td>
                                                    <td>MacBook Pro M3</td>
                                                    <td>85</td>
                                                    <td>3.825.000.000đ</td>
                                                    <td><i class="fas fa-star text-warning"></i> 4.9</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Modals -->

    <!-- Add Product Modal -->
    <div class="modal fade" id="addProductModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-plus"></i> Thêm sản phẩm mới</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="alert alert-warning">
                        <i class="fas fa-exclamation-triangle"></i> Sản phẩm cần được Admin phê duyệt trước khi hiển thị
                    </div>
                    <form>
                        <div class="row g-3">
                            <div class="col-md-12">
                                <label class="form-label">Tên sản phẩm <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">SKU <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Danh mục <span class="text-danger">*</span></label>
                                <select class="form-select" required>
                                    <option value="">-- Chọn danh mục --</option>
                                    <option>Điện thoại</option>
                                    <option>Laptop</option>
                                    <option>Phụ kiện</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Giá <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Số lượng <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Đơn vị</label>
                                <input type="text" class="form-control" value="Chiếc">
                            </div>
                            <div class="col-md-12">
                                <label class="form-label">Mô tả ngắn</label>
                                <textarea class="form-control" rows="2"></textarea>
                            </div>
                            <div class="col-md-12">
                                <label class="form-label">Mô tả chi tiết</label>
                                <textarea class="form-control" rows="4"></textarea>
                            </div>
                            <div class="col-md-12">
                                <label class="form-label">Hình ảnh</label>
                                <input type="file" class="form-control" multiple>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-primary">Gửi yêu cầu</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Product Modal -->
    <div class="modal fade" id="editProductModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-edit"></i> Cập nhật sản phẩm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="alert alert-warning">
                        <i class="fas fa-exclamation-triangle"></i> Thay đổi cần được Admin phê duyệt
                    </div>
                    <form>
                        <div class="row g-3">
                            <div class="col-md-12">
                                <label class="form-label">Tên sản phẩm <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" value="iPhone 15 Pro Max" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">SKU <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" value="IP15PM-001" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Danh mục <span class="text-danger">*</span></label>
                                <select class="form-select" required>
                                    <option>Điện thoại</option>
                                    <option>Laptop</option>
                                    <option>Phụ kiện</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Giá <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" value="35000000" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Số lượng <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" value="50" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Trạng thái</label>
                                <select class="form-select">
                                    <option selected>Đang bán</option>
                                    <option>Hết hàng</option>
                                    <option>Ngừng bán</option>
                                </select>
                            </div>
                            <div class="col-md-12">
                                <label class="form-label">Mô tả ngắn</label>
                                <textarea class="form-control" rows="2">Điện thoại cao cấp từ Apple</textarea>
                            </div>
                            <div class="col-md-12">
                                <label class="form-label">Mô tả chi tiết</label>
                                <textarea class="form-control" rows="4">Chi tiết về sản phẩm iPhone 15 Pro Max...</textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-primary">Gửi yêu cầu cập nhật</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Delete Product Modal -->
    <div class="modal fade" id="deleteProductModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-trash"></i> Xóa sản phẩm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-triangle"></i> Yêu cầu xóa cần được Admin phê duyệt
                    </div>
                    <p>Bạn có chắc chắn muốn gửi yêu cầu xóa sản phẩm này?</p>
                    <div class="form-group">
                        <label>Lý do xóa:</label>
                        <textarea class="form-control" rows="3" placeholder="Nhập lý do..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-danger">Gửi yêu cầu xóa</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Order Detail Modal -->
    <div class="modal fade" id="orderDetailModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-shopping-cart"></i> Chi tiết đơn hàng #ORD-001</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <h6>Thông tin khách hàng</h6>
                            <p><strong>Tên:</strong> Nguyễn Văn A</p>
                            <p><strong>SĐT:</strong> 0912345678</p>
                            <p><strong>Email:</strong> nguyenvana@email.com</p>
                        </div>
                        <div class="col-md-6">
                            <h6>Địa chỉ giao hàng</h6>
                            <p>123 Lê Lợi, Phường Bến Thành<br>Quận 1, TP. Hồ Chí Minh</p>
                        </div>
                        <div class="col-md-12">
                            <h6>Sản phẩm</h6>
                            <table class="table table-sm">
                                <thead>
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th>Đơn giá</th>
                                        <th>Số lượng</th>
                                        <th>Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>iPhone 15 Pro Max</td>
                                        <td>35.000.000đ</td>
                                        <td>1</td>
                                        <td>35.000.000đ</td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="3" class="text-end"><strong>Tổng cộng:</strong></td>
                                        <td><strong>35.000.000đ</strong></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                        <div class="col-md-12">
                            <h6>Lịch sử trạng thái</h6>
                            <ul class="timeline">
                                <li>25/12/2025 10:30 - Đơn hàng đã đặt</li>
                                <li>25/12/2025 11:00 - Chờ xử lý</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="button" class="btn btn-primary">In đơn hàng</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Assign Shipper Modal -->
    <div class="modal fade" id="assignShipperModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-user-plus"></i> Phân công Shipper</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="mb-3">
                            <label class="form-label">Mã đơn hàng</label>
                            <input type="text" class="form-control" value="#ORD-001" readonly>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Chọn Shipper <span class="text-danger">*</span></label>
                            <select class="form-select" required>
                                <option value="">-- Chọn shipper --</option>
                                <option value="1">Shipper 1 - Nguyễn Văn X (Q.1, Q.3) - 5 đơn</option>
                                <option value="2">Shipper 2 - Trần Văn Y (Q.1, Q.5) - 3 đơn</option>
                                <option value="3">Shipper 3 - Lê Văn Z (Q.2, Q.4) - 7 đơn</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Ghi chú cho Shipper</label>
                            <textarea class="form-control" rows="3" placeholder="Ghi chú giao hàng..."></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Thời gian dự kiến lấy hàng</label>
                            <input type="datetime-local" class="form-control">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-success">Xác nhận phân công</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Custom JS -->
    <script>
        // Navigation
        document.querySelectorAll('.sidebar .nav-link').forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();

                // Remove active class from all links and sections
                document.querySelectorAll('.sidebar .nav-link').forEach(l => l.classList.remove('active'));
                document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));

                // Add active class to clicked link
                this.classList.add('active');

                // Show corresponding section
                const sectionId = this.getAttribute('data-section');
                document.getElementById(sectionId).classList.add('active');
            });
        });
    </script>
</body>
</html>

