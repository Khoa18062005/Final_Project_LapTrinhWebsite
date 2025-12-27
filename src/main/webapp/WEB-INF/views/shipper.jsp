<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Shipper Dashboard - VietTech</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shipper.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<header class="top-header">
  <div class="header-left">
    <i class="fas fa-shipping-fast"></i>
    <h1>Shipper Dashboard</h1>
  </div>
  <div class="header-right">
    <div class="status-toggle">
      <label class="switch">
        <input type="checkbox" id="onlineStatus" ${data.shipperInfo.available ? 'checked' : ''}>
        <span class="slider"></span>
      </label>
      <span class="status-text" id="statusText">${data.shipperInfo.available ? 'Đang hoạt động' : 'Tạm nghỉ'}</span>
    </div>
    <div class="shipper-profile">
      <img src="${data.shipperInfo.avatar != null && !data.shipperInfo.avatar.isEmpty() ? data.shipperInfo.avatar : 'https://via.placeholder.com/40'}" alt="Shipper">
      <div class="profile-info">
        <span class="name">${data.shipperInfo.firstName} ${data.shipperInfo.lastName}</span>
        <span class="rating">⭐ ${data.shipperInfo.rating} (${data.shipperInfo.totalDeliveries} đơn)</span>
      </div>
    </div>
  </div>
</header>

