// Mock Data
let currentOrders = [
    { id: 'DH12345', customer: 'Nguyễn Văn A', phone: '0123456789', address: '123 Đường ABC, Quận 1, TP.HCM', amount: 2500000, type: 'COD', status: 'ongoing', items: 3, distance: 2.5, time: 15 },
    { id: 'DH12346', customer: 'Trần Thị B', phone: '0987654321', address: '456 Đường DEF, Quận 3, TP.HCM', amount: 1200000, type: 'COD', status: 'pending', items: 2, distance: 3.2, time: 20 },
    { id: 'DH12347', customer: 'Lê Văn C', phone: '0345678901', address: '789 Đường GHI, Quận 5, TP.HCM', amount: 3500000, type: 'Online', status: 'ongoing', items: 5, distance: 1.8, time: 12 },
    { id: 'DH12348', customer: 'Phạm Thị D', phone: '0912345678', address: '101 Đường JKL, Quận 7, TP.HCM', amount: 850000, type: 'COD', status: 'completed', items: 1, completedTime: '14:30' },
];

// Cấu trúc lại incomeData để hỗ trợ biểu đồ động
let incomeData = {
    today: {
        total: 320000,
        chartData: [0, 0, 0, 320000, 0, 0, 0], // Dữ liệu tập trung vào hôm nay (T5)
        labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN']
    },
    week: {
        total: 2150000,
        chartData: [280000, 310000, 295000, 340000, 315000, 330000, 320000],
        labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN']
    },
    month: {
        total: 8450000,
        chartData: [1800000, 2100000, 2300000, 2250000], // 4 tuần trong tháng
        labels: ['Tuần 1', 'Tuần 2', 'Tuần 3', 'Tuần 4']
    }
};

let reviews = [
    { customer: 'Nguyễn Văn A', avatar: 'https://via.placeholder.com/50', rating: 5, comment: 'Shipper rất nhiệt tình và giao hàng nhanh chóng. Đóng gói cẩn thận, thái độ phục vụ tốt. Sẽ ủng hộ lần sau!', tags: ['Giao nhanh', 'Nhiệt tình', 'Đóng gói tốt'], date: '25/12/2025' },
    { customer: 'Trần Thị B', avatar: 'https://via.placeholder.com/50', rating: 5, comment: 'Gọi điện trước khi đến rất chuyên nghiệp. Giao đúng giờ hẹn. Rất hài lòng với dịch vụ!', tags: ['Đúng giờ', 'Chuyên nghiệp'], date: '24/12/2025' },
];

// Initialize Dashboard
function initShipperDashboard() {
    updateCurrentDate();
    updateStats();
    updateOnlineStatus();
    setInterval(updateCurrentDate, 60000); // Update every minute
}

// Update Current Date
function updateCurrentDate() {
    const dateElement = document.getElementById('currentDate');
    if (dateElement) {
        const now = new Date();
        const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        dateElement.textContent = now.toLocaleDateString('vi-VN', options);
    }
}

// Update Statistics
function updateStats() {
    // Today orders
    const todayTotal = currentOrders.length;
    const ongoing = currentOrders.filter(o => o.status === 'ongoing').length;
    const completed = currentOrders.filter(o => o.status === 'completed').length;

    document.getElementById('todayOrders').textContent = todayTotal;
    document.getElementById('successOrders').textContent = completed;
    document.getElementById('todayIncome').innerHTML = `${formatCurrency(incomeData.today)} <small>₫</small>`;

    // Update orders count badge
    const ordersCount = document.getElementById('ordersCount');
    if (ordersCount) {
        ordersCount.textContent = currentOrders.filter(o => o.status !== 'completed').length;
    }
}

// Format Currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN').format(amount);
}

// Show Section
function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');

    document.querySelectorAll('.nav-item').forEach(item => item.classList.remove('active'));
    const activeNav = document.querySelector(`.nav-item[onclick*="${sectionId}"]`);
    if (activeNav) activeNav.classList.add('active');

    if (sectionId === 'income') {
        // Mặc định hiển thị biểu đồ tháng khi vừa mở tab
        setTimeout(() => {
            filterIncome('month');
        }, 100);
    }
}

// Online Status Toggle
function updateOnlineStatus() {
    const checkbox = document.getElementById('onlineStatus');
    const statusText = document.getElementById('statusText');

    if (checkbox) {
        checkbox.addEventListener('change', function() {
            if (this.checked) {
                statusText.textContent = 'Đang hoạt động';
                statusText.style.color = '#2ecc71';
                showNotification('Bạn đã bật trạng thái hoạt động', 'success');
            } else {
                statusText.textContent = 'Đang nghỉ';
                statusText.style.color = '#e74c3c';
                showNotification('Bạn đã tắt trạng thái hoạt động', 'info');
            }
        });
    }
}

// Filter Orders
function filterOrders(status) {
    // Update active tab
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');

    // Filter logic would go here
    console.log('Filtering orders by:', status);
    showNotification(`Đã lọc đơn hàng: ${status}`, 'info');
}

