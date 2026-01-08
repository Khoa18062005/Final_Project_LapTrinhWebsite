<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<!-- Dashboard Stats Grid -->
<div class="stats-grid">
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon green">
                <i class="fas fa-dollar-sign"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Doanh thu (${currentDays} ngày)</h3>
            <p class="stat-number"><fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/></p>
            <div class="stat-change positive">
                <i class="fas fa-arrow-up"></i>
                <span>12.5%</span>
                <span class="text">so với kỳ trước</span>
            </div>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon blue">
                <i class="fas fa-users"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Người dùng</h3>
            <p class="stat-number">${totalUsers != null ? totalUsers : 0}</p>
            <div class="stat-change positive">
                <i class="fas fa-arrow-up"></i>
                <span>8.2%</span>
                <span class="text">so với tháng trước</span>
            </div>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon purple">
                <i class="fas fa-box"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Sản phẩm</h3>
            <p class="stat-number">${totalProducts != null ? totalProducts : 0}</p>
            <div class="stat-change positive">
                <i class="fas fa-arrow-up"></i>
                <span>24</span>
                <span class="text">sản phẩm mới</span>
            </div>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon orange">
                <i class="fas fa-shopping-cart"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Đơn hàng</h3>
            <p class="stat-number">${totalOrders != null ? totalOrders : 0}</p>
            <div class="stat-change positive">
                <i class="fas fa-arrow-up"></i>
                <span>15.3%</span>
                <span class="text">so với tuần trước</span>
            </div>
        </div>
    </div>
</div>

<!-- Charts Grid -->
<div class="chart-grid">
    <!-- Revenue Chart -->
    <div class="chart-box" style="flex: 2; min-width: 400px;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <h3 style="margin: 0; color: var(--text-primary); font-size: 18px; font-weight: 600;">
                <i class="fas fa-chart-line" style="color: var(--primary-color, #0d6efd); margin-right: 8px;"></i>
                Biểu đồ doanh thu
            </h3>
            <div class="time-filter">
                <a href="?days=7" class="${currentDays == 7 ? 'active' : ''}">7 ngày</a>
                <a href="?days=30" class="${currentDays == 30 ? 'active' : ''}">1 tháng</a>
                <a href="?days=90" class="${currentDays == 90 ? 'active' : ''}">3 tháng</a>
            </div>
        </div>
        <div style="height: 300px; position: relative;">
            <canvas id="revenueChart"></canvas>
        </div>
    </div>

    <!-- Category Chart -->
    <div class="chart-box" style="flex: 1; min-width: 300px;">
        <h3 style="margin-bottom: 20px; color: var(--text-primary); font-size: 18px; font-weight: 600;">
            <i class="fas fa-chart-pie" style="color: var(--cta-500, #f59e0b); margin-right: 8px;"></i>
            Tỷ trọng danh mục
        </h3>
        <div style="height: 300px; position: relative;">
            <canvas id="categoryChart"></canvas>
        </div>
    </div>
</div>

