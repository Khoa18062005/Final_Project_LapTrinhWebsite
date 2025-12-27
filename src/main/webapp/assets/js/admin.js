// Mock Data
let products = [
    { id: 1, name: 'iPhone 15 Pro Max', category: 'Điện thoại', price: 29990000, stock: 45, status: 'active', image: 'https://via.placeholder.com/50?text=iPhone' },
    { id: 2, name: 'Samsung Galaxy S24 Ultra', category: 'Điện thoại', price: 27990000, stock: 32, status: 'active', image: 'https://via.placeholder.com/50?text=Samsung' },
    { id: 3, name: 'MacBook Pro 16"', category: 'Laptop', price: 59990000, stock: 15, status: 'active', image: 'https://via.placeholder.com/50?text=MacBook' },
    { id: 4, name: 'Dell XPS 15', category: 'Laptop', price: 42990000, stock: 20, status: 'active', image: 'https://via.placeholder.com/50?text=Dell' },
    { id: 5, name: 'AirPods Pro 2', category: 'Phụ kiện', price: 5990000, stock: 100, status: 'active', image: 'https://via.placeholder.com/50?text=AirPods' },
    { id: 6, name: 'iPad Pro 12.9"', category: 'Tablet', price: 32990000, stock: 25, status: 'active', image: 'https://via.placeholder.com/50?text=iPad' },
    { id: 7, name: 'Apple Watch Ultra 2', category: 'Phụ kiện', price: 19990000, stock: 18, status: 'active', image: 'https://via.placeholder.com/50?text=Watch' },
    { id: 8, name: 'Sony WH-1000XM5', category: 'Phụ kiện', price: 8990000, stock: 60, status: 'active', image: 'https://via.placeholder.com/50?text=Sony' },
];

let users = [
    { id: 1, name: 'Nguyễn Văn A', email: 'nguyenvana@gmail.com', phone: '0123456789', role: 'Khách hàng', status: 'active', avatar: 'https://via.placeholder.com/50?text=A' },
    { id: 2, name: 'Trần Thị B', email: 'tranthib@gmail.com', phone: '0987654321', role: 'Khách hàng', status: 'active', avatar: 'https://via.placeholder.com/50?text=B' },
    { id: 3, name: 'Lê Văn C', email: 'levanc@gmail.com', phone: '0345678901', role: 'Khách hàng', status: 'active', avatar: 'https://via.placeholder.com/50?text=C' },
    { id: 4, name: 'Phạm Thị D', email: 'phamthid@gmail.com', phone: '0912345678', role: 'Admin', status: 'active', avatar: 'https://via.placeholder.com/50?text=D' },
    { id: 5, name: 'Hoàng Văn E', email: 'hoangvane@gmail.com', phone: '0765432109', role: 'Khách hàng', status: 'inactive', avatar: 'https://via.placeholder.com/50?text=E' },
];

let orders = [
    { id: 'DH001', customer: 'Nguyễn Văn A', date: '2025-12-20', total: 29990000, status: 'completed', payment: 'Đã thanh toán' },
    { id: 'DH002', customer: 'Trần Thị B', date: '2025-12-21', total: 15980000, status: 'processing', payment: 'Đã thanh toán' },
    { id: 'DH003', customer: 'Lê Văn C', date: '2025-12-22', total: 59990000, status: 'pending', payment: 'Chờ thanh toán' },
    { id: 'DH004', customer: 'Phạm Thị D', date: '2025-12-23', total: 8990000, status: 'completed', payment: 'Đã thanh toán' },
    { id: 'DH005', customer: 'Hoàng Văn E', date: '2025-12-24', total: 42990000, status: 'cancelled', payment: 'Đã hoàn tiền' },
];

let categories = [
    { id: 1, name: 'Điện thoại', icon: 'fa-mobile-alt', count: 45, color: '#667eea' },
    { id: 2, name: 'Laptop', icon: 'fa-laptop', count: 32, color: '#f093fb' },
    { id: 3, name: 'Tablet', icon: 'fa-tablet-alt', count: 28, color: '#4facfe' },
    { id: 4, name: 'Phụ kiện', icon: 'fa-headphones', count: 156, color: '#43e97b' },
    { id: 5, name: 'Smartwatch', icon: 'fa-clock', count: 42, color: '#fa709a' },
    { id: 6, name: 'TV & Audio', icon: 'fa-tv', count: 38, color: '#30cfd0' },
];

