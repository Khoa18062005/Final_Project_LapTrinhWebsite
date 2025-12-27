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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vendor.css">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="#"><i class="fas fa-store"></i> VietTech Vendor</a>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
            <%-- Hiển thị tên Shop --%>
            <i class="fas fa-user-circle"></i> ${data.vendorInfo.businessName}
          </a>
          <ul class="dropdown-menu dropdown-menu-end">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>

<div class="container-fluid">
  <div class="row">
    <nav id="sidebar" class="col-md-3 col-lg-2 d-md-block sidebar collapse">
      <div class="position-sticky pt-3">
        <ul class="nav flex-column">
          <li class="nav-item"><a class="nav-link active" href="#dashboard" data-section="dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
          <li class="nav-item"><a class="nav-link" href="#products" data-section="products"><i class="fas fa-box"></i> Quản lý sản phẩm</a></li>
          <li class="nav-item"><a class="nav-link" href="#orders" data-section="orders"><i class="fas fa-shopping-cart"></i> Đơn hàng</a></li>
          <%-- Các menu khác... --%>
        </ul>
      </div>
    </nav>

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">

      <div id="dashboard" class="content-section active">
        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
          <h1 class="h2"><i class="fas fa-tachometer-alt"></i> Dashboard</h1>
        </div>

        <div class="row g-3 mb-4">
          <div class="col-md-3">
            <div class="stat-card">
              <div class="stat-icon bg-primary"><i class="fas fa-box"></i></div>
              <div class="stat-info">
                <h3>${data.totalProducts}</h3>
                <p>Tổng sản phẩm</p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="stat-card">
              <div class="stat-icon bg-success"><i class="fas fa-shopping-cart"></i></div>
              <div class="stat-info">
                <h3>${data.newOrdersCount}</h3>
                <p>Đơn hàng mới</p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="stat-card">
              <div class="stat-icon bg-warning"><i class="fas fa-clock"></i></div>
              <div class="stat-info">
                <h3>${data.pendingApprovals}</h3>
                <p>Chờ phê duyệt</p>
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="stat-card">
              <div class="stat-icon bg-info"><i class="fas fa-dollar-sign"></i></div>
              <div class="stat-info">
                <h3><fmt:formatNumber value="${data.monthlyRevenue}" type="number" maxFractionDigits="0"/></h3>
                <p>Doanh thu tháng</p>
              </div>
            </div>
          </div>
        </div>

        <div class="card mb-4">
          <div class="card-header"><h5><i class="fas fa-shopping-cart"></i> Đơn hàng gần đây</h5></div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-hover">
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
                <c:forEach var="o" items="${data.recentOrders}" end="4"> <%-- Chỉ hiện 5 đơn mới nhất --%>
                  <tr>
                    <td>#${o.orderNumber}</td>
                    <td>${o.customer.lastName} ${o.customer.firstName}</td>
                    <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy"/></td>
                    <td><fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="₫"/></td>
                    <td>
                          <span class="badge ${o.status == 'Completed' ? 'bg-success' : (o.status == 'Cancelled' ? 'bg-danger' : 'bg-warning')}">
                              ${o.status}
                          </span>
                    </td>
                    <td><button class="btn btn-sm btn-primary">Xem</button></td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div id="products" class="content-section">
        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
          <h1 class="h2"><i class="fas fa-box"></i> Quản lý sản phẩm</h1>
          <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addProductModal"><i class="fas fa-plus"></i> Thêm mới</button>
        </div>

        <div class="card">
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-hover">
                <thead>
                <tr>
                  <th>Hình ảnh</th>
                  <th>Tên sản phẩm</th>
                  <th>Giá</th>
                  <th>Trạng thái</th>
                  <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${data.productList}">
                  <tr>
                    <td><img src="https://via.placeholder.com/50" alt="Product" class="img-thumbnail"></td>
                    <td>${p.name}</td>
                    <td><fmt:formatNumber value="${p.basePrice}" type="currency" currencySymbol="₫"/></td>
                    <td><span class="badge ${p.status == 'Available' ? 'bg-success' : 'bg-danger'}">${p.status}</span></td>
                    <td>
                      <button class="btn btn-sm btn-warning"><i class="fas fa-edit"></i></button>
                      <button class="btn btn-sm btn-danger"><i class="fas fa-trash"></i></button>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div id="delivery" class="content-section">
        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
          <h1 class="h2"><i class="fas fa-shipping-fast"></i> Phân phối hàng</h1>
        </div>
        <div class="card">
          <div class="card-body">
            <table class="table table-hover">
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
              <c:forEach var="so" items="${data.pendingShippingOrders}">
                <tr>
                  <td>#${so.orderNumber}</td>
                  <td>${so.customer.lastName} ${so.customer.firstName}</td>
                  <td>${so.address.street}, ${so.address.district}</td>
                  <td><fmt:formatNumber value="${so.totalPrice}" type="currency" currencySymbol="₫"/></td>
                  <td>
                    <button class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#assignShipperModal">
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

    </main>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
  // Script chuyển Tab (Giữ nguyên logic cũ của bạn)
  document.querySelectorAll('.sidebar .nav-link').forEach(link => {
    link.addEventListener('click', function(e) {
      e.preventDefault();
      document.querySelectorAll('.sidebar .nav-link').forEach(l => l.classList.remove('active'));
      document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
      this.classList.add('active');
      const sectionId = this.getAttribute('data-section');
      document.getElementById(sectionId).classList.add('active');
    });
  });
</script>
</body>
</html>