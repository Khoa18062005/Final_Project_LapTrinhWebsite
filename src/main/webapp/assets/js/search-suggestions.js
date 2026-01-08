console.log('Search suggestions script loaded');

(function() {
    'use strict';

    // Đợi DOM sẵn sàng
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initSearch);
    } else {
        initSearch();
    }

    function initSearch() {
        console.log('Initializing search suggestions...');

        const searchInput = document.getElementById('searchInput');
        const suggestionsBox = document.getElementById('searchSuggestions');

        console.log('Elements found:', {
            searchInput: !!searchInput,
            suggestionsBox: !!suggestionsBox
        });

        if (!searchInput || !suggestionsBox) {
            console.error('Search elements not found');
            return;
        }

        let timeoutId = null;
        let currentKeyword = '';

        // Input event với debounce
        searchInput.addEventListener('input', function(e) {
            clearTimeout(timeoutId);
            currentKeyword = this.value.trim();

            if (currentKeyword.length < 2) {
                hideSuggestions();
                return;
            }

            timeoutId = setTimeout(() => {
                fetchSuggestions(currentKeyword);
            }, 300);
        });

        // Focus - hiển thị nếu có keyword
        searchInput.addEventListener('focus', function() {
            if (this.value.trim().length >= 2) {
                fetchSuggestions(this.value.trim());
            }
        });

        // Click ra ngoài -> ẩn
        document.addEventListener('click', function(e) {
            if (!searchInput.contains(e.target) && !suggestionsBox.contains(e.target)) {
                hideSuggestions();
            }
        });

        // Escape key -> ẩn
        searchInput.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                hideSuggestions();
            }
        });

        // Fetch suggestions từ server
        function fetchSuggestions(keyword) {
            console.log('Fetching suggestions for:', keyword);

            const url = window.contextPath + '/search-suggestions?q=' + encodeURIComponent(keyword);
            console.log('URL:', url);

            fetch(url)
                .then(response => {
                    console.log('Response status:', response.status);
                    if (!response.ok) {
                        throw new Error('HTTP ' + response.status);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Received data:', data);
                    if (data && Array.isArray(data) && data.length > 0) {
                        renderSuggestions(data);
                    } else {
                        hideSuggestions();
                    }
                })
                .catch(error => {
                    console.error('Fetch error:', error);
                    hideSuggestions();
                });
        }

        // Render suggestions
        function renderSuggestions(items) {
            console.log('Rendering', items.length, 'items');

            let html = '<ul class="suggestion-list">';

            items.forEach(item => {
                // Format giá
                const price = item.price ?
                    new Intl.NumberFormat('vi-VN').format(item.price) : 'Liên hệ';

                // Xử lý ảnh
                let imageUrl = item.primaryImage ||
                    window.contextPath + '/assets/img/no-image.png';

                // Nếu là đường dẫn tương đối, thêm context path
                if (imageUrl && !imageUrl.startsWith('http') && !imageUrl.startsWith('/')) {
                    imageUrl = '/' + imageUrl;
                }
                if (imageUrl.startsWith('/') && !imageUrl.startsWith(window.contextPath)) {
                    imageUrl = window.contextPath + imageUrl;
                }

                html += `
                    <li class="suggestion-item">
                        <a href="${window.contextPath}/product?id=${item.id}" class="text-decoration-none">
                            <div class="d-flex align-items-center">
                                <img src="${imageUrl}" 
                                     alt="${item.name || 'Sản phẩm'}"
                                     class="suggestion-img me-3"
                                     onerror="this.src='${window.contextPath}/assets/img/no-image.png'">
                                <div class="flex-grow-1">
                                    <div class="fw-medium text-dark">${item.name || 'Không có tên'}</div>
                                    <div class="text-muted small">
                                        ${item.brand || ''}
                                        ${item.categoryName ? ' • ' + item.categoryName : ''}
                                    </div>
                                </div>
                                <div class="text-end">
                                    <strong class="text-danger">${price}₫</strong>
                                </div>
                            </div>
                        </a>
                    </li>`;
            });

            html += '</ul>';
            suggestionsBox.innerHTML = html;
            suggestionsBox.style.display = 'block';

            console.log('Suggestions rendered');
        }

        // Ẩn suggestions
        function hideSuggestions() {
            suggestionsBox.style.display = 'none';
        }
    }

})();