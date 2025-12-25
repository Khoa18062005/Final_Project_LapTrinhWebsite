// Hàm mở modal khuyến khích đăng nhập
function showLoginModal() {
    const loginModalElement = document.getElementById('loginModal');
    if (loginModalElement) {
        const myModal = new bootstrap.Modal(loginModalElement);
        myModal.show();
    }
}