<footer class="footer bg-dark text-light mt-5">
  <div class="modal fade" id="smemberModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content smember-modal-content">
        <!-- Nút đóng góc trên PHẢI -->
        <button type="button" class="btn-close smember-close-btn" data-bs-dismiss="modal" aria-label="Close"></button>

        <div class="modal-body text-center py-5 px-4">
          <!-- Tiêu đề Smember gradient xanh -->
          <h1 class="smember-title mb-5">Smember</h1>

          <!-- Nội dung -->
          <p class="smember-text mb-5">
            Vui lòng đăng nhập tài khoản Smember để<br>
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
  <div class="container py-5">
    <div class="row">

      <!-- About -->
      <div class="col-md-4 mb-4">
        <h5 class="fw-bold">VietTech</h5>
        <p>
          Nền tảng thương mại điện tử chuyên sản phẩm công nghệ chính hãng.
          Cam kết chất lượng – Giá tốt – Hỗ trợ tận tâm.
        </p>
      </div>

      <!-- Links -->
      <div class="col-md-4 mb-4">
        <h5 class="fw-bold">Hỗ trợ khách hàng</h5>
        <ul class="list-unstyled">
          <li><a href="#" class="footer-link">Chính sách bảo hành</a></li>
          <li><a href="#" class="footer-link">Hướng dẫn mua hàng</a></li>
          <li><a href="#" class="footer-link">Phương thức thanh toán</a></li>
          <li><a href="#" class="footer-link">Liên hệ</a></li>
        </ul>
      </div>

      <!-- Contact -->
      <div class="col-md-4 mb-4">
        <h5 class="fw-bold">Liên hệ</h5>
        <p><i class="bi bi-geo-alt"></i> TP. Hồ Chí Minh</p>
        <p><i class="bi bi-telephone"></i> 1900 9999</p>
        <p><i class="bi bi-envelope"></i> support@viettech.vn</p>
      </div>

    </div>

    <hr class="border-secondary">

    <div class="text-center small">
      © 2025 VietTech. All rights reserved.
    </div>
  </div>
</footer>

<!-- JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>

