<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<!-- Top Header -->
<header class="top-header">
  <div class="header-left">
    <i class="fas fa-shipping-fast"></i>
    <h1>Shipper Dashboard</h1>
  </div>
  <div class="header-right">
    <div class="status-toggle">
      <label class="switch">
        <input type="checkbox" id="onlineStatus" checked>
        <span class="slider"></span>
      </label>
      <span class="status-text" id="statusText">Đang hoạt động</span>
    </div>
    <div class="notifications">
      <i class="fas fa-bell"></i>
      <span class="badge">5</span>
    </div>
    <div class="shipper-profile">
      <img src="https://via.placeholder.com/40" alt="Shipper">
      <div class="profile-info">
        <span class="name">Nguyễn Văn Shipper</span>
        <span class="rating">⭐ 4.8 (245 đánh giá)</span>
      </div>
    </div>
  </div>
</header>

<!-- Main Container -->
<div class="main-container">
  <!-- Sidebar Navigation -->
  <aside class="sidebar">
    <nav class="nav-menu">
      <a href="#overview" class="nav-item active" onclick="showSection('overview')">
        <i class="fas fa-tachometer-alt"></i>
        <span>Tổng quan</span>
      </a>
      <a href="#orders" class="nav-item" onclick="showSection('orders')">
        <i class="fas fa-box"></i>
        <span>Đơn hàng</span>
        <span class="count-badge" id="ordersCount">8</span>
      </a>
      <a href="#map" class="nav-item" onclick="showSection('map')">
        <i class="fas fa-map-marked-alt"></i>
        <span>Bản đồ</span>
      </a>
      <a href="#income" class="nav-item" onclick="showSection('income')">
        <i class="fas fa-wallet"></i>
        <span>Thu nhập</span>
      </a>
      <a href="#ratings" class="nav-item" onclick="showSection('ratings')">
        <i class="fas fa-star"></i>
        <span>Đánh giá</span>
      </a>
      <a href="#history" class="nav-item" onclick="showSection('history')">
        <i class="fas fa-history"></i>
        <span>Lịch sử</span>
      </a>
      <a href="#profile" class="nav-item" onclick="showSection('profile')">
        <i class="fas fa-user-cog"></i>
        <span>Cài đặt</span>
      </a>
    </nav>
    <div class="sidebar-footer">
      <a href="#" class="nav-item logout">
        <i class="fas fa-sign-out-alt"></i>
        <span>Đăng xuất</span>
      </a>
    </div>
  </aside>

  <!-- Main Content -->
  <main class="main-content">
    <!-- Overview Section -->
    <section id="overview" class="content-section active">
      <div class="section-title">
        <h2>Tổng quan hôm nay</h2>
        <p class="date" id="currentDate"></p>
      </div>

      <!-- Stats Cards -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon blue">
            <i class="fas fa-box"></i>
          </div>
          <div class="stat-info">
            <h3>Đơn hàng hôm nay</h3>
            <p class="stat-number" id="todayOrders">12</p>
            <span class="stat-sub">8 đang giao, 4 hoàn thành</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon green">
            <i class="fas fa-check-circle"></i>
          </div>
          <div class="stat-info">
            <h3>Giao thành công</h3>
            <p class="stat-number" id="successOrders">4</p>
            <span class="stat-sub">Tỷ lệ: 100%</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon orange">
            <i class="fas fa-clock"></i>
          </div>
          <div class="stat-info">
            <h3>Thời gian trung bình</h3>
            <p class="stat-number" id="avgTime">28 <small>phút</small></p>
            <span class="stat-sub">Nhanh hơn 15% so với TB</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon purple">
            <i class="fas fa-money-bill-wave"></i>
          </div>
          <div class="stat-info">
            <h3>Thu nhập hôm nay</h3>
            <p class="stat-number" id="todayIncome">320,000 <small>₫</small></p>
            <span class="stat-sub">+45,000₫ so với hôm qua</span>
          </div>
        </div>
      </div>

      <!-- Current Delivery -->
      <div class="current-delivery">
        <h3><i class="fas fa-motorcycle"></i> Đơn hàng đang giao</h3>
        <div class="delivery-card active-delivery">
          <div class="delivery-header">
            <div class="order-id">
              <span class="label">Mã đơn:</span>
              <span class="value">#DH12345</span>
            </div>
            <div class="delivery-status ongoing">
              <i class="fas fa-shipping-fast"></i> Đang giao
            </div>
          </div>
          <div class="delivery-body">
            <div class="customer-info">
              <i class="fas fa-user"></i>
              <div>
                <strong>Nguyễn Văn A</strong>
                <p>0123 456 789</p>
              </div>
            </div>
            <div class="address-info">
              <i class="fas fa-map-marker-alt"></i>
              <div>
                <strong>Địa chỉ giao hàng:</strong>
                <p>123 Đường ABC, Quận 1, TP.HCM</p>
              </div>
            </div>
            <div class="delivery-details">
              <div class="detail-item">
                <i class="fas fa-box"></i>
                <span>3 sản phẩm</span>
              </div>
              <div class="detail-item">
                <i class="fas fa-money-bill"></i>
                <span>2,500,000₫</span>
              </div>
              <div class="detail-item">
                <i class="fas fa-credit-card"></i>
                <span>COD</span>
              </div>
            </div>
            <div class="delivery-progress">
              <div class="progress-bar">
                <div class="progress-fill" style="width: 60%"></div>
              </div>
              <span class="progress-text">Còn 2.5km - Dự kiến 15 phút</span>
            </div>
          </div>
          <div class="delivery-actions">
            <button class="btn btn-outline" onclick="callCustomer()">
              <i class="fas fa-phone"></i> Gọi khách
            </button>
            <button class="btn btn-outline" onclick="openMap()">
              <i class="fas fa-directions"></i> Chỉ đường
            </button>
            <button class="btn btn-primary" onclick="confirmDelivery()">
              <i class="fas fa-check"></i> Xác nhận giao hàng
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Orders Section -->
    <section id="orders" class="content-section">
      <div class="section-title">
        <h2>Quản lý đơn hàng</h2>
        <div class="filter-tabs">
          <button class="tab-btn active" onclick="filterOrders('all')">Tất cả (8)</button>
          <button class="tab-btn" onclick="filterOrders('pending')">Chờ nhận (3)</button>
          <button class="tab-btn" onclick="filterOrders('ongoing')">Đang giao (4)</button>
          <button class="tab-btn" onclick="filterOrders('completed')">Hoàn thành (1)</button>
        </div>
      </div>

      <div class="orders-list" id="ordersList">
        <!-- Order Card 1 - Pending -->
        <div class="order-card pending">
          <div class="order-header">
            <div class="order-id">#DH12346</div>
            <div class="order-status pending">Chờ nhận</div>
          </div>
          <div class="order-info">
            <div class="info-row">
              <i class="fas fa-user"></i>
              <span>Trần Thị B - 0987 654 321</span>
            </div>
            <div class="info-row">
              <i class="fas fa-map-marker-alt"></i>
              <span>456 Đường DEF, Quận 3, TP.HCM</span>
            </div>
            <div class="info-row">
              <i class="fas fa-clock"></i>
              <span>Thời gian nhận: Trong 15 phút</span>
            </div>
            <div class="info-row">
              <i class="fas fa-money-bill"></i>
              <span>COD: 1,200,000₫</span>
            </div>
          </div>
          <div class="order-actions">
            <button class="btn btn-success" onclick="acceptOrder('DH12346')">
              <i class="fas fa-check"></i> Nhận đơn
            </button>
            <button class="btn btn-outline" onclick="viewOrderDetail('DH12346')">
              <i class="fas fa-eye"></i> Chi tiết
            </button>
          </div>
        </div>

        <!-- Order Card 2 - Ongoing -->
        <div class="order-card ongoing">
          <div class="order-header">
            <div class="order-id">#DH12347</div>
            <div class="order-status ongoing">Đang giao</div>
          </div>
          <div class="order-info">
            <div class="info-row">
              <i class="fas fa-user"></i>
              <span>Lê Văn C - 0345 678 901</span>
            </div>
            <div class="info-row">
              <i class="fas fa-map-marker-alt"></i>
              <span>789 Đường GHI, Quận 5, TP.HCM</span>
            </div>
            <div class="info-row">
              <i class="fas fa-route"></i>
              <span>Còn 1.8km - Dự kiến 12 phút</span>
            </div>
            <div class="info-row">
              <i class="fas fa-money-bill"></i>
              <span>Đã thanh toán online: 3,500,000₫</span>
            </div>
          </div>
          <div class="order-actions">
            <button class="btn btn-outline" onclick="callCustomer()">
              <i class="fas fa-phone"></i> Gọi
            </button>
            <button class="btn btn-primary" onclick="confirmDelivery()">
              <i class="fas fa-check-circle"></i> Xác nhận giao
            </button>
          </div>
        </div>

        <!-- Order Card 3 - Completed -->
        <div class="order-card completed">
          <div class="order-header">
            <div class="order-id">#DH12348</div>
            <div class="order-status completed">Hoàn thành</div>
          </div>
          <div class="order-info">
            <div class="info-row">
              <i class="fas fa-user"></i>
              <span>Phạm Thị D - 0912 345 678</span>
            </div>
            <div class="info-row">
              <i class="fas fa-check-circle"></i>
              <span>Giao thành công lúc 14:30</span>
            </div>
            <div class="info-row">
              <i class="fas fa-money-bill"></i>
              <span>Đã thu: 850,000₫</span>
            </div>
          </div>
          <div class="order-actions">
            <button class="btn btn-outline" onclick="viewReceipt('DH12348')">
              <i class="fas fa-receipt"></i> Xem biên lai
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Map Section -->
    <section id="map" class="content-section">
      <div class="section-title">
        <h2>Bản đồ giao hàng</h2>
        <button class="btn btn-primary" onclick="updateLocation()">
          <i class="fas fa-sync-alt"></i> Cập nhật vị trí
        </button>
      </div>
      <div class="map-container">
        <div class="map-placeholder">
          <i class="fas fa-map-marked-alt"></i>
          <p>Bản đồ sẽ hiển thị ở đây</p>
          <span>Tích hợp Google Maps / OpenStreetMap</span>
        </div>
        <div class="map-info">
          <div class="location-info">
            <h4><i class="fas fa-location-arrow"></i> Vị trí hiện tại</h4>
            <p>123 Đường Nguyễn Huệ, Quận 1, TP.HCM</p>
            <p class="coords">10.7756° N, 106.7019° E</p>
          </div>
          <div class="route-info">
            <h4><i class="fas fa-route"></i> Tuyến đường</h4>
            <ul class="route-list">
              <li class="current"><i class="fas fa-circle"></i> Vị trí hiện tại</li>
              <li><i class="fas fa-map-pin"></i> Đơn #DH12345 - 2.5km</li>
              <li><i class="fas fa-map-pin"></i> Đơn #DH12347 - 4.2km</li>
              <li><i class="fas fa-map-pin"></i> Đơn #DH12349 - 6.8km</li>
            </ul>
          </div>
        </div>
      </div>
    </section>

    <!-- Income Section -->
    <section id="income" class="content-section">
      <div class="section-title">
        <h2>Thu nhập của tôi</h2>
        <select class="filter-select" onchange="filterIncome(this.value)">
          <option value="today">Hôm nay</option>
          <option value="week">Tuần này</option>
          <option value="month" selected>Tháng này</option>
        </select>
      </div>

      <div class="income-summary">
        <div class="income-card total">
          <div class="income-icon">
            <i class="fas fa-wallet"></i>
          </div>
          <div class="income-details">
            <h3>Tổng thu nhập <span id="incomePeriodText">tháng này</span></h3>
            <p class="income-amount" id="totalIncomeDisplay">8,450,000₫</p>
            <span class="income-change positive" id="incomeChangeDisplay">
        <i class="fas fa-arrow-up"></i> +15.5% so với tháng trước
    </span>
          </div>
        </div>
        <div class="income-breakdown">
          <div class="breakdown-item">
            <div class="item-header">
              <i class="fas fa-shipping-fast"></i>
              <span>Phí giao hàng</span>
            </div>
            <span class="item-amount">6,200,000₫</span>
          </div>
          <div class="breakdown-item">
            <div class="item-header">
              <i class="fas fa-gift"></i>
              <span>Tiền thưởng</span>
            </div>
            <span class="item-amount">1,500,000₫</span>
          </div>
          <div class="breakdown-item">
            <div class="item-header">
              <i class="fas fa-star"></i>
              <span>Đánh giá 5 sao</span>
            </div>
            <span class="item-amount">750,000₫</span>
          </div>
        </div>
      </div>

      <div class="income-chart">
        <h3>Biểu đồ thu nhập</h3>
        <div class="chart-container">
          <canvas id="incomeChart"></canvas>
        </div>
      </div>

      <div class="transactions">
        <h3>Lịch sử giao dịch</h3>
        <div class="transaction-list">
          <div class="transaction-item">
            <div class="trans-info">
              <i class="fas fa-plus-circle"></i>
              <div>
                <strong>Phí giao hàng #DH12345</strong>
                <span>25/12/2025 14:30</span>
              </div>
            </div>
            <span class="trans-amount positive">+35,000₫</span>
          </div>
          <div class="transaction-item">
            <div class="trans-info">
              <i class="fas fa-plus-circle"></i>
              <div>
                <strong>Tiền thưởng giao nhanh</strong>
                <span>25/12/2025 12:15</span>
              </div>
            </div>
            <span class="trans-amount positive">+20,000₫</span>
          </div>
          <div class="transaction-item">
            <div class="trans-info">
              <i class="fas fa-plus-circle"></i>
              <div>
                <strong>Phí giao hàng #DH12344</strong>
                <span>25/12/2025 10:45</span>
              </div>
            </div>
            <span class="trans-amount positive">+40,000₫</span>
          </div>
        </div>
      </div>
    </section>

    <!-- Ratings Section -->
    <section id="ratings" class="content-section">
      <div class="section-title">
        <h2>Đánh giá & Phản hồi</h2>
      </div>

      <div class="rating-summary">
        <div class="rating-score">
          <div class="score-big">4.8</div>
          <div class="stars">
            <i class="fas fa-star"></i>
            <i class="fas fa-star"></i>
            <i class="fas fa-star"></i>
            <i class="fas fa-star"></i>
            <i class="fas fa-star-half-alt"></i>
          </div>
          <p>245 đánh giá</p>
        </div>
        <div class="rating-breakdown">
          <div class="rating-bar">
            <span>5 ⭐</span>
            <div class="bar">
              <div class="fill" style="width: 85%"></div>
            </div>
            <span>208</span>
          </div>
          <div class="rating-bar">
            <span>4 ⭐</span>
            <div class="bar">
              <div class="fill" style="width: 10%"></div>
            </div>
            <span>25</span>
          </div>
          <div class="rating-bar">
            <span>3 ⭐</span>
            <div class="bar">
              <div class="fill" style="width: 3%"></div>
            </div>
            <span>8</span>
          </div>
          <div class="rating-bar">
            <span>2 ⭐</span>
            <div class="bar">
              <div class="fill" style="width: 1%"></div>
            </div>
            <span>3</span>
          </div>
          <div class="rating-bar">
            <span>1 ⭐</span>
            <div class="bar">
              <div class="fill" style="width: 1%"></div>
            </div>
            <span>1</span>
          </div>
        </div>
      </div>

      <div class="reviews-list">
        <div class="review-card">
          <div class="review-header">
            <img src="https://via.placeholder.com/50" alt="Customer">
            <div class="review-info">
              <strong>Nguyễn Văn A</strong>
              <div class="review-stars">
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
              </div>
              <span class="review-date">25/12/2025</span>
            </div>
          </div>
          <p class="review-text">
            Shipper rất nhiệt tình và giao hàng nhanh chóng. Đóng gói cẩn thận,
            thái độ phục vụ tốt. Sẽ ủng hộ lần sau!
          </p>
          <div class="review-tags">
            <span class="tag">Giao nhanh</span>
            <span class="tag">Nhiệt tình</span>
            <span class="tag">Đóng gói tốt</span>
          </div>
        </div>

        <div class="review-card">
          <div class="review-header">
            <img src="https://via.placeholder.com/50" alt="Customer">
            <div class="review-info">
              <strong>Trần Thị B</strong>
              <div class="review-stars">
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
                <i class="fas fa-star"></i>
              </div>
              <span class="review-date">24/12/2025</span>
            </div>
          </div>
          <p class="review-text">
            Gọi điện trước khi đến rất chuyên nghiệp. Giao đúng giờ hẹn.
            Rất hài lòng với dịch vụ!
          </p>
          <div class="review-tags">
            <span class="tag">Đúng giờ</span>
            <span class="tag">Chuyên nghiệp</span>
          </div>
        </div>
      </div>
    </section>

    <!-- History Section -->
    <section id="history" class="content-section">
      <div class="section-title">
        <h2>Lịch sử giao hàng</h2>
        <input type="date" class="date-picker" id="historyDate">
      </div>

      <div class="history-stats">
        <div class="stat-box">
          <i class="fas fa-box"></i>
          <div>
            <h4>Tổng đơn hàng</h4>
            <p>1,248</p>
          </div>
        </div>
        <div class="stat-box">
          <i class="fas fa-check-circle"></i>
          <div>
            <h4>Thành công</h4>
            <p>1,235 (99%)</p>
          </div>
        </div>
        <div class="stat-box">
          <i class="fas fa-times-circle"></i>
          <div>
            <h4>Thất bại</h4>
            <p>13 (1%)</p>
          </div>
        </div>
      </div>

      <div class="history-table">
        <table>
          <thead>
          <tr>
            <th>Mã đơn</th>
            <th>Khách hàng</th>
            <th>Ngày giao</th>
            <th>Giá trị</th>
            <th>Trạng thái</th>
            <th>Đánh giá</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>#DH12345</td>
            <td>Nguyễn Văn A</td>
            <td>25/12/2025</td>
            <td>2,500,000₫</td>
            <td><span class="status-badge completed">Hoàn thành</span></td>
            <td>⭐⭐⭐⭐⭐</td>
          </tr>
          <tr>
            <td>#DH12344</td>
            <td>Trần Thị B</td>
            <td>25/12/2025</td>
            <td>1,200,000₫</td>
            <td><span class="status-badge completed">Hoàn thành</span></td>
            <td>⭐⭐⭐⭐⭐</td>
          </tr>
          <tr>
            <td>#DH12343</td>
            <td>Lê Văn C</td>
            <td>24/12/2025</td>
            <td>3,500,000₫</td>
            <td><span class="status-badge completed">Hoàn thành</span></td>
            <td>⭐⭐⭐⭐</td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <!-- Profile/Settings Section -->
    <section id="profile" class="content-section">
      <div class="section-title">
        <h2>Cài đặt tài khoản</h2>
      </div>

      <div class="profile-container">
        <div class="profile-card">
          <h3>Thông tin cá nhân</h3>
          <form class="profile-form">
            <div class="form-group">
              <label>Họ và tên</label>
              <input type="text" value="Nguyễn Văn Shipper" class="form-control">
            </div>
            <div class="form-group">
              <label>Số điện thoại</label>
              <input type="tel" value="0123 456 789" class="form-control">
            </div>
            <div class="form-group">
              <label>Email</label>
              <input type="email" value="shipper@viettech.com" class="form-control">
            </div>
            <div class="form-group">
              <label>Số CMND/CCCD</label>
              <input type="text" value="079123456789" class="form-control">
            </div>
            <div class="form-group">
              <label>Biển số xe</label>
              <input type="text" value="59-A1 12345" class="form-control">
            </div>
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-save"></i> Lưu thay đổi
            </button>
          </form>
        </div>

        <div class="profile-card">
          <h3>Cài đặt thông báo</h3>
          <div class="settings-list">
            <div class="setting-item">
              <div class="setting-info">
                <i class="fas fa-bell"></i>
                <span>Thông báo đơn hàng mới</span>
              </div>
              <label class="switch">
                <input type="checkbox" checked>
                <span class="slider"></span>
              </label>
            </div>
            <div class="setting-item">
              <div class="setting-info">
                <i class="fas fa-envelope"></i>
                <span>Email thông báo</span>
              </div>
              <label class="switch">
                <input type="checkbox" checked>
                <span class="slider"></span>
              </label>
            </div>
            <div class="setting-item">
              <div class="setting-info">
                <i class="fas fa-volume-up"></i>
                <span>Âm thanh cảnh báo</span>
              </div>
              <label class="switch">
                <input type="checkbox" checked>
                <span class="slider"></span>
              </label>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/shipper.js"></script>
</body>
</html>

