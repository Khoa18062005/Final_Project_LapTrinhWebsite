// user-status.js
// File này nhận dữ liệu user từ server (qua JSP) và lưu vào biến toàn cục để các file JS khác sử dụng

// Biến toàn cục lưu thông tin người dùng hiện tại
window.currentUser = {
    isLoggedIn: false,
    firstName: '',
    lastName: '',
    fullName: '',
    email: '',
    role: ''
};

// Hàm khởi tạo thông tin user từ dữ liệu JSP nhúng vào HTML
function initCurrentUser() {
    // Lấy dữ liệu từ data attributes (an toàn và sạch sẽ)
    const userDataEl = document.getElementById('current-user-data');
    if (userDataEl) {
        const data = {
            isLoggedIn: userDataEl.dataset.loggedIn === 'true',
            firstName: userDataEl.dataset.firstName || '',
            lastName: userDataEl.dataset.lastName || '',
            email: userDataEl.dataset.email || '',
            role: userDataEl.dataset.role || ''
        };

        data.fullName = data.firstName && data.lastName
            ? `${data.firstName} ${data.lastName}`
            : data.firstName || data.email || 'Người dùng';

        window.currentUser = data;

        console.log('User status loaded:', window.currentUser); // Dễ debug
    }
}

// Chạy ngay khi DOM sẵn sàng
document.addEventListener('DOMContentLoaded', initCurrentUser);