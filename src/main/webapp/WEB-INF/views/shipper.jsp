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
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
                    <p>${current.delivery.order.address.street}, ${current.delivery.order.address.ward}, ${current.delivery.order.address.district}, ${current.delivery.order.address.city}</p>
                  </div>
                </div>
                <div class="delivery-details">
                  <div class="detail-item"><i class="fas fa-money-bill"></i> <span>Thu hộ: <fmt:formatNumber value="${current.delivery.order.totalPrice}" type="number"/> ₫</span></div>
                  <div class="detail-item"><i class="fas fa-box"></i> <span>Kho lấy: ${current.delivery.warehouse.name}</span></div>
                </div>
              </div>
              <div class="delivery-actions">
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
        <%-- Đơn chờ nhận --%>
        <h4 style="margin: 20px 0 10px; color: #f39c12;"><i class="fas fa-clock"></i> Đơn chờ nhận (${fn:length(data.pendingOrders)})</h4>
        <c:forEach var="item" items="${data.pendingOrders}">
          <div class="order-card pending">
            <div class="order-header">
              <div class="order-id">#${item.delivery.order.orderNumber}</div>
              <div class="order-status pending">${item.status}</div>
            </div>
            <div class="order-info">
              <div class="info-row"><i class="fas fa-map-marker-alt"></i> <span>Lấy hàng: ${item.delivery.warehouse.addressLine}</span></div>
              <div class="info-row"><i class="fas fa-map-pin"></i> <span>Giao đến: ${item.delivery.order.address.street}, ${item.delivery.order.address.district}</span></div>
              <div class="info-row"><i class="fas fa-money-bill"></i> <span>Tiền công: <fmt:formatNumber value="${item.earnings}" type="number"/> ₫</span></div>
            </div>
            <div class="order-actions">
              <form action="${pageContext.request.contextPath}/shipper" method="post" style="width: 100%;">
                <input type="hidden" name="action" value="accept">
                <input type="hidden" name="id" value="${item.assignmentId}">
                <button type="submit" class="btn btn-success" style="width: 100%;"><i class="fas fa-check"></i> Nhận đơn</button>
              </form>
            </div>
          </div>
        </c:forEach>

        <%-- Đơn đang giao --%>
        <h4 style="margin: 30px 0 10px; color: #3498db;"><i class="fas fa-motorcycle"></i> Đơn đang thực hiện (${fn:length(data.ongoingOrders)})</h4>
        <c:forEach var="item" items="${data.ongoingOrders}">
          <div class="order-card ongoing">
            <div class="order-header">
              <div class="order-id">#${item.delivery.order.orderNumber}</div>
              <div class="order-status ongoing">${item.status}</div>
            </div>
            <div class="order-info">
              <div class="info-row"><i class="fas fa-user"></i> <span>${item.delivery.order.customer.lastName} ${item.delivery.order.customer.firstName}</span></div>
              <div class="info-row"><i class="fas fa-map-pin"></i> <span>${item.delivery.order.address.street}</span></div>
              <div class="info-row"><i class="fas fa-hand-holding-usd" style="color: #2980b9;"></i> <span>COD: <fmt:formatNumber value="${item.delivery.order.totalPrice}" type="number"/> ₫</span></div>
            </div>
            <div class="order-actions">
              <form action="${pageContext.request.contextPath}/shipper" method="post" style="width: 100%;">
                <input type="hidden" name="action" value="complete">
                <input type="hidden" name="id" value="${item.assignmentId}">
                <button type="submit" class="btn btn-primary" style="width: 100%;"><i class="fas fa-check"></i> Hoàn thành</button>
              </form>
            </div>
          </div>
        </c:forEach>
      </div>
    </section>

    <section id="income" class="content-section">
      <div class="section-title">
        <h2>Thống kê thu nhập</h2>
        <div class="filter-wrapper" style="margin-left: auto;">
          <select id="timeFilter" onchange="updateDashboard()" class="form-select" style="padding: 8px 15px; border-radius: 8px; border: 1px solid #ddd; outline: none; cursor: pointer;">
            <option value="today">Hôm nay</option>
            <option value="week" selected>7 ngày qua</option>
            <option value="month">Tháng vừa qua</option>
          </select>
        </div>
      </div>

      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon purple"><i class="fas fa-wallet"></i></div>
          <div class="stat-info">
            <h3>Tổng thu nhập</h3>
            <p class="stat-number" id="lblIncome">0 ₫</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon green"><i class="fas fa-check-double"></i></div>
          <div class="stat-info">
            <h3>Đơn đã giao</h3>
            <p class="stat-number" id="lblCount">0</p>
          </div>
        </div>
      </div>

      <div class="chart-container" style="background: white; padding: 20px; border-radius: 12px; box-shadow: 0 4px 10px rgba(0,0,0,0.05); margin-top: 20px;">
        <canvas id="revenueChart" style="max-height: 400px; width: 100%;"></canvas>
      </div>
    </section>

    <section id="history" class="content-section">
      <div class="section-title"><h2>Lịch sử hoàn thành</h2></div>
      <div class="history-table">
        <c:choose>
          <c:when test="${not empty data.historyOrders}">
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
          </c:when>
          <c:otherwise>
            <div class="empty-state">
              <i class="fas fa-history" style="font-size: 2rem; color: #ccc;"></i>
              <p>Chưa có lịch sử giao hàng nào.</p>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </section>

  </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/shipper.js"></script>
