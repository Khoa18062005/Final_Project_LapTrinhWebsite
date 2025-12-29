<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
  .time-filter a {
    display: inline-block;
    padding: 5px 15px;
    margin-right: 5px;
    border: 1px solid #ddd;
    border-radius: 20px;
    color: #555;
    text-decoration: none;
    font-size: 0.9rem;
    transition: all 0.3s;
  }
  .time-filter a.active, .time-filter a:hover {
    background-color: #3498db;
    color: white;
    border-color: #3498db;
  }
  .chart-grid {
    display: flex; gap: 20px; flex-wrap: wrap; margin-top: 20px;
  }
  .chart-box {
    background: white; padding: 20px; border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.05);
    flex: 1; min-width: 400px;
  }
</style>

<div class="stats-grid">
  <div class="stat-card"><div class="stat-details"><h3>Doanh thu (${currentDays} ngày)</h3><p class="stat-number"><fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/></p></div></div>
  <div class="stat-card"><div class="stat-details"><h3>Người dùng</h3><p class="stat-number">${totalUsers}</p></div></div>
  <div class="stat-card"><div class="stat-details"><h3>Sản phẩm</h3><p class="stat-number">${totalProducts}</p></div></div>
</div>

<div class="chart-grid">

  <div class="chart-box" style="flex: 2;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
      <h3 style="margin: 0; color: #333;">📈 Biểu đồ doanh thu</h3>
      <div class="time-filter">
        <a href="?days=7" class="${currentDays == 7 ? 'active' : ''}">7 ngày</a>
        <a href="?days=30" class="${currentDays == 30 ? 'active' : ''}">1 tháng</a>
        <a href="?days=90" class="${currentDays == 90 ? 'active' : ''}">3 tháng</a>
      </div>
    </div>
    <div style="height: 300px;"><canvas id="revenueChart"></canvas></div>
  </div>

  <div class="chart-box" style="flex: 1;">
    <h3 style="margin-bottom: 15px; color: #333;">🍰 Tỷ trọng danh mục (Số lượng)</h3>
    <div style="height: 300px;"><canvas id="categoryChart"></canvas></div>
  </div>
</div>

<div class="chart-box" style="margin-top: 20px;">
  <h3 style="margin-bottom: 15px; color: #333;">🏆 Top 5 Sản phẩm bán chạy nhất</h3>
  <table class="data-table">
    <thead><tr><th>Tên sản phẩm</th><th>Đã bán</th><th>Doanh thu ước tính</th></tr></thead>
    <tbody>
    <c:forEach var="tp" items="${topProducts}">
      <tr>
        <td>${tp.name}</td>
        <td><strong>${tp.quantity}</strong></td>
        <td><fmt:formatNumber value="${tp.revenue}" type="currency" currencySymbol="₫"/></td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<script>
  // 1. Biểu đồ Doanh thu (Line)
  new Chart(document.getElementById('revenueChart'), {
    type: 'line',
    data: {
      labels: ${chartLabels},
      datasets: [{
        label: 'Doanh thu (VNĐ)',
        data: ${chartData},
        borderColor: '#3498db', backgroundColor: 'rgba(52, 152, 219, 0.1)',
        borderWidth: 2, tension: 0.3, fill: true
      }]
    },
    options: {
      responsive: true, maintainAspectRatio: false,
      plugins: { legend: { display: false } },
      scales: { y: { beginAtZero: true } }
    }
  });

  // 2. Biểu đồ Danh mục (Doughnut)
  new Chart(document.getElementById('categoryChart'), {
    type: 'doughnut',
    data: {
      labels: ${catLabels},
      datasets: [{
        data: ${catData},
        backgroundColor: ['#e74c3c', '#3498db', '#f1c40f', '#2ecc71', '#9b59b6'],
        borderWidth: 1
      }]
    },
    options: {
      responsive: true, maintainAspectRatio: false,
      plugins: { legend: { position: 'bottom' } }
    }
  });
</script>