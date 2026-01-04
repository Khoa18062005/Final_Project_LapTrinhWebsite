let otpVerified = false;
let countdownTimer = null;
let emailCheckTimeout = null;
const OTP_DURATION = 90;

// ===== TRẠNG THÁI VALIDATION =====
let validationState = {
    email: false,
    otp: false,
    password: false,
    confirmPassword: false,
    phone: true,        // Không bắt buộc, mặc định true
    dateOfBirth: true   // Không bắt buộc, mặc định true
};

document.addEventListener('DOMContentLoaded', function() {
    initOTPSystem();
    initFormValidation();
    initEmailValidation();
    initPhoneValidation();
    initDateOfBirthValidation();
    initPasswordStrength();
    initPasswordToggle();
    initConfirmPasswordValidation();

    // Kiểm tra ngay khi load trang
    updateRegisterButtonState();
});

// ========== CẬP NHẬT TRẠNG THÁI NÚT ĐĂNG KÝ ==========
function updateRegisterButtonState() {
    const registerBtn = document.getElementById('registerBtn');

    // Kiểm tra TẤT CẢ điều kiện
    const allValid = validationState.otp &&
        validationState.password &&
        validationState.confirmPassword &&
        validationState.phone &&
        validationState.dateOfBirth;

    registerBtn.disabled = !allValid;

    // Debug log (có thể bỏ sau khi test)
    console.log('Validation State:', validationState);
    console.log('Register Button Enabled:', allValid);
}

// ========== EMAIL VALIDATION ==========
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
        validationState.email = false;

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
                validationState.email = false;
            } else {
                emailInput.classList.add('is-valid');
                feedbackDiv.style.display = 'none';
                sendOtpBtn.disabled = false;
                validationState.email = true;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            validationState.email = false;
        });
}

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// ========== PHONE VALIDATION ==========
function initPhoneValidation() {
    const phoneInput = document.getElementById('phone');
    const phoneFeedback = document.getElementById('phoneFeedback');

    phoneInput.addEventListener('input', function() {
        const phone = this.value.trim();
        this.value = phone.replace(/[^0-9]/g, '');

        if (this.value.length > 10) {
            this.value = this.value.substring(0, 10);
        }

        validatePhone(this.value, this, phoneFeedback);
        updateRegisterButtonState(); // Cập nhật nút đăng ký
    });

    phoneInput.addEventListener('blur', function() {
        validatePhone(this.value, this, phoneFeedback);
        updateRegisterButtonState();
    });
}

function validatePhone(phone, input, feedbackDiv) {
    input.classList.remove('is-valid', 'is-invalid');
    feedbackDiv.textContent = '';
    feedbackDiv.style.display = 'none';

    // Nếu không nhập gì → hợp lệ (không bắt buộc)
    if (!phone) {
        validationState.phone = true;
        return true;
    }

    if (!phone.startsWith('0')) {
        input.classList.add('is-invalid');
        feedbackDiv.textContent = 'Số điện thoại phải bắt đầu bằng số 0';
        feedbackDiv.style.display = 'block';
        validationState.phone = false;
        return false;
    }

    if (phone.length !== 10) {
        input.classList.add('is-invalid');
        feedbackDiv.textContent = 'Số điện thoại phải có đúng 10 số';
        feedbackDiv.style.display = 'block';
        validationState.phone = false;
        return false;
    }

    input.classList.add('is-valid');
    validationState.phone = true;
    return true;
}

// ========== DATE OF BIRTH VALIDATION ==========
function initDateOfBirthValidation() {
    const dobInput = document.getElementById('dateOfBirth');
    const dobFeedback = document.getElementById('dobFeedback');

    dobInput.addEventListener('change', function() {
        validateDateOfBirth(this.value, this, dobFeedback);
        updateRegisterButtonState();
    });

    dobInput.addEventListener('blur', function() {
        validateDateOfBirth(this.value, this, dobFeedback);
        updateRegisterButtonState();
    });
}

