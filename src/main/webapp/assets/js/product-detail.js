document.addEventListener('DOMContentLoaded', function() {
    // CẤU HÌNH
    const ZOOM_STEP = 0.5;
    const MAX_ZOOM = 3.0;
    const MIN_ZOOM = 1.0;

    // Lấy các element
    const imgElement = document.getElementById('product-img');
    const btnZoomIn = document.querySelector('.btn-zoom[onclick="zoomIn()"]'); // Hoặc bạn có thể đặt ID cho nút
    const btnZoomOut = document.querySelector('.btn-zoom[onclick="zoomOut()"]');
    const btnReset = document.querySelector('.btn-zoom[onclick="resetZoom()"]');

    // Biến trạng thái
    let currentScale = 1;
    let isDragging = false;
    let startX, startY, translateX = 0, translateY = 0;

    // Hàm cập nhật CSS transform
    function updateTransform() {
        if (imgElement) {
            // Giới hạn không cho kéo ảnh ra khỏi khung quá xa (tùy chọn)
            imgElement.style.transform = `scale(${currentScale}) translate(${translateX}px, ${translateY}px)`;

            // Thay đổi con trỏ chuột tùy theo trạng thái zoom
            if (currentScale > 1) {
                imgElement.style.cursor = isDragging ? 'grabbing' : 'grab';
            } else {
                imgElement.style.cursor = 'default';
                // Reset vị trí khi về zoom 1
                translateX = 0;
                translateY = 0;
            }
        }
    }

    // --- CÁC HÀM ZOOM (Global để HTML gọi được onclick) ---
    window.zoomIn = function() {
        if (currentScale < MAX_ZOOM) {
            currentScale += ZOOM_STEP;
            updateTransform();
        }
    };

    window.zoomOut = function() {
        if (currentScale > MIN_ZOOM) {
            currentScale -= ZOOM_STEP;
            updateTransform();
        }
    };

    window.resetZoom = function() {
        currentScale = 1;
        translateX = 0;
        translateY = 0;
        updateTransform();
    };

    // --- TÍNH NĂNG KÉO THẢ (DRAG / PAN) ---
    if (imgElement) {
        // Bắt đầu kéo
        imgElement.addEventListener('mousedown', function(e) {
            if (currentScale > 1) { // Chỉ cho kéo khi đã phóng to
                isDragging = true;
                startX = e.clientX - translateX;
                startY = e.clientY - translateY;
                e.preventDefault(); // Ngăn hành động mặc định (như kéo ảnh ra tab mới)
            }
        });

        // Đang kéo
        window.addEventListener('mousemove', function(e) {
            if (isDragging) {
                translateX = e.clientX - startX;
                translateY = e.clientY - startY;
                updateTransform();
            }
        });

        // Kết thúc kéo (thả chuột hoặc chuột ra khỏi màn hình)
        window.addEventListener('mouseup', function() {
            isDragging = false;
            updateTransform();
        });
    }
});