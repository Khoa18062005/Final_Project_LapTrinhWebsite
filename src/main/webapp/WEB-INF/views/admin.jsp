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
    <style>
        /* CSS Badge, Spec Table, v.v... Copy từ file cũ sang hoặc để vào admin.css */
        .badge-type { padding: 4px 8px; border-radius: 12px; font-size: 0.75rem; color: white; font-weight: 600; display: inline-block; margin-top: 4px;}
        .badge-type.phone { background-color: #3498db; }
        .badge-type.laptop { background-color: #e67e22; }
        .badge-type.tablet { background-color: #9b59b6; }
        .badge-type.accessory { background-color: #2ecc71; }
        .spec-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; font-size: 0.9rem; }
        .spec-table th, .spec-table td { padding: 8px 12px; border-bottom: 1px solid #eee; text-align: left; vertical-align: middle; }
        .spec-table th { width: 35%; background-color: #f8f9fa; color: #495057; font-weight: 600; }
        .spec-group-header { background-color: #e9ecef !important; font-weight: 700 !important; color: #2c3e50 !important; text-transform: uppercase; font-size: 0.85rem; }
        .text-success { color: #28a745; }
        .text-danger { color: #dc3545; }
        .text-muted { color: #6c757d; }
        #viewDetailModal .modal-content { max-width: 900px; width: 95%; }
        .filter-section { background: white; padding: 15px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); display: flex; align-items: center; gap: 15px; }
        .filter-section select { padding: 8px 12px; border: 1px solid #ddd; border-radius: 6px; outline: none; min-width: 200px; }
    </style>
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
        <jsp:include page="/WEB-INF/views/admin_pages/dashboard.jsp" />
    </div>

    <div id="products" class="content-section">
        <jsp:include page="/WEB-INF/views/admin_pages/products.jsp" />
    </div>

    <div id="orders" class="content-section">
<%--        <jsp:include page="/WEB-INF/views/admin_pages/orders.jsp" />--%>
    </div>

    <div id="users" class="content-section">
<%--        <jsp:include page="/WEB-INF/views/admin_pages/u sers.jsp" />--%>
    </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script>
    /**
     * Hàm hiển thị chi tiết sản phẩm
     * @param {string} sourceId - ID của thẻ div ẩn chứa thông tin (VD: 'detail-101')
     */
    function showProductDetails(sourceId) {
        // 1. Tìm thẻ div ẩn chứa dữ liệu chi tiết
        var sourceContent = document.getElementById(sourceId);

        // 2. Tìm vùng hiển thị trong Modal
        var targetContent = document.getElementById('viewDetailContent');
        var modal = document.getElementById('viewDetailModal');

        // 3. Kiểm tra và copy dữ liệu
        if (sourceContent && targetContent && modal) {
            // Copy toàn bộ HTML từ div ẩn sang div của Modal
            targetContent.innerHTML = sourceContent.innerHTML;

            // Hiển thị Modal
            modal.style.display = "block";
            modal.classList.add("show"); // Thêm class show nếu dùng CSS animation
        } else {
            console.error("Lỗi: Không tìm thấy dữ liệu chi tiết hoặc Modal. ID:", sourceId);
            alert("Không thể tải thông tin chi tiết sản phẩm này.");
        }
    }

    /**
     * Hàm đóng Modal (Dùng chung cho cả Modal Thêm và Modal Xem)
     * @param {string} modalId - ID của Modal cần đóng
     */
    function closeModal(modalId) {
        var modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = "none";
            modal.classList.remove("show");
        }
    }

    // Xử lý đóng Modal khi click ra ngoài vùng nội dung (Click vào nền đen)
    window.onclick = function(event) {
        var detailModal = document.getElementById('viewDetailModal');
        var addModal = document.getElementById('productModal');

        if (event.target == detailModal) {
            detailModal.style.display = "none";
        }
        if (event.target == addModal) {
            addModal.style.display = "none";
        }
    }
</script>
</body>
</html>