<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Đổi Mật Khẩu" />
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<!-- MAIN CONTENT -->
<div class="col-lg-10 col-md-9">
  <div class="profile-content">
    <div class="profile-header">
      <div>
        <h4><i class="bi bi-shield-lock me-2"></i>Đổi Mật Khẩu</h4>
        <p class="text-muted mb-0">Thay đổi mật khẩu của bạn để bảo mật tài khoản</p>
      </div>
    </div>

    <!-- ALERT MESSAGES -->
    <div class="alert-container">
      <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
          <i class="bi bi-check-circle-fill me-2"></i>
            ${successMessage}
          <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
      </c:if>

      <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
          <i class="bi bi-exclamation-circle-fill me-2"></i>
            ${errorMessage}
          <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
      </c:if>
    </div>

    <!-- FORM ĐỔI MẬT KHẨU -->
    <div class="row">
      <div class="col-lg-8">
        <form id="changePasswordForm" action="${pageContext.request.contextPath}/profile/password/change" method="POST">

          <!-- Mật khẩu hiện tại -->
          <div class="form-group-custom">
            <label for="currentPassword" class="required">Mật khẩu hiện tại</label>
            <div class="form-value">
              <div class="password-field-container">
                <div class="password-input-wrapper">
                  <input type="password"
                         class="form-control-custom"
                         id="currentPassword"
                         name="currentPassword"
                         required
                         autocomplete="current-password">
                  <button type="button" class="btn-toggle-password" data-target="currentPassword">
                    <i class="bi bi-eye-slash"></i>
                  </button>
                </div>
                <div class="invalid-feedback"></div>
              </div>
            </div>
          </div>

          <!-- Mật khẩu mới -->
          <div class="form-group-custom">
            <label for="newPassword" class="required">Mật khẩu mới</label>
            <div class="form-value">
              <div class="password-field-container">
                <div class="password-input-wrapper">
                  <input type="password"
                         class="form-control-custom"
                         id="newPassword"
                         name="newPassword"
                         required
                         autocomplete="new-password">
                  <button type="button" class="btn-toggle-password" data-target="newPassword">
                    <i class="bi bi-eye-slash"></i>
                  </button>
                </div>

                <!-- STRENGTH METER CHỈ CHO PASSWORD MỚI -->
                <div class="password-strength-meter">
                  <div class="strength-bar"></div>
                </div>
                <small class="password-strength-text"></small>

                <div class="invalid-feedback"></div>
              </div>
            </div>
          </div>

          <!-- Xác nhận mật khẩu mới -->
          <div class="form-group-custom">
            <label for="confirmPassword" class="required">Xác nhận mật khẩu mới</label>
            <div class="form-value">
              <div class="password-field-container">
                <div class="password-input-wrapper">
                  <input type="password"
                         class="form-control-custom"
                         id="confirmPassword"
                         name="confirmPassword"
                         required
                         autocomplete="new-password">
                  <button type="button" class="btn-toggle-password" data-target="confirmPassword">
                    <i class="bi bi-eye-slash"></i>
                  </button>
                </div>
                <div class="invalid-feedback"></div>
              </div>
            </div>
          </div>


          <!-- Nút hành động -->
          <div class="form-group-custom">
            <label></label>
            <div class="form-value">
              <button type="submit" class="btn btn-save" id="btnChangePassword" disabled>
                <i class="bi bi-check-circle me-2"></i>Đổi mật khẩu
              </button>
            </div>
          </div>
        </form>
      </div>

      <!-- HƯỚNG DẪN BẢO MẬT -->
      <div class="col-lg-4">
        <div class="password-tips-card">
          <h5><i class="bi bi-info-circle me-2"></i>Mật khẩu mạnh cần có:</h5>
          <ul>
            <li><i class="bi bi-check2"></i> Ít nhất 8 ký tự</li>
            <li><i class="bi bi-check2"></i> Chữ hoa và chữ thường</li>
            <li><i class="bi bi-check2"></i> Ít nhất 1 chữ số</li>
            <li><i class="bi bi-check2"></i> Ít nhất 1 ký tự đặc biệt (!@#$%^&*)</li>
          </ul>

          <div class="security-tips mt-4">
            <h6><i class="bi bi-shield-check me-2"></i>Lời khuyên bảo mật:</h6>
            <ul>
              <li>Không sử dụng mật khẩu giống nhau ở nhiều trang web</li>
              <li>Thay đổi mật khẩu định kỳ mỗi 3-6 tháng</li>
              <li>Không chia sẻ mật khẩu với bất kỳ ai</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<c:set var="pageScript" value="password.js" />
<%@ include file="components/footer.jsp" %>