<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 12/28/2025
  Time: 6:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    .chatbot-wrapper {
        padding: 24px;
    }

    .chatbot-container {
        display: flex;
        flex-direction: column;
        height: calc(100vh - 180px);
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        overflow: hidden;
    }

    .chat-header {
        padding: 20px 24px;
        background: linear-gradient(135deg, #4880ff, #6e9bff);
        color: white;
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .chat-header-icon {
        width: 48px;
        height: 48px;
        background: rgba(255,255,255,0.2);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
    }

    .chat-header-info h2 {
        font-size: 18px;
        font-weight: 700;
        margin-bottom: 4px;
    }

    .chat-header-info p {
        font-size: 13px;
        opacity: 0.9;
    }

    #chat-box {
        flex: 1;
        padding: 24px;
        overflow-y: auto;
        display: flex;
        flex-direction: column;
        gap: 16px;
        background: #f8f9fc;
    }

    .message {
        display: flex;
        gap: 12px;
        max-width: 80%;
        animation: fadeIn 0.3s ease;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0); }
    }

    .message.user {
        align-self: flex-end;
        flex-direction: row-reverse;
    }

    .message.bot {
        align-self: flex-start;
    }

    .message-avatar {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;
        font-size: 16px;
    }

    .message.bot .message-avatar {
        background: linear-gradient(135deg, #4880ff, #6e9bff);
        color: white;
    }

    .message.user .message-avatar {
        background: #e0e7ff;
        color: #4880ff;
    }

    .message-content {
        padding: 12px 16px;
        border-radius: 16px;
        font-size: 14px;
        line-height: 1.6;
        white-space: pre-wrap;
        word-break: break-word;
    }

    .message.bot .message-content {
        background: white;
        border: 1px solid #e5e7eb;
        border-bottom-left-radius: 4px;
        color: #333;
    }

    .message.user .message-content {
        background: linear-gradient(135deg, #4880ff, #6e9bff);
        color: white;
        border-bottom-right-radius: 4px;
    }

    .chat-input-container {
        padding: 20px 24px;
        background: white;
        border-top: 1px solid #e5e7eb;
    }

    .quick-actions {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
        margin-bottom: 12px;
    }

    .quick-action-btn {
        padding: 8px 14px;
        background: #e0e7ff;
        color: #4880ff;
        border: none;
        border-radius: 20px;
        font-size: 13px;
        cursor: pointer;
        transition: all 0.2s ease;
        font-weight: 600;
    }

    .quick-action-btn:hover {
        background: #4880ff;
        color: white;
    }

    .chat-input-wrapper {
        display: flex;
        gap: 12px;
        align-items: flex-end;
    }

    #user-input {
        flex: 1;
        padding: 14px 18px;
        border: 2px solid #e5e7eb;
        border-radius: 24px;
        font-size: 14px;
        outline: none;
        transition: border-color 0.3s ease;
        font-family: inherit;
    }

    #user-input:focus {
        border-color: #4880ff;
    }

    .chat-send-btn {
        width: 48px;
        height: 48px;
        border: none;
        background: linear-gradient(135deg, #4880ff, #6e9bff);
        color: white;
        border-radius: 50%;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        transition: transform 0.2s ease, box-shadow 0.2s ease;
    }

    .chat-send-btn:hover {
        transform: scale(1.05);
        box-shadow: 0 4px 12px rgba(72, 128, 255, 0.4);
    }

    .chat-send-btn:disabled {
        background: #9ca3af;
        cursor: not-allowed;
        transform: none;
        box-shadow: none;
    }

    .typing-indicator {
        display: flex;
        gap: 4px;
        align-items: center;
        padding: 12px 16px;
        background: white;
        border: 1px solid #e5e7eb;
        border-radius: 16px;
        border-bottom-left-radius: 4px;
        width: fit-content;
    }

    .typing-dot {
        width: 8px;
        height: 8px;
        background: #4880ff;
        border-radius: 50%;
        animation: typingBounce 1.4s infinite ease-in-out;
    }

    .typing-dot:nth-child(1) { animation-delay: -0.32s; }
    .typing-dot:nth-child(2) { animation-delay: -0.16s; }

    @keyframes typingBounce {
        0%, 80%, 100% { transform: scale(0.8); opacity: 0.5; }
        40% { transform: scale(1); opacity: 1; }
    }

    /* Styling for bot responses */
    .message.bot .message-content code {
        background: #f3f4f6;
        padding: 2px 6px;
        border-radius: 4px;
        font-family: 'Consolas', 'Monaco', monospace;
        font-size: 13px;
    }

    .message.bot .message-content strong {
        font-weight: 700;
    }
</style>

<div class="chatbot-wrapper">
    <div class="chatbot-container">
        <div class="chat-header">
            <div class="chat-header-icon">
                <i class="fas fa-robot"></i>
            </div>
            <div class="chat-header-info">
                <h2>VietTech AI Assistant</h2>
                <p>Hỏi đáp về dữ liệu kinh doanh, thống kê đơn hàng, sản phẩm...</p>
            </div>
        </div>

        <div id="chat-box">
            <!-- Tin nhắn chào mừng -->
            <div class="message bot">
                <div class="message-avatar">
                    <i class="fas fa-robot"></i>
                </div>
                <div class="message-content">Xin chào! 👋 Tôi là trợ lý AI của VietTech.

Tôi có thể giúp bạn truy vấn dữ liệu về:
📦 Thông tin đơn hàng
📊 Thống kê doanh thu
🛍️ Sản phẩm bán chạy
👥 Thông tin khách hàng

Hãy đặt câu hỏi để bắt đầu!</div>
            </div>
        </div>

        <div class="chat-input-container">
            <div class="quick-actions">
                <button class="quick-action-btn" onclick="sendQuickMessage('Tổng doanh thu hôm nay')">
                    💰 Doanh thu hôm nay
                </button>
                <button class="quick-action-btn" onclick="sendQuickMessage('Top 5 sản phẩm bán chạy nhất')">
                    🏆 Top sản phẩm
                </button>
                <button class="quick-action-btn" onclick="sendQuickMessage('Số đơn hàng chờ xử lý')">
                    📋 Đơn chờ xử lý
                </button>
                <button class="quick-action-btn" onclick="sendQuickMessage('Thống kê khách hàng mới trong tháng')">
                    👥 Khách hàng mới
                </button>
            </div>
            <div class="chat-input-wrapper">
                <input type="text" id="user-input" placeholder="Nhập câu hỏi của bạn..." autocomplete="off">
                <button class="chat-send-btn" onclick="sendMessage()">
                    <i class="fas fa-paper-plane"></i>
                </button>
            </div>
        </div>
    </div>
</div>

<script>
    function sendQuickMessage(message) {
        document.getElementById('user-input').value = message;
        sendMessage();
    }
</script>
