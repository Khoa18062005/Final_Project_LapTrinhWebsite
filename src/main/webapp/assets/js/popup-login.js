document.addEventListener("DOMContentLoaded", function () {
    // Biến isLoggedIn đã được định nghĩa trong file JSP

    // Ví dụ: Bắt sự kiện khi user click vào nút "Yêu thích" hoặc "Giỏ hàng" mà chưa đăng nhập
    const requireAuthActions = document.querySelectorAll('.require-auth'); // Thêm class này vào các nút cần login

    requireAuthActions.forEach(btn => {
        btn.addEventListener('click', function (e) {
            if (!isLoggedIn) {
                e.preventDefault(); // Chặn hành động mặc định
                // Gọi Modal Bootstrap
                var loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
                loginModal.show();
            }
        });
    });

    // Hoặc nếu muốn hiện popup quảng cáo đăng ký sau 5 giây truy cập
    /*
    if (!isLoggedIn) {
        setTimeout(() => {
             var loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
             loginModal.show();
        }, 5000);
    }
    */
});