<script>
  // === SCRIPT CHUYỂN TAB (TAB NAVIGATION) ===
  function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');
    document.querySelector('a[href="#' + sectionId + '"]').classList.add('active');
  }

  // === SCRIPT BIỂU ĐỒ & THỐNG KÊ (INCOME CHART) ===

  // 1. Nhận dữ liệu từ Server (DTO) vào biến JavaScript
  // Sử dụng JSTL c:forEach để in mảng dữ liệu ra
  const dbData = {
    today: {
      income: ${data.todayIncome},
      count: ${data.successOrderCount}, // Dùng lại biến countToday (hoặc successOrderCount)
      labels: ["Hôm nay"],
      values: [${data.todayIncome}]
    },
    week: {
      income: ${data.income7Days},
      count: ${data.count7Days},
      labels: [<c:forEach items="${data.chartLabels7Days}" var="l">"${l}",</c:forEach>],
      values: [<c:forEach items="${data.chartData7Days}" var="v">${v},</c:forEach>]
    },
    month: {
      income: ${data.incomeMonth},
      count: ${data.countMonth},
      labels: [<c:forEach items="${data.chartLabelsMonth}" var="l">"${l}",</c:forEach>],
      values: [<c:forEach items="${data.chartDataMonth}" var="v">${v},</c:forEach>]
    }
  };

  let myChart = null;

  // 2. Hàm vẽ biểu đồ
  function renderChart(labels, dataValues) {
    const ctx = document.getElementById('revenueChart').getContext('2d');

    if (myChart) {
      myChart.destroy(); // Xóa biểu đồ cũ để vẽ mới
    }

    myChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Doanh thu (VNĐ)',
          data: dataValues,
          backgroundColor: 'rgba(52, 152, 219, 0.7)', // Màu xanh dương nhạt
          borderColor: 'rgba(52, 152, 219, 1)',     // Viền xanh đậm
          borderWidth: 1,
          borderRadius: 5,
          barPercentage: 0.6
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false }, // Ẩn chú thích (vì chỉ có 1 cột)
          tooltip: {
            callbacks: {
              label: function(context) {
                // Format số tiền trong tooltip (vd: 50.000 ₫)
                return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(context.raw);
              }
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            grid: { borderDash: [2, 4] }, // Kẻ lưới nét đứt
            ticks: {
              callback: function(value) {
                // Format trục Y rút gọn (vd: 100N, 1M) hoặc đầy đủ
                return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND', maximumSignificantDigits: 3 }).format(value);
              }
            }
          },
          x: {
            grid: { display: false } // Ẩn lưới trục X cho thoáng
          }
        }
      }
    });
  }

  // 3. Hàm xử lý khi chọn Dropdown
  function updateDashboard() {
    const type = document.getElementById('timeFilter').value;
    const currentData = dbData[type];

    // Cập nhật Text thống kê
    document.getElementById('lblIncome').innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(currentData.income);
    document.getElementById('lblCount').innerText = currentData.count + " đơn";

    // Vẽ lại biểu đồ
    renderChart(currentData.labels, currentData.values);
  }

  // Khởi tạo mặc định khi trang load xong (Hiện 7 ngày qua)
  document.addEventListener("DOMContentLoaded", function() {
    updateDashboard();
  });
</script>
</body>
</html>