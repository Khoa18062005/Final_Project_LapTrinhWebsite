// ===== PROFILE PAGE JAVASCRIPT - WITH AVATAR CANCEL & EMAIL OTP =====

document.addEventListener('DOMContentLoaded', function() {

    console.log('🔧 Profile.js loaded');

    // ✅ THÊM ĐOẠN NÀY
    const CONTEXT_PATH = document.querySelector('meta[name="context-path"]')?.content || '';
    console.log('📍 Context Path:', CONTEXT_PATH);

    // ===== VARIABLES =====
    const avatarInput = document.getElementById('avatarInput');
    const avatarPreview = document.getElementById('avatarPreview');
    const avatarPreviewLarge = document.getElementById('avatarPreviewLarge');
    const fileNameDisplay = document.getElementById('fileNameDisplay');
    const fileName = document.getElementById('fileName');
    const cancelAvatarBtn = document.getElementById('cancelAvatarBtn');
    const saveBtn = document.getElementById('saveBtn');
    const profileForm = document.getElementById('profileForm');

    // Email OTP elements
    const emailInput = document.getElementById('email');
    const emailOtpSection = document.getElementById('emailOtpSection');
    const sendOtpBtn = document.getElementById('sendOtpBtn');

    // Track changes
    let hasNewAvatar = false;
    let originalAvatarSrc = avatarPreviewLarge.src; // Lưu src gốc

    // GET ORIGINAL VALUES
    const originalValues = {
        lastName: (document.getElementById('lastName')?.dataset.original || '').trim(),
        firstName: (document.getElementById('firstName')?.dataset.original || '').trim(),
        email: (document.getElementById('email')?.dataset.original || '').trim(),
        phone: (document.getElementById('phone')?.dataset.original || '').trim(),
        gender: (document.getElementById('originalGender')?.value || '').trim(),
        day: (document.getElementById('daySelect')?.dataset.original || '').trim(),
        month: (document.getElementById('monthSelect')?.dataset.original || '').trim(),
        year: (document.getElementById('yearSelect')?.dataset.original || '').trim(),
        avatar: (document.getElementById('originalAvatar')?.value || '').trim()
    };

    console.log('📋 Original Values:', originalValues);

    // ===== FUNCTION: CHECK IF FORM HAS CHANGES =====
    function checkForChanges() {
        let hasChanges = false;
        const changes = [];

        // Check lastName
        const lastName = (document.getElementById('lastName')?.value || '').trim();
        if (lastName !== originalValues.lastName) {
            hasChanges = true;
            changes.push(`lastName: "${originalValues.lastName}" → "${lastName}"`);
        }

        // Check firstName
        const firstName = (document.getElementById('firstName')?.value || '').trim();
        if (firstName !== originalValues.firstName) {
            hasChanges = true;
            changes.push(`firstName: "${originalValues.firstName}" → "${firstName}"`);
        }

        // Check email (NẾU đổi email + OTP verified → hasChanges = true)
        const email = (document.getElementById('email')?.value || '').trim();
        const emailChanged = email !== originalValues.email;

        if (emailChanged) {
            // Hiện OTP section
            if (emailOtpSection) {
                emailOtpSection.style.display = 'block';
            }

            // NẾU đã verify OTP → tính là có thay đổi
            if (otpVerified) {
                hasChanges = true;
                changes.push(`email: "${originalValues.email}" → "${email}" [✓ VERIFIED]`);
            } else {
                changes.push(`email: "${originalValues.email}" → "${email}" [⏳ CHƯA XÁC THỰC]`);
            }
        } else {
            // Ẩn OTP section
            if (emailOtpSection) {
                emailOtpSection.style.display = 'none';
            }
            otpVerified = false;
        }

        // Check phone
        const phone = (document.getElementById('phone')?.value || '').trim();
        if (phone !== originalValues.phone) {
            hasChanges = true;
            changes.push(`phone: "${originalValues.phone}" → "${phone}"`);
        }

        // Check gender
        const selectedGender = (document.querySelector('input[name="gender"]:checked')?.value || '').trim();
        if (selectedGender !== originalValues.gender) {
            hasChanges = true;
            changes.push(`gender: "${originalValues.gender}" → "${selectedGender}"`);
        }

        // Check day
        const day = (document.getElementById('daySelect')?.value || '').trim();
        if (day !== originalValues.day) {
            hasChanges = true;
            changes.push(`day: "${originalValues.day}" → "${day}"`);
        }

        // Check month
        const month = (document.getElementById('monthSelect')?.value || '').trim();
        if (month !== originalValues.month) {
            hasChanges = true;
            changes.push(`month: "${originalValues.month}" → "${month}"`);
        }

        // Check year
        const year = (document.getElementById('yearSelect')?.value || '').trim();
        if (year !== originalValues.year) {
            hasChanges = true;
            changes.push(`year: "${originalValues.year}" → "${year}"`);
        }

        // Check avatar
        if (hasNewAvatar) {
            hasChanges = true;
            changes.push('avatar: NEW FILE SELECTED');
        }

        // Debug log
        if (changes.length > 0) {
            console.log('✅ CHANGES DETECTED:', changes);
        } else {
            console.log('❌ NO CHANGES');
        }

        // Enable/Disable save button
        if (saveBtn) {
            if (hasChanges) {
                // ENABLED
                saveBtn.disabled = false;
                saveBtn.removeAttribute('disabled');
                saveBtn.classList.remove('btn-disabled');
                saveBtn.classList.add('btn-enabled');
                console.log('🟢 Button ENABLED');
            } else {
                // DISABLED
                saveBtn.disabled = true;
                saveBtn.setAttribute('disabled', 'disabled');
                saveBtn.classList.remove('btn-enabled');
                saveBtn.classList.add('btn-disabled');
                console.log('🔴 Button DISABLED');
            }
        }

        return hasChanges;
    }

    // ===== AVATAR UPLOAD =====
    if (avatarInput) {
        avatarInput.addEventListener('change', function(e) {
            const file = e.target.files[0];

            if (file) {
                console.log('📷 Avatar file selected:', file.name);

                // Validate file type
                if (!file.type.startsWith('image/')) {
                    showAlert('Vui lòng chọn file ảnh!', 'error');
                    avatarInput.value = '';
                    return;
                }

                // Validate file size (max 5MB)
                const maxSize = 5 * 1024 * 1024;
                if (file.size > maxSize) {
                    showAlert('Kích thước file không được vượt quá 5MB!', 'error');
                    avatarInput.value = '';
                    return;
                }

                // Preview ảnh
                const reader = new FileReader();
                reader.onload = function(e) {
                    const imageUrl = e.target.result;

                    // Update both preview images
                    if (avatarPreview) {
                        avatarPreview.src = imageUrl;
                    }
                    if (avatarPreviewLarge) {
                        avatarPreviewLarge.src = imageUrl;
                        avatarPreviewLarge.parentElement.classList.add('has-new-image');
                    }

                    // Show cancel button
                    if (cancelAvatarBtn) {
                        cancelAvatarBtn.style.display = 'inline-block';
                    }

                    // Mark as changed
                    hasNewAvatar = true;
                    console.log('✅ Avatar marked as changed');
                    checkForChanges();
                };
                reader.readAsDataURL(file);

                // Hiển thị tên file
                if (fileName && fileNameDisplay) {
                    fileName.textContent = file.name;
                    fileNameDisplay.style.display = 'block';
                }
            }
        });
    }

    // ===== NÚT HỦY ẢNH =====
    if (cancelAvatarBtn) {
        cancelAvatarBtn.addEventListener('click', function() {
            console.log('🗑️ Cancel avatar');

            // Reset input
            if (avatarInput) {
                avatarInput.value = '';
            }

            // Restore original image
            if (avatarPreview) {
                avatarPreview.src = originalAvatarSrc;
            }
            if (avatarPreviewLarge) {
                avatarPreviewLarge.src = originalAvatarSrc;
                avatarPreviewLarge.parentElement.classList.remove('has-new-image');
            }

            // Hide cancel button
            cancelAvatarBtn.style.display = 'none';

            // Hide file name
            if (fileNameDisplay) {
                fileNameDisplay.style.display = 'none';
            }

            // Mark as not changed
            hasNewAvatar = false;
            checkForChanges();
        });
    }

    // ===== EMAIL OTP VARIABLES =====
    let otpVerified = false;
    let countdownTimer = null;
    const OTP_DURATION = 90;

// ===== EMAIL: GỬI OTP =====
    if (sendOtpBtn) {
        sendOtpBtn.addEventListener('click', function() {
            const newEmail = emailInput.value.trim();

            if (!newEmail || !validateEmail(newEmail)) {
                showAlert('Email không hợp lệ!', 'error');
                return;
            }

            if (newEmail === originalValues.email) {
                showAlert('Email không thay đổi!', 'error');
                return;
            }

            // Gọi servlet gửi OTP
            console.log('📧 Sending OTP to:', newEmail);
            const CONTEXT_PATH = document.querySelector('meta[name="context-path"]')?.content || '';

            sendOtpBtn.disabled = true;
            sendOtpBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Đang gửi...';

            fetch(CONTEXT_PATH + '/send-email-otp', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: 'email=' + encodeURIComponent(newEmail)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        showAlert('Mã OTP đã được gửi đến email mới!', 'success');

                        // Focus vào ô OTP
                        document.getElementById('emailOtp').focus();

                        // Start countdown
                        startOtpCountdown(OTP_DURATION);
                    } else {
                        showAlert(data.message || 'Gửi OTP thất bại!', 'error');
                        sendOtpBtn.disabled = false;
                        sendOtpBtn.innerHTML = '<i class="bi bi-send me-1"></i> Gửi OTP';
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showAlert('Lỗi kết nối!', 'error');
                    sendOtpBtn.disabled = false;
                    sendOtpBtn.innerHTML = '<i class="bi bi-send me-1"></i> Gửi OTP';
                });
        });
    }

// ===== COUNTDOWN TIMER =====
    function startOtpCountdown(duration) {
        const timerEl = document.getElementById('otpTimer');
        let timeLeft = duration;

        if (countdownTimer) {
            clearInterval(countdownTimer);
        }

        function updateDisplay() {
            timerEl.textContent = `Mã có hiệu lực trong ${timeLeft} giây`;
            timerEl.className = 'text-success d-block mt-2';

            sendOtpBtn.disabled = true;
            sendOtpBtn.innerHTML = `<i class="bi bi-hourglass me-1"></i> ${timeLeft}s`;
        }

        updateDisplay();

        countdownTimer = setInterval(() => {
            timeLeft--;
            if (timeLeft > 0) {
                updateDisplay();
            } else {
                clearInterval(countdownTimer);
                timerEl.textContent = 'Mã đã hết hạn. Vui lòng gửi lại';
                timerEl.className = 'text-danger d-block mt-2';
                sendOtpBtn.disabled = false;
                sendOtpBtn.innerHTML = '<i class="bi bi-send me-1"></i> Gửi lại';
                otpVerified = false;
            }
        }, 1000);
    }

// ===== OTP INPUT: Enable Save Button khi đủ 6 số =====
    const emailOtpInput = document.getElementById('emailOtp');
    if (emailOtpInput) {
        emailOtpInput.addEventListener('input', function() {
            const otp = this.value.trim();

            if (otp.length === 6 && /^\d{6}$/.test(otp)) {
                // OTP hợp lệ
                otpVerified = true;
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');

                // Kiểm tra lại nếu có thay đổi khác
                checkForChanges();
            } else {
                otpVerified = false;
                if (otp.length > 0) {
                    this.classList.add('is-invalid');
                    this.classList.remove('is-valid');
                } else {
                    this.classList.remove('is-invalid', 'is-valid');
                }
            }
        });
    }

    // ===== LISTEN TO ALL FORM CHANGES =====

    // Text inputs (EXCEPT email)
    const textInputIds = ['lastName', 'firstName', 'phone'];
    textInputIds.forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.addEventListener('input', function() {
                console.log(`📝 ${id} changed:`, this.value);
                checkForChanges();
            });
        }
    });

    // Email input - CHỈ check để hiện/ẩn OTP
    if (emailInput) {
        emailInput.addEventListener('input', function() {
            console.log('📧 email changed:', this.value);
            checkForChanges(); // Sẽ tự hiện/ẩn OTP section
        });
    }

    // Gender radio buttons
    const genderRadios = document.querySelectorAll('.gender-radio');
    if (genderRadios.length > 0) {
        genderRadios.forEach(radio => {
            radio.addEventListener('change', function() {
                console.log('🚻 Gender changed:', this.value);
                checkForChanges();
            });
        });
    }

    // Date selects
    const dateSelects = document.querySelectorAll('.date-select');
    if (dateSelects.length > 0) {
        dateSelects.forEach(select => {
            select.addEventListener('change', function() {
                console.log(`📅 ${this.id} changed:`, this.value);
                checkForChanges();
            });
        });
    }

    // ===== FORM VALIDATION =====
    if (profileForm) {
        profileForm.addEventListener('submit', function(e) {
            const firstName = (document.getElementById('firstName')?.value || '').trim();
            const lastName = (document.getElementById('lastName')?.value || '').trim();
            const email = (document.getElementById('email')?.value || '').trim();
            const phone = (document.getElementById('phone')?.value || '').trim();

            // Validate họ tên
            if (!firstName || !lastName) {
                e.preventDefault();
                showAlert('Vui lòng nhập đầy đủ họ và tên!', 'error');
                return false;
            }

            // Validate email
            if (!email || !validateEmail(email)) {
                e.preventDefault();
                showAlert('Email không hợp lệ!', 'error');
                return false;
            }

            // Validate số điện thoại (nếu có)
            if (phone && !validatePhone(phone)) {
                e.preventDefault();
                showAlert('Số điện thoại không hợp lệ! (10-11 chữ số)', 'error');
                return false;
            }

            // Hiển thị loading khi submit
            showSubmitLoading();

            return true;
        });
    }

    // ===== AUTO-HIDE ALERTS =====
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            if (alert.classList.contains('show')) {
                alert.classList.remove('show');
                setTimeout(function() {
                    const container = alert.closest('.alert-container');
                    if (container) {
                        container.remove();
                    }
                }, 300);
            }
        });
    }, 5000);

    // ===== AUTO-FORMAT PHONE INPUT =====
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 11) {
                value = value.slice(0, 11);
            }
            e.target.value = value;
        });
    }

    // ===== INITIAL CHECK =====
    console.log('🔍 Running initial check...');
    checkForChanges();
});

