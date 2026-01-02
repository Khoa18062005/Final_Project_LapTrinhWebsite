document.addEventListener('DOMContentLoaded', function() {
    const otpInput = document.getElementById('otp');
    const verifyBtn = document.getElementById('verifyBtn');
    const resendBtn = document.getElementById('resendBtn');
    const resendText = document.getElementById('resendText');
    const timerElement = document.getElementById('timer');
    const timerText = document.getElementById('timerText');

    // Lấy thời gian còn lại từ server
    let timeLeft = parseInt(document.getElementById('serverTimeLeft').value) || 90;
    let timerInterval = null;

    // Format OTP input
    otpInput.addEventListener('input', function() {
        this.value = this.value.replace(/\D/g, '').slice(0, 6);
    });

    // Hàm cập nhật hiển thị timer
    function updateTimerDisplay() {
        timerElement.textContent = timeLeft;

        if (timeLeft > 30) {
            timerElement.className = 'timer text-success';
            timerText.textContent = 'giây còn lại';
        } else if (timeLeft > 10) {
            timerElement.className = 'timer text-warning';
            timerText.textContent = 'giây còn lại';
        } else if (timeLeft > 0) {
            timerElement.className = 'timer text-danger';
            timerText.textContent = 'giây còn lại';
        } else {
            timerElement.textContent = '0';
            timerElement.className = 'timer text-danger';
            timerText.textContent = 'Mã OTP đã hết hạn';

            // Disable verify button, enable resend button
            verifyBtn.disabled = true;
            otpInput.disabled = true;
            resendBtn.disabled = false;
            resendText.textContent = 'Gửi lại OTP';

            // Clear interval
            if (timerInterval) {
                clearInterval(timerInterval);
            }
        }
    }

    // Timer countdown
    function startTimer() {
        updateTimerDisplay();

        timerInterval = setInterval(() => {
            timeLeft--;
            updateTimerDisplay();
        }, 1000);
    }

    // Khởi động timer nếu còn thời gian
    if (timeLeft > 0) {
        startTimer();
    } else {
        updateTimerDisplay(); // Hiển thị trạng thái hết hạn
    }

    // Resend OTP
    resendBtn.addEventListener('click', function() {
        if (!this.disabled) {
            // Show loading
            const originalText = resendText.textContent;
            resendText.textContent = 'Đang gửi...';
            this.disabled = true;

            // Gửi request resend OTP
            fetch('${pageContext.request.contextPath}/resend-forgot-otp', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        // Reload trang để nhận timer mới
                        window.location.reload();
                    } else {
                        alert('Gửi lại thất bại: ' + data.message);
                        resendText.textContent = originalText;
                        this.disabled = false;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi gửi lại OTP');
                    resendText.textContent = originalText;
                    this.disabled = false;
                });
        }
    });

    // Validate OTP form
    document.getElementById('otpForm').addEventListener('submit', function(e) {
        const otpValue = otpInput.value.trim();

        if (otpValue.length !== 6 || !/^\d{6}$/.test(otpValue)) {
            e.preventDefault();
            alert('Vui lòng nhập đúng 6 chữ số OTP');
            otpInput.focus();
            return false;
        }

        if (timeLeft <= 0) {
            e.preventDefault();
            alert('Mã OTP đã hết hạn. Vui lòng gửi lại mã mới.');
            return false;
        }

        return true;
    });
});