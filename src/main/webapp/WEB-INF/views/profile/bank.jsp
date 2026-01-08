<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="pageTitle" value="Thông Tin Ngân Hàng" />

<!-- Tạo dữ liệu giả ngay trong JSP -->
<c:set var="bankCards" value="${[
    {'bankName':'Vietcombank', 'bankCode':'VCB', 'cardType':'DEBIT', 'cardNumber':'1234567890123456', 'cardHolderName':'NGUYEN VAN A', 'expiryMonth':'12', 'expiryYear':'2027', 'isDefault':true},
    {'bankName':'BIDV', 'bankCode':'BIDV', 'cardType':'CREDIT', 'cardNumber':'9876543210987654', 'cardHolderName':'TRAN THI B', 'expiryMonth':'06', 'expiryYear':'2025', 'isDefault':false},
    {'bankName':'Techcombank', 'bankCode':'TCB', 'cardType':'DEBIT', 'cardNumber':'4567123498765432', 'cardHolderName':'LE VAN C', 'expiryMonth':'03', 'expiryYear':'2026', 'isDefault':false}
]}" />

<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<!-- MAIN CONTENT -->
<div class="col-lg-10 col-md-9">
  <div class="profile-content">
    <div class="profile-header d-flex justify-content-between align-items-center">
      <div>
        <h4><i class="bi bi-credit-card me-2"></i>Thông Tin Ngân Hàng</h4>
        <p class="text-muted mb-0">Quản lý thông tin thẻ ngân hàng của bạn</p>
      </div>
      <button type="button" class="btn btn-primary btn-add-bank" data-bs-toggle="modal" data-bs-target="#addBankModal">
        <i class="bi bi-plus-circle me-2"></i>Thêm thẻ mới
      </button>
    </div>

    <!-- HIỂN THỊ DANH SÁCH THẺ -->
    <div class="row mt-4">
      <c:choose>
        <c:when test="${empty bankCards}">
          <!-- EMPTY STATE -->
          <div class="col-12">
            <div class="empty-state text-center py-5">
              <i class="bi bi-credit-card fs-1 text-muted mb-3 d-block"></i>
              <h5>Chưa có thẻ ngân hàng nào</h5>
              <p class="text-muted">Bạn chưa thêm thẻ ngân hàng nào vào tài khoản</p>
              <button type="button" class="btn btn-primary btn-add-bank mt-3" data-bs-toggle="modal" data-bs-target="#addBankModal">
                <i class="bi bi-plus-circle me-2"></i>Thêm thẻ đầu tiên
              </button>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <!-- DANH SÁCH THẺ NGÂN HÀNG -->
          <c:forEach var="card" items="${bankCards}">
            <div class="col-md-6 col-lg-4 mb-4">
              <div class="bank-card border rounded p-4 position-relative h-100">
                <!-- Badge mặc định -->
                <c:if test="${card.isDefault}">
                  <span class="badge bg-danger position-absolute top-0 end-0 m-3">Mặc Định</span>
                </c:if>

                <!-- Logo ngân hàng và tên -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                  <div class="d-flex align-items-center gap-3">
                    <div class="bank-logo">
                      <c:choose>
                        <c:when test="${card.bankCode == 'VCB'}">
                          <i class="bi bi-bank text-primary fs-2"></i>
                        </c:when>
                        <c:when test="${card.bankCode == 'BIDV'}">
                          <i class="bi bi-building text-success fs-2"></i>
                        </c:when>
                        <c:when test="${card.bankCode == 'TCB'}">
                          <i class="bi bi-laptop text-info fs-2"></i>
                        </c:when>
                        <c:otherwise>
                          <i class="bi bi-credit-card-2-front text-secondary fs-2"></i>
                        </c:otherwise>
                      </c:choose>
                    </div>
                    <div>
                      <h5 class="mb-1">${card.bankName}</h5>
                      <p class="text-muted mb-0">
                        <c:choose>
                          <c:when test="${card.cardType == 'DEBIT'}">Thẻ ghi nợ</c:when>
                          <c:when test="${card.cardType == 'CREDIT'}">Thẻ tín dụng</c:when>
                          <c:otherwise>Thẻ ATM</c:otherwise>
                        </c:choose>
                      </p>
                    </div>
                  </div>
                </div>

                <!-- Số thẻ (chỉ hiển thị 4 số cuối) -->
                <div class="mb-3">
                  <small class="text-muted d-block mb-1">Số thẻ</small>
                  <h4 class="mb-0">
                    <i class="bi bi-dot"></i>
                    <i class="bi bi-dot"></i>
                    <i class="bi bi-dot"></i>
                    <i class="bi bi-dot"></i>
                      ${fn:substring(card.cardNumber, fn:length(card.cardNumber)-4, fn:length(card.cardNumber))}
                  </h4>
                </div>

                <!-- Chủ thẻ và Ngày hết hạn -->
                <div class="row mt-auto">
                  <div class="col-7">
                    <small class="text-muted d-block mb-1">Chủ thẻ</small>
                    <p class="mb-0 fw-semibold">${card.cardHolderName}</p>
                  </div>
                  <div class="col-5">
                    <small class="text-muted d-block mb-1">Hết hạn</small>
                    <p class="mb-0 fw-semibold">${card.expiryMonth}/${card.expiryYear}</p>
                  </div>
                </div>

                <!-- Nút hành động -->
                <div class="row mt-3">
                  <div class="col-12">
                    <button class="btn btn-outline-primary btn-sm w-100" onclick="alert('Chức năng sửa đang được phát triển!')">
                      <i class="bi bi-pencil me-1"></i> Sửa
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Thống kê -->
    <div class="row mt-5">
      <div class="col-12">
        <div class="card border-0 bg-light">
          <div class="card-body">
            <h6><i class="bi bi-info-circle me-2"></i>Thông tin bảo mật</h6>
            <ul class="mb-0">
              <li>Thông tin thẻ ngân hàng được mã hóa và bảo mật</li>
              <li>Chỉ hiển thị 4 số cuối của thẻ</li>
              <li>Không lưu trữ mã CVV trên hệ thống</li>
              <li>Dữ liệu hiển thị là dữ liệu mẫu để demo</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- MODAL THÊM THẺ MỚI (Demo) -->
