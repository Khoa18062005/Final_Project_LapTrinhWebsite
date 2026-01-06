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

    <div class="shipper-profile" style="cursor:pointer" onclick="openProfileModal()" title="Chỉnh sửa thông tin">
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
                  <i class="fas fa-map-marker-alt" style="color: #e74c3c;"></i>
                  <div>
                    <strong>Địa chỉ nhận:</strong>
                    <a href="http://maps.google.com/maps?q=${current.delivery.order.address.street}, ${current.delivery.order.address.ward}, ${current.delivery.order.address.district}, ${current.delivery.order.address.city}"
                       target="_blank"
                       class="address-link">
                       ${current.delivery.order.address.street}, ${current.delivery.order.address.ward}, ${current.delivery.order.address.district}, ${current.delivery.order.address.city}
                       <i class="fas fa-external-link-alt"></i>
                    </a>
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
        <h4 style="margin: 20px 0 10px; color: #f39c12;"><i class="fas fa-clock"></i> Chờ nhận (${fn:length(data.pendingOrders)})</h4>
        <c:forEach var="item" items="${data.pendingOrders}">
          <div class="order-card pending">
            <div class="order-header">
              <div class="order-id">#${item.delivery.order.orderNumber}</div>
              <div class="order-status pending">${item.status}</div>
            </div>
            <div class="order-info">
              <div class="info-row"><i class="fas fa-map-marker-alt"></i> <span>Kho: ${item.delivery.warehouse.name}</span></div>
              <div class="info-row">
                  <i class="fas fa-map-pin" style="color: #e74c3c;"></i>
                  <span>Giao đến:
                      <a href="http://maps.google.com/maps?q=${item.delivery.order.address.street}, ${item.delivery.order.address.district}, ${item.delivery.order.address.city}"
                         target="_blank" class="address-link">
                         ${item.delivery.order.address.street}, ${item.delivery.order.address.district}
                      </a>
                  </span>
              </div>
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

        <h4 style="margin: 30px 0 10px; color: #3498db;"><i class="fas fa-motorcycle"></i> Đang thực hiện (${fn:length(data.ongoingOrders)})</h4>
        <c:forEach var="item" items="${data.ongoingOrders}">
          <div class="order-card ongoing">
            <div class="order-header">
              <div class="order-id">#${item.delivery.order.orderNumber}</div>
              <div class="order-status ongoing">${item.status}</div>
            </div>
            <div class="order-info">
              <div class="info-row"><i class="fas fa-user"></i> <span>${item.delivery.order.customer.lastName}</span></div>
              <div class="info-row"><i class="fas fa-map-pin" style="color:#e74c3c"></i> <span>${item.delivery.order.address.street}</span></div>
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
          <select id="timeFilter" onchange="updateDashboard()" class="form-select" style="padding: 8px 15px; border-radius: 8px;">
            <option value="today">Hôm nay</option>
            <option value="week" selected>7 ngày qua</option>
            <option value="month">Tháng vừa qua</option>
          </select>
        </div>
      </div>
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon purple"><i class="fas fa-wallet"></i></div>
          <div class="stat-info"><h3>Tổng thu nhập</h3><p class="stat-number" id="lblIncome">0 ₫</p></div>
        </div>
        <div class="stat-card">
          <div class="stat-icon green"><i class="fas fa-check-double"></i></div>
          <div class="stat-info"><h3>Đơn đã giao</h3><p class="stat-number" id="lblCount">0</p></div>
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
            <table style="width:100%">
              <thead>
              <tr><th>Mã đơn</th><th>Ngày xong</th><th>Thu nhập</th><th>Trạng thái</th></tr>
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

