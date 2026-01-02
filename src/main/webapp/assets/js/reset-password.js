document.addEventListener('DOMContentLoaded', function() {
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const toggleNewPassword = document.getElementById('toggleNewPassword');
    const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
    const passwordStrength = document.getElementById('passwordStrength');
    const passwordMatch = document.getElementById('passwordMatch');
    const submitBtn = document.getElementById('submitBtn');

    // Toggle hiển thị mật khẩu
    toggleNewPassword.addEventListener('click', function() {
        const type = newPassword.getAttribute('type') === 'password' ? 'text' : 'password';
        newPassword.setAttribute('type', type);
        this.textContent = type === 'password' ? 'Hiện' : 'Ẩn';
    });

    toggleConfirmPassword.addEventListener('click', function() {
        const type = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
        confirmPassword.setAttribute('type', type);
        this.textContent = type === 'password' ? 'Hiện' : 'Ẩn';
    });

    // Kiểm tra độ mạnh mật khẩu
    newPassword.addEventListener('input', function() {
        const password = this.value;

        // Nếu password rỗng, ẩn thanh strength
        if (password.length === 0) {
            passwordStrength.classList.remove('show');
            passwordStrength.className = 'password-strength';
            passwordStrength.title = '';
            checkPasswordMatch();
            return;
        }

        // Nếu có ký tự, hiện thanh strength
        passwordStrength.classList.add('show');

        let strength = 0;
        let strengthText = '';
        let strengthClass = '';

        // Kiểm tra độ dài
        if (password.length >= 6) strength += 1;
        if (password.length >= 8) strength += 1;

        // Kiểm tra chữ hoa
        if (/[A-Z]/.test(password)) strength += 1;

        // Kiểm tra số
        if (/[0-9]/.test(password)) strength += 1;

        // Kiểm tra ký tự đặc biệt
        if (/[^A-Za-z0-9]/.test(password)) strength += 1;

        // Xác định độ mạnh
        if (strength < 2) {
            strengthText = 'Yếu';
            strengthClass = 'strength-weak';
        } else if (strength < 3) {
            strengthText = 'Trung bình';
            strengthClass = 'strength-fair';
        } else if (strength < 4) {
            strengthText = 'Tốt';
            strengthClass = 'strength-good';
        } else {
            strengthText = 'Mạnh';
            strengthClass = 'strength-strong';
        }

        passwordStrength.className = 'password-strength show ' + strengthClass;
        passwordStrength.title = 'Độ mạnh: ' + strengthText;

        // Kiểm tra trùng khớp
        checkPasswordMatch();
    });

    // Kiểm tra mật khẩu trùng khớp
    confirmPassword.addEventListener('input', checkPasswordMatch);

    function checkPasswordMatch() {
        const pass1 = newPassword.value;
        const pass2 = confirmPassword.value;

        // Reset nếu cả hai ô đều trống
        if (pass1.length === 0 && pass2.length === 0) {
            passwordMatch.innerHTML = '';
            submitBtn.disabled = true;
            return;
        }

        if (pass2.length === 0) {
            passwordMatch.innerHTML = '';
            submitBtn.disabled = true;
            return;
        }

        if (pass1 === pass2 && pass1.length >= 6) {
            passwordMatch.innerHTML = '<span class="text-success">✓ Mật khẩu trùng khớp</span>';
            submitBtn.disabled = false;
        } else if (pass1 !== pass2) {
            passwordMatch.innerHTML = '<span class="text-danger">✗ Mật khẩu không khớp</span>';
            submitBtn.disabled = true;
        } else {
            passwordMatch.innerHTML = '';
            submitBtn.disabled = true;
        }
    }

    // Validate form submit
    document.getElementById('resetForm').addEventListener('submit', function(e) {
        if (newPassword.value !== confirmPassword.value) {
            e.preventDefault();
            alert('Mật khẩu không khớp!');
            return false;
        }

        if (newPassword.value.length < 6) {
            e.preventDefault();
            alert('Mật khẩu phải có ít nhất 6 ký tự!');
            return false;
        }

        return true;
    });
});