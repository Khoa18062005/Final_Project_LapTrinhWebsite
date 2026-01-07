<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vendor Dashboard - VietTech</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
            --success-color: #27ae60;
            --warning-color: #f39c12;
            --danger-color: #e74c3c;
            --sidebar-width: 260px;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
        }

        /* Navbar */
        .navbar {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1030;
        }

        .navbar-brand {
            font-weight: bold;
            font-size: 1.5rem;
            color: white !important;
        }

        .navbar-brand:hover {
            transform: scale(1.05);
            transition: transform 0.3s;
        }

        /* Sidebar */
        .sidebar {
            position: fixed;
            top: 56px;
            left: 0;
            bottom: 0;
            width: var(--sidebar-width);
            background: white;
            box-shadow: 4px 0 15px rgba(0,0,0,0.1);
            overflow-y: auto;
            z-index: 1000;
            padding: 20px 0;
        }

        .sidebar::-webkit-scrollbar {
            width: 6px;
        }

        .sidebar::-webkit-scrollbar-thumb {
            background: #ddd;
            border-radius: 3px;
        }

        .sidebar .nav-link {
            color: #333;
            padding: 15px 25px;
            margin: 5px 15px;
            border-radius: 10px;
            transition: all 0.3s;
            font-weight: 500;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .sidebar .nav-link i {
            margin-right: 12px;
            width: 20px;
            text-align: center;
        }

        .sidebar .nav-link:hover {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            transform: translateX(5px);
        }

        .sidebar .nav-link.active {
            background: linear-gradient(135deg, var(--secondary-color), #5dade2);
            color: white;
            box-shadow: 0 4px 10px rgba(52, 152, 219, 0.3);
        }

        /* Main Content */
        .main-content {
            margin-left: var(--sidebar-width);
            margin-top: 56px;
            padding: 30px;
            min-height: calc(100vh - 56px);
        }

        /* Stats Cards */
        .stats-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            transition: all 0.3s;
            position: relative;
            overflow: hidden;
        }

        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 4px;
            height: 100%;
            background: var(--secondary-color);
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.15);
        }

        .stat-card .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            color: white;
            margin-bottom: 15px;
        }

        .stat-card .stat-value {
            font-size: 2rem;
            font-weight: bold;
            color: var(--primary-color);
            margin: 10px 0;
        }

        .stat-card .stat-label {
            color: #7f8c8d;
            font-size: 0.95rem;
            font-weight: 500;
        }

        .bg-gradient-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        .bg-gradient-success {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
        }

        .bg-gradient-warning {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        .bg-gradient-info {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        /* Quick Actions */
        .quick-actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            margin-bottom: 30px;
        }

        .quick-action-btn {
            background: white;
            border: none;
            border-radius: 12px;
            padding: 20px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            text-decoration: none;
            color: #333;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
        }

        .quick-action-btn:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
            color: var(--secondary-color);
        }

        .quick-action-btn i {
            font-size: 2.5rem;
        }

        .quick-action-btn span {
            font-weight: 600;
            font-size: 0.95rem;
        }

        /* Cards */
        .content-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
            overflow: hidden;
        }

        .content-card .card-header {
            background: linear-gradient(135deg, #f8f9fa, #e9ecef);
            border-bottom: 3px solid var(--secondary-color);
            padding: 20px 25px;
            font-weight: 600;
            font-size: 1.1rem;
            color: var(--primary-color);
        }

        .content-card .card-body {
            padding: 25px;
        }

        /* Table */
        .table {
            margin-bottom: 0;
        }

        .table thead th {
            background: #f8f9fa;
            border: none;
            font-weight: 600;
            color: var(--primary-color);
            padding: 15px;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }

        .table tbody td {
            padding: 15px;
            vertical-align: middle;
            border-bottom: 1px solid #f0f0f0;
        }

        .table tbody tr:hover {
            background: #f8f9fa;
        }

        /* Badges */
        .badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-weight: 500;
            font-size: 0.85rem;
        }

        .badge-pending {
            background: #ffeaa7;
            color: #d63031;
        }

        .badge-processing {
            background: #74b9ff;
            color: #0984e3;
        }

        .badge-confirmed {
            background: #a29bfe;
            color: #6c5ce7;
        }

        .badge-completed {
            background: #55efc4;
            color: #00b894;
        }

        .badge-cancelled {
            background: #fab1a0;
            color: #d63031;
        }

        /* Buttons */
        .btn {
            border-radius: 8px;
            padding: 10px 20px;
            font-weight: 500;
            transition: all 0.3s;
            border: none;
        }

        .btn-primary {
            background: linear-gradient(135deg, var(--secondary-color), #5dade2);
        }

        .btn-primary:hover {
            background: linear-gradient(135deg, #2980b9, var(--secondary-color));
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(52, 152, 219, 0.3);
        }

        .btn-success {
            background: linear-gradient(135deg, var(--success-color), #2ecc71);
        }

        .btn-success:hover {
            background: linear-gradient(135deg, #229954, var(--success-color));
        }

        .btn-warning {
            background: linear-gradient(135deg, var(--warning-color), #f8c471);
        }

        .btn-danger {
            background: linear-gradient(135deg, var(--danger-color), #ec7063);
        }

        /* Modal */
        .modal-content {
            border-radius: 15px;
            border: none;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }

        .modal-header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            border-radius: 15px 15px 0 0;
            padding: 20px 25px;
        }

        .modal-header .btn-close {
            filter: brightness(0) invert(1);
        }

        /* Form */
        .form-label {
            font-weight: 600;
            color: var(--primary-color);
            margin-bottom: 8px;
        }

        .form-control, .form-select {
            border-radius: 8px;
            border: 2px solid #e0e0e0;
            padding: 10px 15px;
            transition: all 0.3s;
        }

        .form-control:focus, .form-select:focus {
            border-color: var(--secondary-color);
            box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
        }

        /* Alerts */
        .alert {
            border-radius: 10px;
            border: none;
            padding: 15px 20px;
            margin-bottom: 20px;
        }

        /* Border utilities */
        .border-left-primary {
            border-left: 4px solid var(--secondary-color) !important;
        }

        /* Warehouse info styles */
        .warehouse-info {
            background: linear-gradient(135deg, #f8f9fa, #e9ecef);
            border-radius: 10px;
            border-left: 4px solid var(--secondary-color);
        }

        .warehouse-details div {
            line-height: 1.4;
        }

        /* Responsive */
        @media (max-width: 992px) {
            .sidebar {
                transform: translateX(-100%);
                transition: transform 0.3s;
            }

            .sidebar.show {
                transform: translateX(0);
            }

            .main-content {
                margin-left: 0;
            }
        }

        /* Empty State */
        .empty-state {
            text-align: center;
            padding: 60px 20px;
        }

        .empty-state i {
            font-size: 4rem;
            color: #bdc3c7;
            margin-bottom: 20px;
        }

        .empty-state h5 {
            color: #7f8c8d;
            margin-bottom: 10px;
        }

        .empty-state p {
            color: #95a5a6;
        }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/vendor">
            <i class="fas fa-store"></i> VietTech Vendor
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-user-circle fa-lg"></i>
                        <c:choose>
                            <c:when test="${not empty data.vendorInfo}">
                                ${data.vendorInfo.businessName}
                            </c:when>
                            <c:otherwise>
                                ${sessionScope.user.email}
                            </c:otherwise>
                        </c:choose>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li><a class="dropdown-item" href="#"><i class="fas fa-user-cog"></i> Cài đặt tài khoản</a></li>
                        <li><a class="dropdown-item" href="#"><i class="fas fa-bell"></i> Thông báo</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                            <i class="fas fa-sign-out-alt"></i> Đăng xuất
                        </a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Sidebar -->
<nav class="sidebar">
    <!-- Warehouse Information -->
    <c:if test="${not empty data.warehouseInfo}">
        <div class="warehouse-info mb-4 p-3 bg-light rounded">
            <h6 class="text-primary mb-2">
                <i class="fas fa-warehouse"></i> Kho hàng
            </h6>
            <div class="warehouse-details small">
                <div class="mb-1">
                    <strong>${data.warehouseInfo.name}</strong>
                </div>
                <div class="mb-1">
                    <i class="fas fa-hashtag"></i> ${data.warehouseInfo.code}
                </div>
                <div class="mb-1">
                    <i class="fas fa-map-marker-alt"></i>
                    ${data.warehouseInfo.addressLine}
                    <c:if test="${not empty data.warehouseInfo.ward}">, ${data.warehouseInfo.ward}</c:if>
                    <c:if test="${not empty data.warehouseInfo.district}">, ${data.warehouseInfo.district}</c:if>
                    <c:if test="${not empty data.warehouseInfo.province}">, ${data.warehouseInfo.province}</c:if>
                </div>
            </div>
        </div>
    </c:if>

    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link active" href="${pageContext.request.contextPath}/vendor">
                <span><i class="fas fa-tachometer-alt"></i> Dashboard</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/vendor?action=products">
                <span><i class="fas fa-box"></i> Quản lý sản phẩm</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/vendor?action=orders">
                <span><i class="fas fa-shopping-cart"></i> Đơn hàng</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/vendor?action=shipping">
                <span><i class="fas fa-truck"></i> Giao hàng</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/vendor?action=stats">
                <span><i class="fas fa-chart-bar"></i> Thống kê</span>
            </a>
        </li>
    </ul>
</nav>

<!-- Main Content - ROUTING BASED ON ACTION -->
<div class="main-content">
    <!-- Alert Messages (shown on all pages) -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> <strong>Lỗi!</strong> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> <strong>Thành công!</strong> ${success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:choose>
        <%-- DASHBOARD PAGE (default) --%>
        <c:when test="${empty param.action or param.action == 'dashboard'}">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2><i class="fas fa-tachometer-alt"></i> Dashboard</h2>
                    <p class="text-muted mb-0">Tổng quan hoạt động gian hàng</p>
                </div>
            </div>

            <div class="stats-row">
                <div class="stat-card">
                    <div class="stat-icon bg-gradient-primary"><i class="fas fa-box"></i></div>
                    <div class="stat-value">${not empty data ? data.totalProducts : 0}</div>
                    <div class="stat-label">Sản phẩm</div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon bg-gradient-warning"><i class="fas fa-clock"></i></div>
                    <div class="stat-value">${not empty data ? data.newOrdersCount : 0}</div>
                    <div class="stat-label">Đơn mới (Pending)</div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon bg-gradient-info"><i class="fas fa-truck"></i></div>
                    <div class="stat-value">${not empty data && not empty data.pendingShippingOrders ? fn:length(data.pendingShippingOrders) : 0}</div>
                    <div class="stat-label">Đơn chờ giao</div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon bg-gradient-success"><i class="fas fa-check-circle"></i></div>
                    <div class="stat-value">${not empty data ? data.pendingApprovals : 0}</div>
                    <div class="stat-label">Sản phẩm chờ duyệt</div>
                </div>
            </div>

            <div class="row g-3">
                <div class="col-lg-7">
                    <div class="card mb-3">
                        <div class="card-header"><i class="fas fa-clock"></i> Đơn hàng gần đây</div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead>
                                    <tr>
                                        <th>Mã đơn</th>
                                        <th>Ngày đặt</th>
                                        <th class="text-end">Tổng tiền</th>
                                        <th>Trạng thái</th>
                                        <th class="text-end"></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${not empty data and not empty data.recentOrders}">
                                            <c:forEach items="${data.recentOrders}" var="o" begin="0" end="9">
                                                <tr>
                                                    <td><strong>#${o.orderNumber}</strong></td>
                                                    <td>
                                                        <c:if test="${not empty o.orderDate}">
                                                            <fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy"/>
                                                        </c:if>
                                                    </td>
                                                    <td class="text-end text-success fw-bold">
                                                        <fmt:formatNumber value="${o.totalPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-secondary">${o.status}</span>
                                                    </td>
                                                    <td class="text-end">
                                                        <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/vendor?action=orders">Chi tiết</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="5" class="text-center text-muted p-4">Chưa có đơn hàng gần đây</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-5">
                    <div class="card mb-3">
                        <div class="card-header"><i class="fas fa-truck"></i> Đơn chờ giao (Ready)</div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead>
                                    <tr>
                                        <th>Mã đơn</th>
                                        <th>Ngày đặt</th>
                                        <th class="text-end">Tổng</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${not empty data and not empty data.pendingShippingOrders}">
                                            <c:forEach items="${data.pendingShippingOrders}" var="o" begin="0" end="9">
                                                <tr>
                                                    <td><strong>#${o.orderNumber}</strong></td>
                                                    <td>
                                                        <c:if test="${not empty o.orderDate}">
                                                            <fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy"/>
                                                        </c:if>
                                                    </td>
                                                    <td class="text-end fw-bold">
                                                        <fmt:formatNumber value="${o.totalPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="3" class="text-center text-muted p-4">Không có đơn chờ giao</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="card-footer bg-white text-end">
                            <a class="btn btn-sm btn-primary" href="${pageContext.request.contextPath}/vendor?action=shipping">
                                <i class="fas fa-truck"></i> Quản lý giao hàng
                            </a>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header"><i class="fas fa-box"></i> Sản phẩm (tóm tắt)</div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <span class="text-muted">Tổng sản phẩm</span>
                                <strong>${not empty data ? data.totalProducts : 0}</strong>
                            </div>
                            <div class="d-flex justify-content-between mt-2">
                                <span class="text-muted">Sản phẩm chờ duyệt</span>
                                <strong>${not empty data ? data.pendingApprovals : 0}</strong>
                            </div>
                            <div class="mt-3 text-end">
                                <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/vendor?action=products">
                                    <i class="fas fa-box"></i> Quản lý sản phẩm
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>

        <%-- ORDERS PAGE --%>
        <c:when test="${param.action == 'orders'}">
            <!-- Page Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2><i class="fas fa-shopping-cart"></i> Quản lý đơn hàng</h2>
                    <p class="text-muted mb-0">Xử lý và theo dõi đơn hàng</p>
                </div>
            </div>

            <!-- Status Filter (Vendor minimal statuses only) -->
            <div class="status-filter mb-4">
                <a href="${pageContext.request.contextPath}/vendor?action=orders"
                   class="btn ${empty param.status ? 'btn-primary' : 'btn-outline-secondary'}">
                    <i class="fas fa-list"></i> Tất cả
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=CONFIRMED"
                   class="btn ${(param.status == 'CONFIRMED' or param.status == 'Confirmed') ? 'btn-primary' : 'btn-outline-primary'}">
                    <i class="fas fa-check"></i> CONFIRMED
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=PROCESSING"
                   class="btn ${(param.status == 'PROCESSING' or param.status == 'Processing') ? 'btn-info' : 'btn-outline-info'}">
                    <i class="fas fa-box"></i> PROCESSING
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=READY"
                   class="btn ${(param.status == 'READY' or param.status == 'Ready') ? 'btn-warning' : 'btn-outline-warning'}">
                    <i class="fas fa-dolly"></i> READY
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=SHIPPING"
                   class="btn ${(param.status == 'SHIPPING' or param.status == 'Shipping') ? 'btn-warning' : 'btn-outline-warning'}">
                    <i class="fas fa-truck"></i> SHIPPING
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=COMPLETED"
                   class="btn ${(param.status == 'COMPLETED' or param.status == 'Completed') ? 'btn-success' : 'btn-outline-success'}">
                    <i class="fas fa-check-double"></i> COMPLETED
                </a>
            </div>

            <!-- Orders Table -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="fas fa-list"></i>
                        Danh sách đơn hàng
                        <c:if test="${not empty param.status}"> - ${param.status}</c:if>
                    </h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Mã đơn hàng</th>
                                    <th>Khách hàng</th>
                                    <th>Sản phẩm</th>
                                    <th>Ngày đặt</th>
                                    <th>Tổng tiền</th>
                                    <th>Trạng thái</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty orders}">
                                        <c:forEach items="${orders}" var="order">
                                            <tr>
                                                <td><strong>#${order.orderNumber}</strong></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty order.customer}">
                                                            <c:choose>
                                                                <c:when test="${not empty order.customer.firstName || not empty order.customer.lastName}">
                                                                    ${order.customer.firstName} ${order.customer.lastName}<br>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${order.customer.email}<br>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <small class="text-muted">${order.customer.email}</small>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">N/A</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <button class="btn btn-sm btn-info"
                                                            onclick="viewOrderDetails(${order.orderId})">
                                                        <i class="fas fa-box"></i> Xem SP
                                                    </button>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/><br>
                                                    <small class="text-muted">
                                                        <fmt:formatDate value="${order.orderDate}" pattern="HH:mm"/>
                                                    </small>
                                                </td>
                                                <td>
                                                    <strong class="text-success">
                                                        <fmt:formatNumber value="${order.totalPrice}" type="currency"
                                                                        currencySymbol="" maxFractionDigits="0"/>đ
                                                    </strong>
                                                </td>
                                                <td>
                                                    <span class="badge badge-${fn:toLowerCase(order.status)}">
                                                        ${order.status}
                                                    </span>
                                                </td>
                                                <td class="order-actions">

                                                    <%-- Vendor minimal flow: CONFIRMED -> PROCESSING -> READY -> SHIPPING -> COMPLETED --%>
                                                    <c:if test="${order.status == 'CONFIRMED' || order.status == 'Confirmed'}">
                                                        <button class="btn btn-sm btn-primary"
                                                                onclick="updateOrderStatus(${order.orderId}, 'PROCESSING')">
                                                            <i class="fas fa-play"></i> PROCESSING
                                                        </button>
                                                    </c:if>

                                                    <c:if test="${order.status == 'PROCESSING' || order.status == 'Processing'}">
                                                        <button class="btn btn-sm btn-info"
                                                                onclick="updateOrderStatus(${order.orderId}, 'READY')">
                                                            <i class="fas fa-box"></i> READY
                                                        </button>
                                                    </c:if>

                                                    <c:if test="${order.status == 'READY' || order.status == 'Ready'}">
                                                        <button class="btn btn-sm btn-warning"
                                                                onclick="updateOrderStatus(${order.orderId}, 'SHIPPING')">
                                                            <i class="fas fa-truck"></i> SHIPPING
                                                        </button>
                                                    </c:if>

                                                    <c:if test="${order.status == 'SHIPPING' || order.status == 'Shipping'}">
                                                        <button class="btn btn-sm btn-success"
                                                                onclick="updateOrderStatus(${order.orderId}, 'COMPLETED')">
                                                            <i class="fas fa-check-double"></i> COMPLETED
                                                        </button>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="7">
                                                <div class="empty-state">
                                                    <i class="fas fa-inbox"></i>
                                                    <h5>Không có đơn hàng nào</h5>
                                                    <c:if test="${not empty param.status}">
                                                        <p>Không có đơn hàng với trạng thái "${param.status}"</p>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Cancel Order Modal -->
            <div class="modal fade" id="cancelOrderModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="fas fa-times-circle"></i> Hủy đơn hàng</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" id="cancelOrderId">
                            <div class="mb-3">
                                <label class="form-label">Lý do hủy *</label>
                                <textarea class="form-control" id="cancelReason" rows="3" required
                                          placeholder="Nhập lý do hủy đơn hàng..."></textarea>
                            </div>
                            <div class="alert alert-warning">
                                <i class="fas fa-exclamation-triangle"></i>
                                Hành động này không thể hoàn tác!
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="button" class="btn btn-danger" onclick="confirmCancelOrder()">
                                <i class="fas fa-times"></i> Xác nhận hủy
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Edit Notes Modal (Order) -->
            <div class="modal fade" id="editNotesModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="fas fa-pen"></i> Cập nhật ghi chú đơn hàng</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" id="editNotesOrderId">
                            <div class="mb-3">
                                <label class="form-label">Ghi chú</label>
                                <textarea class="form-control" id="editNotesText" rows="3" placeholder="Nhập ghi chú..."></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="button" class="btn btn-primary" onclick="submitEditNotes()">
                                <i class="fas fa-save"></i> Lưu
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>

        <%-- PRODUCTS PAGE --%>
        <c:when test="${param.action == 'products'}">
            <!-- Page Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2><i class="fas fa-box"></i> Quản lý sản phẩm</h2>
                    <p class="text-muted mb-0">Tổng số: ${fn:length(products)} sản phẩm</p>
                </div>
                <div>
                    <button class="btn btn-primary" onclick="showAddProductModal()">
                        <i class="fas fa-plus"></i> Thêm sản phẩm
                    </button>
                </div>
            </div>

            <!-- Products Table -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="fas fa-list"></i>
                        Danh sách sản phẩm
                    </h5>
                </div>
                <div class="card-body p-0">
                    <c:choose>
                        <c:when test="${not empty products}">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Sản phẩm</th>
                                            <th>Danh mục</th>
                                            <th>Giá bán</th>
                                            <th>Đã bán</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%-- Duyệt danh sách sản phẩm - CHỈ truy cập các field scalar, KHÔNG truy cập LAZY relations --%>
                                        <c:forEach var="p" items="${products}" varStatus="loop">
                                            <tr>
                                                <td><strong>#${p.productId}</strong></td>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <div>
                                                            <strong><c:out value="${p.name}" default="N/A"/></strong><br>
                                                            <small class="text-muted"><c:out value="${p.brand}" default=""/></small>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                        <c:choose>
                                                        <c:when test="${p.categoryId == 1}">Điện thoại</c:when>
                                                        <c:when test="${p.categoryId == 2}">Phụ kiện điện thoại</c:when>
                                                        <c:when test="${p.categoryId == 3}">Laptop</c:when>
                                                        <c:when test="${p.categoryId == 4}">Máy tính bảng</c:when>
                                                        <c:when test="${p.categoryId == 5}">Tai nghe</c:when>
                                                        <c:otherwise>Khác (${p.categoryId})</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <strong class="text-success">
                                                        <fmt:formatNumber value="${p.basePrice}" type="number"
                                                                        groupingUsed="true" maxFractionDigits="0"/>đ
                                                    </strong>
                                                </td>
                                                <td>${p.totalSold}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${p.status eq 'AVAILABLE'}">
                                                            <span class="badge bg-success">Hoạt động</span>
                                                        </c:when>
                                                        <c:when test="${p.status eq 'INACTIVE'}">
                                                            <span class="badge bg-secondary">Tạm dừng</span>
                                                        </c:when>
                                                        <c:when test="${not empty p.status}">
                                                            <span class="badge bg-warning">${p.status}</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">N/A</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                         <td>
                                                    <div class="btn-group btn-group-sm">
                                                        <button class="btn btn-outline-warning"
                                                                onclick="editProduct(${p.productId})"
                                                                title="Sửa sản phẩm">
                                                            <i class="fas fa-edit"></i>
                                                        </button>
                                                        <button class="btn btn-outline-danger"
                                                                onclick="deleteProduct(${p.productId})"
                                                                title="Xóa sản phẩm">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state p-5 text-center">
                                <i class="fas fa-box-open fa-3x text-muted mb-3"></i>
                                <h5>Chưa có sản phẩm nào</h5>
                                <p class="text-muted">Hãy bắt đầu bằng cách thêm sản phẩm đầu tiên</p>
                                <button class="btn btn-primary" onclick="showAddProductModal()">
                                    <i class="fas fa-plus"></i> Thêm sản phẩm
                                </button>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:when>

        <%-- SHIPPING PAGE --%>
        <c:when test="${param.action == 'shipping'}">
            <!-- Page Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2><i class="fas fa-truck"></i> Quản lý giao hàng</h2>
                    <p class="text-muted mb-0">Phân chia và theo dõi đơn hàng giao cho shipper</p>
                </div>
            </div>

            <!-- Summary Stats -->
            <div class="row g-3 mb-4">
                <div class="col-md-4">
                    <div class="card border-info">
                        <div class="card-body text-center">
                            <h3 class="text-info mb-0">${not empty orders ? fn:length(orders) : 0}</h3>
                            <p class="text-muted mb-0">Đơn cần giao</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card border-success">
                        <div class="card-body text-center">
                            <h3 class="text-success mb-0">
                                <c:set var="assignedCount" value="0"/>
                                <c:forEach items="${orders}" var="order">
                                    <c:if test="${order.status == 'Assigned'}">
                                        <c:set var="assignedCount" value="${assignedCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${assignedCount}
                            </h3>
                            <p class="text-muted mb-0">Đã gán shipper</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card border-warning">
                        <div class="card-body text-center">
                            <h3 class="text-warning mb-0">
                                <c:set var="unassignedCount" value="0"/>
                                <c:forEach items="${orders}" var="order">
                                    <c:if test="${order.status != 'Assigned'}">
                                        <c:set var="unassignedCount" value="${unassignedCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${unassignedCount}
                            </h3>
                            <p class="text-muted mb-0">Chưa gán shipper</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Orders Ready for Shipping -->
            <c:choose>
                <c:when test="${not empty orders}">
                    <c:forEach items="${orders}" var="order">
                        <div class="card mb-3 border-left-primary" data-order-id="${order.orderId}">
                            <div class="card-header bg-light">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h5 class="mb-1">
                                            <i class="fas fa-shopping-cart text-primary"></i>
                                            Đơn hàng #${order.orderNumber}
                                        </h5>
                                        <small class="text-muted">
                                            <i class="far fa-clock"></i>
                                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                        </small>
                                    </div>
                                    <div class="text-end">
                                        <span class="badge bg-warning fs-6" id="shipper-badge-${order.orderId}">Chờ gán shipper</span>
                                    </div>
                                </div>
                            </div>

                            <div class="card-body">
                                <div class="row">
                                    <!-- Customer Information -->
                                    <div class="col-md-4">
                                        <div class="border-end pe-3">
                                            <h6 class="text-primary mb-2">
                                                <i class="fas fa-user"></i> Thông tin khách hàng
                                            </h6>
                                            <c:choose>
                                                <c:when test="${not empty order.customer}">
                                                    <div class="mb-1">
                                                        <strong>${order.customer.firstName} ${order.customer.lastName}</strong>
                                                    </div>
                                                    <div class="mb-1">
                                                        <i class="fas fa-envelope text-muted"></i>
                                                        ${order.customer.email}
                                                    </div>
                                                    <div class="mb-1">
                                                        <i class="fas fa-phone text-muted"></i>
                                                        ${order.customer.phone}
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Thông tin khách hàng không có</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <!-- Delivery Address -->
                                    <div class="col-md-4">
                                        <div class="border-end pe-3">
                                            <h6 class="text-primary mb-2">
                                                <i class="fas fa-map-marker-alt"></i> Địa chỉ giao hàng
                                            </h6>
                                            <c:choose>
                                                <c:when test="${not empty order.address}">
                                                    <div class="mb-1">
                                                        <strong>${order.address.street}</strong>
                                                    </div>
                                                    <div class="mb-1">
                                                        ${order.address.district}, ${order.address.city}
                                                    </div>
                                                    <c:if test="${not empty order.address.ward}">
                                                        <div class="mb-1">${order.address.ward}</div>
                                                    </c:if>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Địa chỉ không có</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <!-- Order Details -->
                                    <div class="col-md-4">
                                        <h6 class="text-primary mb-2">
                                            <i class="fas fa-box"></i> Chi tiết đơn hàng
                                        </h6>
                                        <div class="mb-2">
                                            <strong class="text-success">
                                                <fmt:formatNumber value="${order.totalPrice}" type="currency"
                                                                currencySymbol="" maxFractionDigits="0"/>đ
                                            </strong>
                                        </div>
                                        <div class="mb-2">
                                            <small class="text-muted">Phí ship:</small>
                                            <fmt:formatNumber value="${order.shippingFee}" type="currency"
                                                            currencySymbol="" maxFractionDigits="0"/>đ
                                        </div>

                                        <!-- Product List -->
                                        <c:if test="${not empty order.orderDetails}">
                                            <div class="mt-2">
                                                <small class="text-muted fw-bold">Sản phẩm:</small>
                                                <c:forEach items="${order.orderDetails}" var="detail" begin="0" end="2">
                                                    <div class="small">
                                                        • ${detail.productName} (x${detail.quantity})
                                                    </div>
                                                </c:forEach>
                                                <c:if test="${fn:length(order.orderDetails) > 3}">
                                                    <div class="small text-muted">
                                                        và ${fn:length(order.orderDetails) - 3} sản phẩm khác...
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Shipper Information -->
                                <div class="row mt-3 pt-3 border-top">
                                    <div class="col-12">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div id="shipper-info-${order.orderId}">
                                                <small class="text-muted">
                                                    <i class="fas fa-user-tie"></i>
                                                    Đang tải thông tin shipper...
                                                </small>
                                            </div>
                                            <div class="d-flex gap-2" id="shipper-actions-${order.orderId}">
                                                <button class="btn btn-outline-primary btn-sm"
                                                        onclick="viewOrderDetails(${order.orderId})">
                                                </button>

                                                <!-- READY: broadcast to all available shippers -->
                                                <c:if test="${fn:toLowerCase(order.status) == 'ready'}">
                                                    <button class="btn btn-success btn-sm" id="btn-broadcast-${order.orderId}"
                                                            onclick="broadcastDelivery(${order.orderId})">
                                                        <i class="fas fa-bullhorn"></i> Gửi cho shipper
                                                    </button>
                                                </c:if>

                                                <!-- SHIPPING: show as assigned; allow refresh only (optional) -->
                                                <c:if test="${fn:toLowerCase(order.status) == 'shipping'}">
                                                    <button class="btn btn-outline-secondary btn-sm" onclick="loadShipperInfo(${order.orderId})">
                                                        <i class="fas fa-sync"></i> Làm mới shipper
                                                    </button>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-5">
                        <i class="fas fa-truck fa-4x text-muted mb-3"></i>
                        <h4 class="text-muted">Không có đơn hàng cần giao</h4>
                        <p class="text-muted">Tất cả đơn hàng sẵn sàng giao sẽ hiển thị ở đây</p>
                        <a href="${pageContext.request.contextPath}/vendor" class="btn btn-primary mt-3">
                            <i class="fas fa-arrow-left"></i> Quay lại Dashboard
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:when>

        <%-- STATS PAGE --%>
        <c:when test="${param.action == 'stats'}">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2><i class="fas fa-chart-bar"></i> Thống kê</h2>
                    <p class="text-muted mb-0">Theo dõi nhanh sản phẩm bán chạy, số lượng đã bán và tồn kho</p>
                </div>
                <div class="d-flex gap-2">
                    <select class="form-select form-select-sm" id="statsPeriod" style="width: 170px;">
                        <option value="month" selected>Tháng này</option>
                        <option value="3months">3 tháng gần đây</option>
                        <option value="year">Năm nay</option>
                        <option value="all">Tất cả</option>
                    </select>
                    <button class="btn btn-sm btn-primary" onclick="loadVendorStats()">
                        <i class="fas fa-sync"></i> Tải lại
                    </button>
                </div>
            </div>

            <div class="row g-3 mb-4">
                <div class="col-md-3">
                    <div class="card border-success">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <div class="text-muted small">Tổng số lượng đã bán</div>
                                    <div class="fs-3 fw-bold text-success" id="statsTotalSold">--</div>
                                </div>
                                <i class="fas fa-shopping-bag fa-2x text-success"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card border-primary">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <div class="text-muted small">Tổng số lượng (Product.quantity)</div>
                                    <div class="fs-3 fw-bold text-primary" id="statsTotalProductQty">--</div>
                                </div>
                                <i class="fas fa-layer-group fa-2x text-primary"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card border-info">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <div class="text-muted small">Tổng tồn kho (ước tính)</div>
                                    <div class="fs-3 fw-bold text-info" id="statsTotalStock">--</div>
                                </div>
                                <i class="fas fa-warehouse fa-2x text-info"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card border-warning">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <div class="text-muted small">Sắp hết hàng</div>
                                    <div class="fs-3 fw-bold text-warning" id="statsLowStockCount">--</div>
                                </div>
                                <i class="fas fa-exclamation-triangle fa-2x text-warning"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-3">
                <div class="col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-box"></i> Tồn kho theo sản phẩm
                            <span class="text-muted small">(Product.quantity)</span>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-sm align-middle">
                                    <thead>
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th class="text-end">Tồn</th>
                                        <th class="text-end">Trạng thái</th>
                                    </tr>
                                    </thead>
                                    <tbody id="statsProductQtyBody">
                                    <tr><td colspan="3" class="text-muted">Đang tải...</td></tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-fire"></i> Top sản phẩm bán chạy
                            <span class="text-muted small">(theo số lượng)</span>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-sm align-middle">
                                    <thead>
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th class="text-end">Đã bán</th>
                                    </tr>
                                    </thead>
                                    <tbody id="statsTopSoldBody">
                                    <tr><td colspan="2" class="text-muted">Đang tải...</td></tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-6">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-boxes"></i> Sản phẩm sắp hết hàng</div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-sm align-middle">
                                    <thead>
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th class="text-end">Tồn</th>
                                        <th class="text-end">Ngưỡng</th>
                                    </tr>
                                    </thead>
                                    <tbody id="statsLowStockBody">
                                    <tr><td colspan="3" class="text-muted">Đang tải...</td></tr>
                                    </tbody>
                                </table>
                            </div>
                            <small class="text-muted">Gợi ý: vào Kho hàng để bổ sung tồn kho kịp thời.</small>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-3 mt-1">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header"><i class="fas fa-tags"></i> Sản phẩm theo trạng thái</div>
                        <div class="card-body">
                            <div id="statsStatusBadges" class="d-flex flex-wrap gap-2">
                                <span class="text-muted">Đang tải...</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                // ---- Stats helpers (safe JSON parsing) ----
                function parseJsonSafe(response) {
                    const ct = (response.headers && response.headers.get) ? (response.headers.get('content-type') || '') : '';
                    return response.text().then(text => {
                        if (!text) return null;
                        try {
                            return JSON.parse(text);
                        } catch (e) {
                            // Helpful debug: server returned HTML (usually forwarded JSP or login page)
                            const preview = text.substring(0, 200);
                            throw new Error('Response is not JSON. HTTP ' + response.status + '. content-type=' + ct + '. Preview: ' + preview);
                        }
                    });
                }

                function getStatsParams() {
                    const period = document.getElementById('statsPeriod')?.value || 'month';
                    return { period };
                }

                function renderTopSold(items) {
                    const body = document.getElementById('statsTopSoldBody');
                    if (!body) return;
                    body.innerHTML = '';
                    if (!items || items.length === 0) {
                        body.innerHTML = '<tr><td colspan="2" class="text-muted">Chưa có dữ liệu bán hàng</td></tr>';
                        return;
                    }
                    items.forEach(it => {
                        const tr = document.createElement('tr');
                        tr.innerHTML = '<td>' + (it.productName || 'N/A') + '</td>' +
                            '<td class="text-end fw-bold text-success">' + (it.soldQuantity ?? 0) + '</td>';
                        body.appendChild(tr);
                    });
                }

                function renderLowStock(items) {
                    const body = document.getElementById('statsLowStockBody');
                    if (!body) return;
                    body.innerHTML = '';
                    if (!items || items.length === 0) {
                        body.innerHTML = '<tr><td colspan="3" class="text-muted">Không có sản phẩm sắp hết hàng</td></tr>';
                        return;
                    }
                    items.forEach(it => {
                        const tr = document.createElement('tr');
                        tr.innerHTML = '<td>' + (it.productName || 'N/A') + '</td>' +
                            '<td class="text-end fw-bold">' + (it.availableQuantity ?? 0) + '</td>' +
                            '<td class="text-end text-muted">' + (it.threshold ?? 0) + '</td>';
                        body.appendChild(tr);
                    });
                }

                function renderStatusCounts(items) {
                    const box = document.getElementById('statsStatusBadges');
                    if (!box) return;
                    box.innerHTML = '';
                    if (!items || items.length === 0) {
                        box.innerHTML = '<span class="text-muted">Không có dữ liệu</span>';
                        return;
                    }

                    const color = (status) => {
                        switch ((status || '').toUpperCase()) {
                            case 'AVAILABLE': return 'bg-success';
                            case 'OUT_OF_STOCK': return 'bg-warning text-dark';
                            case 'INACTIVE': return 'bg-secondary';
                            default: return 'bg-info';
                        }
                    };

                    items.forEach(it => {
                        const span = document.createElement('span');
                        span.className = 'badge ' + color(it.status) + ' p-2';
                        span.textContent = (it.status || 'UNKNOWN') + ': ' + (it.count ?? 0);
                        box.appendChild(span);
                    });
                }

                function renderProductQty(items) {
                    const body = document.getElementById('statsProductQtyBody');
                    if (!body) return;
                    body.innerHTML = '';
                    if (!items || items.length === 0) {
                        body.innerHTML = '<tr><td colspan="3" class="text-muted">Không có dữ liệu</td></tr>';
                        return;
                    }
                    items.forEach(it => {
                        const tr = document.createElement('tr');
                        tr.innerHTML = '<td>' + (it.name || 'N/A') + '</td>' +
                            '<td class="text-end fw-bold">' + (it.quantity ?? 0) + '</td>' +
                            '<td class="text-end text-muted">' + (it.status || 'UNKNOWN') + '</td>';
                        body.appendChild(tr);
                    });
                }

                function loadVendorStats() {
                    const p = getStatsParams();
                    const qs = new URLSearchParams({ action: 'statsData', period: p.period }).toString();

                    fetch('${pageContext.request.contextPath}/vendor?' + qs, {
                        method: 'GET',
                        headers: { 'Accept': 'application/json' }
                    })
                        .then(res => parseJsonSafe(res))
                        .then(result => {
                            if (!result || !result.success) {
                                throw new Error((result && result.message) ? result.message : 'Không thể tải thống kê');
                            }

                            const data = result.data || {};
                            const soldEl = document.getElementById('statsTotalSold');
                            if (soldEl) soldEl.textContent = (data.totalSoldQuantity ?? 0);

                            const totalQtyEl = document.getElementById('statsTotalProductQty');
                            if (totalQtyEl) totalQtyEl.textContent = (data.totalProductQuantity ?? 0);

                            const stockEl = document.getElementById('statsTotalStock');
                            if (stockEl) stockEl.textContent = (data.totalStockQuantity ?? 0);

                            const lowEl = document.getElementById('statsLowStockCount');
                            if (lowEl) lowEl.textContent = (data.lowStockCount ?? 0);

                            renderProductQty(data.productsQuantity || []);
                            renderTopSold(data.topSoldProducts || []);
                            renderLowStock(data.lowStockProducts || []);
                            renderStatusCounts(data.statusCounts || []);
                        })
                        .catch(err => {
                            console.error('Stats load error', err);
                            alert('Có lỗi khi tải thống kê: ' + err.message);
                        });
                }

                // Auto load when on stats page
                document.addEventListener('DOMContentLoaded', function () {
                    loadVendorStats();
                });
            </script>
        </c:when>
    </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
    // Sidebar toggle
    document.addEventListener('DOMContentLoaded', function () {
        const sidebarToggle = document.getElementById('sidebarToggle');
        const sidebar = document.querySelector('.sidebar');

        sidebarToggle.addEventListener('click', function () {
            sidebar.classList.toggle('show');
        });
    });

    // Order details modal
    function viewOrderDetails(orderId) {
        // TODO: Implement order details view
        alert('View details for order ID: ' + orderId);
    }

    // Update order status
    function updateOrderStatus(orderId, status) {
        // TODO: Implement order status update
        alert('Update order ID ' + orderId + ' to status: ' + status);
    }

    // Show cancel order modal
    function showCancelModal(orderId) {
        const cancelOrderId = document.getElementById('cancelOrderId');
        cancelOrderId.value = orderId;

        const modal = new bootstrap.Modal(document.getElementById('cancelOrderModal'));
        modal.show();
    }

    // Confirm cancel order
    function confirmCancelOrder() {
        const orderId = document.getElementById('cancelOrderId').value;
        const reason = document.getElementById('cancelReason').value;

        // TODO: Implement cancel order logic

        alert('Order ID ' + orderId + ' cancelled. Reason: ' + reason);

        // Hide modal
        const modal = bootstrap.Modal.getInstance(document.getElementById('cancelOrderModal'));
        modal.hide();
    }

    // Show add product modal
    function showAddProductModal() {
        const modal = new bootstrap.Modal(document.getElementById('addProductModal'));
        modal.show();
    }

    // Show edit product modal
    function editProduct(productId) {
        // TODO: Implement edit product logic

        alert('Edit product ID: ' + productId);
    }

    // Delete product
    function deleteProduct(productId) {
        // TODO: Implement delete product logic

        alert('Delete product ID: ' + productId);
    }

    // Broadcast delivery request to all available shippers
    function broadcastDelivery(orderId) {
        if (!orderId) return;

        const url = '${pageContext.request.contextPath}/vendor?action=broadcastDelivery&orderId=' + encodeURIComponent(orderId);

        fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(res => res.json())
            .then(result => {
                if (!result || !result.success) {
                    throw new Error((result && result.message) ? result.message : 'Không thể gửi yêu cầu giao hàng');
                }

                const btn = document.getElementById('btn-broadcast-' + orderId);
                if (btn) {
                    btn.disabled = true;
                    btn.classList.remove('btn-success');
                    btn.classList.add('btn-outline-success');
                    btn.innerHTML = '<i class="fas fa-check"></i> Đã gửi';
                }

                const box = document.getElementById('shipper-info-' + orderId);
                if (box) {
                    box.innerHTML = '<small class="text-muted"><i class="fas fa-paper-plane"></i> Đã gửi yêu cầu tới shipper đang rảnh. Đang chờ nhận...</small>';
                }

                // Auto refresh shipper info after a short delay
                setTimeout(() => loadShipperInfo(orderId), 1500);
            })
            .catch(err => {
                console.error('Broadcast delivery error', err);
                alert('Có lỗi: ' + err.message);
            });
    }

    // Load shipper info for an order and update UI
    function loadShipperInfo(orderId) {
        const url = '${pageContext.request.contextPath}/vendor?action=getShipper&orderId=' + encodeURIComponent(orderId);

        fetch(url, {
            method: 'GET',
            headers: { 'Accept': 'application/json' }
        })
            .then(res => res.json())
            .then(result => {
                const infoBox = document.getElementById('shipper-info-' + orderId);
                if (!infoBox) return;

                if (!result || !result.success) {
                    infoBox.innerHTML = '<small class="text-danger"><i class="fas fa-exclamation-circle"></i> Không tải được shipper</small>';
                    return;
                }

                const data = result.data || {};
                if (data.hasShipper && data.shipper) {
                    const s = data.shipper;
                    infoBox.innerHTML = '<small class="text-success"><i class="fas fa-user-check"></i> ' +
                        'Shipper: <strong>' + (s.firstName || '') + ' ' + (s.lastName || '') + '</strong>' +
                        ' - ' + (s.phone || '') + '</small>';
                } else {
                    infoBox.innerHTML = '<small class="text-muted"><i class="fas fa-user-clock"></i> Chưa có shipper nhận đơn</small>';
                }
            })
            .catch(err => {
                console.error('Load shipper info error', err);
            });
    }

    // On shipping page, auto load shipper info for all orders rendered
    document.addEventListener('DOMContentLoaded', function () {
        const ids = Array.from(document.querySelectorAll('[id^="shipper-info-"]'))
            .map(el => el.id.replace('shipper-info-', ''))
            .map(v => parseInt(v, 10))
            .filter(v => !isNaN(v));

        ids.forEach(id => loadShipperInfo(id));
    });
</script>
</body>
</html>

