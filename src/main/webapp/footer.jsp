<footer class="footer">
  <div class="container py-5">
    <div class="row g-4">

      <!-- Column 1: Brand & Info -->
      <div class="col-lg-4 col-md-6">
        <div class="footer-brand mb-4">
          <!-- LOGO THAY CHO CHỮ VIETTECH -->
          <img src="${pageContext.request.contextPath}/assets/PNG/LogoVT.png"
               alt="VietTech Logo"
               class="footer-logo mb-3">
          <p class="footer-brand-subtitle mb-3">Siêu Thị Công Nghệ Trực Tuyến Hàng Đầu Việt Nam</p>
        </div>

        <div class="footer-description">
          <p class="footer-description-text lh-lg">
            VietTech là nền tảng thương mại điện tử chuyên cung cấp các sản phẩm công nghệ
            chính hãng với giá tốt nhất. Chúng tôi cam kết mang đến trải nghiệm mua sắm
            tuyệt vời nhất cho khách hàng.
          </p>
        </div>

        <div class="footer-commitments mt-3">
          <p class="footer-commitment-title mb-2">Cam kết của chúng tôi:</p>
          <ul class="footer-commitment-list list-unstyled">
            <li class="mb-1">→ 100% hàng chính hãng</li>
            <li class="mb-1">→ Giá tốt nhất thị trường</li>
            <li class="mb-1">→ Giao hàng toàn quốc</li>
            <li class="mb-1">→ Bảo hành tận tâm</li>
          </ul>
        </div>
      </div>

      <!-- Column 2: Quick Links -->
      <div class="col-lg-2 col-md-6">
        <h5 class="footer-heading mb-3">HỖ TRỢ</h5>
        <ul class="footer-links list-unstyled">
          <li><a href="${pageContext.request.contextPath}/policy/warranty">Chính sách bảo hành</a></li>
          <li><a href="${pageContext.request.contextPath}/policy/shipping">Chính sách vận chuyển</a></li>
          <li><a href="${pageContext.request.contextPath}/policy/return">Chính sách đổi trả</a></li>
          <li><a href="${pageContext.request.contextPath}/policy/payment">Thanh toán</a></li>
          <li><a href="${pageContext.request.contextPath}/faq">Câu hỏi thường gặp</a></li>
        </ul>
      </div>

      <!-- Column 3: Company -->
      <div class="col-lg-3 col-md-6">
        <h5 class="footer-heading mb-3">VỀ VIETTECH</h5>
        <ul class="footer-links list-unstyled">
          <li><a href="${pageContext.request.contextPath}/about">Giới thiệu</a></li>
          <li><a href="${pageContext.request.contextPath}/contact">Liên hệ</a></li>
          <li><a href="${pageContext.request.contextPath}/careers">Tuyển dụng</a></li>
          <li><a href="${pageContext.request.contextPath}/policy/terms">Điều khoản sử dụng</a></li>
          <li><a href="${pageContext.request.contextPath}/policy/privacy">Chính sách bảo mật</a></li>
        </ul>
      </div>

      <!-- Column 4: Contact -->
      <div class="col-lg-3 col-md-6">
        <h5 class="footer-heading mb-3">LIÊN HỆ</h5>
        <div class="footer-contact">
          <p class="footer-contact-item mb-2">
            <span class="footer-contact-label">Địa chỉ:</span><br>
            <span class="footer-contact-value">01 Đ. Võ Văn Ngân, Linh Chiểu, Thủ Đức, Thành phố Hồ Chí Minh</span>
          </p>
          <p class="footer-contact-item mb-2">
            <span class="footer-contact-label">Hotline:</span><br>
            <a href="tel:19009999" class="footer-contact-link">0866 448 892</a>
          </p>
          <p class="footer-contact-item mb-2">
            <span class="footer-contact-label">Email:</span><br>
            <a href="mailto:support@viettech.vn" class="footer-contact-link">support@viettech.vn</a>
          </p>
          <p class="footer-contact-item mb-0">
            <span class="footer-contact-label">Giờ làm việc:</span><br>
            <span class="footer-contact-value">8:00 - 22:00 (Thứ 2 - CN)</span>
          </p>
        </div>
      </div>

    </div>

    <!-- Divider -->
    <hr class="footer-divider my-4">

    <!-- Footer Bottom -->
    <div class="footer-bottom">
      <div class="row align-items-center g-3">

        <!-- Left: Copyright -->
        <div class="col-md-6 text-center text-md-start">
          <p class="footer-copyright mb-1">
            © 2025 VietTech. Tất cả các quyền được bảo lưu.
          </p>
          <p class="footer-legal mb-0">
            MST: 0123456789 | ĐKKD: 0123456789 do Sở KH&ĐT TP.HCM cấp
          </p>
        </div>

        <!-- Right: Payment Methods -->
        <div class="col-md-6 text-center text-md-end">
          <p class="footer-payment-title mb-2">Phương thức thanh toán:</p>
          <div class="payment-methods d-flex justify-content-center justify-content-md-end gap-2 flex-wrap">
            <span class="payment-badge">Visa</span>
            <span class="payment-badge">Mastercard</span>
            <span class="payment-badge">Momo</span>
            <span class="payment-badge">ZaloPay</span>
            <span class="payment-badge">COD</span>
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
      <button type="button" class="btn-close smember-close-btn" data-bs-dismiss="modal" aria-label="Close"></button>

      <div class="modal-body text-center py-5 px-4">
        <h1 class="smember-title mb-5">VietTech Member</h1>

        <p class="smember-text mb-5">
          Vui lòng đăng nhập tài khoản VTMember để<br>
          <strong>xem ưu đãi và thanh toán dễ dàng hơn.</strong>
        </p>

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