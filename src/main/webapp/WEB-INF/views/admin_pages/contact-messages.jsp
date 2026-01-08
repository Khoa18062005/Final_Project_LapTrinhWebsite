<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Section Header -->
<div class="section-header">
    <h2>Tin nhắn từ khách hàng</h2>
    <div class="header-actions">
        <button class="btn btn-secondary" onclick="refreshContactMessages()">
            <i class="fas fa-sync-alt"></i> Làm mới
        </button>
    </div>
</div>

<!-- Alert Messages -->
<c:if test="${not empty param.contactMessage}">
    <div class="alert alert-success">
        <i class="fas fa-check-circle"></i>
        <c:choose>
            <c:when test="${param.contactMessage == 'replied'}">Đã gửi phản hồi thành công!</c:when>
            <c:when test="${param.contactMessage == 'marked_read'}">Đã đánh dấu đã đọc!</c:when>
            <c:when test="${param.contactMessage == 'deleted'}">Đã xóa tin nhắn!</c:when>
            <c:otherwise>${param.contactMessage}</c:otherwise>
        </c:choose>
    </div>
</c:if>
<c:if test="${not empty param.contactError}">
    <div class="alert alert-error">
        <i class="fas fa-exclamation-triangle"></i> Lỗi: ${param.contactError}
    </div>
</c:if>