// Accept Order
function acceptOrder(orderId) {
    if (confirm(`Bạn có chắc muốn nhận đơn hàng ${orderId}?`)) {
        const order = currentOrders.find(o => o.id === orderId);
        if (order) {
            order.status = 'ongoing';
            showNotification(`Đã nhận đơn hàng ${orderId}`, 'success');
            updateStats();
        }
    }
}

// View Order Detail
function viewOrderDetail(orderId) {
    const order = currentOrders.find(o => o.id === orderId);
    if (order) {
        alert(`Chi tiết đơn hàng ${orderId}\n\nKhách hàng: ${order.customer}\nĐịa chỉ: ${order.address}\nSố tiền: ${formatCurrency(order.amount)}₫\nTrạng thái: ${order.status}`);
    }
}

// Confirm Delivery
function confirmDelivery() {
    const confirmation = confirm('Xác nhận giao hàng thành công?\n\nVui lòng kiểm tra:\n✓ Khách hàng đã nhận hàng\n✓ Đã thu tiền (nếu COD)\n✓ Khách hàng đã ký nhận');

    if (confirmation) {
        showNotification('Đã xác nhận giao hàng thành công!', 'success');
        // Update order status
        setTimeout(() => {
            alert('⭐ Đơn hàng hoàn thành!\n\nThu nhập: +35,000₫\nBạn đã kiếm được tiền thưởng giao nhanh: +20,000₫');
        }, 500);
    }
}

// Call Customer
function callCustomer() {
    showNotification('Đang gọi cho khách hàng...', 'info');
    setTimeout(() => {
        alert('📞 Đang kết nối với khách hàng...');
    }, 500);
}

// Open Map
function openMap() {
    showNotification('Đang mở bản đồ chỉ đường...', 'info');
    // Navigate to map section
    showSection('map');
}

// Update Location
function updateLocation() {
    showNotification('Đang cập nhật vị trí...', 'info');

    // Simulate location update
    setTimeout(() => {
        showNotification('Đã cập nhật vị trí thành công!', 'success');
    }, 1000);
}

// Quick Actions
function scanQR() {
    showNotification('Đang mở camera quét QR...', 'info');
    setTimeout(() => {
        alert('📱 Chức năng quét QR code sẽ được tích hợp sau');
    }, 500);
}

function reportIssue() {
    const issue = prompt('Mô tả sự cố:\n\nVí dụ: Khách hàng không nghe máy, địa chỉ không chính xác, v.v.');
    if (issue) {
        showNotification('Đã gửi báo cáo sự cố!', 'success');
        console.log('Issue reported:', issue);
    }
}

function takeBreak() {
    const confirm = window.confirm('Bạn muốn nghỉ giải lao?\n\nTrạng thái của bạn sẽ chuyển sang "Đang nghỉ" và bạn sẽ không nhận đơn hàng mới.');
    if (confirm) {
        document.getElementById('onlineStatus').checked = false;
        document.getElementById('statusText').textContent = 'Đang nghỉ';
        showNotification('Chúc bạn nghỉ ngơi vui vẻ! ☕', 'info');
    }
}

function viewSchedule() {
    alert('📅 Lịch trình của bạn:\n\n• 08:00-12:00: Ca sáng (4 đơn)\n• 12:00-13:00: Nghỉ trưa\n• 13:00-17:00: Ca chiều (5 đơn)\n• 17:00-20:00: Ca tối (3 đơn)');
}

// Filter Income
function filterIncome(period) {
    const totalDisplay = document.getElementById('totalIncomeDisplay');
    const periodText = document.getElementById('incomePeriodText');
    const changeDisplay = document.getElementById('incomeChangeDisplay');

    // Lấy dữ liệu từ object theo key (today/week/month)
    const dataObj = incomeData[period];

    let text = "";
    let changeHtml = "";

    switch(period) {
        case 'today':
            text = "hôm nay";
            changeHtml = `<i class="fas fa-arrow-up"></i> +5% so với hôm qua`;
            break;
        case 'week':
            text = "tuần này";
            changeHtml = `<i class="fas fa-arrow-up"></i> +10% so với tuần trước`;
            break;
        case 'month':
            text = "tháng này";
            changeHtml = `<i class="fas fa-arrow-up"></i> +15.5% so với tháng trước`;
            break;
    }

    // 1. Cập nhật con số tổng
    if (totalDisplay) totalDisplay.textContent = formatCurrency(dataObj.total) + "₫";
    if (periodText) periodText.textContent = text;
    if (changeDisplay) changeDisplay.innerHTML = changeHtml;

    // 2. QUAN TRỌNG: Vẽ lại biểu đồ với dữ liệu mới
    drawIncomeChart(dataObj.chartData, dataObj.labels);

    showNotification(`Đã cập nhật dữ liệu cho ${text}`, 'success');
}

// View Receipt
function viewReceipt(orderId) {
    alert(`🧾 Biên lai giao hàng\n\nMã đơn: ${orderId}\nNgười giao: Nguyễn Văn Shipper\nThời gian: 25/12/2025 14:30\nTrạng thái: Hoàn thành\n\n✓ Khách hàng đã ký nhận\n✓ Đã thu tiền COD: 2,500,000₫\n✓ Phí giao hàng: 35,000₫`);
}