let reviews = [
    { id: 1, user: 'Nguyễn Văn A', avatar: 'https://via.placeholder.com/50?text=A', product: 'iPhone 15 Pro Max', rating: 5, comment: 'Sản phẩm rất tuyệt vời, giao hàng nhanh!', date: '2025-12-20' },
    { id: 2, user: 'Trần Thị B', avatar: 'https://via.placeholder.com/50?text=B', product: 'MacBook Pro 16"', rating: 5, comment: 'Máy chạy rất mượt, đáng đồng tiền!', date: '2025-12-21' },
    { id: 3, user: 'Lê Văn C', avatar: 'https://via.placeholder.com/50?text=C', product: 'AirPods Pro 2', rating: 4, comment: 'Âm thanh tốt, chống ồn hiệu quả.', date: '2025-12-22' },
    { id: 4, user: 'Phạm Thị D', avatar: 'https://via.placeholder.com/50?text=D', product: 'Samsung Galaxy S24', rating: 5, comment: 'Màn hình đẹp, camera xuất sắc!', date: '2025-12-23' },
];

// Initialize Dashboard
function initDashboard() {
    updateStats();
    renderProducts();
    renderUsers();
    renderOrders();
    renderCategories();
    renderReviews();
    renderTopProducts();
    initCharts();
}

// Update Statistics
function updateStats() {
    document.getElementById('total-products').textContent = products.length;
    document.getElementById('total-users').textContent = users.length;
    document.getElementById('total-orders').textContent = orders.length;

    const totalRevenue = orders
        .filter(order => order.status === 'completed')
        .reduce((sum, order) => sum + order.total, 0);

    document.getElementById('total-revenue').textContent = formatCurrency(totalRevenue);
    document.getElementById('monthRevenue').textContent = formatCurrency(totalRevenue);

    const completedOrders = orders.filter(order => order.status === 'completed').length;
    document.getElementById('completedOrders').textContent = completedOrders;

    const avgOrder = completedOrders > 0 ? totalRevenue / completedOrders : 0;
    document.getElementById('avgOrderValue').textContent = formatCurrency(avgOrder);
}

