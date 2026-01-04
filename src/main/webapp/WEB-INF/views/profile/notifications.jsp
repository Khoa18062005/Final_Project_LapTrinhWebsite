<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Thông Báo Của Tôi" />
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<!-- MAIN CONTENT -->
<div class="col-lg-10 col-md-9">
  <div class="profile-content">
    <!-- Header -->
    <div class="profile-header d-flex justify-content-between align-items-center">
      <div>
        <h4><i class="bi bi-bell me-2"></i>Thông Báo Của Tôi</h4>
        <p class="text-muted mb-0">Quản lý và xem các thông báo từ VietTech</p>
      </div>

      <div class="d-flex gap-2">
        <c:if test="${unreadCount > 0}">
          <button type="button" class="btn btn-primary btn-mark-all-read"
                  data-bs-toggle="modal" data-bs-target="#markAllReadModal">
            <i class="bi bi-check-all me-2"></i>Đánh dấu đã đọc tất cả
          </button>
        </c:if>
        <a href="${pageContext.request.contextPath}/profile/notifications"
           class="btn btn-outline-secondary">
          <i class="bi bi-arrow-clockwise"></i>
        </a>
      </div>
    </div>

    <!-- Thống kê -->
    <div class="row mt-4">
      <div class="col-md-6">
        <div class="notification-stats-card">
          <div class="stats-icon">
            <i class="bi bi-bell"></i>
          </div>
          <div class="stats-info">
            <h6>Tổng số thông báo</h6>
            <h4>${totalNotifications}</h4>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div class="notification-stats-card">
          <div class="stats-icon unread">
            <i class="bi bi-bell-fill"></i>
          </div>
          <div class="stats-info">
            <h6>Thông báo chưa đọc</h6>
            <h4>${unreadCount}</h4>
          </div>
        </div>
      </div>
    </div>

    <!-- Danh sách thông báo -->
    <div class="notifications-list mt-4">
      <c:choose>
        <c:when test="${empty notifications}">
          <!-- Empty State -->
          <div class="empty-state text-center py-5">
            <i class="bi bi-bell-slash fs-1 text-muted mb-3 d-block"></i>
            <h5>Không có thông báo nào</h5>
            <p class="text-muted">Bạn chưa nhận được thông báo nào từ VietTech</p>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-sm mt-1">
              <i class="bi bi-arrow-left me-1"></i> Về trang chủ
            </a>
          </div>
        </c:when>
        <c:otherwise>
          <!-- Tabs -->
          <ul class="nav nav-tabs mb-4" id="notificationTabs" role="tablist">
            <li class="nav-item" role="presentation">
              <button class="nav-link active" id="all-notifications-tab" data-bs-toggle="tab"
                      data-bs-target="#all-notifications" type="button" role="tab">
                <i class="bi bi-grid me-1"></i> Tất cả
                <span class="badge bg-secondary ms-1">${totalNotifications}</span>
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button class="nav-link" id="unread-notifications-tab" data-bs-toggle="tab"
                      data-bs-target="#unread-notifications" type="button" role="tab">
                <i class="bi bi-bell-fill me-1"></i> Chưa đọc
                <span class="badge bg-primary ms-1">${unreadCount}</span>
              </button>
            </li>
          </ul>

          <!-- Tab Content -->
          <div class="tab-content" id="notificationTabContent">
            <!-- Tab: Tất cả -->
            <div class="tab-pane fade show active" id="all-notifications" role="tabpanel">
              <c:forEach var="notification" items="${notifications}">
                <div class="notification-item ${notification.read ? 'read' : 'unread'}"
                     data-notification-id="${notification.notificationId}">
                  <div class="notification-header">
                    <div class="notification-type">
                      <c:choose>
                        <c:when test="${notification.type == 'order'}">
                          <i class="bi bi-box-seam text-primary"></i>
                          <span>Đơn hàng</span>
                        </c:when>
                        <c:when test="${notification.type == 'promotion'}">
                          <i class="bi bi-tag-fill text-success"></i>
                          <span>Khuyến mãi</span>
                        </c:when>
                        <c:when test="${notification.type == 'system'}">
                          <i class="bi bi-gear-fill text-warning"></i>
                          <span>Hệ thống</span>
                        </c:when>
                        <c:when test="${notification.type == 'voucher'}">
                          <i class="bi bi-ticket-perforated text-info"></i>
                          <span>Voucher</span>
                        </c:when>
                        <c:otherwise>
                          <i class="bi bi-info-circle text-secondary"></i>
                          <span>Thông báo</span>
                        </c:otherwise>
                      </c:choose>
                    </div>
                    <div class="notification-time">
                      <fmt:formatDate value="${notification.createdAt}" pattern="HH:mm dd/MM/yyyy" />
                    </div>
                  </div>

                  <div class="notification-body">
                    <h6 class="notification-title">${notification.title}</h6>
                    <p class="notification-message">${notification.message}</p>

                    <c:if test="${not empty notification.imageUrl}">
                      <div class="notification-image mt-2">
                        <img src="${notification.imageUrl}" alt="Notification Image"
                             class="img-fluid rounded" style="max-height: 150px;">
                      </div>
                    </c:if>
                  </div>

                  <div class="notification-footer">
                    <div class="d-flex justify-content-between align-items-center">
                      <div>
                        <c:if test="${!notification.read}">
                          <span class="badge bg-primary">Mới</span>
                        </c:if>
                      </div>
                      <div class="notification-actions">
                        <c:if test="${!notification.read}">
                          <form action="${pageContext.request.contextPath}/profile/notifications/mark-read"
                                method="post" class="d-inline">
                            <input type="hidden" name="notificationId" value="${notification.notificationId}">
                            <input type="hidden" name="markAll" value="false">
                            <button type="submit" class="btn btn-sm btn-outline-success">
                              <i class="bi bi-check"></i> Đánh dấu đã đọc
                            </button>
                          </form>
                        </c:if>
                        <c:if test="${not empty notification.actionUrl}">
                          <a href="${notification.actionUrl}" class="btn btn-sm btn-primary">
                            <i class="bi bi-arrow-right"></i> Xem ngay
                          </a>
                        </c:if>
                        <!-- SỬA NÚT XÓA: BỎ data-bs-toggle VÀ data-bs-target, THÊM class và data attributes -->
                        <button type="button" class="btn btn-sm btn-outline-danger btn-delete-notification"
                                data-notification-id="${notification.notificationId}"
                                data-notification-title="${notification.title}">
                          <i class="bi bi-trash"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </c:forEach>
            </div>

            <!-- Tab: Chưa đọc -->
            <div class="tab-pane fade" id="unread-notifications" role="tabpanel">
              <c:forEach var="notification" items="${notifications}">
                <c:if test="${!notification.read}">
                  <div class="notification-item unread"
                       data-notification-id="${notification.notificationId}">
                    <div class="notification-header">
                      <div class="notification-type">
                        <c:choose>
                          <c:when test="${notification.type == 'order'}">
                            <i class="bi bi-box-seam text-primary"></i>
                            <span>Đơn hàng</span>
                          </c:when>
                          <c:when test="${notification.type == 'promotion'}">
                            <i class="bi bi-tag-fill text-success"></i>
                            <span>Khuyến mãi</span>
                          </c:when>
                          <c:otherwise>
                            <i class="bi bi-info-circle text-secondary"></i>
                            <span>Thông báo</span>
                          </c:otherwise>
                        </c:choose>
                      </div>
                      <div class="notification-time">
                        <fmt:formatDate value="${notification.createdAt}" pattern="HH:mm dd/MM/yyyy" />
                      </div>
                    </div>

                    <div class="notification-body">
                      <h6 class="notification-title">${notification.title}</h6>
                      <p class="notification-message">${notification.message}</p>
                    </div>

                    <div class="notification-footer">
                      <div class="d-flex justify-content-between align-items-center">
                        <span class="badge bg-primary">Mới</span>
                        <div class="notification-actions">
                          <button type="button" class="btn btn-sm btn-outline-success mark-as-read-btn"
                                  data-notification-id="${notification.notificationId}">
                            <i class="bi bi-check"></i> Đánh dấu đã đọc
                          </button>
                          <c:if test="${not empty notification.actionUrl}">
                            <a href="${notification.actionUrl}" class="btn btn-sm btn-primary">
                              <i class="bi bi-arrow-right"></i> Xem ngay
                            </a>
                          </c:if>
                          <!-- THÊM NÚT XÓA CHO TAB CHƯA ĐỌC -->
                          <button type="button" class="btn btn-sm btn-outline-danger btn-delete-notification"
                                  data-notification-id="${notification.notificationId}"
                                  data-notification-title="${notification.title}">
                            <i class="bi bi-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:if>
              </c:forEach>
            </div>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>

