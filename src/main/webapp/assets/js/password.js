document.addEventListener('DOMContentLoaded', function() {
    console.log('✅ Password.js loaded');

    const form = document.getElementById('changePasswordForm');
    const currentPasswordInput = document.getElementById('currentPassword');
    const newPasswordInput = document.getElementById('newPassword');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const btnSubmit = document.getElementById('btnChangePassword');

    // ===== TOGGLE SHOW/HIDE PASSWORD =====
    const toggleButtons = document.querySelectorAll('.btn-toggle-password');
    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetId = this.getAttribute('data-target');
            const input = document.getElementById(targetId);
            const icon = this.querySelector('i');

            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('bi-eye-slash');
                icon.classList.add('bi-eye');
            } else {
                input.type = 'password';
                icon.classList.remove('bi-eye');
                icon.classList.add('bi-eye-slash');
            }
        });
    });

    // ===== PASSWORD STRENGTH METER =====
    if (newPasswordInput) {
        newPasswordInput.addEventListener('input', function() {
            const password = this.value;
            const strength = calculatePasswordStrength(password);
            updateStrengthMeter(strength);
            validateForm(); // ← CHECK ENABLE/DISABLE NÚT
        });
    }

    // ===== CHECK CONFIRM PASSWORD MATCH =====
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', function() {
            validateForm(); // ← CHECK ENABLE/DISABLE NÚT
        });
    }

    function calculatePasswordStrength(password) {
        let strength = 0;

        if (password.length === 0) return strength;

        // Độ dài
        if (password.length >= 8) strength += 25;
        if (password.length >= 12) strength += 15;

        // Chữ hoa
        if (/[A-Z]/.test(password)) strength += 20;

        // Chữ thường
        if (/[a-z]/.test(password)) strength += 20;

        // Số
        if (/[0-9]/.test(password)) strength += 20;

        // Ký tự đặc biệt
        if (/[^A-Za-z0-9]/.test(password)) strength += 20;

        return Math.min(strength, 100);
    }

    function updateStrengthMeter(strength) {
        const bar = document.querySelector('.strength-bar');
        const text = document.querySelector('.password-strength-text');

        bar.style.width = strength + '%';

        if (strength === 0) {
            bar.className = 'strength-bar';
            text.textContent = '';
        } else if (strength < 40) {
            bar.className = 'strength-bar weak';
            text.textContent = 'Mật khẩu yếu';
        } else if (strength < 70) {
            bar.className = 'strength-bar medium';
            text.textContent = 'Mật khẩu trung bình';
        } else {
            bar.className = 'strength-bar strong';
            text.textContent = 'Mật khẩu mạnh';
        }
    }

    // ===== VALIDATE FORM & ENABLE/DISABLE BUTTON =====
    function validateForm() {
        const newPassword = newPasswordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        // ĐIỀU KIỆN 1: Mật khẩu mới phải mạnh (≥ 70%)
        const strength = calculatePasswordStrength(newPassword);
        const isPasswordStrong = strength >= 70;

        // ĐIỀU KIỆN 2: Mật khẩu mới === Xác nhận mật khẩu
        const passwordsMatch = newPassword.length > 0 &&
            confirmPassword.length > 0 &&
            newPassword === confirmPassword;

        // ✅ ENABLE NÚT NẾU CẢ 2 ĐIỀU KIỆN ĐÚN
        if (isPasswordStrong && passwordsMatch) {
            btnSubmit.disabled = false;
            btnSubmit.classList.remove('btn-disabled');
            btnSubmit.classList.add('btn-enabled');
            console.log('✅ Button ENABLED');
        } else {
            btnSubmit.disabled = true;
            btnSubmit.classList.remove('btn-enabled');
            btnSubmit.classList.add('btn-disabled');
            console.log('❌ Button DISABLED');
        }
    }

    // ===== FORM SUBMISSION VALIDATION =====
    if (form) {
        form.addEventListener('submit', function(e) {
            let isValid = true;

            // Clear previous errors
            clearErrors();

            // Validate current password
            if (!currentPasswordInput.value.trim()) {
                showError(currentPasswordInput, 'Vui lòng nhập mật khẩu hiện tại');
                isValid = false;
            }

            // Validate new password
            const newPassword = newPasswordInput.value;
            if (!newPassword.trim()) {
                showError(newPasswordInput, 'Vui lòng nhập mật khẩu mới');
                isValid = false;
            } else if (newPassword.length < 8) {
                showError(newPasswordInput, 'Mật khẩu phải có ít nhất 8 ký tự');
                isValid = false;
            } else if (!isPasswordStrong(newPassword)) {
                showError(newPasswordInput, 'Mật khẩu phải có chữ hoa, chữ thường, số và ký tự đặc biệt');
                isValid = false;
            }

            // Validate confirm password
            if (!confirmPasswordInput.value.trim()) {
                showError(confirmPasswordInput, 'Vui lòng xác nhận mật khẩu mới');
                isValid = false;
            } else if (newPassword !== confirmPasswordInput.value) {
                showError(confirmPasswordInput, 'Mật khẩu xác nhận không khớp');
                isValid = false;
            }

            // ❌ BỎ CHECK: Mật khẩu mới khác mật khẩu cũ (backend sẽ check)

            if (!isValid) {
                e.preventDefault();
                console.log('❌ Form validation failed');
            } else {
                console.log('✅ Form validation passed');
            }
        });
    }
    function isPasswordStrong(password) {
        return /[A-Z]/.test(password) &&
            /[a-z]/.test(password) &&
            /[0-9]/.test(password) &&
            /[^A-Za-z0-9]/.test(password);
    }

    function showError(input, message) {
        input.classList.add('is-invalid');
        const feedback = input.parentElement.parentElement.querySelector('.invalid-feedback');
        if (feedback) {
            feedback.textContent = message;
            feedback.style.display = 'block';
        }
    }

    function clearErrors() {
        document.querySelectorAll('.is-invalid').forEach(el => {
            el.classList.remove('is-invalid');
        });
        document.querySelectorAll('.invalid-feedback').forEach(el => {
            el.style.display = 'none';
        });
    }

    // ===== INITIAL CHECK =====
    validateForm();
});