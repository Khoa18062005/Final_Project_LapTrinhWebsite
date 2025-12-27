let otpVerified = false;
let countdownTimer = null;
let emailCheckTimeout = null;
const OTP_DURATION = 90;

document.addEventListener('DOMContentLoaded', function() {
    initOTPSystem();
    initFormValidation();
    initEmailValidation();
});

function initEmailValidation() {
    const emailInput = document.getElementById('email');
    const sendOtpBtn = document.getElementById('sendOtpBtn');
    const emailFeedback = document.getElementById('emailFeedback');

    emailInput.addEventListener('input', function() {
        const email = this.value.trim();

        if (emailCheckTimeout) {
            clearTimeout(emailCheckTimeout);
        }

        this.classList.remove('is-valid', 'is-invalid');
        emailFeedback.textContent = '';
        emailFeedback.style.display = 'none';
        sendOtpBtn.disabled = false;

        if (!email || !isValidEmail(email)) {
            return;
        }

        emailCheckTimeout = setTimeout(() => {
            checkEmailExists(email, emailInput, emailFeedback, sendOtpBtn);
        }, 500);
    });
}

function checkEmailExists(email, emailInput, feedbackDiv, sendOtpBtn) {
    const contextPath = document.querySelector('meta[name="context-path"]').content;

    fetch(contextPath + '/check-email', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'email=' + encodeURIComponent(email)
    })
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                emailInput.classList.add('is-invalid');
                feedbackDiv.textContent = 'Email đã được sử dụng';
                feedbackDiv.style.display = 'block';
                sendOtpBtn.disabled = true;
            } else {
                emailInput.classList.add('is-valid');
                feedbackDiv.style.display = 'none';
                sendOtpBtn.disabled = false;
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function initOTPSystem() {
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

    document.getElementById('sendOtpBtn').addEventListener('click', handleSendOTP);
}

function handleSendOTP() {
    const email = document.getElementById('email').value;
    const btn = this;
    const btnText = document.getElementById('btnText');

    if (!email) {
        alert('Vui lòng nhập email');
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
                showAlert('success', 'Mã xác thực đã được gửi đến email của bạn');
            } else {
                alert('Gửi mã thất bại: ' + data.message);
                btn.disabled = false;
                btnText.textContent = 'Gửi mã';
            }
        })
        .catch(error => {
            alert('Lỗi kết nối');
            btn.disabled = false;
            btnText.textContent = 'Gửi mã';
        });
}

function startCountdown(duration, btn, btnText) {
    const timerEl = document.getElementById('otpTimer');
    let timeLeft = duration;

    if (countdownTimer) {
        clearInterval(countdownTimer);
    }

    function updateDisplay() {
        timerEl.textContent = `Mã có hiệu lực trong ${timeLeft} giây`;
        timerEl.className = '';

        btn.disabled = true;
        btnText.textContent = `${timeLeft}s`;
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
            btnText.textContent = 'Gửi lại';
            document.getElementById('registerBtn').disabled = true;
            otpVerified = false;
        }
    }, 1000);
}

function showExpiredMessage() {
    const timerEl = document.getElementById('otpTimer');
    timerEl.textContent = 'Mã đã hết hạn. Vui lòng gửi lại';
    timerEl.className = 'text-danger';
}

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
            alert('Vui lòng nhập mã xác thực trước khi đăng ký');
            document.getElementById('otpInput').focus();
        }
    });
}

function showAlert(type, message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.setAttribute('role', 'alert');

    const strong = document.createElement('strong');
    strong.textContent = type === 'success' ? 'Thành công: ' : 'Lỗi: ';

    const textNode = document.createTextNode(message);

    const closeBtn = document.createElement('button');
    closeBtn.type = 'button';
    closeBtn.className = 'btn-close';
    closeBtn.setAttribute('data-bs-dismiss', 'alert');

    alertDiv.appendChild(strong);
    alertDiv.appendChild(textNode);
    alertDiv.appendChild(closeBtn);

    document.querySelector('.card-body').insertBefore(alertDiv, document.querySelector('form'));

    setTimeout(() => alertDiv.remove(), 5000);
}