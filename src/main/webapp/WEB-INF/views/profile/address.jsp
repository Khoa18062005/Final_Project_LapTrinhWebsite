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
      <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addAddressModal">
        <i class="bi bi-plus-circle me-2"></i> Thêm Địa Chỉ Mới
      </button>
    </div>

    <div class="row mt-4">
      <!-- ĐỊA CHỈ 1 - MẶC ĐỊNH -->
      <div class="col-md-6 mb-4">
        <div class="address-card border rounded p-4 position-relative">
          <span class="badge bg-danger position-absolute top-0 end-0 m-3">Mặc Định</span>

          <div class="d-flex justify-content-between align-items-start mb-3">
            <div>
              <h5 class="mb-1">${user.lastName} ${user.firstName}</h5>
              <p class="text-muted mb-0">
                <i class="bi bi-telephone me-1"></i> 0123 456 789
              </p>
            </div>
          </div>

          <p class="mb-3">
            <i class="bi bi-house-door me-2"></i>
            123 Nguyễn Văn Cừ, Phường 4, Quận 5, TP. Hồ Chí Minh
          </p>

          <div class="d-flex gap-2">
            <button class="btn btn-outline-primary btn-sm">
              <i class="bi bi-pencil me-1"></i> Cập Nhật
            </button>
            <button class="btn btn-outline-danger btn-sm" disabled>
              <i class="bi bi-trash me-1"></i> Xóa
            </button>
          </div>
        </div>
      </div>

      <!-- ĐỊA CHỈ 2 -->
      <div class="col-md-6 mb-4">
        <div class="address-card border rounded p-4">
          <div class="d-flex justify-content-between align-items-start mb-3">
            <div>
              <h5 class="mb-1">${user.lastName} ${user.firstName}</h5>
              <p class="text-muted mb-0">
                <i class="bi bi-telephone me-1"></i> 0987 654 321
              </p>
            </div>
          </div>

          <p class="mb-3">
            <i class="bi bi-building me-2"></i>
            456 Võ Văn Tần, Phường 5, Quận 3, TP. Hồ Chí Minh
          </p>

          <div class="d-flex gap-2">
            <button class="btn btn-outline-primary btn-sm">
              <i class="bi bi-pencil me-1"></i> Cập Nhật
            </button>
            <button class="btn btn-outline-danger btn-sm">
              <i class="bi bi-trash me-1"></i> Xóa
            </button>
            <button class="btn btn-outline-secondary btn-sm ms-auto">
              <i class="bi bi-star me-1"></i> Đặt Mặc Định
            </button>
          </div>
        </div>
      </div>

      <!-- ĐỊA CHỈ 3 -->
      <div class="col-md-6 mb-4">
        <div class="address-card border rounded p-4">
          <div class="d-flex justify-content-between align-items-start mb-3">
            <div>
              <h5 class="mb-1">${user.lastName} ${user.firstName}</h5>
              <p class="text-muted mb-0">
                <i class="bi bi-telephone me-1"></i> 0345 678 901
              </p>
            </div>
          </div>

          <p class="mb-3">
            <i class="bi bi-shop me-2"></i>
            789 Lê Lợi, Phường Bến Thành, Quận 1, TP. Hồ Chí Minh
          </p>

          <div class="d-flex gap-2">
            <button class="btn btn-outline-primary btn-sm">
              <i class="bi bi-pencil me-1"></i> Cập Nhật
            </button>
            <button class="btn btn-outline-danger btn-sm">
              <i class="bi bi-trash me-1"></i> Xóa
            </button>
            <button class="btn btn-outline-secondary btn-sm ms-auto">
              <i class="bi bi-star me-1"></i> Đặt Mặc Định
            </button>
          </div>
        </div>
      </div>

      <!-- EMPTY STATE (nếu không có địa chỉ) -->
      <!-- <div class="col-12">
          <div class="empty-state text-center py-5">
              <i class="bi bi-geo-alt fs-1 text-muted mb-3 d-block"></i>
              <h5>Chưa có địa chỉ nào</h5>
              <p class="text-muted">Thêm địa chỉ để giao hàng nhanh hơn</p>
              <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addAddressModal">
                  <i class="bi bi-plus-circle me-2"></i> Thêm Địa Chỉ Mới
              </button>
          </div>
      </div> -->
    </div>
  </div>
</div>

<!-- MODAL THÊM ĐỊA CHỈ -->
<div class="modal fade" id="addAddressModal" tabindex="-1">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">
          <i class="bi bi-plus-circle me-2"></i>Thêm Địa Chỉ Mới
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <form>
          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="form-label">Họ và tên <span class="text-danger">*</span></label>
              <input type="text" class="form-control" placeholder="Nhập họ và tên" required>
            </div>
            <div class="col-md-6 mb-3">
              <label class="form-label">Số điện thoại <span class="text-danger">*</span></label>
              <input type="tel" class="form-control" placeholder="Nhập số điện thoại" required>
            </div>
          </div>

          <div class="row">
            <div class="col-md-4 mb-3">
              <label class="form-label">Tỉnh/Thành phố <span class="text-danger">*</span></label>
              <select class="form-select" required>
                <option value="">Chọn Tỉnh/Thành phố</option>
                <option>TP. Hồ Chí Minh</option>
                <option>Hà Nội</option>
                <option>Đà Nẵng</option>
              </select>
            </div>
            <div class="col-md-4 mb-3">
              <label class="form-label">Quận/Huyện <span class="text-danger">*</span></label>
              <select class="form-select" required>
                <option value="">Chọn Quận/Huyện</option>
                <option>Quận 1</option>
                <option>Quận 3</option>
                <option>Quận 5</option>
              </select>
            </div>
            <div class="col-md-4 mb-3">
              <label class="form-label">Phường/Xã <span class="text-danger">*</span></label>
              <select class="form-select" required>
                <option value="">Chọn Phường/Xã</option>
                <option>Phường 1</option>
                <option>Phường 2</option>
                <option>Phường 3</option>
              </select>
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label">Địa chỉ cụ thể <span class="text-danger">*</span></label>
            <textarea class="form-control" rows="3"
                      placeholder="Số nhà, tên đường..." required></textarea>
          </div>

          <div class="form-check">
            <input class="form-check-input" type="checkbox" id="setDefault">
            <label class="form-check-label" for="setDefault">
              Đặt làm địa chỉ mặc định
            </label>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
        <button type="button" class="btn btn-primary">
          <i class="bi bi-check-circle me-1"></i> Lưu Địa Chỉ
        </button>
      </div>
    </div>
  </div>
</div>

<%@ include file="components/footer.jsp" %>