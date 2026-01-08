/**
 * Product Review JavaScript
 * Xử lý các chức năng liên quan đến đánh giá sản phẩm
 * @author VietTech Team
 */

document.addEventListener('DOMContentLoaded', function() {
    initReviewForm();
    initStarRating();
    initHelpfulButtons();
});

/**
 * Khởi tạo form đánh giá
 */
function initReviewForm() {
    const reviewForm = document.getElementById('reviewForm');
    if (!reviewForm) return;

    reviewForm.addEventListener('submit', function(e) {
        e.preventDefault();
        submitReview(this);
    });
}

/**
 * Khởi tạo chức năng chọn sao
 */
function initStarRating() {
    const starInputs = document.querySelectorAll('.star-rating-input input');
    const ratingText = document.getElementById('ratingText');

    const ratingLabels = {
        1: 'Rất tệ',
        2: 'Tệ',
        3: 'Bình thường',
        4: 'Tốt',
        5: 'Tuyệt vời'
    };

    starInputs.forEach(input => {
        input.addEventListener('change', function() {
            const rating = parseInt(this.value);
            if (ratingText) {
                ratingText.textContent = ratingLabels[rating] || '';
            }
        });
    });
}

/**
 * Gửi đánh giá
 */
function submitReview(form) {
    const formData = new FormData(form);
    const submitBtn = form.querySelector('.btn-submit-review');

    // Validate rating
    const rating = formData.get('rating');
    if (!rating) {
        showToast('Vui lòng chọn số sao đánh giá', 'warning');
        return;
    }

    // Validate comment
    const comment = formData.get('comment');
    if (!comment || comment.trim() === '') {
        showToast('Vui lòng nhập nội dung đánh giá', 'warning');
        return;
    }

    // Disable button
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="bi bi-hourglass-split"></i> Đang gửi...';
    submitBtn.disabled = true;

    // Send request
    fetch(contextPath + '/review/add', {
        method: 'POST',
        body: new URLSearchParams(formData),
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast(data.message || 'Đánh giá của bạn đã được gửi thành công!', 'success');
            // Reload page to show new review
            setTimeout(() => {
                location.reload();
            }, 1500);
        } else {
            showToast(data.message || 'Không thể gửi đánh giá. Vui lòng thử lại.', 'error');
            // Re-enable button
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        }
    })
    .catch(error => {
        console.error('Error submitting review:', error);
        showToast('Có lỗi xảy ra. Vui lòng thử lại sau.', 'error');
        // Re-enable button
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    });
}

/**
 * Khởi tạo nút hữu ích
 */
function initHelpfulButtons() {
    document.querySelectorAll('.btn-helpful').forEach(btn => {
        btn.addEventListener('click', function() {
            const reviewId = this.dataset.reviewId;
            markReviewHelpful(reviewId, this);
        });
    });
}

/**
 * Đánh dấu review hữu ích
 */
function markReviewHelpful(reviewId, button) {
    // Check if user is logged in
    if (typeof isLoggedIn !== 'undefined' && !isLoggedIn) {
        showToast('Vui lòng đăng nhập để đánh dấu hữu ích', 'warning');
        return;
    }

    // Toggle active state (for UI feedback)
    button.classList.toggle('active');

    // Update count visually
    const countSpan = button.querySelector('.helpful-count');
    if (countSpan) {
        const currentCount = parseInt(countSpan.textContent.replace(/[()]/g, '')) || 0;
        const newCount = button.classList.contains('active') ? currentCount + 1 : currentCount - 1;
        countSpan.textContent = `(${newCount})`;
    }
}

/**
 * Hiển thị toast notification
 */
function showToast(message, type = 'info') {
    // Try to use existing toast
    const toastEl = document.getElementById('cartToast');
    const toastMessage = document.getElementById('toast-message');

    if (toastEl && toastMessage) {
        // Update toast content
        toastMessage.textContent = message;

        // Update toast header color based on type
        const toastHeader = toastEl.querySelector('.toast-header');
        if (toastHeader) {
            toastHeader.className = 'toast-header text-white';
            switch(type) {
                case 'success':
                    toastHeader.classList.add('bg-success');
                    break;
                case 'error':
                    toastHeader.classList.add('bg-danger');
                    break;
                case 'warning':
                    toastHeader.classList.add('bg-warning');
                    break;
                default:
                    toastHeader.classList.add('bg-info');
            }
        }

        // Show toast
        const toast = new bootstrap.Toast(toastEl);
        toast.show();
    } else {
        // Fallback to alert
        alert(message);
    }
}

