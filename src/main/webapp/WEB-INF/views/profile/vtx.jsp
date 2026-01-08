<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="pageTitle" value="VietTech Xu (VTX)" />
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<!-- MAIN CONTENT -->
<div class="col-lg-10 col-md-9">
  <div class="profile-content">
    <!-- Header -->
    <div class="profile-header">
      <h4><i class="bi bi-coin me-2"></i>VietTech Xu (VTX)</h4>
      <p class="text-muted">Tích điểm thưởng và nhận ưu đãi hấp dẫn từ VietTech</p>
    </div>

    <!-- Current Balance Card -->
    <div class="vtx-balance-card">
      <div class="balance-icon">
        <i class="bi bi-wallet2"></i>
      </div>
      <div class="balance-info">
        <small class="text-muted">Số dư VTX hiện tại</small>
        <h2 class="balance-amount">
          <fmt:formatNumber value="${loyaltyPoints}" type="number" groupingUsed="true"/> VTX
        </h2>
        <p class="text-muted mb-0">
          ≈ <fmt:formatNumber value="${loyaltyPoints * 1000}" type="number" groupingUsed="true"/>đ
        </p>
      </div>
    </div>

    <!-- Membership Tier Card -->
    <div class="row mt-4">
      <div class="col-md-12">
        <div class="vtx-tier-card">
          <div class="tier-header">
            <h5><i class="bi bi-trophy-fill me-2"></i>Hạng Thành Viên</h5>
          </div>

          <div class="tier-content">
            <div class="current-tier">
              <c:choose>
                <c:when test="${membershipTier == 'Bronze'}">
                  <div class="tier-badge bronze">
                    <i class="bi bi-award-fill"></i>
                    <span>Đồng</span>
                  </div>
                </c:when>
                <c:when test="${membershipTier == 'Silver'}">
                  <div class="tier-badge silver">
                    <i class="bi bi-award-fill"></i>
                    <span>Bạc</span>
                  </div>
                </c:when>
                <c:when test="${membershipTier == 'Gold'}">
                  <div class="tier-badge gold">
                    <i class="bi bi-award-fill"></i>
                    <span>Vàng</span>
                  </div>
                </c:when>
                <c:when test="${membershipTier == 'Platinum'}">
                  <div class="tier-badge platinum">
                    <i class="bi bi-gem"></i>
                    <span>Bạch Kim</span>
                  </div>
                </c:when>
              </c:choose>

              <div class="tier-info">
                <h4 class="mb-1">Hạng ${membershipTier}</h4>
                <p class="text-muted mb-0">
                  Tổng chi tiêu: <strong><fmt:formatNumber value="${totalSpent}" type="number" groupingUsed="true"/>đ</strong>
                </p>
              </div>
            </div>

            <!-- Progress to Next Tier -->
            <c:if test="${nextTierThreshold > 0}">
              <div class="tier-progress-section">
                <div class="d-flex justify-content-between align-items-center mb-2">
                  <small class="text-muted">Tiến độ lên hạng tiếp theo</small>
                  <small class="text-primary fw-bold">${tierProgress}%</small>
                </div>
                <div class="progress" style="height: 12px;">
                  <div class="progress-bar bg-primary"
                       role="progressbar"
                       style="width: ${tierProgress}%"
                       aria-valuenow="${tierProgress}"
                       aria-valuemin="0"
                       aria-valuemax="100">
                  </div>
                </div>
                <small class="text-muted mt-2 d-block">
                  Còn <strong><fmt:formatNumber value="${nextTierThreshold - totalSpent}" type="number" groupingUsed="true"/>đ</strong>
                  để đạt hạng tiếp theo
                </small>
              </div>
            </c:if>

            <c:if test="${membershipTier == 'Platinum'}">
              <div class="tier-max-reached">
                <i class="bi bi-check-circle-fill text-success me-2"></i>
                <span>Bạn đã đạt hạng thành viên cao nhất!</span>
              </div>
            </c:if>
          </div>
        </div>
      </div>
    </div>

    <!-- How to Earn VTX -->
    <div class="row mt-4">
      <div class="col-md-12">
        <div class="vtx-info-card">
          <h5 class="mb-3"><i class="bi bi-info-circle-fill me-2"></i>Cách Tích VTX</h5>

          <div class="vtx-earn-methods">
            <div class="earn-method-item">
              <div class="method-icon shopping">
                <i class="bi bi-cart-check-fill"></i>
              </div>
              <div class="method-content">
                <h6>Mua Sắm</h6>
                <p class="text-muted mb-0">Nhận 1 VTX cho mỗi 1.000đ chi tiêu</p>
              </div>
            </div>

            <div class="earn-method-item">
              <div class="method-icon review">
                <i class="bi bi-star-fill"></i>
              </div>
              <div class="method-content">
                <h6>Đánh Giá Sản Phẩm</h6>
                <p class="text-muted mb-0">Nhận 50 VTX cho mỗi đánh giá</p>
              </div>
            </div>

            <div class="earn-method-item">
              <div class="method-icon birthday">
                <i class="bi bi-gift-fill"></i>
              </div>
              <div class="method-content">
                <h6>Sinh Nhật</h6>
                <p class="text-muted mb-0">Nhận 500 VTX vào ngày sinh nhật</p>
              </div>
            </div>

            <div class="earn-method-item">
              <div class="method-icon refer">
                <i class="bi bi-people-fill"></i>
              </div>
              <div class="method-content">
                <h6>Giới Thiệu Bạn Bè</h6>
                <p class="text-muted mb-0">Nhận 200 VTX khi bạn bè đăng ký qua mã giới thiệu</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- How to Use VTX -->
    <div class="row mt-4">
      <div class="col-md-12">
        <div class="vtx-info-card">
          <h5 class="mb-3"><i class="bi bi-lightning-charge-fill me-2"></i>Cách Sử Dụng VTX</h5>

          <div class="vtx-use-methods">
            <div class="use-method-item">
              <div class="use-icon">
                <i class="bi bi-percent"></i>
              </div>
              <div class="use-content">
                <h6>Đổi Voucher</h6>
                <p class="text-muted mb-0">Dùng VTX để đổi các voucher giảm giá hấp dẫn</p>
              </div>
            </div>

            <div class="use-method-item">
              <div class="use-icon">
                <i class="bi bi-cash-coin"></i>
              </div>
              <div class="use-content">
                <h6>Thanh Toán</h6>
                <p class="text-muted mb-0">Sử dụng VTX để thanh toán khi mua hàng (1 VTX = 1.000đ)</p>
              </div>
            </div>

            <div class="use-method-item">
              <div class="use-icon">
                <i class="bi bi-box-seam"></i>
              </div>
              <div class="use-content">
                <h6>Đổi Quà Tặng</h6>
                <p class="text-muted mb-0">Đổi VTX lấy các phần quà đặc biệt từ VietTech</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Tier Benefits -->
    <div class="row mt-4">
      <div class="col-md-12">
        <div class="vtx-info-card">
          <h5 class="mb-3"><i class="bi bi-star-fill me-2"></i>Đặc Quyền Theo Hạng</h5>

          <div class="tier-benefits-grid">
            <div class="benefit-card bronze-border">
              <div class="benefit-header bronze">
                <i class="bi bi-award-fill"></i>
                <h6>Đồng</h6>
              </div>
              <ul class="benefit-list">
                <li><i class="bi bi-check-circle-fill text-success"></i> Tích điểm cơ bản</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Miễn phí vận chuyển đơn > 500k</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Voucher sinh nhật</li>
              </ul>
            </div>

            <div class="benefit-card silver-border">
              <div class="benefit-header silver">
                <i class="bi bi-award-fill"></i>
                <h6>Bạc</h6>
              </div>
              <ul class="benefit-list">
                <li><i class="bi bi-check-circle-fill text-success"></i> Tích điểm x1.5</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Miễn phí vận chuyển đơn > 300k</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Ưu tiên hỗ trợ</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Giảm 5% mọi đơn hàng</li>
              </ul>
            </div>

            <div class="benefit-card gold-border">
              <div class="benefit-header gold">
                <i class="bi bi-award-fill"></i>
                <h6>Vàng</h6>
              </div>
              <ul class="benefit-list">
                <li><i class="bi bi-check-circle-fill text-success"></i> Tích điểm x2</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Miễn phí vận chuyển mọi đơn</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Hỗ trợ VIP 24/7</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Giảm 10% mọi đơn hàng</li>
              </ul>
            </div>

            <div class="benefit-card platinum-border">
              <div class="benefit-header platinum">
                <i class="bi bi-gem"></i>
                <h6>Bạch Kim</h6>
              </div>
              <ul class="benefit-list">
                <li><i class="bi bi-check-circle-fill text-success"></i> Tích điểm x3</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Giao hàng ưu tiên</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Quản lý tài khoản riêng</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Giảm 15% mọi đơn hàng</li>
                <li><i class="bi bi-check-circle-fill text-success"></i> Quà tặng độc quyền</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="components/footer.jsp" %>