// ===== VALIDATION FUNCTIONS =====

function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function validatePhone(phone) {
    phone = phone.replace(/[\s\-\.]/g, '');
    const phoneRegex = /^[0-9]{10,11}$/;
    const validPrefixes = /^(03|05|07|08|09)/;
    return phoneRegex.test(phone) && validPrefixes.test(phone);
}

// ===== UI FUNCTIONS =====

function showSubmitLoading() {
    const submitBtn = document.getElementById('saveBtn');
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang lưu...';
    }
}

function showAlert(message, type = 'info') {
    const existingAlert = document.querySelector('.dynamic-alert');
    if (existingAlert) {
        existingAlert.remove();
    }

    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type === 'error' ? 'danger' : type === 'success' ? 'success' : 'info'} alert-dismissible fade show dynamic-alert`;
    alertDiv.style.position = 'fixed';
    alertDiv.style.top = '80px';
    alertDiv.style.right = '20px';
    alertDiv.style.zIndex = '9999';
    alertDiv.style.minWidth = '300px';
    alertDiv.style.boxShadow = '0 4px 12px rgba(0,0,0,0.15)';

    const icon = type === 'error' ? 'bi-exclamation-triangle-fill' : type === 'success' ? 'bi-check-circle-fill' : 'bi-info-circle-fill';

    alertDiv.innerHTML = `
        <i class="bi ${icon} me-2"></i>
        <strong>${type === 'error' ? 'Lỗi!' : type === 'success' ? 'Thành công!' : 'Thông báo'}</strong> ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(alertDiv);

    setTimeout(function() {
        if (alertDiv && alertDiv.parentElement) {
            alertDiv.classList.remove('show');
            setTimeout(function() {
                alertDiv.remove();
            }, 300);
        }
    }, 5000);
}