<!-- Stats Cards -->
<div class="stats-grid" style="grid-template-columns: repeat(3, 1fr); margin-bottom: 24px;">
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon" style="background: linear-gradient(135deg, #3B82F6, #2563EB);">
                <i class="fas fa-envelope"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Tổng tin nhắn</h3>
            <p class="stat-number">${totalContactMessages != null ? totalContactMessages : 0}</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon" style="background: linear-gradient(135deg, #F59E0B, #D97706);">
                <i class="fas fa-envelope-open"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Chưa đọc</h3>
            <p class="stat-number">${unreadContactMessages != null ? unreadContactMessages : 0}</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon" style="background: linear-gradient(135deg, #10B981, #059669);">
                <i class="fas fa-check-double"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Đã xử lý</h3>
            <p class="stat-number">${totalContactMessages - unreadContactMessages}</p>
        </div>
    </div>
</div>

<!-- Sentiment Analysis Summary -->
<div class="sentiment-analysis-section" style="margin-bottom: 24px;">
    <div class="section-header" style="margin-bottom: 16px;">
        <h3 style="display: flex; align-items: center; gap: 8px; margin: 0;">
            <i class="fas fa-chart-pie"></i> Phân tích cảm xúc tin nhắn
        </h3>
    </div>
    <div class="sentiment-stats-grid">
        <div class="sentiment-stat-card sentiment-positive-card">
            <div class="sentiment-stat-icon">
                <i class="fas fa-smile"></i>
            </div>
            <div class="sentiment-stat-details">
                <h4>Tích cực</h4>
                <p class="sentiment-stat-number" id="positiveCount">
                    <c:set var="positiveCount" value="0" />
                    <c:forEach var="message" items="${contactMessagesList}">
                        <c:if test="${sentimentMap[message.notificationId] == 'positive'}">
                            <c:set var="positiveCount" value="${positiveCount + 1}" />
                        </c:if>
                    </c:forEach>
                    ${positiveCount}
                </p>
                <div class="sentiment-progress-bar">
                    <div class="sentiment-progress positive-progress" style="width: ${contactMessagesList.size() > 0 ? (positiveCount * 100 / contactMessagesList.size()) : 0}%"></div>
                </div>
                <p class="sentiment-percentage">${contactMessagesList.size() > 0 ? String.format("%.1f", positiveCount * 100.0 / contactMessagesList.size()) : 0}%</p>
            </div>
        </div>

        <div class="sentiment-stat-card sentiment-neutral-card">
            <div class="sentiment-stat-icon">
                <i class="fas fa-meh"></i>
            </div>
            <div class="sentiment-stat-details">
                <h4>Trung lập</h4>
                <p class="sentiment-stat-number" id="neutralCount">
                    <c:set var="neutralCount" value="0" />
                    <c:forEach var="message" items="${contactMessagesList}">
                        <c:if test="${sentimentMap[message.notificationId] == 'neutral'}">
                            <c:set var="neutralCount" value="${neutralCount + 1}" />
                        </c:if>
                    </c:forEach>
                    ${neutralCount}
                </p>
                <div class="sentiment-progress-bar">
                    <div class="sentiment-progress neutral-progress" style="width: ${contactMessagesList.size() > 0 ? (neutralCount * 100 / contactMessagesList.size()) : 0}%"></div>
                </div>
                <p class="sentiment-percentage">${contactMessagesList.size() > 0 ? String.format("%.1f", neutralCount * 100.0 / contactMessagesList.size()) : 0}%</p>
            </div>
        </div>

        <div class="sentiment-stat-card sentiment-negative-card">
            <div class="sentiment-stat-icon">
                <i class="fas fa-frown"></i>
            </div>
            <div class="sentiment-stat-details">
                <h4>Tiêu cực</h4>
                <p class="sentiment-stat-number" id="negativeCount">
                    <c:set var="negativeCount" value="0" />
                    <c:forEach var="message" items="${contactMessagesList}">
                        <c:if test="${sentimentMap[message.notificationId] == 'negative'}">
                            <c:set var="negativeCount" value="${negativeCount + 1}" />
                        </c:if>
                    </c:forEach>
                    ${negativeCount}
                </p>
                <div class="sentiment-progress-bar">
                    <div class="sentiment-progress negative-progress" style="width: ${contactMessagesList.size() > 0 ? (negativeCount * 100 / contactMessagesList.size()) : 0}%"></div>
                </div>
                <p class="sentiment-percentage">${contactMessagesList.size() > 0 ? String.format("%.1f", negativeCount * 100.0 / contactMessagesList.size()) : 0}%</p>
            </div>
        </div>
    </div>
</div>

<!-- Filter Section -->
<div class="filter-section" style="margin-bottom: 20px;">
    <form action="${pageContext.request.contextPath}/admin" method="GET" style="display: flex; gap: 12px; flex-wrap: wrap; align-items: center;">
        <input type="hidden" name="section" value="contact-messages">

        <select name="statusFilter" class="form-control" style="width: auto; min-width: 150px;">
            <option value="">Tất cả trạng thái</option>
            <option value="unread" ${param.statusFilter == 'unread' ? 'selected' : ''}>Chưa đọc</option>
            <option value="read" ${param.statusFilter == 'read' ? 'selected' : ''}>Đã đọc</option>
        </select>

        <button type="submit" class="btn btn-primary">
            <i class="fas fa-filter"></i> Lọc
        </button>

        <a href="${pageContext.request.contextPath}/admin?section=contact-messages" class="btn btn-secondary">
            <i class="fas fa-times"></i> Xóa bộ lọc
        </a>
    </form>
</div>

<!-- Messages List -->
<div class="contact-messages-container">
    <c:choose>
        <c:when test="${not empty contactMessagesList}">
            <div class="messages-list">
                <c:forEach var="message" items="${contactMessagesList}">
                    <div class="message-card ${message.read ? '' : 'unread'}" data-id="${message.notificationId}">
                        <div class="message-header">
                            <div class="message-sender">
                                <div class="sender-avatar">
                                    <i class="fas fa-user"></i>
                                </div>
                                <div class="sender-info">
                                    <h4 class="sender-name">${message.title}</h4>
                                    <span class="message-date">
                                        <i class="fas fa-clock"></i>
                                        <fmt:formatDate value="${message.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </span>
                                </div>
                            </div>
                            <div class="message-status">
                                <!-- Sentiment Analysis Badge -->
                                <c:set var="sentiment" value="${sentimentMap[message.notificationId]}" />
                                <c:choose>
                                    <c:when test="${sentiment == 'positive'}">
                                        <span class="sentiment-badge sentiment-positive">
                                            <i class="fas fa-smile"></i> Tích cực
                                        </span>
                                    </c:when>
                                    <c:when test="${sentiment == 'negative'}">
                                        <span class="sentiment-badge sentiment-negative">
                                            <i class="fas fa-frown"></i> Tiêu cực
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="sentiment-badge sentiment-neutral">
                                            <i class="fas fa-meh"></i> Trung lập
                                        </span>
                                    </c:otherwise>
                                </c:choose>

                                <!-- Read Status Badge -->
                                <c:choose>
                                    <c:when test="${message.read}">
                                        <span class="status-badge status-read">
                                            <i class="fas fa-check"></i> Đã đọc
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-unread">
                                            <i class="fas fa-circle"></i> Chưa đọc
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="message-body">
                            <p class="message-content">${message.message}</p>

                            <c:if test="${not empty message.data}">
                                <div class="message-extra-info">
                                    <small class="text-muted">
                                        <i class="fas fa-info-circle"></i> Thông tin bổ sung: ${message.data}
                                    </small>
                                </div>
                            </c:if>
                        </div>

                        <div class="message-actions">
                            <c:if test="${not message.read}">
                                <form action="${pageContext.request.contextPath}/admin" method="POST" style="display: inline;">
                                    <input type="hidden" name="action" value="mark_contact_read">
                                    <input type="hidden" name="notificationId" value="${message.notificationId}">
                                    <button type="submit" class="btn btn-sm btn-secondary">
                                        <i class="fas fa-check"></i> Đánh dấu đã đọc
                                    </button>
                                </form>
                            </c:if>

                            <button type="button" class="btn btn-sm btn-primary"
                                    onclick="openReplyModal(${message.notificationId}, '${fn:escapeXml(message.title)}', ${message.userId})">
                                <i class="fas fa-reply"></i> Phản hồi
                            </button>

                            <form action="${pageContext.request.contextPath}/admin" method="POST" style="display: inline;"
                                  onsubmit="return confirm('Bạn có chắc muốn xóa tin nhắn này?');">
                                <input type="hidden" name="action" value="delete_contact_message">
                                <input type="hidden" name="notificationId" value="${message.notificationId}">
                                <button type="submit" class="btn btn-sm btn-danger">
                                    <i class="fas fa-trash"></i> Xóa
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="pagination-container" style="margin-top: 20px; text-align: center;">
                    <nav aria-label="Page navigation">
                        <ul class="pagination" style="justify-content: center;">
                            <c:if test="${currentPage > 1}">
                                <li class="page-item">
                                    <a class="page-link" href="${pageContext.request.contextPath}/admin?section=contact-messages&page=${currentPage - 1}">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </li>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/admin?section=contact-messages&page=${i}">${i}</a>
                                </li>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <li class="page-item">
                                    <a class="page-link" href="${pageContext.request.contextPath}/admin?section=contact-messages&page=${currentPage + 1}">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </c:if>
        </c:when>
        <c:otherwise>
            <div class="empty-state" style="text-align: center; padding: 60px 20px;">
                <i class="fas fa-inbox" style="font-size: 64px; color: var(--text-muted); margin-bottom: 20px;"></i>
                <h3 style="color: var(--text-muted); margin-bottom: 10px;">Chưa có tin nhắn nào</h3>
                <p style="color: var(--text-muted);">Các tin nhắn từ khách hàng sẽ xuất hiện tại đây</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Reply Modal -->
<div id="replyModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-reply"></i> Phản hồi tin nhắn</h3>
            <button class="modal-close" onclick="closeModal('replyModal')">&times;</button>
        </div>
        <div class="modal-body">
            <form id="replyForm" action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="reply_contact_message">
                <input type="hidden" id="replyNotificationId" name="notificationId" value="">
                <input type="hidden" id="replyUserId" name="userId" value="">

                <div class="form-group">
                    <label>Gửi đến:</label>
                    <p id="replyToName" style="font-weight: 600; color: var(--text-color);"></p>
                </div>

                <div class="form-group">
                    <label for="replyTitle">Tiêu đề phản hồi <span class="required">*</span></label>
                    <input type="text" id="replyTitle" name="replyTitle" class="form-control" required
                           placeholder="Nhập tiêu đề phản hồi...">
                </div>

                <div class="form-group">
                    <label for="replyMessage">Nội dung phản hồi <span class="required">*</span></label>
                    <textarea id="replyMessage" name="replyMessage" class="form-control" rows="5" required
                              placeholder="Nhập nội dung phản hồi của bạn..."></textarea>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeModal('replyModal')">Hủy</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-paper-plane"></i> Gửi phản hồi
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<style>
/* Contact Messages Styles */
.contact-messages-container {
    background: var(--card-bg);
    border-radius: 12px;
    padding: 20px;
}

/* Sentiment Analysis Summary Section */
.sentiment-analysis-section {
    background: var(--card-bg);
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.sentiment-stats-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
}

.sentiment-stat-card {
    background: white;
    border-radius: 10px;
    padding: 20px;
    display: flex;
    gap: 16px;
    transition: all 0.3s ease;
    border: 2px solid transparent;
}

.sentiment-stat-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.sentiment-positive-card {
    border-color: #6EE7B7;
    background: linear-gradient(135deg, #ECFDF5, #D1FAE5);
}

.sentiment-neutral-card {
    border-color: #D1D5DB;
    background: linear-gradient(135deg, #F9FAFB, #F3F4F6);
}

.sentiment-negative-card {
    border-color: #FCA5A5;
    background: linear-gradient(135deg, #FEF2F2, #FEE2E2);
}

.sentiment-stat-icon {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28px;
    flex-shrink: 0;
}

.sentiment-positive-card .sentiment-stat-icon {
    background: linear-gradient(135deg, #10B981, #059669);
    color: white;
}

.sentiment-neutral-card .sentiment-stat-icon {
    background: linear-gradient(135deg, #6B7280, #4B5563);
    color: white;
}

.sentiment-negative-card .sentiment-stat-icon {
    background: linear-gradient(135deg, #EF4444, #DC2626);
    color: white;
}

.sentiment-stat-details {
    flex: 1;
}

.sentiment-stat-details h4 {
    margin: 0 0 8px 0;
    font-size: 14px;
    font-weight: 600;
    color: #374151;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.sentiment-stat-number {
    margin: 0 0 12px 0;
    font-size: 32px;
    font-weight: 700;
    line-height: 1;
}

.sentiment-positive-card .sentiment-stat-number {
    color: #059669;
}

.sentiment-neutral-card .sentiment-stat-number {
    color: #4B5563;
}

.sentiment-negative-card .sentiment-stat-number {
    color: #DC2626;
}

.sentiment-progress-bar {
    width: 100%;
    height: 8px;
    background: rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    overflow: hidden;
    margin-bottom: 8px;
}

.sentiment-progress {
    height: 100%;
    border-radius: 4px;
    transition: width 0.5s ease;
}

.positive-progress {
    background: linear-gradient(90deg, #10B981, #059669);
}

.neutral-progress {
    background: linear-gradient(90deg, #6B7280, #4B5563);
}

.negative-progress {
    background: linear-gradient(90deg, #EF4444, #DC2626);
}

.sentiment-percentage {
    margin: 0;
    font-size: 13px;
    font-weight: 600;
    color: #6B7280;
}

/* Messages List */
.messages-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.message-card {
    background: var(--bg-color);
    border: 1px solid var(--border-color);
    border-radius: 10px;
    padding: 20px;
    transition: all 0.3s ease;
}

.message-card:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.message-card.unread {
    border-left: 4px solid #F59E0B;
    background: linear-gradient(to right, rgba(245, 158, 11, 0.05), transparent);
}

.message-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 16px;
}

.message-sender {
    display: flex;
    align-items: center;
    gap: 12px;
}

.sender-avatar {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    background: linear-gradient(135deg, #3B82F6, #2563EB);
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 20px;
}

.sender-info {
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.sender-name {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-color);
    margin: 0;
}

.message-date {
    font-size: 13px;
    color: var(--text-muted);
    display: flex;
    align-items: center;
    gap: 6px;
}

.message-status {
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: flex-end;
}

/* Sentiment Analysis Badges */
.sentiment-badge {
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
    display: inline-flex;
    align-items: center;
    gap: 6px;
    transition: all 0.3s ease;
}

.sentiment-positive {
    background: linear-gradient(135deg, #D1FAE5, #A7F3D0);
    color: #065F46;
    border: 1px solid #6EE7B7;
}

.sentiment-positive:hover {
    background: linear-gradient(135deg, #A7F3D0, #6EE7B7);
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.sentiment-negative {
    background: linear-gradient(135deg, #FEE2E2, #FECACA);
    color: #991B1B;
    border: 1px solid #FCA5A5;
}

.sentiment-negative:hover {
    background: linear-gradient(135deg, #FECACA, #FCA5A5);
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
}

.sentiment-neutral {
    background: linear-gradient(135deg, #F3F4F6, #E5E7EB);
    color: #374151;
    border: 1px solid #D1D5DB;
}

.sentiment-neutral:hover {
    background: linear-gradient(135deg, #E5E7EB, #D1D5DB);
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(107, 114, 128, 0.3);
}

/* Status Badges */
.status-badge {
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
    display: flex;
    align-items: center;
    gap: 6px;
}

.status-unread {
    background: #FEF3C7;
    color: #D97706;
}

.status-read {
    background: #D1FAE5;
    color: #059669;
}

.message-body {
    margin-bottom: 16px;
}

.message-content {
    color: var(--text-color);
    line-height: 1.6;
    margin: 0;
    white-space: pre-wrap;
}

.message-extra-info {
    margin-top: 12px;
    padding: 10px;
    background: var(--card-bg);
    border-radius: 6px;
}

.message-actions {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    padding-top: 16px;
    border-top: 1px solid var(--border-color);
}

.message-actions .btn {
    display: inline-flex;
    align-items: center;
    gap: 6px;
}

/* Responsive */
@media (max-width: 768px) {
    /* Sentiment Analysis Grid - Stack on mobile */
    .sentiment-stats-grid {
        grid-template-columns: 1fr;
    }

    .sentiment-stat-card {
        padding: 16px;
    }

    .sentiment-stat-icon {
        width: 50px;
        height: 50px;
        font-size: 24px;
    }

    .sentiment-stat-number {
        font-size: 28px;
    }

    /* Message Cards */
    .message-header {
        flex-direction: column;
        gap: 12px;
    }

    .message-actions {
        flex-direction: column;
    }

    .message-actions form,
    .message-actions .btn {
        width: 100%;
    }

    .message-actions .btn {
        justify-content: center;
    }

    /* Message Status - Stack badges */
    .message-status {
        align-items: flex-start;
        width: 100%;
    }
}

@media (max-width: 992px) {
    /* Sentiment Analysis - 2 columns on tablet */
    .sentiment-stats-grid {
        grid-template-columns: repeat(2, 1fr);
    }

    .sentiment-stat-card:last-child {
        grid-column: span 2;
    }
}
</style>

<script>
// Open reply modal
function openReplyModal(notificationId, title, userId) {
    document.getElementById('replyNotificationId').value = notificationId;
    document.getElementById('replyUserId').value = userId;
    document.getElementById('replyToName').textContent = title;
    document.getElementById('replyTitle').value = 'Re: ' + title;
    openModal('replyModal');
}

// Refresh contact messages
function refreshContactMessages() {
    window.location.href = '${pageContext.request.contextPath}/admin?section=contact-messages';
}
</script>

