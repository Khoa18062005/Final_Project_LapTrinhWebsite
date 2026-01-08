// fireworks.js - Hiệu ứng pháo hoa đẹp và mượt

document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('fireworks-container');
    if (!container) {
        console.warn('Không tìm thấy #fireworks-container trong DOM');
        return;
    }

    // Danh sách màu sắc lung linh
    const colors = [
        '#ff006e',  // Hồng neon
        '#3a86ff',  // Xanh dương
        '#06ffa5',  // Xanh lá mint
        '#ffbe0b',  // Vàng cam
        '#fb5607',  // Cam đỏ
        '#8338ec',  // Tím
        '#ff70a6',  // Hồng phấn
        '#00f5ff'   // Cyan
    ];

    function createFirework() {
        const firework = document.createElement('div');
        firework.classList.add('firework-explosion');

        // Vị trí ngẫu nhiên (ưu tiên nửa trên màn hình để đẹp hơn)
        const x = Math.random() * window.innerWidth;
        const y = Math.random() * (window.innerHeight * 0.7) + (window.innerHeight * 0.1);

        firework.style.left = x + 'px';
        firework.style.top = y + 'px';

        container.appendChild(firework);

        // Số lượng particle (30-50 cho mỗi vụ nổ)
        const particleCount = 30 + Math.floor(Math.random() * 25);

        for (let i = 0; i < particleCount; i++) {
            const particle = document.createElement('div');
            particle.classList.add('particle');

            const angle = (i / particleCount) * 360;
            const velocity = 5 + Math.random() * 8; // Tốc độ bay
            const color = colors[Math.floor(Math.random() * colors.length)];

            particle.style.background = color;
            particle.style.boxShadow = `0 0 12px ${color}, 0 0 20px ${color}`;

            const distance = 100 + Math.random() * 100; // Khoảng cách bay xa
            const dx = Math.cos(angle * Math.PI / 180) * velocity * distance / 10;
            const dy = Math.sin(angle * Math.PI / 180) * velocity * distance / 10;

            const duration = 1.2 + Math.random() * 1; // Thời gian nổ

            particle.style.setProperty('--dx', `${dx}px`);
            particle.style.setProperty('--dy', `${dy}px`);
            particle.style.animationDuration = `${duration}s`;

            firework.appendChild(particle);
        }

        // Xóa vụ nổ sau khi hoàn tất để tránh đầy DOM
        setTimeout(() => {
            if (firework.parentNode) {
                firework.remove();
            }
        }, 2500);
    }

    // Tạo pháo hoa định kỳ
    setInterval(createFirework, 1300);

    // Bonus: Tạo pháo hoa khi click chuột (rất vui!)
    document.addEventListener('click', function (e) {
        const firework = document.createElement('div');
        firework.classList.add('firework-explosion');
        firework.style.left = e.clientX + 'px';
        firework.style.top = e.clientY + 'px';
        container.appendChild(firework);

        const particleCount = 40;
        const colorsClick = ['#ff006e', '#ffd60a', '#06ffa5', '#3a86ff', '#ff70a6'];

        for (let i = 0; i < particleCount; i++) {
            const particle = document.createElement('div');
            particle.classList.add('particle');
            const angle = (i / particleCount) * 360;
            const velocity = 6 + Math.random() * 7;
            const color = colorsClick[Math.floor(Math.random() * colorsClick.length)];

            particle.style.background = color;
            particle.style.boxShadow = `0 0 15px ${color}`;

            const distance = 120 + Math.random() * 80;
            const dx = Math.cos(angle * Math.PI / 180) * velocity * distance / 10;
            const dy = Math.sin(angle * Math.PI / 180) * velocity * distance / 10;

            particle.style.setProperty('--dx', `${dx}px`);
            particle.style.setProperty('--dy', `${dy}px`);
            particle.style.animationDuration = '1.3s';

            firework.appendChild(particle);
        }
        setTimeout(() => firework.remove(), 2000);
    });
});