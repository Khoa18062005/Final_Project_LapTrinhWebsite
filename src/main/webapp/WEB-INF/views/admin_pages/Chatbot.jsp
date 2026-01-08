<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="chatbot-section">
    <!-- Section Header -->
    <div class="section-header">
        <h2>AI Assistant</h2>
        <div style="display: flex; gap: 12px;">
            <button class="btn btn-secondary" onclick="clearChatHistory()">
                <i class="fas fa-eraser"></i> Xóa lịch sử
            </button>
        </div>
    </div>

    <div class="chatbot-container">
        <!-- Chat Header -->
        <div class="chat-header">
            <div class="chat-header-left">
                <div class="chat-header-icon">
                    <i class="fas fa-robot"></i>
                </div>
                <div class="chat-header-info">
                    <h3>VietTech AI Assistant</h3>
                    <div class="chat-status">
                        <span class="status-dot"></span>
                        <span>Đang hoạt động</span>
                    </div>
                </div>
            </div>
            <div class="chat-header-actions">
                <button class="chat-action-btn" onclick="minimizeChat()" title="Thu nhỏ">
                    <i class="fas fa-minus"></i>
                </button>
                <button class="chat-action-btn" onclick="expandChat()" title="Mở rộng">
                    <i class="fas fa-expand"></i>
                </button>
            </div>
        </div>

        <!-- Chat Messages -->
        <div id="chat-box">
            <!-- Welcome Message -->
            <div class="message bot">
                <div class="message-avatar">
                    <i class="fas fa-robot"></i>
                </div>
                <div class="message-bubble">
                    <div class="message-content">
                        <p><strong>Xin chào!</strong> Tôi là trợ lý AI của VietTech.</p>
                        <p style="margin-top: 12px;">Tôi có thể giúp bạn:</p>
                        <ul class="feature-list">
                            <li><i class="fas fa-box"></i> Tra cứu thông tin đơn hàng</li>
                            <li><i class="fas fa-chart-line"></i> Thống kê doanh thu</li>
                            <li><i class="fas fa-fire"></i> Sản phẩm bán chạy</li>
                            <li><i class="fas fa-user-friends"></i> Phân tích khách hàng</li>
                        </ul>
                    </div>
                    <div class="message-time">Vừa xong</div>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="quick-actions-container">
            <div class="quick-actions-label">Gợi ý nhanh:</div>
            <div class="quick-actions">
                <button class="quick-action-chip" onclick="sendQuickMessage('Tổng doanh thu hôm nay là bao nhiêu?')">
                    <i class="fas fa-coins"></i> Doanh thu hôm nay
                </button>
                <button class="quick-action-chip" onclick="sendQuickMessage('Top 5 sản phẩm bán chạy nhất')">
                    <i class="fas fa-medal"></i> Top sản phẩm
                </button>
                <button class="quick-action-chip" onclick="sendQuickMessage('Có bao nhiêu đơn hàng chờ xử lý?')">
                    <i class="fas fa-hourglass-half"></i> Đơn chờ xử lý
                </button>
                <button class="quick-action-chip" onclick="sendQuickMessage('Khách hàng mới trong tuần này')">
                    <i class="fas fa-user-plus"></i> Khách mới
                </button>
            </div>
        </div>

        <!-- Chat Input -->
        <div class="chat-input-container">
            <div class="chat-input-wrapper">
                <div class="input-actions">
                    <button class="input-action-btn" title="Đính kèm file">
                        <i class="fas fa-paperclip"></i>
                    </button>
                </div>
                <input type="text" id="user-input" placeholder="Nhập câu hỏi của bạn..."
                       autocomplete="off">
                <button class="chat-send-btn" onclick="sendMessage()" id="sendBtn" title="Gửi tin nhắn">
                    <i class="fas fa-paper-plane"></i>
                </button>
            </div>
            <div class="input-hint">
                <i class="fas fa-keyboard"></i> Nhấn Enter để gửi
            </div>
        </div>
    </div>
</div>