<!-- Modal Đánh dấu tất cả đã đọc -->
<div class="modal fade" id="markAllReadModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title"><i class="bi bi-check-all me-2"></i>Đánh dấu tất cả đã đọc</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <form action="${pageContext.request.contextPath}/profile/notifications/mark-read"
            method="post" id="markAllReadForm">
        <div class="modal-body">
          <p>Bạn có chắc chắn muốn đánh dấu tất cả thông báo là đã đọc không?</p>
          <p class="text-muted small">Sau khi xác nhận, tất cả thông báo sẽ được đánh dấu là đã đọc.</p>
          <input type="hidden" name="markAll" value="true">
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
          <button type="submit" class="btn btn-primary">Xác nhận</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- THÊM MODAL XÓA DUY NHẤT Ở ĐÂY -->
<div class="modal fade" id="deleteNotificationModal" tabindex="-1">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title text-danger">
          <i class="bi bi-exclamation-triangle-fill me-2"></i>Xóa thông báo
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <form action="${pageContext.request.contextPath}/profile/notifications/delete" method="post">
        <div class="modal-body">
          <p>Bạn có chắc chắn muốn xóa thông báo này không?</p>
          <p class="text-muted small" id="notificationTitleText"></p>
          <p class="text-danger small">Hành động này không thể hoàn tác.</p>
          <input type="hidden" name="notificationId" id="deleteNotificationId">
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
          <button type="submit" class="btn btn-danger">Xóa</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- THÊM JAVASCRIPT Ở CUỐI FILE -->
