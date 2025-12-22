<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>BlueShop | Sàn Thương Mại Điện Tử</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    <!-- CSS riêng -->
    <link rel="stylesheet" href="style_index.css">
</head>
<body>

<!-- HEADER -->
<header>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand fw-bold" href="#">VietTech</a>

            <form class="d-flex w-50 mx-3">
                <input class="form-control me-2" type="search" placeholder="Hôm nay bạn muốn tìm gì...">
                <button class="btn-search" type="submit">
                    <i class="bi bi-search text-dark"></i>
                </button>
            </form>

            <div class="items-header">
                <h5>Giỏ hàng</h5><i class="bi bi-cart3 fs-4 text-white"></i>
            </div>
            <div class="items-header" onclick="window.location.href='Register/register.jsp'">
                <h5>Đăng nhập</h5><i class="bi bi-person-circle fs-4 text-white"></i>
            </div>
        </div>
    </nav>
</header>

<!-- BANNER -->
<%--<section class="container my-4">--%>
<%--    <img src="https://via.placeholder.com/1200x350?text=BlueShop+Khuyen+Mai"--%>
<%--         alt="Banner khuyến mãi"--%>
<%--         class="img-fluid w-100">--%>
<%--</section>--%>

<!-- CATEGORY -->
<section class="categories">
    <div class="category-items">
        <i class="bi bi-phone fs-5"></i> <h7>Điện thoại</h7>
    </div>
    <div class="category-items">
        <i class="bi bi-laptop fs-5"></i> <h7>Laptop</h7>
    </div>
    <div class="category-items">
        <i class="bi bi-tablet-landscape fs-5"></i> <h7>Máy tính bảng</h7>
    </div>
    <div class="category-items">
        <i class="bi bi-earbuds fs-5"></i> <h7>Tai nghe</h7>
    </div>
    <div class="category-items">
        <i class="bi bi-mouse fs-5"></i> <h7>Phụ kiện điện tử</h7>
    </div>
</section>

<!-- PRODUCTS -->


<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
