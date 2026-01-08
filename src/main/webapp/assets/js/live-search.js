document.addEventListener("DOMContentLoaded", function() {
    // Lấy các element từ DOM
    const searchInput = document.getElementById('searchInput');
    const suggestionBox = document.getElementById('suggestionBox');
    let timeout = null;

    // Kiểm tra xem các element có tồn tại không để tránh lỗi javascript
    if (!searchInput || !suggestionBox) return;

    // Bắt sự kiện khi người dùng nhập liệu
    searchInput.addEventListener('input', function() {
        const query = this.value.trim();

        // Xóa timeout cũ nếu người dùng đang gõ liên tục
        clearTimeout(timeout);

        if (query.length < 2) {
            suggestionBox.style.display = 'none';
            return;
        }

        // Debounce: Chờ 300ms sau khi ngừng gõ mới gửi request (giảm tải cho server)
        timeout = setTimeout(() => {
            fetchSuggestion(query);
        }, 300);
    });

    // Ẩn gợi ý khi click ra ngoài vùng tìm kiếm
    document.addEventListener('click', function(e) {
        if (!searchInput.contains(e.target) && !suggestionBox.contains(e.target)) {
            suggestionBox.style.display = 'none';
        }
    });

    // Hiện lại gợi ý khi focus vào ô input (nếu đã có nội dung cũ)
    searchInput.addEventListener('focus', function() {
        if(this.value.trim().length >= 2) {
            suggestionBox.style.display = 'block';
        }
    });

    // Hàm gọi API về Server
    function fetchSuggestion(query) {
        // Lấy contextPath từ biến toàn cục (được khai báo bên JSP)
        const baseUrl = window.contextPath || "";

        fetch(`${baseUrl}/api/search-suggestions?q=${encodeURIComponent(query)}`)
            .then(response => {
                if (!response.ok) throw new Error("Lỗi kết nối");
                return response.json();
            })
            .then(data => {
                renderSuggestions(data, baseUrl);
            })
            .catch(error => console.error('Lỗi tìm kiếm:', error));
    }

    // Hàm vẽ giao diện gợi ý
    function renderSuggestions(products, baseUrl) {
        if (!products || products.length === 0) {
            suggestionBox.style.display = 'none';
            return;
        }

        let html = '<div class="suggestion-header">Sản phẩm gợi ý</div>';

        products.forEach(p => {
            // 1. Format giá tiền sang VND
            const price = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(p.price);

            // 2. TẠO LINK: Khớp với ProductDetailServlet (@WebServlet("/product"))
            const link = `${baseUrl}/product?id=${p.id}`;

            // 3. Xử lý ảnh: Ưu tiên ảnh thật, nếu lỗi dùng ảnh no-image nội bộ
            // Lưu ý: Key 'image' này phải khớp với key trong chuỗi JSON mà Servlet trả về
            let imgUrl = p.image;
            if (!imgUrl || imgUrl.trim() === "") {
                imgUrl = `${baseUrl}/assets/images/no-image.png`;
            }

            html += `
                <a href="${link}" class="suggestion-item">
                    <img src="${imgUrl}" alt="${p.name}" onerror="this.src='${baseUrl}/assets/images/no-image.png'">
                    <div class="suggestion-info">
                        <h4>${p.name}</h4>
                        <div class="price">${price}</div>
                    </div>
                </a>
            `;
        });

        suggestionBox.innerHTML = html;
        suggestionBox.style.display = 'block';
    }
});