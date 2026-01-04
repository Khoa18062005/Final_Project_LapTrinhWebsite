<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<footer class="footer bg-dark text-light">
  <div class="container py-5">
    <div class="row">
      <!-- Column 1: Brand & Slogan -->
      <div class="col-lg-4 mb-4">
        <div class="footer-brand mb-3">
          <h4 class="fw-bold text-primary">VietTech</h4>
          <p class="text-muted">Siêu Thị Công Nghệ Trực Tuyến</p>
        </div>

        <div class="footer-slogan mb-4">
          <h5 class="text-white mb-3">"Mua công nghệ - Chọn VietTech"</h5>
        </div>

        <div class="footer-commitments">
          <div class="commitment-item d-flex align-items-center mb-2">
            <i class="bi bi-check-circle-fill text-success me-2"></i>
            <span>100% hàng chính hãng</span>
          </div>
          <div class="commitment-item d-flex align-items-center mb-2">
            <i class="bi bi-check-circle-fill text-success me-2"></i>
            <span>Giá tốt nhất - Hoàn tiền nếu rẻ hơn</span>
          </div>
          <div class="commitment-item d-flex align-items-center mb-2">
            <i class="bi bi-check-circle-fill text-success me-2"></i>
            <span>Giao hàng toàn quốc</span>
          </div>
          <div class="commitment-item d-flex align-items-center mb-2">
            <i class="bi bi-check-circle-fill text-success me-2"></i>
            <span>Bảo hành tận tâm</span>
          </div>
        </div>
      </div>

      <!-- Column 2: Customer Support -->
      <div class="col-lg-4 mb-4">
        <h5 class="footer-title mb-4">Hỗ trợ khách hàng</h5>
        <ul class="footer-links list-unstyled">
          <li class="mb-2">
            <a href="${pageContext.request.contextPath}/policy/warranty" class="footer-link">
              <i class="bi bi-chevron-right me-2"></i>Chính sách bảo hành
            </a>
          </li>
          <li class="mb-2">
            <a href="${pageContext.request.contextPath}/policy/shipping" class="footer-link">
              <i class="bi bi-chevron-right me-2"></i>Chính sách vận chuyển
            </a>
          </li>
          <li class="mb-2">
            <a href="${pageContext.request.contextPath}/policy/return" class="footer-link">
              <i class="bi bi-chevron-right me-2"></i>Chính sách đổi trả
            </a>
          </li>
          <li class="mb-2">
            <a href="${pageContext.request.contextPath}/policy/payment" class="footer-link">
              <i class="bi bi-chevron-right me-2"></i>Phương thức thanh toán
            </a>
          </li>
          <li class="mb-2">
            <a href="${pageContext.request.contextPath}/faq" class="footer-link">
              <i class="bi bi-chevron-right me-2"></i>Câu hỏi thường gặp
            </a>
          </li>
          <li class="mb-2">
            <a href="${pageContext.request.contextPath}/contact" class="footer-link">
              <i class="bi bi-chevron-right me-2"></i>Liên hệ & Hỗ trợ
            </a>
          </li>
        </ul>
      </div>

      <!-- Column 3: Contact & Legal -->
      <div class="col-lg-4 mb-4">
        <h5 class="footer-title mb-4">Thông tin liên hệ</h5>
        <div class="contact-info mb-4">
          <div class="contact-item d-flex align-items-start mb-3">
            <i class="bi bi-geo-alt text-primary mt-1 me-3"></i>
            <div>
              <strong>Trụ sở chính:</strong>
              <p class="mb-0">123 Đường ABC, Quận XYZ, TP. Hồ Chí Minh</p>
            </div>
          </div>
          <div class="contact-item d-flex align-items-center mb-3">
            <i class="bi bi-telephone text-primary me-3"></i>
            <div>
              <strong>Hotline:</strong>
              <p class="mb-0">1900 9999</p>
            </div>
          </div>
          <div class="contact-item d-flex align-items-center mb-3">
            <i class="bi bi-envelope text-primary me-3"></i>
            <div>
              <strong>Email:</strong>
              <p class="mb-0">support@viettech.vn</p>
            </div>
          </div>
          <div class="contact-item d-flex align-items-center mb-3">
            <i class="bi bi-clock text-primary me-3"></i>
            <div>
              <strong>Thời gian làm việc:</strong>
              <p class="mb-0">8:00 - 22:00 (Thứ 2 - Chủ Nhật)</p>
            </div>
          </div>
        </div>

        <div class="legal-links">
          <a href="${pageContext.request.contextPath}/policy/terms" class="legal-link me-3">Điều khoản sử dụng</a>
          <a href="${pageContext.request.contextPath}/policy/privacy" class="legal-link">Chính sách bảo mật</a>
        </div>
      </div>
    </div>

    <hr class="footer-divider my-4">

    <!-- Copyright & Payment Methods -->
    <div class="footer-bottom">
      <div class="row align-items-center">
        <div class="col-md-6 mb-3 mb-md-0">
          <div class="copyright">
            <p class="mb-0 text-muted">
              <i class="bi bi-c-circle me-1"></i> 2025 VietTech. MST: 0123456789
            </p>
            <p class="mb-0 text-muted">
              Giấy chứng nhận ĐKKD số: 0123456789 do Sở KH&ĐT TP.HCM cấp ngày 01/01/2025
            </p>
          </div>
        </div>
        <div class="col-md-6">
          <div class="payment-methods">
            <p class="text-muted mb-2">Chấp nhận thanh toán:</p>
            <div class="payment-icons">
              <i class="bi bi-credit-card-2-front me-2" title="Thẻ tín dụng"></i>
              <i class="bi bi-bank me-2" title="Chuyển khoản ngân hàng"></i>
              <i class="bi bi-cash-coin me-2" title="Tiền mặt"></i>
              <i class="bi bi-phone me-2" title="Ví điện tử"></i>
              <span class="badge bg-light text-dark me-2">COD</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</footer>

<!-- Modal Smember -->
<div class="modal fade" id="smemberModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content smember-modal-content">
      <!-- Nút đóng góc trên PHẢI -->
      <button type="button" class="btn-close smember-close-btn" data-bs-dismiss="modal" aria-label="Close"></button>

      <div class="modal-body text-center py-5 px-4">
        <!-- Tiêu đề Smember gradient xanh -->
        <h1 class="smember-title mb-5">VietTech Member</h1>

        <!-- Nội dung -->
        <p class="smember-text mb-5">
          Vui lòng đăng nhập tài khoản VTMember để<br>
          <strong>xem ưu đãi và thanh toán dễ dàng hơn.</strong>
        </p>

        <!-- Hai nút pill -->
        <div class="d-flex flex-column flex-sm-row justify-content-center gap-4">
          <a href="${pageContext.request.contextPath}/register" class="smember-btn-register">
            Đăng ký
          </a>
          <a href="${pageContext.request.contextPath}/login" class="smember-btn-login">
            Đăng nhập
          </a>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>