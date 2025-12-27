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
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link active" href="${pageContext.request.contextPath}/vendor">
                <span><i class="fas fa-tachometer-alt"></i> Dashboard</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="#" onclick="showProductModal(); return false;">
                <span><i class="fas fa-box"></i> Quản lý sản phẩm</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/vendor?action=approvals">
                <span><i class="fas fa-clipboard-check"></i> Yêu cầu phê duyệt</span>
                <c:if test="${data.pendingApprovals > 0}">
                    <span class="badge bg-warning rounded-pill">${data.pendingApprovals}</span>
                </c:if>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/vendor?action=orders">
                <span><i class="fas fa-shopping-cart"></i> Đơn hàng</span>
                <c:if test="${data.newOrdersCount > 0}">
                    <span class="badge bg-success rounded-pill">${data.newOrdersCount}</span>
                </c:if>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/vendor?action=shipping">
                <span><i class="fas fa-truck"></i> Giao hàng</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="#">
                <span><i class="fas fa-chart-line"></i> Thống kê</span>
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

    <!-- ROUTING: Choose content based on action parameter -->
    <c:choose>
        <%-- APPROVALS PAGE --%>
        <c:when test="${param.action == 'approvals'}">
            <!-- Page Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2><i class="fas fa-clipboard-check"></i> Yêu cầu phê duyệt</h2>
                    <p class="text-muted mb-0">Theo dõi trạng thái yêu cầu phê duyệt sản phẩm</p>
                </div>
            </div>

            <!-- Summary Stats -->
            <div class="row g-3 mb-4">
                <div class="col-md-4">
                    <div class="card border-warning">
                        <div class="card-body text-center">
                            <h3 class="text-warning mb-0">
                                <c:set var="pendingCount" value="0"/>
                                <c:forEach items="${approvals}" var="approval">
                                    <c:if test="${approval.status == 'PENDING'}">
                                        <c:set var="pendingCount" value="${pendingCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${pendingCount}
                            </h3>
                            <p class="text-muted mb-0">Đang chờ duyệt</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card border-success">
                        <div class="card-body text-center">
                            <h3 class="text-success mb-0">
                                <c:set var="approvedCount" value="0"/>
                                <c:forEach items="${approvals}" var="approval">
                                    <c:if test="${approval.status == 'APPROVED'}">
                                        <c:set var="approvedCount" value="${approvedCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${approvedCount}
                            </h3>
                            <p class="text-muted mb-0">Đã phê duyệt</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card border-danger">
                        <div class="card-body text-center">
                            <h3 class="text-danger mb-0">
                                <c:set var="rejectedCount" value="0"/>
                                <c:forEach items="${approvals}" var="approval">
                                    <c:if test="${approval.status == 'REJECTED'}">
                                        <c:set var="rejectedCount" value="${rejectedCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${rejectedCount}
                            </h3>
                            <p class="text-muted mb-0">Bị từ chối</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Approvals List -->
            <c:choose>
                <c:when test="${not empty approvals}">
                    <c:forEach items="${approvals}" var="approval">
                        <div class="approval-card">
                            <div class="approval-header">
                                <div>
                                    <span class="approval-type ${fn:toLowerCase(approval.actionType)}">
                                        <c:choose>
                                            <c:when test="${approval.actionType == 'ADD'}">
                                                <i class="fas fa-plus-circle"></i> Thêm mới
                                            </c:when>
                                            <c:when test="${approval.actionType == 'UPDATE'}">
                                                <i class="fas fa-edit"></i> Cập nhật
                                            </c:when>
                                            <c:when test="${approval.actionType == 'DELETE'}">
                                                <i class="fas fa-trash"></i> Xóa
                                            </c:when>
                                        </c:choose>
                                    </span>
                                    <span class="ms-2 text-muted">
                                        <small>
                                            <i class="far fa-clock"></i>
                                            <fmt:formatDate value="${approval.requestedAt}" pattern="dd/MM/yyyy HH:mm"/>
                                        </small>
                                    </span>
                                </div>
                                <div>
                                    <span class="approval-status status-${fn:toLowerCase(approval.status)}">
                                        <c:choose>
                                            <c:when test="${approval.status == 'PENDING'}">
                                                <i class="fas fa-hourglass-half"></i> Đang chờ
                                            </c:when>
                                            <c:when test="${approval.status == 'APPROVED'}">
                                                <i class="fas fa-check-circle"></i> Đã duyệt
                                            </c:when>
                                            <c:when test="${approval.status == 'REJECTED'}">
                                                <i class="fas fa-times-circle"></i> Từ chối
                                            </c:when>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>

                            <div class="approval-body">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <h6 class="mb-1">Yêu cầu #${approval.approvalId}</h6>
                                        <c:if test="${not empty approval.productId}">
                                            <p class="text-muted mb-0">
                                                <small>Sản phẩm ID: #${approval.productId}</small>
                                            </p>
                                        </c:if>
                                    </div>
                                    <div class="text-end">
                                        <c:if test="${approval.status == 'APPROVED' && not empty approval.reviewedAt}">
                                            <small class="text-success">
                                                <i class="fas fa-check"></i>
                                                Duyệt lúc: <fmt:formatDate value="${approval.reviewedAt}" pattern="dd/MM HH:mm"/>
                                            </small>
                                        </c:if>
                                        <c:if test="${approval.status == 'REJECTED' && not empty approval.reviewedAt}">
                                            <small class="text-danger">
                                                <i class="fas fa-times"></i>
                                                Từ chối lúc: <fmt:formatDate value="${approval.reviewedAt}" pattern="dd/MM HH:mm"/>
                                            </small>
                                        </c:if>
                                    </div>
                                </div>

                                <!-- Product Data Preview -->
                                <c:if test="${not empty approval.productData && approval.actionType != 'DELETE'}">
                                    <div class="product-preview">
                                        <h6><i class="fas fa-info-circle"></i> Thông tin sản phẩm</h6>
                                        <div class="text-muted">
                                            <small class="font-monospace">
                                                ${fn:substring(approval.productData, 0, 150)}...
                                            </small>
                                        </div>
                                    </div>
                                </c:if>

                                <!-- Rejection Reason -->
                                <c:if test="${approval.status == 'REJECTED' && not empty approval.rejectionReason}">
                                    <div class="alert alert-danger mt-3 mb-0">
                                        <strong><i class="fas fa-exclamation-triangle"></i> Lý do từ chối:</strong>
                                        <p class="mb-0 mt-2">${approval.rejectionReason}</p>
                                    </div>
                                </c:if>

                                <!-- Notes -->
                                <c:if test="${not empty approval.notes}">
                                    <div class="alert alert-info mt-3 mb-0">
                                        <strong><i class="fas fa-sticky-note"></i> Ghi chú:</strong>
                                        <p class="mb-0 mt-2">${approval.notes}</p>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-clipboard"></i>
                        <h4 class="text-muted">Chưa có yêu cầu phê duyệt nào</h4>
                        <p class="text-muted">Tất cả yêu cầu thêm, sửa, xóa sản phẩm sẽ hiển thị ở đây</p>
                        <a href="${pageContext.request.contextPath}/vendor" class="btn btn-primary mt-3">
                            <i class="fas fa-arrow-left"></i> Quay lại Dashboard
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
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

            <!-- Status Filter -->
            <div class="status-filter mb-4">
                <a href="${pageContext.request.contextPath}/vendor?action=orders"
                   class="btn ${empty param.status ? 'btn-primary' : 'btn-outline-secondary'}">
                    <i class="fas fa-list"></i> Tất cả
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=Pending"
                   class="btn ${param.status == 'Pending' ? 'btn-warning' : 'btn-outline-warning'}">
                    <i class="fas fa-clock"></i> Chờ xử lý
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=Processing"
                   class="btn ${param.status == 'Processing' ? 'btn-info' : 'btn-outline-info'}">
                    <i class="fas fa-sync"></i> Đang xử lý
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=Confirmed"
                   class="btn ${param.status == 'Confirmed' ? 'btn-primary' : 'btn-outline-primary'}">
                    <i class="fas fa-check"></i> Đã xác nhận
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=Completed"
                   class="btn ${param.status == 'Completed' ? 'btn-success' : 'btn-outline-success'}">
                    <i class="fas fa-check-double"></i> Hoàn thành
                </a>
                <a href="${pageContext.request.contextPath}/vendor?action=orders&status=Cancelled"
                   class="btn ${param.status == 'Cancelled' ? 'btn-danger' : 'btn-outline-danger'}">
                    <i class="fas fa-times"></i> Đã hủy
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
                                                    <c:if test="${order.status == 'Pending'}">
                                                        <button class="btn btn-sm btn-primary"
                                                                onclick="updateOrderStatus(${order.orderId}, 'Processing')">
                                                            <i class="fas fa-play"></i> Xử lý
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${order.status == 'Processing'}">
                                                        <button class="btn btn-sm btn-success"
                                                                onclick="updateOrderStatus(${order.orderId}, 'Confirmed')">
                                                            <i class="fas fa-check"></i> Xác nhận
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${order.status == 'Confirmed'}">
                                                        <button class="btn btn-sm btn-info"
                                                                onclick="updateOrderStatus(${order.orderId}, 'Ready')">
                                                            <i class="fas fa-box"></i> Sẵn sàng
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${order.status != 'Completed' && order.status != 'Cancelled'}">
                                                        <button class="btn btn-sm btn-danger"
                                                                onclick="showCancelModal(${order.orderId})">
                                                            <i class="fas fa-times"></i> Hủy
                                                        </button>
                                                    </c:if>
                                                    <button class="btn btn-sm btn-outline-secondary"
                                                            onclick="viewOrderDetails(${order.orderId})">
                                                        <i class="fas fa-eye"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="6">
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
            </div>

            <!-- Orders Ready for Shipping -->
            <c:choose>
                <c:when test="${not empty orders}">
                    <c:forEach items="${orders}" var="order">
                        <div class="order-card mb-3 p-3 border rounded">
                            <div class="order-header d-flex justify-content-between align-items-center mb-3">
                                <div>
                                    <h5 class="mb-1">#${order.orderNumber}</h5>
                                    <small class="text-muted">
                                        <i class="far fa-clock"></i>
                                        <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                    </small>
                                </div>
                                <div>
                                    <span class="badge bg-info">${order.status}</span>
                                </div>
                            </div>

                            <div class="order-info row g-3">
                                <div class="col-md-6">
                                    <div class="info-item d-flex">
                                        <i class="fas fa-user me-2 mt-1"></i>
                                        <div>
                                            <div class="label text-muted small">Khách hàng:</div>
                                            <div class="value">
                                                <c:choose>
                                                    <c:when test="${not empty order.customer}">
                                                        <c:choose>
                                                            <c:when test="${not empty order.customer.firstName || not empty order.customer.lastName}">
                                                                ${order.customer.firstName} ${order.customer.lastName}
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${order.customer.email}
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <br>
                                                        <small>${order.customer.phone}</small>
                                                    </c:when>
                                                    <c:otherwise>N/A</c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="info-item d-flex">
                                        <i class="fas fa-map-marker-alt me-2 mt-1"></i>
                                        <div>
                                            <div class="label text-muted small">Địa chỉ giao:</div>
                                            <div class="value">
                                                <c:choose>
                                                    <c:when test="${not empty order.address}">
                                                        ${order.address.street}<br>
                                                        ${order.address.district}, ${order.address.city}
                                                    </c:when>
                                                    <c:otherwise>Chưa có địa chỉ</c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="info-item d-flex">
                                        <i class="fas fa-money-bill-wave me-2 mt-1"></i>
                                        <div>
                                            <div class="label text-muted small">Giá trị đơn:</div>
                                            <div class="value">
                                                <strong class="text-success">
                                                    <fmt:formatNumber value="${order.totalPrice}" type="currency"
                                                                    currencySymbol="" maxFractionDigits="0"/>đ
                                                </strong>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="info-item d-flex">
                                        <i class="fas fa-shipping-fast me-2 mt-1"></i>
                                        <div>
                                            <div class="label text-muted small">Phí ship:</div>
                                            <div class="value">
                                                <fmt:formatNumber value="${order.shippingFee}" type="currency"
                                                                currencySymbol="" maxFractionDigits="0"/>đ
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="mt-3 d-flex gap-2">
                                <button class="btn btn-success" onclick="showAssignShipperModal(${order.orderId}, '${order.orderNumber}')">
                                    <i class="fas fa-user-plus"></i> Gán Shipper
                                </button>
                                <button class="btn btn-outline-primary" onclick="viewOrderDetail(${order.orderId})">
                                    <i class="fas fa-eye"></i> Xem chi tiết
                                </button>
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

            <!-- Assign Shipper Modal -->
            <div class="modal fade" id="assignShipperModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="fas fa-user-plus"></i> Gán Shipper</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" id="assignOrderId">
                            <div class="alert alert-info">
                                <strong>Đơn hàng:</strong> <span id="assignOrderNumber"></span>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Chọn Shipper *</label>
                                <input type="number" class="form-control" id="shipperId"
                                       placeholder="Nhập ID của Shipper" required>
                                <small class="text-muted">Nhập ID của shipper bạn muốn gán cho đơn hàng này</small>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="button" class="btn btn-success" onclick="confirmAssignShipper()">
                                <i class="fas fa-check"></i> Xác nhận gán
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>

        <%-- DASHBOARD (default) --%>
        <c:otherwise>
            <!-- Dashboard Page Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="mb-1"><i class="fas fa-tachometer-alt"></i> Dashboard</h2>
                    <p class="text-muted mb-0">Chào mừng trở lại! Đây là tổng quan hoạt động kinh doanh của bạn.</p>
                </div>
                <div>
                    <button class="btn btn-primary" onclick="showAddProductModal()">
                        <i class="fas fa-plus"></i> Thêm sản phẩm
                    </button>
                </div>
            </div>

    <!-- Statistics Cards -->
    <div class="stats-row">
        <div class="stat-card">
            <div class="stat-icon bg-gradient-primary">
                <i class="fas fa-box"></i>
            </div>
            <div class="stat-value">${data.totalProducts != null ? data.totalProducts : 0}</div>
            <div class="stat-label">Tổng sản phẩm</div>
        </div>
        <div class="stat-card">
            <div class="stat-icon bg-gradient-success">
                <i class="fas fa-shopping-cart"></i>
            </div>
            <div class="stat-value">${data.newOrdersCount != null ? data.newOrdersCount : 0}</div>
            <div class="stat-label">Đơn hàng mới</div>
        </div>
        <div class="stat-card">
            <div class="stat-icon bg-gradient-warning">
                <i class="fas fa-clock"></i>
            </div>
            <div class="stat-value">${data.pendingApprovals != null ? data.pendingApprovals : 0}</div>
            <div class="stat-label">Chờ phê duyệt</div>
        </div>
        <div class="stat-card">
            <div class="stat-icon bg-gradient-info">
                <i class="fas fa-money-bill-wave"></i>
            </div>
            <div class="stat-value">
                <fmt:formatNumber value="${data.monthlyRevenue != null ? data.monthlyRevenue : 0}"
                                 type="currency" currencySymbol="" maxFractionDigits="0"/>đ
            </div>
            <div class="stat-label">Doanh thu tháng này</div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="quick-actions">
        <a href="#" class="quick-action-btn" onclick="showAddProductModal(); return false;">
            <i class="fas fa-plus-circle text-primary"></i>
            <span>Thêm sản phẩm</span>
        </a>
        <a href="${pageContext.request.contextPath}/vendor?action=orders&status=Pending" class="quick-action-btn">
            <i class="fas fa-list-check text-success"></i>
            <span>Xử lý đơn hàng</span>
        </a>
        <a href="${pageContext.request.contextPath}/vendor?action=shipping" class="quick-action-btn">
            <i class="fas fa-truck-fast text-info"></i>
            <span>Giao hàng</span>
        </a>
        <a href="${pageContext.request.contextPath}/vendor?action=approvals" class="quick-action-btn">
            <i class="fas fa-clipboard-check text-warning"></i>
            <span>Phê duyệt</span>
        </a>
    </div>

    <!-- Recent Orders -->
    <div class="content-card">
        <div class="card-header d-flex justify-content-between align-items-center">
            <h5 class="mb-0"><i class="fas fa-shopping-cart"></i> Đơn hàng gần đây</h5>
            <a href="${pageContext.request.contextPath}/vendor?action=orders" class="btn btn-sm btn-primary">
                Xem tất cả <i class="fas fa-arrow-right"></i>
            </a>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead>
                        <tr>
                            <th>Mã đơn</th>
                            <th>Khách hàng</th>
                            <th>Ngày đặt</th>
                            <th>Tổng tiền</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty data.recentOrders}">
                                <c:forEach items="${data.recentOrders}" var="order" begin="0" end="4">
                                    <tr>
                                        <td><strong>#${order.orderNumber}</strong></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty order.customer}">
                                                    <c:choose>
                                                        <c:when test="${not empty order.customer.firstName || not empty order.customer.lastName}">
                                                            ${order.customer.firstName} ${order.customer.lastName}
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${order.customer.email}
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <br>
                                                    <small class="text-muted">${order.customer.email}</small>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Khách hàng</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/><br>
                                            <small class="text-muted"><fmt:formatDate value="${order.orderDate}" pattern="HH:mm"/></small>
                                        </td>
                                        <td>
                                            <strong class="text-success">
                                                <fmt:formatNumber value="${order.totalPrice}" type="currency"
                                                                currencySymbol="" maxFractionDigits="0"/>đ
                                            </strong>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${order.status == 'Pending'}">
                                                    <span class="badge badge-pending">Chờ xử lý</span>
                                                </c:when>
                                                <c:when test="${order.status == 'Processing'}">
                                                    <span class="badge badge-processing">Đang xử lý</span>
                                                </c:when>
                                                <c:when test="${order.status == 'Confirmed'}">
                                                    <span class="badge badge-confirmed">Đã xác nhận</span>
                                                </c:when>
                                                <c:when test="${order.status == 'Completed'}">
                                                    <span class="badge badge-completed">Hoàn thành</span>
                                                </c:when>
                                                <c:when test="${order.status == 'Cancelled'}">
                                                    <span class="badge badge-cancelled">Đã hủy</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${order.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-primary"
                                                    onclick="viewOrderDetail(${order.orderId})">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                            <c:if test="${order.status == 'Pending'}">
                                                <button class="btn btn-sm btn-success"
                                                        onclick="updateOrderStatus(${order.orderId}, 'Processing')">
                                                    <i class="fas fa-play"></i>
                                                </button>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6">
                                        <div class="empty-state">
                                            <i class="fas fa-inbox"></i>
                                            <h5>Chưa có đơn hàng nào</h5>
                                            <p>Các đơn hàng mới sẽ hiển thị ở đây</p>
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

    <!-- Products List -->
    <div class="content-card">
        <div class="card-header d-flex justify-content-between align-items-center">
            <h5 class="mb-0"><i class="fas fa-box"></i> Sản phẩm của tôi</h5>
            <div>
                <button class="btn btn-sm btn-success" onclick="showAddProductModal()">
                    <i class="fas fa-plus"></i> Thêm mới
                </button>
            </div>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead>
                        <tr>
                            <th>Sản phẩm</th>
                            <th>Danh mục</th>
                            <th>Giá bán</th>
                            <th>Đã bán</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty data.productList}">
                                <c:forEach items="${data.productList}" var="product" begin="0" end="9">
                                    <tr>
                                        <td>
                                            <div class="d-flex align-items-center">
                                                <div>
                                                    <strong>${product.name}</strong><br>
                                                    <small class="text-muted">${product.brand}</small>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${product.categoryId == 1}">Laptop</c:when>
                                                <c:when test="${product.categoryId == 2}">Điện thoại</c:when>
                                                <c:when test="${product.categoryId == 3}">Tablet</c:when>
                                                <c:when test="${product.categoryId == 4}">Tai nghe</c:when>
                                                <c:otherwise>Khác</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <strong>
                                                <fmt:formatNumber value="${product.basePrice}" type="currency"
                                                                currencySymbol="" maxFractionDigits="0"/>đ
                                            </strong>
                                        </td>
                                        <td>${product.totalSold != null ? product.totalSold : 0}</td>
                                        <td>
                                            <span class="badge ${product.status == 'Active' ? 'bg-success' : 'bg-secondary'}">
                                                ${product.status}
                                            </span>
                                        </td>
                                        <td>
                                            <button class="btn btn-sm btn-warning"
                                                    onclick="editProduct(${product.productId})">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="btn btn-sm btn-danger"
                                                    onclick="deleteProduct(${product.productId})">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6">
                                        <div class="empty-state">
                                            <i class="fas fa-box-open"></i>
                                            <h5>Chưa có sản phẩm nào</h5>
                                            <p>Hãy bắt đầu bằng cách <a href="#" onclick="showAddProductModal(); return false;">thêm sản phẩm đầu tiên</a></p>
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

    <!-- Shipping Orders -->
    <c:if test="${not empty data.pendingShippingOrders}">
        <div class="content-card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0"><i class="fas fa-truck"></i> Đơn hàng cần giao</h5>
                <a href="${pageContext.request.contextPath}/vendor?action=shipping" class="btn btn-sm btn-info">
                    Xem chi tiết <i class="fas fa-arrow-right"></i>
                </a>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead>
                            <tr>
                                <th>Mã đơn</th>
                                <th>Khách hàng</th>
                                <th>Địa chỉ</th>
                                <th>Giá trị</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${data.pendingShippingOrders}" var="shipOrder" begin="0" end="4">
                                <tr>
                                    <td><strong>#${shipOrder.orderNumber}</strong></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty shipOrder.customer}">
                                                <c:choose>
                                                    <c:when test="${not empty shipOrder.customer.firstName || not empty shipOrder.customer.lastName}">
                                                        ${shipOrder.customer.firstName} ${shipOrder.customer.lastName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${shipOrder.customer.email}
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty shipOrder.address}">
                                                ${shipOrder.address.street}, ${shipOrder.address.district}
                                            </c:when>
                                            <c:otherwise>Địa chỉ</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <strong class="text-success">
                                            <fmt:formatNumber value="${shipOrder.totalPrice}" type="currency"
                                                            currencySymbol="" maxFractionDigits="0"/>đ
                                        </strong>
                                    </td>
                                    <td>
                                        <button class="btn btn-sm btn-success"
                                                onclick="assignShipper(${shipOrder.orderId})">
                                            <i class="fas fa-user-plus"></i> Gán Shipper
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>
        </c:otherwise>
    </c:choose>