<div class="main-container">
  <aside class="sidebar">
    <nav class="nav-menu">
      <a href="#overview" class="nav-item active" onclick="showSection('overview')">
        <i class="fas fa-tachometer-alt"></i> <span>Tổng quan</span>
      </a>
      <a href="#orders" class="nav-item" onclick="showSection('orders')">
        <i class="fas fa-box"></i> <span>Đơn hàng</span>
        <span class="count-badge">${data.pendingOrders.size() + data.ongoingOrders.size()}</span>
      </a>
      <a href="#income" class="nav-item" onclick="showSection('income')"><i class="fas fa-wallet"></i> <span>Thu nhập</span></a>
      <a href="#history" class="nav-item" onclick="showSection('history')"><i class="fas fa-history"></i> <span>Lịch sử</span></a>
    </nav>
    <div class="sidebar-footer">
      <a href="${pageContext.request.contextPath}/logout" class="nav-item logout">
        <i class="fas fa-sign-out-alt"></i> <span>Đăng xuất</span>
      </a>
    </div>
  </aside>

  <main class="main-content">
    <section id="overview" class="content-section active">
      <div class="section-title">
        <h2>Tổng quan hôm nay</h2>
        <p class="date"><script>document.write(new Date().toLocaleDateString('vi-VN'))</script></p>
      </div>

      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon blue"><i class="fas fa-box"></i></div>
          <div class="stat-info">
            <h3>Đơn phân công</h3>
            <p class="stat-number">${data.todayOrderCount}</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon green"><i class="fas fa-check-circle"></i></div>
          <div class="stat-info">
            <h3>Hoàn thành</h3>
            <p class="stat-number">${data.successOrderCount}</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon purple"><i class="fas fa-money-bill-wave"></i></div>
          <div class="stat-info">
            <h3>Thu nhập hôm nay</h3>
            <p class="stat-number">
              <fmt:formatNumber value="${data.todayIncome}" type="currency" currencySymbol="₫"/>
            </p>
          </div>
        </div>
      </div>

      <div class="current-delivery">
        <h3><i class="fas fa-motorcycle"></i> Đơn hàng đang thực hiện</h3>
        <c:choose>
          <c:when test="${not empty data.ongoingOrders}">
            <c:set var="current" value="${data.ongoingOrders[0]}" />
            <div class="delivery-card active-delivery">
              <div class="delivery-header">
                <div class="order-id"><span class="label">Mã vận đơn:</span> <span class="value">${current.delivery.trackingNumber}</span></div>
                <div class="delivery-status ongoing"><i class="fas fa-shipping-fast"></i> ${current.status}</div>
              </div>
              <div class="delivery-body">
                <div class="customer-info">
                  <i class="fas fa-user"></i>
                  <div>
                    <strong>${current.delivery.order.customer.lastName} ${current.delivery.order.customer.firstName}</strong>
                    <p>${current.delivery.order.customer.phone}</p>
                  </div>
                </div>
                <div class="address-info">
                  <i class="fas fa-map-marker-alt"></i>
                  <div>
                    <strong>Địa chỉ nhận:</strong>
                    <p>${current.delivery.order.address.street}, ${current.delivery.order.address.ward}, ${current.delivery.order.address.district}, ${current.delivery.order.address.city}</p>
                  </div>
                </div>
                <div class="delivery-details">
                  <div class="detail-item"><i class="fas fa-money-bill"></i> <span>Thu hộ: <fmt:formatNumber value="${current.delivery.order.totalPrice}" type="number"/> ₫</span></div>
                  <div class="detail-item"><i class="fas fa-box"></i> <span>Kho lấy: ${current.delivery.warehouse.name}</span></div>
                </div>
              </div>
              <div class="delivery-actions">
                <button class="btn btn-outline"><i class="fas fa-phone"></i> Gọi khách</button>
                <button class="btn btn-primary"><i class="fas fa-check"></i> Hoàn thành</button>
              </div>
            </div>
          </c:when>
          <c:otherwise>
            <p class="empty-state">Hiện không có đơn hàng nào đang giao.</p>
          </c:otherwise>
        </c:choose>
      </div>
    </section>

    <section id="orders" class="content-section">
      <div class="section-title">
        <h2>Danh sách đơn hàng</h2>
      </div>

      <div class="orders-list">
        <c:forEach var="item" items="${data.pendingOrders}">
          <div class="order-card pending">
            <div class="order-header">
              <div class="order-id">#${item.delivery.order.orderNumber}</div>
              <div class="order-status pending">Chờ nhận</div>
            </div>
            <div class="order-info">
              <div class="info-row"><i class="fas fa-map-marker-alt"></i> <span>Lấy hàng: ${item.delivery.warehouse.addressLine}</span></div>
              <div class="info-row"><i class="fas fa-map-pin"></i> <span>Giao đến: ${item.delivery.order.address.street}, ${item.delivery.order.address.district}</span></div>
              <div class="info-row"><i class="fas fa-money-bill"></i> <span>Tiền công: <fmt:formatNumber value="${item.earnings}" type="number"/> ₫</span></div>
            </div>
            <div class="order-actions">
              <button class="btn btn-success"><i class="fas fa-check"></i> Nhận đơn</button>
            </div>
          </div>
        </c:forEach>

        <c:forEach var="item" items="${data.ongoingOrders}">
          <div class="order-card ongoing">
            <div class="order-header">
              <div class="order-id">#${item.delivery.order.orderNumber}</div>
              <div class="order-status ongoing">Đang giao</div>
            </div>
            <div class="order-info">
              <div class="info-row"><i class="fas fa-user"></i> <span>${item.delivery.order.customer.lastName}</span></div>
              <div class="info-row"><i class="fas fa-map-pin"></i> <span>${item.delivery.order.address.street}</span></div>
            </div>
          </div>
        </c:forEach>
      </div>
    </section>

    <section id="history" class="content-section">
      <div class="section-title"><h2>Lịch sử hoàn thành</h2></div>
      <div class="history-table">
        <table>
          <thead>
          <tr>
            <th>Mã đơn</th>
            <th>Ngày hoàn thành</th>
            <th>Thu nhập</th>
            <th>Trạng thái</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="h" items="${data.historyOrders}">
            <tr>
              <td>#${h.delivery.order.orderNumber}</td>
              <td><fmt:formatDate value="${h.completedAt}" pattern="dd/MM/yyyy HH:mm"/></td>
              <td><fmt:formatNumber value="${h.earnings}" type="number"/> ₫</td>
              <td><span class="status-badge completed">${h.status}</span></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </section>

  </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/shipper.js"></script>
<script>
  // JS đơn giản để chuyển Tab (nếu shipper.js chưa có)
  function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));

    document.getElementById(sectionId).classList.add('active');
    document.querySelector('a[href="#' + sectionId + '"]').classList.add('active');
  }
</script>
</body>
</html>