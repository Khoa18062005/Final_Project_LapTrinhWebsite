// ===== CRITICAL FUNCTIONS - MUST BE LOADED FIRST =====

// Get context path from page or default
function getContextPath() {
    if (typeof contextPath !== 'undefined') {
        return contextPath;
    }
    const path = window.location.pathname;
    const contextEnd = path.indexOf('/', 1);
    return contextEnd > 0 ? path.substring(0, contextEnd) : '';
}

// Toggle Notifications
function toggleNotifications() {
    console.log('Toggle notifications');
}

// Toggle Sidebar
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('main-content');
    if (sidebar) sidebar.classList.toggle('collapsed');
    if (mainContent) mainContent.classList.toggle('expanded');
}

// Navigation - Show Section
function showSection(sectionId, clickedItem) {
    if (!sectionId) return false;

    const targetSection = document.getElementById(sectionId);
    if (!targetSection) {
        console.warn('Section not found:', sectionId);
        // Try to find any content-section and show the first one (dashboard) as fallback
        const fallbackSection = document.getElementById('dashboard');
        if (fallbackSection) {
            console.log('Falling back to dashboard section');
            return showSection('dashboard', document.querySelector('a[href="#dashboard"]'));
        }
        return false;
    }

    // Hide all sections first
    document.querySelectorAll('.content-section').forEach(section => {
        if (section) section.classList.remove('active');
    });

    // Show target section
    targetSection.classList.add('active');

    // Update nav items
    document.querySelectorAll('.nav-item').forEach(item => {
        if (item) item.classList.remove('active');
    });
    if (clickedItem && clickedItem.classList) {
        clickedItem.classList.add('active');
    }

    const titles = {
        'dashboard': 'Dashboard', 'products': 'Sản phẩm', 'vouchers': 'Voucher',
        'users': 'Người dùng', 'orders': 'Đơn hàng', 'reviews': 'Đánh giá',
        'settings': 'Cài đặt', 'flash-sales': 'Flash Sale', 'chatbot': 'AI Chatbot'
    };
    const pageTitle = document.getElementById('page-title');
    if (pageTitle) pageTitle.textContent = titles[sectionId] || 'Dashboard';

    if (history.pushState) history.pushState(null, null, '#' + sectionId);
    return false;
}

// ===== CHATBOT FUNCTIONS =====

function sendMessage() {
    const input = document.getElementById('user-input');
    if (!input) return;

    const message = input.value.trim();
    if (!message) return;

    addChatMessage(message, 'user');
    input.value = '';
    showTypingIndicator();

    const ctx = getContextPath();
    fetch(ctx + '/admin/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ question: message })
    })
    .then(response => response.json())
    .then(data => {
        hideTypingIndicator();
        addChatMessage(data.success ? data.response : (data.response || 'Lỗi xảy ra!'), 'bot');
    })
    .catch(error => {
        hideTypingIndicator();
        console.error('Error:', error);
        addChatMessage('Không thể kết nối đến server!', 'bot');
    });
}

function sendQuickMessage(message) {
    const input = document.getElementById('user-input');
    if (input) {
        input.value = message;
        sendMessage();
    }
}

function addChatMessage(content, type) {
    const chatBox = document.getElementById('chat-box');
    if (!chatBox) return;

    const messageDiv = document.createElement('div');
    messageDiv.className = 'message ' + type;
    const now = new Date();
    const timeStr = now.getHours().toString().padStart(2, '0') + ':' + now.getMinutes().toString().padStart(2, '0');

    if (type === 'bot') {
        messageDiv.innerHTML = '<div class="message-avatar"><i class="fas fa-robot"></i></div>' +
            '<div class="message-bubble"><div class="message-content">' + formatChatMessage(content) + '</div>' +
            '<div class="message-time">' + timeStr + '</div></div>';
    } else {
        messageDiv.innerHTML = '<div class="message-bubble"><div class="message-content">' + escapeHtmlChat(content) + '</div>' +
            '<div class="message-time">' + timeStr + '</div></div>' +
            '<div class="message-avatar"><i class="fas fa-user"></i></div>';
    }
    chatBox.appendChild(messageDiv);
    chatBox.scrollTop = chatBox.scrollHeight;
}

function showTypingIndicator() {
    const chatBox = document.getElementById('chat-box');
    if (!chatBox || document.getElementById('typing-indicator')) return;

    const typingDiv = document.createElement('div');
    typingDiv.id = 'typing-indicator';
    typingDiv.className = 'message bot';
    typingDiv.innerHTML = '<div class="message-avatar"><i class="fas fa-robot"></i></div>' +
        '<div class="typing-indicator"><span class="typing-text">Đang trả lời</span>' +
        '<div class="typing-dots"><div class="typing-dot"></div><div class="typing-dot"></div><div class="typing-dot"></div></div></div>';
    chatBox.appendChild(typingDiv);
    chatBox.scrollTop = chatBox.scrollHeight;

    const sendBtn = document.getElementById('sendBtn');
    if (sendBtn) sendBtn.disabled = true;
}