</div>

<!-- Add Product Modal -->
<div class="modal fade" id="addProductModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><i class="fas fa-plus-circle"></i> Thêm sản phẩm mới</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="addProductForm">
                    <div class="row">
                        <div class="col-md-12 mb-3">
                            <label class="form-label">Tên sản phẩm *</label>
                            <input type="text" class="form-control" name="name" required
                                   placeholder="Nhập tên sản phẩm">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Danh mục *</label>
                            <select class="form-select" name="categoryId" required>
                                <option value="">Chọn danh mục...</option>
                                <option value="1">Laptop</option>
                                <option value="2">Điện thoại</option>
                                <option value="3">Tablet</option>
                                <option value="4">Tai nghe</option>
                            </select>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Thương hiệu *</label>
                            <input type="text" class="form-control" name="brand" required
                                   placeholder="VD: Apple, Dell, Samsung...">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Giá bán (VNĐ) *</label>
                            <input type="number" class="form-control" name="basePrice" min="0" step="1000" required
                                   placeholder="Nhập giá bán">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Trạng thái *</label>
                            <select class="form-select" name="status" required>
                                <option value="Active">Hoạt động</option>
                                <option value="Inactive">Không hoạt động</option>
                            </select>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mô tả sản phẩm</label>
                        <textarea class="form-control" name="description" rows="3"
                                  placeholder="Nhập mô tả chi tiết về sản phẩm..."></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Thông số kỹ thuật</label>
                        <textarea class="form-control" name="specifications" rows="2"
                                  placeholder="VD: CPU: Intel Core i7, RAM: 16GB, SSD: 512GB"></textarea>
                    </div>
                </form>
                <div class="alert alert-info">
                    <i class="fas fa-info-circle"></i>
                    <strong>Lưu ý:</strong> Sản phẩm sẽ được gửi yêu cầu phê duyệt đến Admin.
                    Sau khi được duyệt, sản phẩm mới hiển thị trên website.
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                    <i class="fas fa-times"></i> Hủy
                </button>
                <button type="button" class="btn btn-primary" onclick="submitAddProduct()">
                    <i class="fas fa-paper-plane"></i> Gửi yêu cầu
                </button>
            </div>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Show Add Product Modal
    function showAddProductModal() {
        const modal = new bootstrap.Modal(document.getElementById('addProductModal'));
        modal.show();
    }

    // Submit Add Product
    function submitAddProduct() {
        const form = document.getElementById('addProductForm');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());
        data.conditions = 'New';

        fetch('${pageContext.request.contextPath}/vendor?action=addProduct', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
        .then(res => res.json())
        .then(result => {
            if (result.success) {
                alert('✓ ' + result.message);
                location.reload();
            } else {
                alert('✗ Lỗi: ' + result.message);
            }
        })
        .catch(err => {
            alert('✗ Có lỗi xảy ra: ' + err.message);
        });
    }

    // Update Order Status
    function updateOrderStatus(orderId, newStatus) {
        if (!confirm('Bạn có chắc muốn cập nhật trạng thái đơn hàng này?')) return;

        fetch('${pageContext.request.contextPath}/vendor?action=updateOrderStatus&orderId=' + orderId + '&status=' + newStatus, {
            method: 'POST'
        })
        .then(res => res.json())
        .then(result => {
            if (result.success) {
                alert('✓ ' + result.message);
                location.reload();
            } else {
                alert('✗ Lỗi: ' + result.message);
            }
        })
        .catch(err => alert('✗ Có lỗi xảy ra: ' + err.message));
    }

    // Show Cancel Order Modal
    function showCancelModal(orderId) {
        document.getElementById('cancelOrderId').value = orderId;
        document.getElementById('cancelReason').value = '';
        new bootstrap.Modal(document.getElementById('cancelOrderModal')).show();
    }

    // Confirm Cancel Order
    function confirmCancelOrder() {
        const orderId = document.getElementById('cancelOrderId').value;
        const reason = document.getElementById('cancelReason').value.trim();

        if (!reason) {
            alert('Vui lòng nhập lý do hủy đơn hàng');
            return;
        }

        fetch('${pageContext.request.contextPath}/vendor?action=cancelOrder&orderId=' + orderId + '&reason=' + encodeURIComponent(reason), {
            method: 'POST'
        })
        .then(res => res.json())
        .then(result => {
            if (result.success) {
                alert('✓ ' + result.message);
                bootstrap.Modal.getInstance(document.getElementById('cancelOrderModal')).hide();
                location.reload();
            } else {
                alert('✗ Lỗi: ' + result.message);
            }
        })
        .catch(err => {
            alert('✗ Có lỗi xảy ra: ' + err.message);
        });
    }

    // View Order Details
    function viewOrderDetails(orderId) {
        window.location.href = '${pageContext.request.contextPath}/vendor?action=orderDetail&id=' + orderId;
    }

    // Show Assign Shipper Modal
    function showAssignShipperModal(orderId, orderNumber) {
        document.getElementById('assignOrderId').value = orderId;
        document.getElementById('assignOrderNumber').textContent = '#' + orderNumber;
        document.getElementById('shipperId').value = '';
        new bootstrap.Modal(document.getElementById('assignShipperModal')).show();
    }

    // Confirm Assign Shipper
    function confirmAssignShipper() {
        const orderId = document.getElementById('assignOrderId').value;
        const shipperId = document.getElementById('shipperId').value;

        if (!shipperId) {
            alert('Vui lòng nhập ID của Shipper');
            return;
        }

        fetch('${pageContext.request.contextPath}/vendor?action=assignShipper&orderId=' + orderId + '&shipperId=' + shipperId, {
            method: 'POST'
        })
        .then(res => res.json())
        .then(result => {
            if (result.success) {
                alert('✓ ' + result.message);
                bootstrap.Modal.getInstance(document.getElementById('assignShipperModal')).hide();
                location.reload();
            } else {
                alert('✗ Lỗi: ' + result.message);
            }
        })
        .catch(err => {
            alert('✗ Có lỗi xảy ra: ' + err.message);
        });
    }

    // Delete Product
    function deleteProduct(productId) {
        if (!confirm('Bạn có chắc muốn xóa sản phẩm này? Yêu cầu xóa sẽ được gửi đến Admin để phê duyệt.')) return;

        fetch('${pageContext.request.contextPath}/vendor?action=deleteProduct&productId=' + productId, {
            method: 'POST'
        })
        .then(res => res.json())
        .then(result => {
            if (result.success) {
                alert('✓ ' + result.message);
                location.reload();
            } else {
                alert('✗ Lỗi: ' + result.message);
            }
        })
        .catch(err => alert('✗ Có lỗi xảy ra: ' + err.message));
    }

    // Edit Product (redirect to edit page or show modal)
    function editProduct(productId) {
        // You can implement edit modal similar to add modal
        alert('Chức năng chỉnh sửa sản phẩm #' + productId);
    }

    // Show Product Modal
    function showProductModal() {
        alert('Chức năng quản lý sản phẩm sẽ được triển khai ở trang riêng');
    }

</script>
</body>
</html>