<div class="modal fade" id="addBankModal" tabindex="-1" aria-labelledby="addBankModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header bg-primary text-white">
        <h5 class="modal-title" id="addBankModalLabel">
          <i class="bi bi-plus-circle me-2"></i>Thêm thẻ ngân hàng mới
        </h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <div class="alert alert-info">
          <i class="bi bi-info-circle me-2"></i>
          <strong>Chức năng đang phát triển</strong> - Đây chỉ là giao diện demo
        </div>

        <form id="addBankForm">
          <div class="row">
            <!-- Ngân hàng -->
            <div class="col-md-6 mb-3">
              <label class="form-label">Ngân hàng <span class="text-danger">*</span></label>
              <select class="form-select">
                <option selected>Vietcombank (VCB)</option>
                <option>BIDV</option>
                <option>Techcombank</option>
                <option>ACB</option>
              </select>
            </div>

            <!-- Loại thẻ -->
            <div class="col-md-6 mb-3">
              <label class="form-label">Loại thẻ <span class="text-danger">*</span></label>
              <select class="form-select">
                <option selected>Thẻ ghi nợ (Debit)</option>
                <option>Thẻ tín dụng (Credit)</option>
                <option>Thẻ ATM</option>
              </select>
            </div>

            <!-- Số thẻ -->
            <div class="col-md-12 mb-3">
              <label class="form-label">Số thẻ <span class="text-danger">*</span></label>
              <input type="text" class="form-control" placeholder="1234 5678 9012 3456">
            </div>

            <!-- Tên chủ thẻ -->
            <div class="col-md-6 mb-3">
              <label class="form-label">Tên chủ thẻ <span class="text-danger">*</span></label>
              <input type="text" class="form-control" placeholder="NGUYEN VAN A">
            </div>

            <!-- Ngày hết hạn -->
            <div class="col-md-6 mb-3">
              <label class="form-label">Ngày hết hạn <span class="text-danger">*</span></label>
              <div class="row g-2">
                <div class="col-6">
                  <select class="form-select">
                    <option selected>12</option>
                    <option>01</option>
                    <option>02</option>
                  </select>
                </div>
                <div class="col-6">
                  <select class="form-select">
                    <option selected>2027</option>
                    <option>2025</option>
                    <option>2026</option>
                  </select>
                </div>
              </div>
            </div>

            <!-- Đặt làm mặc định -->
            <div class="col-12 mb-3">
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="setAsDefault">
                <label class="form-check-label" for="setAsDefault">
                  Đặt làm thẻ mặc định
                </label>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
        <button type="button" class="btn btn-primary" onclick="alert('Chức năng thêm thẻ đang được phát triển!')">
          <i class="bi bi-save me-1"></i> Lưu thẻ
        </button>
      </div>
    </div>
  </div>
</div>

<!-- JavaScript đơn giản -->
<script>
  document.addEventListener('DOMContentLoaded', function() {
    console.log('Bank page loaded with mock data');

    // Xử lý click nút thêm thẻ
    document.querySelectorAll('.btn-add-bank').forEach(btn => {
      btn.addEventListener('click', function() {
        console.log('Opening add bank modal');
      });
    });

    // Format số thẻ khi nhập (demo)
    const cardInput = document.querySelector('input[placeholder*="1234"]');
    if (cardInput) {
      cardInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
        let formatted = value.replace(/(\d{4})/g, '$1 ').trim();
        e.target.value = formatted;
      });
    }

    // Xử lý nút xóa thẻ (nếu có)
    document.querySelectorAll('.btn-outline-danger').forEach(btn => {
      btn.addEventListener('click', function() {
        if (confirm('Bạn có chắc chắn muốn xóa thẻ này?')) {
          alert('Chức năng xóa đang được phát triển!');
        }
      });
    });
  });
</script>

<%@ include file="components/footer.jsp" %>