function hideTypingIndicator() {
    const typing = document.getElementById('typing-indicator');
    if (typing) typing.remove();
    const sendBtn = document.getElementById('sendBtn');
    if (sendBtn) sendBtn.disabled = false;
}

function escapeHtmlChat(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function formatChatMessage(text) {
    return escapeHtmlChat(text).replace(/\n/g, '<br>');
}

function clearChatHistory() {
    if (confirm('Bạn có chắc muốn xóa lịch sử chat?')) {
        const chatBox = document.getElementById('chat-box');
        if (chatBox) {
            chatBox.innerHTML = '<div class="message bot"><div class="message-avatar"><i class="fas fa-robot"></i></div>' +
                '<div class="message-bubble"><div class="message-content"><p>Lịch sử chat đã được xóa!</p></div>' +
                '<div class="message-time">Vừa xong</div></div></div>';
        }
    }
}

function minimizeChat() {
    const container = document.querySelector('.chatbot-container');
    if (container) container.classList.toggle('minimized');
}

function expandChat() {
    const container = document.querySelector('.chatbot-container');
    if (container) container.classList.toggle('expanded');
}

// Modal Functions
function openAddProductModal() {
    const modal = document.getElementById('productModal');
    if (modal) modal.classList.add('show');
}

function openAddUserModal() {
    alert('Chức năng thêm người dùng đang được phát triển.');
}

function openAddCategoryModal() {
    alert('Chức năng thêm danh mục đang được phát triển.');
}

function openAddVoucherModal() {
    const modal = document.getElementById('voucherModal');
    if (modal) modal.classList.add('show');
    else alert('Chức năng thêm voucher đang được phát triển.');
}

function openAddFlashSaleModal() {
    const modal = document.getElementById('flashSaleModal');
    if (modal) modal.classList.add('show');
    else alert('Chức năng tạo Flash Sale đang được phát triển.');
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.classList.remove('show');
}

// ===== END CRITICAL FUNCTIONS =====



// Initialize Dashboard
function initDashboard() {
    try {
        updateStats();
        renderProducts();
        renderUsers();
        renderOrders();
        renderCategories();
        renderReviews();
        renderTopProducts();
        initCharts();
    } catch (e) {
        console.log('Dashboard init - some elements may not exist:', e.message);
    }
}

// Update Statistics
function updateStats() {
    const totalProductsEl = document.getElementById('total-products');
    const totalUsersEl = document.getElementById('total-users');
    const totalOrdersEl = document.getElementById('total-orders');
    const totalRevenueEl = document.getElementById('total-revenue');
    const monthRevenueEl = document.getElementById('monthRevenue');
    const completedOrdersEl = document.getElementById('completedOrders');
    const avgOrderValueEl = document.getElementById('avgOrderValue');

    if (totalProductsEl) totalProductsEl.textContent = products.length;
    if (totalUsersEl) totalUsersEl.textContent = users.length;
    if (totalOrdersEl) totalOrdersEl.textContent = orders.length;

    const totalRevenue = orders
        .filter(order => order.status === 'completed')
        .reduce((sum, order) => sum + order.total, 0);

    if (totalRevenueEl) totalRevenueEl.textContent = formatCurrency(totalRevenue);
    if (monthRevenueEl) monthRevenueEl.textContent = formatCurrency(totalRevenue);

    const completedOrders = orders.filter(order => order.status === 'completed').length;
    if (completedOrdersEl) completedOrdersEl.textContent = completedOrders;

    const avgOrder = completedOrders > 0 ? totalRevenue / completedOrders : 0;
    if (avgOrderValueEl) avgOrderValueEl.textContent = formatCurrency(avgOrder);
}

// Render Products Table
function renderProducts() {
    const tbody = document.getElementById('productsTable');
    if (!tbody) return;

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
    if (!tbody) return;

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
    if (!tbody) return;

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
    if (!grid) return;

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
    if (!list) return;

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
    const container = document.getElementById('topProducts');
    if (!container) return;

    const topProductsData = products.slice(0, 5);
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

// Product Form Submit
document.addEventListener('DOMContentLoaded', function() {
    // Product Form Handler
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

    // Check URL hash on page load for navigation
    const hash = window.location.hash.replace('#', '');
    if (hash) {
        const navItem = document.querySelector('a[href="#' + hash + '"]');
        const targetSection = document.getElementById(hash);
        if (navItem && targetSection) {
            showSection(hash, navItem);
        }
    }

    // Handle browser back/forward buttons
    window.addEventListener('popstate', function() {
        const hash = window.location.hash.replace('#', '');
        if (hash) {
            const navItem = document.querySelector('a[href="#' + hash + '"]');
            if (navItem) {
                showSection(hash, navItem);
            }
        } else {
            // Default to dashboard if no hash
            const dashboardNav = document.querySelector('a[href="#dashboard"]');
            if (dashboardNav) {
                showSection('dashboard', dashboardNav);
            }
        }
    });

    // Handle responsive sidebar on initial load
    if (window.innerWidth <= 768) {
        const sidebar = document.getElementById('sidebar');
        if (sidebar) {
            sidebar.classList.add('collapsed');
        }
    }

    // Store Settings Form Handler
    const storeSettingsForm = document.getElementById('storeSettingsForm');
    if (storeSettingsForm) {
        storeSettingsForm.addEventListener('submit', function(e) {
            e.preventDefault();
            alert('Đã lưu cài đặt thành công!');
        });
    }

    // Email Settings Form Handler
    const emailSettingsForm = document.getElementById('emailSettingsForm');
    if (emailSettingsForm) {
        emailSettingsForm.addEventListener('submit', function(e) {
            e.preventDefault();
            alert('Đã lưu cấu hình email!');
        });
    }

    // Close modal when clicking outside
    document.querySelectorAll('.modal').forEach(modal => {
        modal.addEventListener('click', function(e) {
            if (e.target === this) {
                this.classList.remove('show');
            }
        });
    });

    // Initialize dashboard
    initDashboard();
});

// Handle window resize for responsive sidebar
window.addEventListener('resize', function() {
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('main-content');

    if (window.innerWidth <= 768) {
        if (sidebar) sidebar.classList.add('collapsed');
        if (mainContent) mainContent.classList.add('expanded');
    } else {
        if (sidebar) sidebar.classList.remove('collapsed');
        if (mainContent) mainContent.classList.remove('expanded');
    }
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

// Filter orders by status
function filterOrders() {
    const filter = document.getElementById('orderStatusFilter');
    if (filter) {
        const status = filter.value;
        console.log('Filtering orders by status:', status);
        // TODO: Implement order filtering
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
        // Fallback: Create dynamic modal if viewDetailModal doesn't exist
        const detailElement = document.getElementById(sourceId);
        if (!detailElement) {
            console.error("Error: Cannot find detail data. ID:", sourceId);
            return;
        }

        // Remove existing modal if any
        const existingModal = document.getElementById('productDetailModal');
        if (existingModal) existingModal.remove();

        // Create new modal
        const newModal = document.createElement('div');
        newModal.className = 'modal show';
        newModal.id = 'productDetailModal';
        newModal.innerHTML =
            '<div class="modal-content modal-lg">' +
                '<div class="modal-header">' +
                    '<h3>Chi tiết sản phẩm</h3>' +
                    '<button class="modal-close" onclick="closeProductDetailModal()">&times;</button>' +
                '</div>' +
                '<div class="modal-body" style="max-height: 70vh; overflow-y: auto;">' +
                    detailElement.innerHTML +
                '</div>' +
                '<div class="modal-footer">' +
                    '<button class="btn btn-secondary" onclick="closeProductDetailModal()"><i class="fas fa-times"></i> Đóng</button>' +
                '</div>' +
            '</div>';

        document.body.appendChild(newModal);

        newModal.addEventListener('click', function(e) {
            if (e.target === this) {
                closeProductDetailModal();
            }
        });
    }
}

/**
 * Close product detail modal
 */
function closeProductDetailModal() {
    const modal = document.getElementById('productDetailModal');
    if (modal) modal.remove();

    // Also try to close viewDetailModal if it exists
    const viewModal = document.getElementById('viewDetailModal');
    if (viewModal) {
        viewModal.style.display = "none";
        viewModal.classList.remove("show");
    }
}

// ===== MODAL FUNCTIONS =====

/**
 * Open a modal by ID
 * @param {string} modalId - The ID of the modal to open
 */
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'flex';
        modal.classList.add('show');
        document.body.style.overflow = 'hidden'; // Prevent background scrolling

        // Focus on first input if exists
        setTimeout(() => {
            const firstInput = modal.querySelector('input:not([type="hidden"]), select, textarea');
            if (firstInput) firstInput.focus();
        }, 100);
    }
}

/**
 * Close a modal by ID
 * @param {string} modalId - The ID of the modal to close
 */
function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'none';
        modal.classList.remove('show');
        document.body.style.overflow = ''; // Restore scrolling
    }
}

/**
 * Close modal when clicking outside
 */
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('modal')) {
        e.target.style.display = 'none';
        e.target.classList.remove('show');
        document.body.style.overflow = '';
    }
});

/**
 * Close modal on Escape key
 */
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        const openModals = document.querySelectorAll('.modal.show');
        openModals.forEach(modal => {
            modal.style.display = 'none';
            modal.classList.remove('show');
        });
        document.body.style.overflow = '';
    }
});

// ===== PRODUCT MODAL FUNCTIONS =====

function openAddProductModal() {
    openModal('productModal');
}


// Initialize chat input event listener when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    const userInput = document.getElementById('user-input');
    if (userInput) {
        userInput.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });
    }

    // Debug: Log available sections
    const sections = document.querySelectorAll('.content-section');
    console.log('Available sections:', Array.from(sections).map(s => s.id));

    // Handle hash-based navigation for admin sections
    if (window.location.hash) {
        const hash = window.location.hash.substring(1);
        const navItem = document.querySelector(`a[href="#${hash}"]`);
        if (navItem) {
            // Small delay to ensure all content is loaded
            setTimeout(() => {
                showSection(hash, navItem);
            }, 100);
        }
    }
});

