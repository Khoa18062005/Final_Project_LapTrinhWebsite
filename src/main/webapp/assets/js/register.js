let otpVerified = false;
let countdownTimer = null;
const OTP_DURATION = 90;

// ✅ Khởi tạo khi trang load
document.addEventListener('DOMContentLoaded', function() {
    initOTPSystem();
    initFormValidation();
});

// ✅ Khởi tạo hệ thống OTP
function initOTPSystem() {
    // Kiểm tra xem có OTP đang chạy không (khi reload sau khi nhập sai)
    const otpTimeElement = document.getElementById('otpTimeData');
    if (otpTimeElement) {
        const otpTime = parseInt(otpTimeElement.value);
        const currentTime = Date.now();
        const elapsed = Math.floor((currentTime - otpTime) / 1000);
        const timeLeft = OTP_DURATION - elapsed;

        if (timeLeft > 0) {
            const btn = document.getElementById('sendOtpBtn');
            const btnText = document.getElementById('btnText');
            startCountdown(timeLeft, btn, btnText);
        } else {
            showExpiredMessage();
        }
    }

    // Gắn sự kiện cho nút gửi OTP
    document.getElementById('sendOtpBtn').addEventListener('click', handleSendOTP);
}

// ✅ Xử lý gửi OTP
function handleSendOTP() {
    const email = document.getElementById('email').value;
    const btn = this;
    const btnText = document.getElementById('btnText');

    if (!email) {
        alert('Vui lòng nhập email!');
        return;
    }

    btn.disabled = true;
    btnText.textContent = 'Đang gửi...';

    const contextPath = document.querySelector('meta[name="context-path"]').content;

    fetch(contextPath + '/send-otp', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'email=' + encodeURIComponent(email)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('otpSection').style.display = 'block';
                document.getElementById('otpInput').value = '';
                document.getElementById('otpInput').focus();

                startCountdown(OTP_DURATION, btn, btnText);
                showAlert('success', 'Mã OTP đã được gửi đến email của bạn!');
            } else {
                alert('✗ Gửi OTP thất bại: ' + data.message);
                btn.disabled = false;
                btnText.textContent = 'Gửi OTP';
            }
        })
        .catch(error => {
            alert('✗ Lỗi kết nối server!');
            btn.disabled = false;
            btnText.textContent = 'Gửi OTP';
        });
}

// ✅ Countdown timer
function startCountdown(duration, btn, btnText) {
    const timerEl = document.getElementById('otpTimer');
    let timeLeft = duration;

    if (countdownTimer) {
        clearInterval(countdownTimer);
    }

    function updateDisplay() {
        timerEl.innerHTML = '';

        const icon = document.createElement('i');
        icon.className = 'bi bi-clock';

        const text = document.createTextNode('Mã có hiệu lực trong ');

        const num = document.createElement('span');
        num.textContent = timeLeft + 's';

        timerEl.appendChild(icon);
        timerEl.appendChild(text);
        timerEl.appendChild(num);
        timerEl.className = 'text-success';

        btn.disabled = true;
        btnText.textContent = timeLeft + 's';
    }

    updateDisplay();

    countdownTimer = setInterval(() => {
        timeLeft--;

        if (timeLeft > 0) {
            updateDisplay();
        } else {
            clearInterval(countdownTimer);
            showExpiredMessage();
            btn.disabled = false;
            btnText.textContent = 'Gửi lại OTP';
            document.getElementById('registerBtn').disabled = true;
            otpVerified = false;
        }
    }, 1000);
}

// ✅ Hiển thị thông báo hết hạn
function showExpiredMessage() {
    const timerEl = document.getElementById('otpTimer');
    timerEl.innerHTML = '';
    const icon = document.createElement('i');
    icon.className = 'bi bi-exclamation-triangle';
    const text = document.createTextNode('Mã OTP đã hết hạn! Vui lòng gửi lại.');
    timerEl.appendChild(icon);
    timerEl.appendChild(text);
    timerEl.className = 'text-danger';
}

// ✅ Validation form
function initFormValidation() {
    document.getElementById('otpInput').addEventListener('input', function() {
        const otp = this.value;
        const registerBtn = document.getElementById('registerBtn');

        if (otp.length === 6 && /^\d{6}$/.test(otp)) {
            registerBtn.disabled = false;
            otpVerified = true;
            this.classList.remove('is-invalid');
            this.classList.add('is-valid');
        } else {
            registerBtn.disabled = true;
            otpVerified = false;
            if (otp.length > 0) {
                this.classList.add('is-invalid');
                this.classList.remove('is-valid');
            } else {
                this.classList.remove('is-invalid', 'is-valid');
            }
        }
    });

    document.getElementById('registerForm').addEventListener('submit', function(e) {
        if (!otpVerified) {
            e.preventDefault();
            alert('Vui lòng nhập đúng mã OTP (6 số) trước khi đăng ký!');
            document.getElementById('otpInput').focus();
        }
    });
}

// ✅ Hiển thị alert
function showAlert(type, message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.setAttribute('role', 'alert');

    const icon = document.createElement('i');
    icon.className = type === 'success' ? 'bi bi-check-circle-fill me-2' : 'bi bi-exclamation-triangle-fill me-2';

    const textNode = document.createTextNode(message);

    const closeBtn = document.createElement('button');
    closeBtn.type = 'button';
    closeBtn.className = 'btn-close';
    closeBtn.setAttribute('data-bs-dismiss', 'alert');
    closeBtn.setAttribute('aria-label', 'Close');

    alertDiv.appendChild(icon);
    alertDiv.appendChild(textNode);
    alertDiv.appendChild(closeBtn);

    document.querySelector('.card-body').insertBefore(alertDiv, document.querySelector('form'));

    setTimeout(() => alertDiv.remove(), 5000);
}