function validateDateOfBirth(dateString, input, feedbackDiv) {
    input.classList.remove('is-valid', 'is-invalid');
    feedbackDiv.textContent = '';
    feedbackDiv.style.display = 'none';

    // Nếu không nhập gì → hợp lệ (không bắt buộc)
    if (!dateString) {
        validationState.dateOfBirth = true;
        return true;
    }

    const birthDate = new Date(dateString);
    const today = new Date();

    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }

    if (age < 13) {
        input.classList.add('is-invalid');
        feedbackDiv.textContent = 'Bạn phải từ 13 tuổi trở lên để đăng ký';
        feedbackDiv.style.display = 'block';
        validationState.dateOfBirth = false;
        return false;
    }

    if (age > 99) {
        input.classList.add('is-invalid');
        feedbackDiv.textContent = 'Ngày sinh không hợp lệ (tuổi không được quá 99)';
        feedbackDiv.style.display = 'block';
        validationState.dateOfBirth = false;
        return false;
    }

    if (birthDate > today) {
        input.classList.add('is-invalid');
        feedbackDiv.textContent = 'Ngày sinh không thể là ngày trong tương lai';
        feedbackDiv.style.display = 'block';
        validationState.dateOfBirth = false;
        return false;
    }

    input.classList.add('is-valid');
    validationState.dateOfBirth = true;
    return true;
}

// ========== PASSWORD STRENGTH ==========
function initPasswordStrength() {
    const passwordInput = document.getElementById('password');
    const strengthBar = document.getElementById('passwordStrengthBar');
    const strengthText = document.getElementById('passwordStrengthText');
    const passwordFeedback = document.getElementById('passwordFeedback');

    passwordInput.addEventListener('input', function() {
        const password = this.value;
        checkPasswordStrength(password, strengthBar, strengthText, passwordFeedback);

        // Validate password (ít nhất 6 ký tự)
        if (password.length >= 6) {
            validationState.password = true;
        } else {
            validationState.password = false;
        }

        updateRegisterButtonState();
    });
}

function checkPasswordStrength(password, strengthBar, strengthText, feedbackDiv) {
    if (!password) {
        strengthBar.style.width = '0%';
        strengthText.textContent = '';
        feedbackDiv.textContent = '';
        feedbackDiv.style.display = 'none';
        return;
    }

    let strength = 0;
    const checks = {
        length: password.length >= 8,
        lowercase: /[a-z]/.test(password),
        uppercase: /[A-Z]/.test(password),
        number: /[0-9]/.test(password),
        special: /[^a-zA-Z0-9]/.test(password)
    };

    if (checks.length) strength += 20;
    if (checks.lowercase) strength += 20;
    if (checks.uppercase) strength += 20;
    if (checks.number) strength += 20;
    if (checks.special) strength += 20;

    strengthBar.style.width = strength + '%';

    let strengthLevel = '';
    let strengthClass = '';
    let tips = [];

    if (strength <= 40) {
        strengthLevel = 'Yếu';
        strengthClass = 'bg-danger';
        if (!checks.length) tips.push('ít nhất 8 ký tự');
        if (!checks.lowercase) tips.push('chữ thường (a-z)');
        if (!checks.uppercase) tips.push('chữ hoa (A-Z)');
        if (!checks.number) tips.push('số (0-9)');
        if (!checks.special) tips.push('ký tự đặc biệt (!@#$)');
    } else if (strength <= 60) {
        strengthLevel = 'Trung bình';
        strengthClass = 'bg-warning';
        if (!checks.uppercase) tips.push('thêm chữ hoa');
        if (!checks.special) tips.push('thêm ký tự đặc biệt');
    } else if (strength <= 80) {
        strengthLevel = 'Khá';
        strengthClass = 'bg-info';
        if (!checks.special) tips.push('thêm ký tự đặc biệt để mạnh hơn');
    } else {
        strengthLevel = 'Mạnh';
        strengthClass = 'bg-success';
    }

    strengthBar.className = 'progress-bar ' + strengthClass;
    strengthText.textContent = strengthLevel;
    strengthText.className = 'password-strength-text ' + strengthClass.replace('bg-', 'text-');

    if (tips.length > 0) {
        feedbackDiv.textContent = 'Mật khẩu nên có: ' + tips.join(', ');
        feedbackDiv.style.display = 'block';
        feedbackDiv.className = 'form-text text-muted';
    } else {
        feedbackDiv.style.display = 'none';
    }
}