// Render Products Table
function renderProducts() {
    const tbody = document.getElementById('productsTable');
    tbody.innerHTML = products.map(product => `
        <tr>
            <td>${product.id}</td>
            <td><img src="${product.image}" alt="${product.name}"></td>
            <td>${product.name}</td>
            <td>${product.category}</td>
            <td>${formatCurrency(product.price)}</td>
            <td>${product.stock}</td>
            <td><span class="status-badge ${product.status}">${product.status === 'active' ? 'Hoạt động' : 'Ngừng bán'}</span></td>
            <td>
                <div class="action-buttons">
                    <button class="action-btn view" onclick="viewProduct(${product.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="action-btn edit" onclick="editProduct(${product.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn delete" onclick="deleteProduct(${product.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

// Render Users Table
function renderUsers() {
    const tbody = document.getElementById('usersTable');
    tbody.innerHTML = users.map(user => `
        <tr>
            <td>${user.id}</td>
            <td><img src="${user.avatar}" alt="${user.name}"></td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.phone}</td>
            <td>${user.role}</td>
            <td><span class="status-badge ${user.status}">${user.status === 'active' ? 'Hoạt động' : 'Khóa'}</span></td>
            <td>
                <div class="action-buttons">
                    <button class="action-btn view" onclick="viewUser(${user.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="action-btn edit" onclick="editUser(${user.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn delete" onclick="deleteUser(${user.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

// Render Orders Table
function renderOrders() {
    const tbody = document.getElementById('ordersTable');
    tbody.innerHTML = orders.map(order => `
        <tr>
            <td>${order.id}</td>
            <td>${order.customer}</td>
            <td>${order.date}</td>
            <td>${formatCurrency(order.total)}</td>
            <td><span class="status-badge ${order.status}">${getOrderStatusText(order.status)}</span></td>
            <td>${order.payment}</td>
            <td>
                <div class="action-buttons">
                    <button class="action-btn view" onclick="viewOrder('${order.id}')">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="action-btn edit" onclick="updateOrderStatus('${order.id}')">
                        <i class="fas fa-edit"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

// Render Categories Grid
function renderCategories() {
    const grid = document.getElementById('categoriesGrid');
    grid.innerHTML = categories.map(category => `
        <div class="category-card">
            <div class="category-icon" style="background: ${category.color}">
                <i class="fas ${category.icon}"></i>
            </div>
            <h3>${category.name}</h3>
            <p>${category.count} sản phẩm</p>
            <div class="action-buttons" style="justify-content: center; margin-top: 15px;">
                <button class="action-btn edit" onclick="editCategory(${category.id})">
                    <i class="fas fa-edit"></i> Sửa
                </button>
                <button class="action-btn delete" onclick="deleteCategory(${category.id})">
                    <i class="fas fa-trash"></i> Xóa
                </button>
            </div>
        </div>
    `).join('');
}

// Render Reviews List
function renderReviews() {
    const list = document.getElementById('reviewsList');
    list.innerHTML = reviews.map(review => `
        <div class="review-card">
            <div class="review-header">
                <div class="review-user">
                    <img src="${review.avatar}" alt="${review.user}">
                    <div class="review-user-info">
                        <h4>${review.user}</h4>
                        <small>${review.date}</small>
                    </div>
                </div>
                <div class="review-rating">
                    ${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}
                </div>
            </div>
            <p><strong>Sản phẩm:</strong> ${review.product}</p>
            <p class="review-content">${review.comment}</p>
            <div class="action-buttons" style="margin-top: 10px;">
                <button class="action-btn view" onclick="approveReview(${review.id})">
                    <i class="fas fa-check"></i> Duyệt
                </button>
                <button class="action-btn delete" onclick="deleteReview(${review.id})">
                    <i class="fas fa-trash"></i> Xóa
                </button>
            </div>
        </div>
    `).join('');
}

// Render Top Products
function renderTopProducts() {
    const topProductsData = products.slice(0, 5);
    const container = document.getElementById('topProducts');
    container.innerHTML = topProductsData.map(product => `
        <div class="product-item">
            <div class="product-info">
                <img src="${product.image}" alt="${product.name}">
                <span class="product-name">${product.name}</span>
            </div>
            <span class="product-sales">${product.stock} đã bán</span>
        </div>
    `).join('');
}

// Initialize Charts
function initCharts() {
    // Revenue Chart
    const revenueCtx = document.getElementById('revenueChart');
    if (revenueCtx) {
        const revenueChart = revenueCtx.getContext('2d');
        createRevenueChart(revenueChart);
    }

    // Revenue Detail Chart
    const detailCtx = document.getElementById('revenueDetailChart');
    if (detailCtx) {
        const detailChart = detailCtx.getContext('2d');
        createRevenueDetailChart(detailChart);
    }
}

// Create Revenue Chart (Simple Canvas Drawing)
function createRevenueChart(ctx) {
    const canvas = ctx.canvas;
    const width = canvas.width;
    const height = canvas.height;

    // Sample data
    const data = [15, 25, 20, 35, 30, 45, 40, 55, 50, 60, 55, 65];
    const months = ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'];

    // Clear canvas
    ctx.clearRect(0, 0, width, height);

    // Draw grid
    ctx.strokeStyle = '#e0e0e0';
    ctx.lineWidth = 1;
    for (let i = 0; i <= 5; i++) {
        const y = (height / 5) * i;
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(width, y);
        ctx.stroke();
    }

    // Draw line
    const max = Math.max(...data);
    const barWidth = width / data.length;

    ctx.fillStyle = '#667eea';
    data.forEach((value, index) => {
        const barHeight = (value / max) * (height - 20);
        const x = index * barWidth;
        const y = height - barHeight;
        ctx.fillRect(x + 5, y, barWidth - 10, barHeight);
    });

    // Draw labels
    ctx.fillStyle = '#2c3e50';
    ctx.font = '12px Arial';
    ctx.textAlign = 'center';
    months.forEach((month, index) => {
        const x = index * barWidth + barWidth / 2;
        ctx.fillText(month, x, height - 5);
    });
}

// Create Revenue Detail Chart
function createRevenueDetailChart(ctx) {
    createRevenueChart(ctx); // Reuse same chart for now
}

// Utility Functions
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

function getOrderStatusText(status) {
    const statusMap = {
        'completed': 'Hoàn thành',
        'processing': 'Đang xử lý',
        'pending': 'Chờ xử lý',
        'cancelled': 'Đã hủy'
    };
    return statusMap[status] || status;
}

// Navigation Functions
function showSection(sectionId) {
    // Hide all sections
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });

    // Show selected section
    document.getElementById(sectionId).classList.add('active');

    // Update active nav item
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });
    event.target.closest('.nav-item').classList.add('active');

    // Update page title
    const titles = {
        'dashboard': 'Dashboard',
        'products': 'Quản lý sản phẩm',
        'users': 'Quản lý người dùng',
        'orders': 'Quản lý đơn hàng',
        'revenue': 'Báo cáo doanh thu',
        'categories': 'Quản lý danh mục',
        'reviews': 'Quản lý đánh giá',
        'settings': 'Cài đặt hệ thống'
    };
    document.getElementById('page-title').textContent = titles[sectionId] || 'Dashboard';
}

function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');
    sidebar.classList.toggle('collapsed');
    mainContent.classList.toggle('collapsed');
}

// Modal Funct4ions
function openAddProductModal() {
    const modal = document.getElementById('productModal');
    modal.classList.add('show');
}

function openAddUserModal() {
    alert('Mở form thêm người dùng (chưa implement UI)');
}

function openAddCategoryModal() {
    alert('Mở form thêm danh mục (chưa implement UI)');
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.remove('show');
}

// Product Form Submit
document.addEventListener('DOMContentLoaded', function() {
    const productForm = document.getElementById('productForm');
    if (productForm) {
        productForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const newProduct = {
                id: products.length + 1,
                name: document.getElementById('productName').value,
                category: document.getElementById('productCategory').value,
                price: parseInt(document.getElementById('productPrice').value),
                stock: parseInt(document.getElementById('productStock').value),
                status: 'active',
                image: 'https://via.placeholder.com/50?text=New'
            };

            products.push(newProduct);
            renderProducts();
            updateStats();
            closeModal('productModal');
            productForm.reset();

            alert('Thêm sản phẩm thành công!');
        });
    }

    // Initialize dashboard
    initDashboard();
});

// CRUD Operations
function viewProduct(id) {
    const product = products.find(p => p.id === id);
    alert(`Xem chi tiết sản phẩm:\n\nID: ${product.id}\nTên: ${product.name}\nGiá: ${formatCurrency(product.price)}\nKho: ${product.stock}`);
}

function editProduct(id) {
    alert(`Sửa sản phẩm ID: ${id} (chưa implement form)`);
}

function deleteProduct(id) {
    if (confirm('Bạn có chắc muốn xóa sản phẩm này?')) {
        products = products.filter(p => p.id !== id);
        renderProducts();
        updateStats();
        alert('Đã xóa sản phẩm!');
    }
}

function viewUser(id) {
    const user = users.find(u => u.id === id);
    alert(`Xem chi tiết người dùng:\n\nID: ${user.id}\nTên: ${user.name}\nEmail: ${user.email}\nVai trò: ${user.role}`);
}

function editUser(id) {
    alert(`Sửa người dùng ID: ${id} (chưa implement form)`);
}

function deleteUser(id) {
    if (confirm('Bạn có chắc muốn xóa người dùng này?')) {
        users = users.filter(u => u.id !== id);
        renderUsers();
        updateStats();
        alert('Đã xóa người dùng!');
    }
}

function viewOrder(id) {
    const order = orders.find(o => o.id === id);
    alert(`Xem chi tiết đơn hàng:\n\nMã: ${order.id}\nKhách: ${order.customer}\nTổng: ${formatCurrency(order.total)}\nTrạng thái: ${getOrderStatusText(order.status)}`);
}

function updateOrderStatus(id) {
    alert(`Cập nhật trạng thái đơn hàng ${id} (chưa implement)`);
}

function editCategory(id) {
    alert(`Sửa danh mục ID: ${id} (chưa implement)`);
}

function deleteCategory(id) {
    if (confirm('Bạn có chắc muốn xóa danh mục này?')) {
        categories = categories.filter(c => c.id !== id);
        renderCategories();
        alert('Đã xóa danh mục!');
    }
}

function approveReview(id) {
    alert(`Đã duyệt đánh giá ID: ${id}`);
}

function deleteReview(id) {
    if (confirm('Bạn có chắc muốn xóa đánh giá này?')) {
        reviews = reviews.filter(r => r.id !== id);
        renderReviews();
        alert('Đã xóa đánh giá!');
    }
}

function filterRevenue() {
    const filter = document.getElementById('revenueFilter').value;
    alert(`Lọc doanh thu theo: ${filter} (chưa implement)`);
}

// Close modal when clicking outside
window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.classList.remove('show');
        event.target.style.display = 'none';
    }
    
    // Handle specific modals by ID
    var detailModal = document.getElementById('viewDetailModal');
    var addModal = document.getElementById('productModal');

    if (event.target == detailModal) {
        detailModal.style.display = "none";
    }
    if (event.target == addModal) {
        addModal.style.display = "none";
    }
}

/**
 * Show product details in modal
 * @param {string} sourceId - ID of hidden div containing details (e.g., 'detail-101')
 */
function showProductDetails(sourceId) {
    // 1. Find the hidden div containing detail data
    var sourceContent = document.getElementById(sourceId);

    // 2. Find the display area in Modal
    var targetContent = document.getElementById('viewDetailContent');
    var modal = document.getElementById('viewDetailModal');

    // 3. Check and copy data
    if (sourceContent && targetContent && modal) {
        // Copy all HTML from hidden div to Modal div
        targetContent.innerHTML = sourceContent.innerHTML;

        // Show Modal
        modal.style.display = "block";
        modal.classList.add("show");
    } else {
        console.error("Error: Cannot find detail data or Modal. ID:", sourceId);
        alert("Không thể tải thông tin chi tiết sản phẩm này.");
    }
}

