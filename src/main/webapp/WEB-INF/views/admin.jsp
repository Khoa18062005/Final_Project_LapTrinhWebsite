<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - VietTech</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">


</head>
<body>
<div class="sidebar">
    <div class="logo">
        <h2>Viet<span>Tech</span></h2>
    </div>
    <nav class="nav-menu">
        <div class="nav-menu-section">
            <a href="#dashboard" class="nav-item active" onclick="showSection('dashboard')">
                <i class="fas fa-chart-pie"></i>
                <span>Dashboard</span>
            </a>
            <a href="#products" class="nav-item" onclick="showSection('products')">
                <i class="fas fa-shopping-bag"></i>
                <span>Sản phẩm</span>
            </a>
            <a href="#orders" class="nav-item" onclick="showSection('orders')">
                <i class="fas fa-list-ul"></i>
                <span>Đơn hàng</span>
            </a>
            <a href="#users" class="nav-item" onclick="showSection('users')">
                <i class="fas fa-users"></i>
                <span>Người dùng</span>
            </a>
        </div>
    </nav>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/logout" class="nav-item">
            <i class="fas fa-sign-out-alt"></i>
            <span>Đăng xuất</span>
        </a>
    </div>
</div>

<div class="main-content">
    <div class="top-navbar">
        <div class="navbar-left">
            <button class="menu-toggle" onclick="toggleSidebar()">
                <i class="fas fa-bars"></i>
            </button>
            <h1 id="page-title">Dashboard</h1>
        </div>
        <div class="navbar-right">
            <div class="user-profile">
                <img src="${pageContext.request.contextPath}/assets/PNG/AVT.png" alt="Admin">
                <div class="user-profile-info">
                    <div class="name">${sessionScope.user.firstName} ${sessionScope.user.lastName}</div>
                    <div class="role">Admin</div>
                </div>
            </div>
        </div>
    </div>

    <div id="dashboard" class="content-section active">
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-icon blue"><i class="fas fa-users"></i></div>
                </div>
                <div class="stat-details">
                    <h3>Tổng người dùng</h3>
                    <p class="stat-number">${totalUsers}</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-icon green"><i class="fas fa-shopping-bag"></i></div>
                </div>
                <div class="stat-details">
                    <h3>Tổng sản phẩm</h3>
                    <p class="stat-number">${totalProducts}</p>
                </div>
            </div>
        </div>
    </div>

    <div id="products" class="content-section">
        <div class="section-header">
            <h2>Quản lý sản phẩm</h2>
            <button class="btn btn-primary" onclick="openAddProductModal()">
                <i class="fas fa-plus"></i> Thêm sản phẩm
            </button>
        </div>

        <div class="filter-section">
            <label><i class="fas fa-filter"></i> <strong>Lọc theo danh mục:</strong></label>
            <form action="${pageContext.request.contextPath}/admin" method="GET" id="filterForm">
                <select name="category" onchange="document.getElementById('filterForm').submit()">
                    <option value="">-- Tất cả sản phẩm --</option>
                    <option value="1" ${currentCategory == 1 ? 'selected' : ''}>📱 Điện thoại</option>
                    <option value="2" ${currentCategory == 2 ? 'selected' : ''}>🎧 Phụ kiện</option>
                    <option value="3" ${currentCategory == 3 ? 'selected' : ''}>💻 Laptop</option>
                    <option value="4" ${currentCategory == 4 ? 'selected' : ''}>📱 Tablet</option>
                </select>
            </form>
        </div>

        <c:if test="${not empty param.message}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> ${param.message}
            </div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-triangle"></i> Đã có lỗi xảy ra!
            </div>
        </c:if>

        <div class="table-container">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Mã (ID)</th>
                    <th>Tên sản phẩm</th>
                    <th>Mã người bán</th> <th>Danh mục</th>
                    <th>Giá gốc</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody id="productsTable">
                <c:forEach var="p" items="${productList}">
                    <tr>
                        <td>#${p.productId}</td>
                        <td>
                            <strong>${p.name}</strong>
                            <br><small class="product-slug">${p.slug}</small>
                        </td>
                        <td>
                            <span class="vendor-code">Vendor #${p.vendorId}</span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${p.categoryId == 1}"><span class="badge-type phone">Điện thoại</span></c:when>
                                <c:when test="${p.categoryId == 3}"><span class="badge-type laptop">Laptop</span></c:when>
                                <c:when test="${p.categoryId == 4}"><span class="badge-type tablet">Tablet</span></c:when>
                                <c:when test="${p.categoryId == 2}"><span class="badge-type accessory">Phụ kiện</span></c:when>
                                <c:otherwise><span class="badge-type">Khác</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <fmt:formatNumber value="${p.basePrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </td>
                        <td>
                            <span class="status-badge ${p.status == 'Active' ? 'delivered' : 'rejected'}">
                                    ${p.status != null ? p.status : 'Active'}
                            </span>
                        </td>
                        <td>
                            <div class="action-buttons">
                                <button type="button" class="btn-icon view" onclick="showProductDetails('detail-${p.productId}')" title="Xem chi tiết">
                                    <i class="fas fa-eye"></i>
                                </button>

                                <button class="btn-icon edit" title="Sửa"><i class="fas fa-edit"></i></button>

                                <form action="${pageContext.request.contextPath}/admin" method="POST" class="delete-form" onsubmit="return confirm('Xóa sản phẩm này?');">
                                    <input type="hidden" name="action" value="delete_product">
                                    <input type="hidden" name="id" value="${p.productId}">
                                    <button type="submit" class="btn-icon delete" title="Xóa"><i class="fas fa-trash"></i></button>
                                </form>
                            </div>

                            <div id="detail-${p.productId}" class="product-detail-hidden">
                                <div class="product-detail-header">
                                    <h3>${p.name}</h3>
                                    <p>ID: #${p.productId} | Vendor ID: ${p.vendorId}</p>
                                </div>

                                <c:if test="${p.categoryId == 1}">
                                    <h4 class="spec-title">📱 Thông số kỹ thuật</h4>
                                    <table class="spec-table">
                                        <tr><th colspan="2">Màn hình & Camera</th></tr>
                                        <tr><td>Màn hình</td><td>${p.screenSize} - ${p.screenType} (${p.screenResolution})</td></tr>
                                        <tr><td>Camera</td><td>Sau: ${p.rearCamera} <br> Trước: ${p.frontCamera}</td></tr>

                                        <tr><th colspan="2">Cấu hình</th></tr>
                                        <tr><td>Chip (CPU/GPU)</td><td>${p.processor} / ${p.gpu}</td></tr>
                                        <tr><td>Hệ điều hành</td><td>${p.os} ${p.osVersion}</td></tr>

                                        <tr><th colspan="2">Pin & Khác</th></tr>
                                        <tr><td>Pin/Sạc</td><td>${p.batteryCapacity} (${p.chargingSpeed})</td></tr>
                                        <tr><td>SIM/Mạng</td><td>${p.simType} / ${p.networkSupport}</td></tr>
                                        <tr><td>Tiện ích</td><td>
                                                ${p.fingerprintSensor ? 'Vân tay,' : ''}
                                                ${p.faceRecognition ? 'FaceID,' : ''}
                                                ${p.waterproofRating}
                                        </td></tr>
                                    </table>
                                </c:if>

                                <h4 class="spec-title">📝 Thông tin quản lý</h4>
                                <table class="spec-table">
                                    <tr><td>Giá gốc</td><td><fmt:formatNumber value="${p.basePrice}" type="currency" currencySymbol="₫"/></td></tr>
                                    <tr><td>Tình trạng</td><td>${p.conditions}</td></tr>
                                    <tr><td>Thống kê</td><td>Xem: ${p.viewCount} | Bán: ${p.totalSold}</td></tr>
                                    <tr><td>Mô tả</td><td>${p.description}</td></tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty productList}">
                    <tr>
                        <td colspan="7" class="empty-state">
                            <div class="empty-state-content">
                                <p>Không tìm thấy sản phẩm nào.</p>
                                <form action="${pageContext.request.contextPath}/admin" method="POST">
                                    <input type="hidden" name="action" value="init_data">
                                    <button type="submit" class="btn btn-secondary btn-sm">Tạo dữ liệu mẫu</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <div id="users" class="content-section">
        <div class="section-header"><h2>Quản lý người dùng</h2></div>
        <div class="table-container"><p class="in-progress">Tính năng đang phát triển...</p></div>
    </div>
    <div id="orders" class="content-section">
        <div class="section-header"><h2>Quản lý đơn hàng</h2></div>
        <div class="table-container"><p class="in-progress">Tính năng đang phát triển...</p></div>
    </div>
