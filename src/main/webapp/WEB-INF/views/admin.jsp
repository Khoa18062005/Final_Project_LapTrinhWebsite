<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - VietTech</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        /* CSS nhanh cho khung chat nằm gọn trong dashboard */
        .chat-container {
            display: flex;
            flex-direction: column;
            height: 80vh; /* Chiều cao cố định */
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        .chat-messages {
            flex: 1;
            padding: 20px;
            overflow-y: auto;
            background: #f8f9fa;
        }
        .chat-input-area {
            padding: 20px;
            border-top: 1px solid #eee;
            display: flex;
            gap: 10px;
        }
        .message {
            margin-bottom: 15px;
            max-width: 80%;
            padding: 10px 15px;
            border-radius: 15px;
            line-height: 1.5;
        }
        .message.user {
            background: #007bff;
            color: white;
            margin-left: auto; /* Đẩy sang phải */
            border-bottom-right-radius: 2px;
        }
        .message.bot {
            background: #e9ecef;
            color: #333;
            margin-right: auto; /* Đẩy sang trái */
            border-bottom-left-radius: 2px;
        }
    </style>
</head>
<body>
<div class="sidebar">
    <div class="logo">
        <h2>Viet<span>Tech</span></h2>
    </div>
    <nav class="nav-menu">
        <div class="nav-menu-section">
            <a href="#dashboard" class="nav-item active" onclick="showSection('dashboard')">
                <i class="fas fa-chart-pie"></i>
                <span>Dashboard</span>
            </a>
            <a href="#products" class="nav-item" onclick="showSection('products')">
                <i class="fas fa-shopping-bag"></i>
                <span>Sản phẩm</span>
            </a>
            <a href="#voucher" class="nav-item" onclick="showSection('voucher')">
                <i class="fas fa-ticket-alt"></i>
                <span>Voucher</span>
            </a>
            <a href="#orders" class="nav-item" onclick="showSection('orders')">
                <i class="fas fa-list-ul"></i>
                <span>Đơn hàng</span>
            </a>
            <a href="#users" class="nav-item" onclick="showSection('users')">
                <i class="fas fa-users"></i>
                <span>Người dùng</span>
            </a>
            <a href="#chatbot" class="nav-item" onclick="showSection('chatbot')">
                <i class="fas fa-robot"></i>
                <span>AI Chatbot</span>
            </a>
        </div>
    </nav>
    <div class="sidebar-footer">
        <a href="${pageContext.request.contextPath}/logout" class="nav-item">
            <i class="fas fa-sign-out-alt"></i>
            <span>Đăng xuất</span>
        </a>
    </div>
</div>

<div class="main-content">
    <div class="top-navbar">
        <div class="navbar-left">
            <button class="menu-toggle" onclick="toggleSidebar()">
                <i class="fas fa-bars"></i>
            </button>
            <h1 id="page-title">Dashboard</h1>
        </div>
        <div class="navbar-right">
            <div class="user-profile">
                <img src="${pageContext.request.contextPath}/assets/PNG/AVT.png" alt="Admin">
                <div class="user-profile-info">
                    <div class="name">${sessionScope.user.firstName} ${sessionScope.user.lastName}</div>
                    <div class="role">Admin</div>
                </div>
            </div>
        </div>
    </div>

    <div id="dashboard" class="content-section active">
        <jsp:include page="/WEB-INF/views/admin_pages/dashboard.jsp" />
    </div>

    <div id="products" class="content-section">
        <jsp:include page="/WEB-INF/views/admin_pages/products.jsp" />
    </div>

    <div id="voucher" class="content-section">
        <jsp:include page="/WEB-INF/views/admin_pages/voucher.jsp" />
    </div>

    <div id="orders" class="content-section">
        <h2>Quản lý đơn hàng</h2>
        <p>Đang phát triển...</p>
    </div>

    <div id="users" class="content-section">
        <%-- <jsp:include page="/WEB-INF/views/admin_pages/users.jsp" /> --%>
    </div>

    <div id="chatbot" class="content-section">
        <jsp:include page="/WEB-INF/views/admin_pages/chatbot.jsp" />
    </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>

<script>
    // Hàm gửi tin nhắn
    function sendMessage() {
        const input = document.getElementById('user-input');
        const message = input.value.trim();
        if (!message) return;

        // 1. Hiển thị tin nhắn người dùng
        appendMessage(message, 'user');
        input.value = '';

        // 2. Hiện loading...
        const loadingId = showTypingIndicator();

        // 3. Gọi Servlet bằng AJAX (Fetch API)
        fetch('${pageContext.request.contextPath}/admin/chatbot', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: 'message=' + encodeURIComponent(message)
        })
            .then(response => response.json())
            .then(data => {
                // Xóa loading và hiện câu trả lời
                hideTypingIndicator(loadingId);
                if (data.success) {
                    appendMessage(data.message, 'bot');
                } else {
                    appendMessage('❌ ' + data.message, 'bot');
                }
            })
            .catch(error => {
                hideTypingIndicator(loadingId);
                appendMessage('❌ Lỗi kết nối: ' + error, 'bot');
            });
    }

    // Hàm render tin nhắn ra màn hình
    function appendMessage(text, sender) {
        const chatBox = document.getElementById('chat-box');
        const msgDiv = document.createElement('div');
        msgDiv.className = 'message ' + sender;

        const avatar = sender === 'user'
            ? '<i class="fas fa-user"></i>'
            : '<i class="fas fa-robot"></i>';

        // Xử lý format text
        let formattedText = text
            .replace(/\n/g, '<br>')
            .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
            .replace(/`(.*?)`/g, '<code>$1</code>');

        msgDiv.innerHTML =
            '<div class="message-avatar">' + avatar + '</div>' +
            '<div class="message-content">' + formattedText + '</div>';

        chatBox.appendChild(msgDiv);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    // Hiện typing indicator
    function showTypingIndicator() {
        const chatBox = document.getElementById('chat-box');
        const typingDiv = document.createElement('div');
        const typingId = 'typing-' + Date.now();
        typingDiv.id = typingId;
        typingDiv.className = 'message bot';
        typingDiv.innerHTML =
            '<div class="message-avatar"><i class="fas fa-robot"></i></div>' +
            '<div class="typing-indicator">' +
                '<div class="typing-dot"></div>' +
                '<div class="typing-dot"></div>' +
                '<div class="typing-dot"></div>' +
            '</div>';
        chatBox.appendChild(typingDiv);
        chatBox.scrollTop = chatBox.scrollHeight;
        return typingId;
    }

    // Ẩn typing indicator
    function hideTypingIndicator(typingId) {
        const typingDiv = document.getElementById(typingId);
        if (typingDiv) {
            typingDiv.remove();
        }
    }

    // Enter để gửi
    document.addEventListener("DOMContentLoaded", function() {
        const input = document.getElementById("user-input");
        if(input){
            input.addEventListener("keypress", function(event) {
                if (event.key === "Enter") {
                    event.preventDefault();
                    sendMessage();
                }
            });
        }
    });
</script>
</body>
</html>