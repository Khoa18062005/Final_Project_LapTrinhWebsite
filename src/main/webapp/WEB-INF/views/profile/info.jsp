<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="pageTitle" value="Hồ Sơ Của Tôi" />
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<!-- MAIN CONTENT -->
<div class="col-lg-10 col-md-9">
    <div class="profile-content">
        <div class="profile-header">
            <h4>Hồ Sơ Của Tôi</h4>
            <p class="text-muted">Quản lý thông tin hồ sơ để bảo mật tài khoản</p>
        </div>

        <form action="${pageContext.request.contextPath}/profile"
              method="POST"
              id="profileForm"
              enctype="multipart/form-data">

            <input type="hidden" name="action" value="update_info">
            <input type="hidden" id="originalAvatar" value="${not empty user.avatar ? user.avatar : ''}">
            <input type="hidden" id="originalGender" value="${user.gender}">

            <div class="row">
                <!-- Left column - Form -->
                <div class="col-lg-8">
                    <!-- Tên đăng nhập -->
                    <div class="form-group-custom">
                        <label>Tên đăng nhập</label>
                        <div class="form-value">
                            <span class="text-dark fw-semibold">${user.email}</span>
                        </div>
                    </div>

                    <!-- Họ -->
                    <div class="form-group-custom">
                        <label>Họ và tên đệm<span class="text-danger">*</span></label>
                        <input type="text" class="form-control-custom" name="lastName" id="lastName"
                               value="${user.firstName}" placeholder="Nhập họ"
                               data-original="${user.firstName}" required>
                    </div>

                    <!-- Tên -->
                    <div class="form-group-custom">
                        <label>Tên <span class="text-danger">*</span></label>
                        <input type="text" class="form-control-custom" name="firstName" id="firstName"
                               value="${user.lastName}" placeholder="Nhập tên"
                               data-original="${user.lastName}" required>
                    </div>

                    <!-- Email -->
                    <div class="form-group-custom">
                        <label>Email<span class="text-danger">*</span></label>
                        <div style="flex: 1;">
                            <input type="email" class="form-control-custom" name="email" id="email"
                                   value="${user.email}" placeholder="Nhập email"
                                   data-original="${user.email}" required>

                            <!-- OTP SECTION -->
                            <div id="emailOtpSection" style="display: none; margin-top: 12px;">
                                <div class="d-flex gap-2 align-items-center">
                                    <input type="text" class="form-control-custom" id="emailOtp" name="emailOtp"
                                           placeholder="Nhập mã OTP" maxlength="6" pattern="[0-9]{6}"
                                           style="max-width: 200px;">
                                    <button type="button" class="btn btn-primary btn-sm" id="sendOtpBtn">
                                        <i class="bi bi-send me-1"></i> Gửi OTP
                                    </button>
                                </div>
                                <small id="otpTimer" class="text-muted d-block mt-2"></small>
                            </div>
                        </div>
                    </div>

                    <!-- Số điện thoại -->
                    <div class="form-group-custom">
                        <label>Số điện thoại</label>
                        <input type="tel" class="form-control-custom" name="phone" id="phone"
                               value="${user.phone}" placeholder="Nhập số điện thoại"
                               pattern="[0-9]{10,11}" data-original="${user.phone}">
                    </div>

                    <!-- Giới tính -->
                    <div class="form-group-custom">
                        <label>Giới tính</label>
                        <div class="gender-options">
                            <div class="form-check form-check-inline">
                                <input class="form-check-input gender-radio" type="radio" name="gender"
                                       id="male" value="Male" ${user.gender eq 'Male' ? 'checked' : ''}>
                                <label class="form-check-label" for="male">Nam</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input gender-radio" type="radio" name="gender"
                                       id="female" value="Female" ${user.gender eq 'Female' ? 'checked' : ''}>
                                <label class="form-check-label" for="female">Nữ</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input gender-radio" type="radio" name="gender"
                                       id="other" value="Other" ${user.gender eq 'Other' ? 'checked' : ''}>
                                <label class="form-check-label" for="other">Khác</label>
                            </div>
                        </div>
                    </div>

                    <!-- Ngày sinh -->
                    <div class="form-group-custom">
                        <label>Ngày sinh</label>
                        <div class="row g-2">
                            <jsp:useBean id="today" class="java.util.Date" />
                            <fmt:formatDate value="${user.dateOfBirth}" pattern="dd" var="userDay"/>
                            <fmt:formatDate value="${user.dateOfBirth}" pattern="MM" var="userMonth"/>
                            <fmt:formatDate value="${user.dateOfBirth}" pattern="yyyy" var="userYear"/>

                            <div class="col-4">
                                <select class="form-select-custom date-select" name="day" id="daySelect"
                                        data-original="${userDay}">
                                    <option value="">Ngày</option>
                                    <c:forEach begin="1" end="31" var="day">
                                        <option value="${day}" ${day eq userDay ? 'selected' : ''}>${day}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-4">
                                <select class="form-select-custom date-select" name="month" id="monthSelect"
                                        data-original="${userMonth}">
                                    <option value="">Tháng</option>
                                    <c:forEach begin="1" end="12" var="month">
                                        <option value="${month}" ${month eq userMonth ? 'selected' : ''}>${month}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-4">
                                <select class="form-select-custom date-select" name="year" id="yearSelect"
                                        data-original="${userYear}">
                                    <option value="">Năm</option>
                                    <c:forEach begin="1950" end="2024" var="year">
                                        <option value="${year}" ${year eq userYear ? 'selected' : ''}>${year}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- Mã giới thiệu -->
                    <div class="form-group-custom referral-code-section">
                        <label>Mã giới thiệu</label>
                        <div class="referral-code-display">
                            <div class="referral-code-container">
                                <span class="referral-code-text" id="referralCodeText">
                                    <c:if test="${not empty user.username}">
                                        ${fn:substring(user.username, 5, fn:length(user.username))}
                                    </c:if>
                                </span>
                                <button type="button" class="btn-copy" id="copyReferralBtn"
                                        data-bs-toggle="tooltip"
                                        data-bs-placement="top"
                                        title="Sao chép mã">
                                    <i class="bi bi-clipboard"></i>
                                </button>
                            </div>
                            <i class="bi bi-info-circle referral-info-icon"
                               data-bs-toggle="tooltip"
                               data-bs-placement="top"
                               title="Mã giới thiệu của bạn dùng để mời bạn bè tham gia VietTech"></i>
                        </div>
                        <div id="copySuccessMessage" class="copy-success-message" style="display: none;">
                            <i class="bi bi-check-circle-fill me-1"></i> Đã sao chép mã vào clipboard!
                        </div>
                    </div>

                    <!-- Button Save -->
                    <div class="form-group-custom mt-4">
                        <label></label>
                        <button type="submit" class="btn btn-primary btn-save" id="saveBtn" disabled>
                            <i class="bi bi-check-circle me-2"></i> Lưu Thay Đổi
                        </button>
                    </div>
                </div>

                <!-- Right column - Avatar -->
                <div class="col-lg-4">
                    <div class="avatar-upload-box">
                        <div class="avatar-preview-large">
                            <c:choose>
                                <c:when test="${not empty user.avatar and user.avatar != ''}">
                                    <img src="${user.avatar}" alt="Avatar" id="avatarPreviewLarge"
                                         onerror="this.src='${pageContext.request.contextPath}/assets/img/default-avatar.jpg'">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/assets/img/default-avatar.jpg"
                                         alt="Avatar" id="avatarPreviewLarge">
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <button type="button" class="btn btn-outline-secondary btn-sm mt-3"
                                onclick="document.getElementById('avatarInput').click()">
                            <i class="bi bi-upload me-1"></i> Chọn Ảnh
                        </button>

                        <button type="button" class="btn btn-outline-danger btn-sm mt-2"
                                id="cancelAvatarBtn" style="display: none;">
                            <i class="bi bi-x-circle me-1"></i> Hủy Ảnh
                        </button>

                        <input type="file" id="avatarInput" name="avatarFile" accept="image/*" style="display: none;">

                        <small class="text-muted d-block mt-2">
                            Dung lượng file tối đa 5 MB<br>Định dạng: .JPEG, .PNG
                        </small>

                        <div id="fileNameDisplay" class="mt-2 text-primary small" style="display: none;">
                            <i class="bi bi-file-earmark-image"></i>
                            <span id="fileName"></span>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<c:set var="pageScript" value="profile.js" />
<%@ include file="components/footer.jsp" %>