<script>
  document.addEventListener('DOMContentLoaded', function() {
    // Xử lý nút xóa thông báo
    const deleteButtons = document.querySelectorAll('.btn-delete-notification');
    const deleteModal = new bootstrap.Modal(document.getElementById('deleteNotificationModal'));
    const deleteNotificationIdInput = document.getElementById('deleteNotificationId');
    const notificationTitleText = document.getElementById('notificationTitleText');

    deleteButtons.forEach(button => {
      button.addEventListener('click', function() {
        const notificationId = this.getAttribute('data-notification-id');
        const notificationTitle = this.getAttribute('data-notification-title');

        // Cập nhật thông tin vào modal
        deleteNotificationIdInput.value = notificationId;
        notificationTitleText.textContent = '"' + notificationTitle + '"';

        // Hiển thị modal
        deleteModal.show();
      });
    });

    // Xử lý nút đánh dấu đã đọc (nếu có)
    const markAsReadButtons = document.querySelectorAll('.mark-as-read-btn');
    markAsReadButtons.forEach(button => {
      button.addEventListener('click', function() {
        const notificationId = this.getAttribute('data-notification-id');
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '${pageContext.request.contextPath}/profile/notifications/mark-read';

        const notificationIdInput = document.createElement('input');
        notificationIdInput.type = 'hidden';
        notificationIdInput.name = 'notificationId';
        notificationIdInput.value = notificationId;

        const markAllInput = document.createElement('input');
        markAllInput.type = 'hidden';
        markAllInput.name = 'markAll';
        markAllInput.value = 'false';

        form.appendChild(notificationIdInput);
        form.appendChild(markAllInput);
        document.body.appendChild(form);
        form.submit();
      });
    });
  });
</script>

<%@ include file="components/footer.jsp" %>