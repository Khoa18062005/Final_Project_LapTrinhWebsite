<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
      <img src="${not empty data.shipperInfo.avatar ? data.shipperInfo.avatar : 'https://via.placeholder.com/40'}" alt="Shipper">
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
        <span class="count-badge">${fn:length(data.pendingOrders) + fn:length(data.ongoingOrders)}</span>
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
                    <p>
                      <c:if test="${not empty current.delivery.order.address}">
                        ${current.delivery.order.address.street}, ${current.delivery.order.address.ward}, ${current.delivery.order.address.district}
                      </c:if>
                    </p>
                  </div>
                </div>
                <div class="delivery-details">
                  <div class="detail-item"><i class="fas fa-money-bill"></i> <span>Thu hộ: <fmt:formatNumber value="${current.delivery.order.totalPrice}" type="number"/> ₫</span></div>
                  <div class="detail-item"><i class="fas fa-box"></i> <span>Kho lấy: ${current.delivery.warehouse.name}</span></div>
                </div>
              </div>
              <div class="delivery-actions">
                  <%-- Đã xóa nút Gọi khách --%>
                <form action="${pageContext.request.contextPath}/shipper" method="post" style="width: 100%;">
                  <input type="hidden" name="action" value="complete">
                  <input type="hidden" name="id" value="${current.assignmentId}">
                  <button type="submit" class="btn btn-primary" style="width: 100%;">
                    <i class="fas fa-check"></i> Hoàn thành
                  </button>
                </form>
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

        <%-- KHỐI 1: ĐƠN HÀNG CHỜ NHẬN (PENDING) --%>
        <h4 style="margin: 20px 0 10px; color: #f39c12;"><i class="fas fa-clock"></i> Đơn chờ nhận (${fn:length(data.pendingOrders)})</h4>

        <c:choose>
          <c:when test="${not empty data.pendingOrders}">
            <c:forEach var="item" items="${data.pendingOrders}">
              <div class="order-card pending">
                <div class="order-header">
                  <div class="order-id">#${item.delivery.order.orderNumber}</div>
                  <div class="order-status pending">Chờ xác nhận</div>
                </div>
                <div class="order-info">
                  <div class="info-row"><i class="fas fa-warehouse" style="color: #e67e22;"></i> <span><strong>Lấy tại:</strong> ${item.delivery.warehouse.name}</span></div>
                  <div class="info-row" style="padding-left: 25px; font-size: 0.9em; color: #666;"><span>${item.delivery.warehouse.addressLine}, ${item.delivery.warehouse.district}</span></div>
                  <div class="info-row" style="margin-top: 5px;"><i class="fas fa-map-pin" style="color: #e74c3c;"></i>
                    <span><strong>Giao đến:</strong> <c:if test="${not empty item.delivery.order.address}">${item.delivery.order.address.street}, ${item.delivery.order.address.district}</c:if></span>
                  </div>
                  <div class="info-row" style="margin-top: 5px;"><i class="fas fa-money-bill-wave" style="color: #27ae60;"></i> <span>Tiền công: <strong><fmt:formatNumber value="${item.earnings}" type="number"/> ₫</strong></span></div>
                </div>
                <div class="order-actions">
                    <%-- NÚT NHẬN ĐƠN (Form POST) --%>
                  <form action="${pageContext.request.contextPath}/shipper" method="post" style="width: 100%;">
                    <input type="hidden" name="action" value="accept">
                    <input type="hidden" name="id" value="${item.assignmentId}">
                    <button type="submit" class="btn btn-success" style="width: 100%;">
                      <i class="fas fa-check"></i> Nhận đơn ngay
                    </button>
                  </form>
                </div>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div class="empty-state" style="text-align: center; padding: 20px; color: #999;"><i class="fas fa-clipboard-check" style="font-size: 2rem; margin-bottom: 10px;"></i><p>Hiện không có đơn hàng nào chờ nhận.</p></div>
          </c:otherwise>
        </c:choose>

        <%-- KHỐI 2: ĐƠN HÀNG ĐANG GIAO (ONGOING) --%>
        <h4 style="margin: 30px 0 10px; color: #3498db;"><i class="fas fa-motorcycle"></i> Đơn đang thực hiện (${fn:length(data.ongoingOrders)})</h4>

        <c:choose>
          <c:when test="${not empty data.ongoingOrders}">
            <c:forEach var="item" items="${data.ongoingOrders}">
              <div class="order-card ongoing">
                <div class="order-header">
                  <div class="order-id">#${item.delivery.order.orderNumber}</div>
                  <div class="order-status ongoing">
                    <c:choose>
                      <c:when test="${item.status == 'In Transit'}">Đang giao hàng</c:when>
                      <c:when test="${item.status == 'Picking Up'}">Đang lấy hàng</c:when>
                      <c:when test="${item.status == 'Accepted'}">Đã nhận đơn</c:when>
                      <c:otherwise>${item.status}</c:otherwise>
                    </c:choose>
                  </div>
                </div>
                <div class="order-info">
                  <div class="info-row"><i class="fas fa-user"></i> <span>${item.delivery.order.customer.lastName} ${item.delivery.order.customer.firstName}</span></div>
                  <div class="info-row"><i class="fas fa-map-pin"></i> <span><c:if test="${not empty item.delivery.order.address}">${item.delivery.order.address.street}</c:if></span></div>
                  <div class="info-row"><i class="fas fa-hand-holding-usd" style="color: #2980b9;"></i> <span>Thu: <fmt:formatNumber value="${item.delivery.order.totalPrice}" type="number"/> ₫</span></div>
                </div>

                  <%-- NÚT HOÀN THÀNH CHO DANH SÁCH --%>
                <div class="order-actions">
                  <form action="${pageContext.request.contextPath}/shipper" method="post" style="width: 100%;">
                    <input type="hidden" name="action" value="complete">
                    <input type="hidden" name="id" value="${item.assignmentId}">
                    <button type="submit" class="btn btn-primary" style="width: 100%;">
                      <i class="fas fa-check"></i> Hoàn thành
                    </button>
                  </form>
                </div>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div class="empty-state" style="text-align: center; padding: 20px; color: #999;"><p>Bạn đang rảnh rỗi.</p></div>
          </c:otherwise>
        </c:choose>
      </div>
    </section>

    <section id="history" class="content-section">
      <div class="section-title"><h2>Lịch sử giao hàng</h2></div>
      <div class="history-table">
        <c:choose>
          <c:when test="${not empty data.historyOrders}">
            <table style="width: 100%; border-collapse: collapse;">
              <thead>
              <tr style="background: #f8f9fa; text-align: left;">
                <th style="padding: 12px;">Mã đơn</th>
                <th style="padding: 12px;">Thời gian</th>
                <th style="padding: 12px;">Thu nhập</th>
                <th style="padding: 12px;">Trạng thái</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="h" items="${data.historyOrders}">
                <tr style="border-bottom: 1px solid #eee;">
                  <td style="padding: 12px; font-weight: bold; color: #2c3e50;">#${h.delivery.order.orderNumber}</td>
                  <td style="padding: 12px; color: #666;"><fmt:formatDate value="${h.completedAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                  <td style="padding: 12px; color: #27ae60; font-weight: bold;">+<fmt:formatNumber value="${h.earnings}" type="number"/> ₫</td>
                  <td style="padding: 12px;"><span class="status-badge completed" style="background: #e8f5e9; color: #2ecc71; padding: 5px 10px; border-radius: 15px;">Hoàn thành</span></td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
          </c:when>
          <c:otherwise>
            <div class="empty-state" style="text-align: center; padding: 40px; color: #999;"><i class="fas fa-history" style="font-size: 3rem; margin-bottom: 15px; color: #ddd;"></i><p>Chưa có lịch sử giao hàng nào.</p></div>
          </c:otherwise>
        </c:choose>
      </div>
    </section>

  </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/shipper.js"></script>
<script>
  function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');
    document.querySelector('a[href="#' + sectionId + '"]').classList.add('active');
  }
</script>
</body>
</html>