<!-- Top Products Table -->
<div class="chart-box" style="margin-top: 24px;">
    <h3 style="margin-bottom: 20px; color: var(--text-primary); font-size: 18px; font-weight: 600;">
        <i class="fas fa-trophy" style="color: var(--warning, #ffc107); margin-right: 8px;"></i>
        Top 5 Sản phẩm bán chạy nhất
    </h3>
    <div class="table-container" style="box-shadow: none; border: none;">
        <table class="data-table">
            <thead>
                <tr>
                    <th style="width: 50px;">#</th>
                    <th>Tên sản phẩm</th>
                    <th style="text-align: center;">Đã bán</th>
                    <th style="text-align: right;">Doanh thu</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="tp" items="${topProducts}" varStatus="status">
                    <tr>
                        <td>
                            <span style="
                                width: 28px;
                                height: 28px;
                                border-radius: 50%;
                                display: inline-flex;
                                align-items: center;
                                justify-content: center;
                                font-size: 12px;
                                font-weight: 700;
                                background: ${status.index == 0 ? '#ffc107' : status.index == 1 ? '#999999' : status.index == 2 ? '#CD7F32' : '#f8f9fa'};
                                color: ${status.index < 3 ? 'white' : '#666666'};
                            ">
                                ${status.index + 1}
                            </span>
                        </td>
                        <td>
                            <strong>${tp.name}</strong>
                        </td>
                        <td style="text-align: center;">
                            <span style="
                                background: rgba(40, 167, 69, 0.1);
                                color: #28a745;
                                padding: 4px 12px;
                                border-radius: 20px;
                                font-weight: 600;
                                font-size: 13px;
                            ">
                                ${tp.quantity} đã bán
                            </span>
                        </td>
                        <td style="text-align: right;">
                            <strong style="color: var(--primary-color, #0d6efd);">
                                <fmt:formatNumber value="${tp.revenue}" type="currency" currencySymbol="₫"/>
                            </strong>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty topProducts}">
                    <tr>
                        <td colspan="4" style="text-align: center; padding: 32px;">
                            <i class="fas fa-chart-bar" style="font-size: 32px; color: var(--text-muted); margin-bottom: 12px; display: block;"></i>
                            <p style="color: var(--text-muted); margin: 0;">Chưa có dữ liệu sản phẩm bán chạy</p>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
    // Chart.js Configuration
    Chart.defaults.font.family = "'Open Sans', sans-serif";
    Chart.defaults.color = '#64748B';

    // Revenue Line Chart
    const revenueCtx = document.getElementById('revenueChart');
    if (revenueCtx) {
        new Chart(revenueCtx, {
            type: 'line',
            data: {
                labels: ${chartLabels != null ? chartLabels : '[]'},
                datasets: [{
                    label: 'Doanh thu (VNĐ)',
                    data: ${chartData != null ? chartData : '[]'},
                    borderColor: '#2563EB',
                    backgroundColor: 'rgba(37, 99, 235, 0.1)',
                    borderWidth: 2,
                    tension: 0.4,
                    fill: true,
                    pointBackgroundColor: '#2563EB',
                    pointBorderColor: '#ffffff',
                    pointBorderWidth: 2,
                    pointRadius: 4,
                    pointHoverRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        backgroundColor: '#1E293B',
                        titleFont: { size: 13 },
                        bodyFont: { size: 12 },
                        padding: 12,
                        cornerRadius: 8,
                        displayColors: false,
                        callbacks: {
                            label: function(context) {
                                return new Intl.NumberFormat('vi-VN', {
                                    style: 'currency',
                                    currency: 'VND'
                                }).format(context.raw);
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        grid: { display: false },
                        ticks: { font: { size: 11 } }
                    },
                    y: {
                        beginAtZero: true,
                        grid: {
                            color: '#E2E8F0',
                            drawBorder: false
                        },
                        ticks: {
                            font: { size: 11 },
                            callback: function(value) {
                                if (value >= 1000000) {
                                    return (value / 1000000) + 'M';
                                } else if (value >= 1000) {
                                    return (value / 1000) + 'K';
                                }
                                return value;
                            }
                        }
                    }
                },
                interaction: {
                    intersect: false,
                    mode: 'index'
                }
            }
        });
    }

    // Category Doughnut Chart
    const categoryCtx = document.getElementById('categoryChart');
    if (categoryCtx) {
        new Chart(categoryCtx, {
            type: 'doughnut',
            data: {
                labels: ${catLabels != null ? catLabels : '["Điện thoại", "Laptop", "Tablet", "Phụ kiện"]'},
                datasets: [{
                    data: ${catData != null ? catData : '[35, 25, 20, 20]'},
                    backgroundColor: [
                        '#2563EB',  // Primary Blue
                        '#F97316',  // Orange
                        '#A855F7',  // Purple
                        '#22C55E',  // Green
                        '#EAB308'   // Yellow
                    ],
                    borderWidth: 0,
                    hoverOffset: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '65%',
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 16,
                            usePointStyle: true,
                            pointStyle: 'circle',
                            font: { size: 12 }
                        }
                    },
                    tooltip: {
                        backgroundColor: '#1E293B',
                        titleFont: { size: 13 },
                        bodyFont: { size: 12 },
                        padding: 12,
                        cornerRadius: 8
                    }
                }
            }
        });
    }
</script>

