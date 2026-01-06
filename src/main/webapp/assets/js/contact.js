// Contact Form Validation
document.getElementById('contactForm').addEventListener('submit', function (e) {
    e.preventDefault();

    // Get form data
    const fullName = document.getElementById('fullName').value.trim();
    const email = document.getElementById('email').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const subject = document.getElementById('subject').value;
    const message = document.getElementById('message').value.trim();

    // Basic validation
    if (!fullName || !email || !phone || !subject || !message) {
        alert('Vui lòng điền đầy đủ thông tin!');
        return;
    }

    // Phone validation
    const phoneRegex = /^[0-9]{10}$/;
    if (!phoneRegex.test(phone)) {
        alert('Số điện thoại không hợp lệ!');
        return;
    }

    // Success message (in real app, submit to server)
    alert('Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi trong vòng 24 giờ.');
    this.reset();
});