<div id="profileModalBackdrop" class="profile-modal-backdrop" role="dialog" aria-modal="true" aria-hidden="true">
  <div class="profile-modal" role="document">
    <h3><i class="fas fa-user-edit" style="color: #3498db;"></i> Cập nhật thông tin</h3>

    <form id="profileForm" action="${pageContext.request.contextPath}/shipper" method="post" enctype="multipart/form-data" onsubmit="return submitProfileForm()">
      <input type="hidden" name="action" value="updateProfile" />
      <input type="hidden" name="deleteAvatar" id="deleteAvatarFlag" value="false" />

      <div style="text-align: center; margin-bottom: 20px;">
          <div style="position: relative; width: 100px; height: 100px; margin: 0 auto 10px;">
              <img id="avatarPreview" src="${not empty data.shipperInfo.avatar ? data.shipperInfo.avatar : 'https://via.placeholder.com/100'}"
                   style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover; border: 3px solid #f0f2f5; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">

              <label for="avatarInput" style="position: absolute; bottom: 0; right: 0; background: #3498db; color: white; width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; cursor: pointer; border: 2px solid white; transition: 0.2s;">
                  <i class="fas fa-camera"></i>
              </label>
              <input type="file" id="avatarInput" name="avatarFile" accept="image/*" style="display: none;" onchange="previewImage(this)">
          </div>
          <button type="button" style="background: none; border: none; color: #e74c3c; font-size: 13px; cursor: pointer; text-decoration: underline;" onclick="removeAvatar()">
              <i class="fas fa-trash"></i> Xóa ảnh
          </button>
      </div>

      <div class="profile-row">
        <div style="flex: 1;">
            <label>Họ</label>
            <input type="text" name="lastName" id="pfLastName" value="${data.shipperInfo.lastName}" required />
        </div>
        <div style="flex: 1;">
            <label>Tên</label>
            <input type="text" name="firstName" id="pfFirstName" value="${data.shipperInfo.firstName}" required />
        </div>
      </div>

      <div class="form-group">
        <label>Email</label>
        <input type="email" value="${data.shipperInfo.email}" disabled />
      </div>

      <div class="profile-row">
          <div style="flex: 1;">
              <label>Số điện thoại</label>
              <input type="tel" name="phone" id="pfPhone" value="${data.shipperInfo.phone}" required />
          </div>
          <div style="flex: 1;">
              <label>Biển số xe</label>
              <input type="text" name="vehiclePlate" class="license-plate"
                     value="${data.shipperInfo.vehiclePlate}" placeholder="59A-123.45" />
          </div>
      </div>

      <div class="form-group">
          <label>Giấy phép lái xe (Bằng lái)</label>
          <input type="text" name="licenseNumber"
                 value="${data.shipperInfo.licenseNumber}" placeholder="Nhập số GPLX..." />
      </div>

      <div class="form-group">
        <label>Đổi mật khẩu</label>
        <input type="password" name="password" id="pfPassword" placeholder="Nhập mật khẩu mới (để trống nếu không đổi)" />
      </div>

      <div class="profile-actions">
        <button type="button" class="btn btn-secondary" onclick="closeProfileModal()">Đóng</button>
        <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Lưu thay đổi</button>
      </div>
    </form>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/shipper.js"></script>
<script>
  // --- SCRIPT CHUYỂN TAB ---
  function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');
    document.querySelector('a[href="#' + sectionId + '"]').classList.add('active');
  }

  // --- SCRIPT MODAL & AVATAR ---
  function openProfileModal() {
    document.getElementById('profileModalBackdrop').style.display = 'flex';
    document.getElementById('profileModalBackdrop').setAttribute('aria-hidden', 'false');
  }

  function closeProfileModal() {
    document.getElementById('profileModalBackdrop').style.display = 'none';
    document.getElementById('profileModalBackdrop').setAttribute('aria-hidden', 'true');
  }

  function submitProfileForm() {
    const first = document.getElementById('pfFirstName').value.trim();
    if (!first) { alert('Vui lòng nhập tên.'); return false; }
    return true;
  }

  // Xem trước ảnh khi chọn file
  function previewImage(input) {
      if (input.files && input.files[0]) {
          var reader = new FileReader();
          reader.onload = function(e) {
              document.getElementById('avatarPreview').src = e.target.result;
              document.getElementById('deleteAvatarFlag').value = "false"; // Reset cờ xóa
          }
          reader.readAsDataURL(input.files[0]);
      }
  }

  // Xóa ảnh (Hiển thị placeholder + đặt cờ xóa)
  function removeAvatar() {
      document.getElementById('avatarPreview').src = 'https://via.placeholder.com/100?text=No+Img';
      document.getElementById('avatarInput').value = ""; // Xóa input file
      document.getElementById('deleteAvatarFlag').value = "true"; // Đánh dấu xóa
  }

  document.addEventListener('click', function(e){
    if (e.target === document.getElementById('profileModalBackdrop')) closeProfileModal();
  });

  // --- SCRIPT BIỂU ĐỒ ---
  const dbData = {
    today: {
      income: ${data.todayIncome},
      count: ${data.successOrderCount},
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

  function renderChart(labels, dataValues) {
    const ctx = document.getElementById('revenueChart').getContext('2d');
    if (myChart) myChart.destroy();

    myChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Doanh thu (VNĐ)',
          data: dataValues,
          backgroundColor: 'rgba(52, 152, 219, 0.7)',
          borderColor: 'rgba(52, 152, 219, 1)',
          borderWidth: 1,
          borderRadius: 5,
          barPercentage: 0.6
        }]
      },
      options: {
        responsive: true, maintainAspectRatio: false,
        plugins: { legend: { display: false }, tooltip: { callbacks: { label: function(context) { return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(context.raw); } } } },
        scales: { y: { beginAtZero: true, grid: { borderDash: [2, 4] }, ticks: { callback: function(value) { return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND', maximumSignificantDigits: 3 }).format(value); } } }, x: { grid: { display: false } } }
      }
    });
  }

  function updateDashboard() {
    const type = document.getElementById('timeFilter').value;
    const currentData = dbData[type];
    document.getElementById('lblIncome').innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(currentData.income);
    document.getElementById('lblCount').innerText = currentData.count + " đơn";
    renderChart(currentData.labels, currentData.values);
  }

  document.addEventListener("DOMContentLoaded", function() {
    updateDashboard();
  });
</script>
</body>
</html>