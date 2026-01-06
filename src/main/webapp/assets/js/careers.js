// Form validation & submission (temporary - will be handled by backend later)
document.getElementById('vendorForm').addEventListener('submit', function(e) {
    e.preventDefault();
    alert('Cảm ơn bạn đã đăng ký! Chúng tôi sẽ xem xét và liên hệ lại trong vòng 3-5 ngày làm việc.');
    this.reset();
});

document.getElementById('shipperForm').addEventListener('submit', function(e) {
    e.preventDefault();
    alert('Cảm ơn bạn đã đăng ký! Chúng tôi sẽ xem xét và liên hệ lại trong vòng 3-5 ngày làm việc.');
    this.reset();
});