// Notification System
function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <i class="fas ${type === 'success' ? 'fa-check-circle' : type === 'error' ? 'fa-times-circle' : 'fa-info-circle'}"></i>
        <span>${message}</span>
    `;

    // Add styles
    notification.style.position = 'fixed';
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.padding = '15px 20px';
    notification.style.borderRadius = '8px';
    notification.style.boxShadow = '0 4px 12px rgba(0,0,0,0.15)';
    notification.style.display = 'flex';
    notification.style.alignItems = 'center';
    notification.style.gap = '10px';
    notification.style.zIndex = '9999';
    notification.style.animation = 'slideIn 0.3s ease';
    notification.style.minWidth = '250px';
    notification.style.fontWeight = '500';

    // Set colors based on type
    if (type === 'success') {
        notification.style.background = '#2ecc71';
        notification.style.color = 'white';
    } else if (type === 'error') {
        notification.style.background = '#e74c3c';
        notification.style.color = 'white';
    } else {
        notification.style.background = '#3498db';
        notification.style.color = 'white';
    }

    // Add to body
    document.body.appendChild(notification);

    // Remove after 3 seconds
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// Add animation styles
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// Simple Income Chart (Canvas)
function drawIncomeChart(dataArray, labelArray) {
    const canvas = document.getElementById('incomeChart');
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    const parent = canvas.parentElement;

    // Đảm bảo lấy được kích thước thực tế
    const width = canvas.width = parent.offsetWidth || 300;
    const height = canvas.height = 200;

    // Nếu không có dữ liệu truyền vào, dùng mặc định của tháng
    const data = dataArray || incomeData.month.chartData;
    const labels = labelArray || incomeData.month.labels;

    const max = Math.max(...data) * 1.3; // Khoảng trống cho nhãn k trên đầu

    ctx.clearRect(0, 0, width, height);

    // Vẽ lưới
    ctx.strokeStyle = '#f0f0f0';
    ctx.lineWidth = 1;
    for (let i = 0; i <= 4; i++) {
        const y = (height - 30) / 4 * i;
        ctx.beginPath(); ctx.moveTo(0, y); ctx.lineTo(width, y); ctx.stroke();
    }

    // Vẽ cột
    const padding = 20;
    const barAreaWidth = width - (padding * 2);
    const barWidth = barAreaWidth / data.length;

    data.forEach((value, index) => {
        const barHeight = (value / max) * (height - 40);
        const x = padding + (index * barWidth);
        const y = height - barHeight - 25;

        // Vẽ cột xanh bo góc
        ctx.fillStyle = '#667eea';
        ctx.beginPath();
        if (ctx.roundRect) {
            ctx.roundRect(x + 5, y, barWidth - 10, barHeight, [4, 4, 0, 0]);
        } else {
            ctx.fillRect(x + 5, y, barWidth - 10, barHeight); // Fallback
        }
        ctx.fill();

        // Vẽ nhãn trục X
        ctx.fillStyle = '#9ca3af';
        ctx.font = 'bold 11px Arial';
        ctx.textAlign = 'center';
        ctx.fillText(labels[index], x + barWidth / 2, height - 5);

        // Vẽ giá trị (viết tắt k)
        ctx.fillStyle = '#4b5563';
        ctx.font = '10px Arial';
        const displayVal = value >= 1000 ? (value/1000).toFixed(0) + 'k' : value;
        ctx.fillText(displayVal, x + barWidth / 2, y - 5);
    });
}

// Initialize when page loads
document.addEventListener('DOMContentLoaded', function() {
    initShipperDashboard();

    // Draw chart if on income section
    setTimeout(() => {
        drawIncomeChart();
    }, 100);

    // Redraw chart on window resize
    window.addEventListener('resize', () => {
        const canvas = document.getElementById('incomeChart');
        if (canvas && document.getElementById('income').classList.contains('active')) {
            drawIncomeChart();
        }
    });

    // Show welcome notification
    setTimeout(() => {
        showNotification('Chào mừng bạn trở lại! Bạn có 3 đơn hàng mới.', 'info');
    }, 1000);
});

// Simulate new order notification
setInterval(() => {
    const random = Math.random();
    if (random > 0.95 && document.getElementById('onlineStatus').checked) {
        playNotificationSound();
        showNotification('🔔 Bạn có đơn hàng mới! Kiểm tra ngay.', 'success');

        // Update badge
        const badge = document.getElementById('ordersCount');
        if (badge) {
            badge.textContent = parseInt(badge.textContent) + 1;
        }
    }
}, 30000); // Check every 30 seconds

// Notification Sound
function playNotificationSound() {
    // Create audio context for notification sound
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const oscillator = audioContext.createOscillator();
    const gainNode = audioContext.createGain();

    oscillator.connect(gainNode);
    gainNode.connect(audioContext.destination);

    oscillator.frequency.value = 800;
    oscillator.type = 'sine';

    gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
    gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);

    oscillator.start(audioContext.currentTime);
    oscillator.stop(audioContext.currentTime + 0.5);
}

console.log('🚚 Shipper Dashboard Loaded Successfully!');