// ========== PASSWORD TOGGLE ==========
function initPasswordToggle() {
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');
    const eyeIcon = document.getElementById('eyeIcon');

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function() {
            togglePasswordVisibility(passwordInput, eyeIcon);
        });
    }

    const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const eyeIconConfirm = document.getElementById('eyeIconConfirm');

    if (toggleConfirmPassword && confirmPasswordInput) {
        toggleConfirmPassword.addEventListener('click', function() {
            togglePasswordVisibility(confirmPasswordInput, eyeIconConfirm);
        });
    }
}

function togglePasswordVisibility(input, icon) {
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// ========== CONFIRM PASSWORD VALIDATION ==========
function initConfirmPasswordValidation() {
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const confirmPasswordFeedback = document.getElementById('confirmPasswordFeedback');

    confirmPasswordInput.addEventListener('input', function() {
        validateConfirmPassword(passwordInput.value, this.value, this, confirmPasswordFeedback);
        updateRegisterButtonState();
    });

    confirmPasswordInput.addEventListener('blur', function() {
        validateConfirmPassword(passwordInput.value, this.value, this, confirmPasswordFeedback);
        updateRegisterButtonState();
    });

    passwordInput.addEventListener('input', function() {
        if (confirmPasswordInput.value) {
            validateConfirmPassword(this.value, confirmPasswordInput.value, confirmPasswordInput, confirmPasswordFeedback);
            updateRegisterButtonState();
        }
    });
}

function validateConfirmPassword(password, confirmPassword, input, feedbackDiv) {
    input.classList.remove('is-valid', 'is-invalid');
    feedbackDiv.textContent = '';
    feedbackDiv.style.display = 'none';

    if (!confirmPassword) {
        validationState.confirmPassword = false;
        return false;
    }

    if (password !== confirmPassword) {
        input.classList.add('is-invalid');
        feedbackDiv.textContent = 'Mật khẩu không khớp';
        feedbackDiv.style.display = 'block';
        validationState.confirmPassword = false;
        return false;
    }

    input.classList.add('is-valid');
    validationState.confirmPassword = true;
    return true;
}

// ========== OTP SYSTEM ==========
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
            validationState.otp = false;
            updateRegisterButtonState();
        }
    }, 1000);
}

function showExpiredMessage() {
    const timerEl = document.getElementById('otpTimer');
    timerEl.textContent = 'Mã đã hết hạn. Vui lòng gửi lại';
    timerEl.className = 'text-danger';
}

// ========== FORM VALIDATION ==========
function initFormValidation() {
    const otpInput = document.getElementById('otpInput');

    otpInput.addEventListener('input', function() {
        const otp = this.value;

        if (otp.length === 6 && /^\d{6}$/.test(otp)) {
            validationState.otp = true;
            this.classList.remove('is-invalid');
            this.classList.add('is-valid');
        } else {
            validationState.otp = false;
            if (otp.length > 0) {
                this.classList.add('is-invalid');
                this.classList.remove('is-valid');
            } else {
                this.classList.remove('is-invalid', 'is-valid');
            }
        }

        updateRegisterButtonState();
    });

    document.getElementById('registerForm').addEventListener('submit', function(e) {
        if (!validationState.otp) {
            e.preventDefault();
            alert('Vui lòng nhập mã xác thực trước khi đăng ký');
            document.getElementById('otpInput').focus();
            return;
        }

        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (password !== confirmPassword) {
            e.preventDefault();
            alert('Mật khẩu xác nhận không khớp. Vui lòng kiểm tra lại!');
            document.getElementById('confirmPassword').focus();
            return;
        }

        const phone = document.getElementById('phone').value;
        if (phone && !validatePhone(phone, document.getElementById('phone'), document.getElementById('phoneFeedback'))) {
            e.preventDefault();
            alert('Vui lòng nhập số điện thoại hợp lệ');
            document.getElementById('phone').focus();
            return;
        }

        const dob = document.getElementById('dateOfBirth').value;
        if (dob && !validateDateOfBirth(dob, document.getElementById('dateOfBirth'), document.getElementById('dobFeedback'))) {
            e.preventDefault();
            alert('Vui lòng nhập ngày sinh hợp lệ');
            document.getElementById('dateOfBirth').focus();
            return;
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