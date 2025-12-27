// ===== XÓA HOÀN TOÀN ALERT CONTAINER KHI ĐÓNG =====
document.addEventListener('DOMContentLoaded', function() {
    // Lắng nghe sự kiện đóng alert
    const alerts = document.querySelectorAll('.alert-dismissible');

    alerts.forEach(function(alert) {
        alert.addEventListener('closed.bs.alert', function() {
            // Lấy container cha (.alert-container)
            const container = this.closest('.alert-container');

            // Xóa luôn container để không còn khoảng trắng
            if (container) {
                container.remove();
            }
        });
    });
});

// ===== HÀM MỞ MODAL KHUYẾN KHÍCH ĐĂNG NHẬP (GIỮ NGUYÊN) =====
function showLoginModal() {
    const loginModalElement = document.getElementById('loginModal');
    if (loginModalElement) {
        const myModal = new bootstrap.Modal(loginModalElement);
        myModal.show();
    }
}