</div>

<div id="productModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Thêm sản phẩm mới</h2>
            <span class="close" onclick="closeModal('productModal')">&times;</span>
        </div>
        <div class="modal-body">
            <form id="productForm" action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="add_product">
                <div class="form-group">
                    <label>Tên sản phẩm <span class="required">*</span></label>
                    <input type="text" name="name" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>Danh mục <span class="required">*</span></label>
                    <select name="categoryId" class="form-control" required>
                        <option value="1">Điện thoại</option>
                        <option value="3">Laptop</option>
                        <option value="4">Tablet</option>
                        <option value="2">Phụ kiện</option>
                    </select>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Giá (VNĐ) <span class="required">*</span></label>
                        <input type="number" name="price" class="form-control" required min="0" step="1000">
                    </div>
                    <div class="form-group">
                        <label>Tồn kho</label>
                        <input type="number" name="stock" class="form-control" value="10">
                    </div>
                </div>
                <div class="form-group">
                    <label>Mô tả</label>
                    <textarea name="description" class="form-control" rows="3"></textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeModal('productModal')">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="viewDetailModal" class="modal">
    <div class="modal-content">
        <div class="modal-header modal-footer-actions">
            <h2>Chi tiết sản phẩm</h2>
            <span class="close" onclick="closeModal('viewDetailModal')">&times;</span>
        </div>
        <div class="modal-body modal-body-scroll" id="viewDetailContent"></div>
        <div class="modal-footer modal-footer-actions">
            <button type="button" class="btn btn-secondary" onclick="closeModal('viewDetailModal')">Đóng</button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script>
    function showProductDetails(sourceId) {
        document.getElementById('viewDetailContent').innerHTML = document.getElementById(sourceId).innerHTML;
        document.getElementById('viewDetailModal').style.display = "block";
    }
    window.onclick = function(event) {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = "none";
        }
    }
</script>
</body>
</html>