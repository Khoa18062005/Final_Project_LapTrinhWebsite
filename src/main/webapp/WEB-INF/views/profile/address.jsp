<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Địa Chỉ Của Tôi" />
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<!-- MAIN CONTENT -->
<div class="col-lg-10 col-md-9">
  <div class="profile-content">
    <div class="profile-header d-flex justify-content-between align-items-center">
      <div>
        <h4><i class="bi bi-geo-alt me-2"></i>Địa Chỉ Của Tôi</h4>
        <p class="text-muted mb-0">Quản lý địa chỉ giao hàng của bạn</p>
      </div>
      <button type="button" class="btn btn-primary btn-add-address" data-bs-toggle="modal" data-bs-target="#addAddressModal">
        <i class="bi bi-plus-circle me-2"></i>Thêm địa chỉ mới
      </button>
    </div>

    <!-- HIỂN THỊ DANH SÁCH ĐỊA CHỈ -->
    <div class="row mt-4">
      <c:choose>
        <c:when test="${empty addresses}">
          <!-- EMPTY STATE -->
          <div class="col-12">
            <div class="empty-state text-center py-5">
              <i class="bi bi-geo-alt fs-1 text-muted mb-3 d-block"></i>
              <h5>Chưa có địa chỉ nào</h5>
              <p class="text-muted">Bạn chưa thêm địa chỉ giao hàng nào</p>
              <button type="button" class="btn btn-primary btn-add-address mt-3" data-bs-toggle="modal" data-bs-target="#addAddressModal">
                <i class="bi bi-plus-circle me-2"></i>Thêm địa chỉ đầu tiên
              </button>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <!-- DANH SÁCH ĐỊA CHỈ -->
          <c:forEach var="address" items="${addresses}">
            <div class="col-md-6 mb-4">
              <div class="address-card border rounded p-4 position-relative">
                <!-- Badge mặc định -->
                <c:if test="${address.isDefault()}">
                  <span class="badge bg-danger position-absolute top-0 end-0 m-3">Mặc Định</span>
                </c:if>

                <!-- Thông tin người nhận -->
                <div class="d-flex justify-content-between align-items-start mb-3">
                  <div>
                    <h5 class="mb-1">${address.receiverName}</h5>
                    <p class="text-muted mb-0">
                      <i class="bi bi-telephone me-1"></i> ${address.phone}
                    </p>
                  </div>
                </div>

                <!-- Địa chỉ -->
                <p class="mb-3">
                  <i class="bi bi-house-door me-2"></i>
                    ${address.street}, ${address.ward}, ${address.district}, ${address.city}
                </p>
              </div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>

<!-- MODAL THÊM ĐỊA CHỈ MỚI -->
<div class="modal fade modal-top-fixed" id="addAddressModal" tabindex="-1" aria-labelledby="addAddressModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
    <form id="addAddressForm" action="${pageContext.request.contextPath}/profile/address/add" method="POST">
      <div class="modal-content">
        <div class="modal-header bg-primary text-white">
          <h5 class="modal-title" id="addAddressModalLabel">
            <i class="bi bi-plus-circle me-2"></i>Thêm địa chỉ mới
          </h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="row">
            <!-- Người nhận -->
            <div class="col-md-6 mb-3">
              <label for="receiverName" class="form-label">Tên người nhận <span class="text-danger">*</span></label>
              <input type="text" class="form-control" id="receiverName" name="receiverName" required>
            </div>

            <!-- Số điện thoại -->
            <div class="col-md-6 mb-3">
              <label for="phone" class="form-label">Số điện thoại <span class="text-danger">*</span></label>
              <input type="tel" class="form-control" id="phone" name="phone" required>
            </div>

            <!-- Tỉnh/Thành phố -->
            <div class="col-md-4 mb-3">
              <label for="city" class="form-label">Tỉnh/Thành phố <span class="text-danger">*</span></label>
              <select class="form-select" id="city" name="city" required>
                <option value="" selected disabled>Chọn tỉnh/thành phố</option>
              </select>
            </div>

            <!-- Quận/Huyện -->
            <div class="col-md-4 mb-3">
              <label for="district" class="form-label">Quận/Huyện <span class="text-danger">*</span></label>
              <select class="form-select" id="district" name="district" required disabled>
                <option value="" selected disabled>Chọn quận/huyện</option>
              </select>
            </div>

            <!-- Phường/Xã -->
            <div class="col-md-4 mb-3">
              <label for="ward" class="form-label">Phường/Xã <span class="text-danger">*</span></label>
              <select class="form-select" id="ward" name="ward" required disabled>
                <option value="" selected disabled>Chọn phường/xã</option>
              </select>
            </div>

            <!-- Đường/Số nhà -->
            <div class="col-12 mb-3">
              <label for="street" class="form-label">Đường/Số nhà <span class="text-danger">*</span></label>
              <input type="text" class="form-control" id="street" name="street" required>
            </div>

            <!-- Đặt làm mặc định -->
            <div class="col-12 mb-3">
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="isDefault" name="isDefault">
                <label class="form-check-label" for="isDefault">
                  Đặt làm địa chỉ mặc định
                </label>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
          <button type="submit" class="btn btn-primary">Lưu địa chỉ</button>
        </div>
      </div>
    </form>
  </div>
</div>

<!-- JavaScript đơn giản cho API -->
<!-- JavaScript đơn giản cho API -->
<c:set var="pageScript" value="address.js" />
<%@ include file="components/footer.jsp" %>