<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
  <meta charset="UTF-8">
  <title>VietTech | Sàn Thương Mại Điện Tử</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

  <!-- CSS riêng -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shipping-info.css">

  <style>
    .voucher-section {
      margin-top: 1.5rem;
      padding: 1.5rem;
      background-color: #f8f9fa;
      border-radius: 8px;
      border: 1px solid #dee2e6;
    }

    .voucher-input-group {
      display: flex;
      gap: 10px;
      margin-bottom: 1rem;
    }

    .voucher-list {
      max-height: 300px;
      overflow-y: auto;
    }

    .voucher-card {
      border: 2px solid #e9ecef;
      border-radius: 8px;
      padding: 1rem;
      margin-bottom: 0.75rem;
      cursor: pointer;
      transition: all 0.3s ease;
      background: white;
    }

    .voucher-card:hover {
      border-color: #0d6efd;
      box-shadow: 0 2px 8px rgba(13, 110, 253, 0.15);
    }

    .voucher-card.selected {
      border-color: #0d6efd;
      background-color: #e7f1ff;
    }

    .voucher-code {
      font-weight: bold;
      color: #0d6efd;
      font-size: 1.1rem;
    }

    .voucher-discount {
      color: #dc3545;
      font-weight: bold;
      font-size: 1.2rem;
    }

    .voucher-condition {
      font-size: 0.85rem;
      color: #6c757d;
    }

    .voucher-expiry {
      font-size: 0.8rem;
      color: #6c757d;
    }

    .discount-badge {
      background-color: #ffc107;
      color: #000;
      padding: 0.25rem 0.5rem;
      border-radius: 4px;
      font-weight: bold;
      font-size: 0.9rem;
    }
  </style>
</head>

<body>
<!-- Header -->
<jsp:include page="../../header.jsp" />

<div class="container mt-4 mb-5">
  <!-- Breadcrumb -->
  <nav aria-label="breadcrumb">
    <ol class="breadcrumb">
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/cart">Giỏ hàng</a></li>
      <li class="breadcrumb-item active">Chọn địa chỉ giao hàng</li>
    </ol>
  </nav>

  <!-- Thông báo lỗi nếu có -->
  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        ${errorMessage}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  </c:if>

  <!-- Hiển thị thông tin Buy Now nếu có -->
  <div class="main-layout-container">
    <!-- Phần 1: Thông tin đơn hàng -->
    <div class="order-summary-wrapper">
      <c:if test="${not empty selectedCartItems}">
        <div class="order-summary-card">
          <div class="order-summary-header">
            <h5><i class="bi bi-cart-check"></i> Đơn hàng từ giỏ hàng</h5>
          </div>

          <!-- NỘI DUNG CHÍNH - Phải có class này -->
          <div class="order-summary-main">
            <!-- Container cho danh sách sản phẩm -->
            <div class="order-items-container">
              <c:forEach var="item" items="${selectedCartItems}" varStatus="status">
                <div class="order-item">
                  <div class="order-item-image">
                    <img src="${item.productImage}" alt="${item.productName}"
                         onerror="this.src='${pageContext.request.contextPath}/assets/images/default-product.jpg'">
                  </div>
                  <div class="order-item-details">
                    <h6>${item.productName}</h6>
                    <p class="text-muted mb-0">Số lượng: ${item.quantity}</p>
                    <c:if test="${not empty item.variantDisplay}">
                      <p class="text-muted mb-0">${item.variantDisplay}</p>
                    </c:if>
                  </div>
                  <div class="order-item-price">
                    <h5>
                      <fmt:formatNumber value="${item.price}" type="currency"
                                        currencySymbol="₫" groupingUsed="true" />
                    </h5>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>

          <!-- Total section - LUÔN Ở DƯỚI CÙNG -->
          <div class="total-section">
            <div class="total-row">
              <div class="total-label">Tổng cộng:</div>
              <div class="total-value">
                <fmt:formatNumber value="${total}" type="currency"
                                  currencySymbol="₫" groupingUsed="true" />
              </div>
            </div>
          </div>
        </div>
      </c:if>
    </div>

    <!-- Phần 2: Chọn địa chỉ và phương thức thanh toán -->
    <div class="address-selection-wrapper">
      <div class="card shadow">
        <div class="card-header bg-primary text-white">
          <h4 class="mb-0"><i class="bi bi-geo-alt"></i> Chọn địa chỉ giao hàng</h4>
        </div>
        <div class="card-body">
          <div class="card-body-content">
            <c:choose>
              <c:when test="${empty savedAddresses}">
                <div class="text-center py-5">
                  <i class="bi bi-geo-alt-x display-1 text-muted"></i>
                  <h4 class="mt-3">Bạn chưa có địa chỉ nào</h4>
                  <p class="text-muted">Vui lòng thêm địa chỉ trước khi thanh toán</p>
                  <a href="${pageContext.request.contextPath}/address/add" class="btn btn-primary">
                    <i class="bi bi-plus-circle"></i> Thêm địa chỉ mới
                  </a>
                  <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-secondary ms-2">
                    <i class="bi bi-arrow-left"></i> Quay lại giỏ hàng
                  </a>
                </div>
              </c:when>
              <c:otherwise>
                <form action="${pageContext.request.contextPath}/checkout" method="POST" id="addressForm">
                  <h5 class="mb-4">Vui lòng chọn địa chỉ giao hàng:</h5>

                  <!-- Danh sách địa chỉ -->
                  <div class="address-list">
                    <!-- Default Address -->
                    <c:if test="${not empty defaultAddress}">
                      <div class="card mb-3 border-2 border-primary">
                        <div class="card-body">
                          <div class="form-check">
                            <input class="form-check-input" type="radio"
                                   name="selectedAddressId"
                                   value="${defaultAddress.addressId}"
                                   checked
                                   id="default-address">
                            <label class="form-check-label w-100" for="default-address">
                              <div class="d-flex justify-content-between align-items-start">
                                <div>
                                  <strong>${defaultAddress.receiverName}</strong> | ${defaultAddress.phone}
                                  <p class="mb-1">${defaultAddress.street}</p>
                                  <p class="mb-1">${defaultAddress.ward}, ${defaultAddress.district}, ${defaultAddress.city}</p>
                                </div>
                                <span class="badge bg-primary">Mặc định</span>
                              </div>
                            </label>
                          </div>
                        </div>
                      </div>
                    </c:if>

                    <!-- Other Addresses -->
                    <c:forEach var="address" items="${savedAddresses}" varStatus="status">
                      <c:if test="${not address.isDefault()}">
                        <div class="card mb-3 border-2 border-light">
                          <div class="card-body">
                            <div class="form-check">
                              <input class="form-check-input" type="radio"
                                     name="selectedAddressId"
                                     value="${address.addressId}"
                                     id="address-${status.index}">
                              <label class="form-check-label w-100" for="address-${status.index}">
                                <div class="d-flex justify-content-between align-items-start">
                                  <div>
                                    <strong>${address.receiverName}</strong> | ${address.phone}
                                    <p class="mb-1">${address.street}</p>
                                    <p class="mb-1">${address.ward}, ${address.district}, ${address.city}</p>
                                  </div>
                                </div>
                              </label>
                            </div>
                          </div>
                        </div>
                      </c:if>
                    </c:forEach>
                  </div>

                  <!-- ========================================== -->
                  <!-- PHẦN CHỌN VOUCHER - MỚI THÊM -->
                  <!-- ========================================== -->
                  <div class="voucher-section">
                    <h5 class="mb-3"><i class="bi bi-ticket-perforated"></i> Mã giảm giá</h5>

                    <!-- Input nhập mã voucher -->
                    <div class="voucher-input-group">
                      <input type="text"
                             class="form-control"
                             id="voucherCodeInput"
                             name="voucherCode"
                             placeholder="Nhập mã voucher (nếu có)">
                      <button type="button"
                              class="btn btn-outline-primary"
                              id="applyVoucherBtn">
                        Áp dụng
                      </button>
                    </div>

                    <!-- Hiển thị voucher đang áp dụng -->
                    <div id="appliedVoucherDisplay" class="alert alert-success d-none">
                      <i class="bi bi-check-circle"></i>
                      Đã áp dụng voucher: <strong id="appliedVoucherCode"></strong>
                      <button type="button" class="btn-close float-end" id="removeVoucherBtn"></button>
                    </div>

                    <!-- Danh sách voucher có sẵn -->
                    <c:if test="${not empty availableVouchers}">
                      <div class="mt-3">
                        <h6 class="text-muted mb-2">Hoặc chọn từ danh sách voucher:</h6>
                        <div class="voucher-list">
                          <c:forEach var="voucher" items="${availableVouchers}">
                            <div class="voucher-card"
                                 data-voucher-code="${voucher.code}"
                                 data-voucher-id="${voucher.voucherId}"
                                 data-voucher-type="${voucher.type}"
                                 data-discount-percent="${voucher.discountPercent}"
                                 data-discount-amount="${voucher.discountAmount}"
                                 data-max-discount="${voucher.maxDiscount}"
                                 data-min-order="${voucher.minOrderValue}">
                              <div class="d-flex justify-content-between align-items-start">
                                <div class="flex-grow-1">
                                  <div class="voucher-code">${voucher.code}</div>
                                  <div class="voucher-name mb-1">${voucher.name}</div>
                                  <div class="voucher-discount mb-1">
                                    <c:choose>
                                      <c:when test="${voucher.type == 'PERCENTAGE'}">
                                        Giảm ${voucher.discountPercent}%
                                        <c:if test="${voucher.maxDiscount > 0}">
                                          (Tối đa <fmt:formatNumber value="${voucher.maxDiscount}" type="currency" currencySymbol="₫" groupingUsed="true" />)
                                        </c:if>
                                      </c:when>
                                      <c:when test="${voucher.type == 'FIXED_AMOUNT'}">
                                        Giảm ngay <fmt:formatNumber value="${voucher.discountAmount}" type="currency" currencySymbol="₫" groupingUsed="true" />
                                      </c:when>
                                      <c:otherwise>
                                        Tối đa <fmt:formatNumber value="${voucher.maxDiscount}" type="currency" currencySymbol="₫" groupingUsed="true" />
                                      </c:otherwise>
                                    </c:choose>
                                  </div>
                                  <div class="voucher-condition">
                                    Đơn tối thiểu: <fmt:formatNumber value="${voucher.minOrderValue}" type="currency" currencySymbol="₫" groupingUsed="true" />
                                  </div>
                                  <div class="voucher-expiry">
                                    <i class="bi bi-clock"></i>
                                    HSD: <fmt:formatDate value="${voucher.expiryDate}" pattern="dd/MM/yyyy HH:mm" />
                                  </div>
                                </div>
                                <div>
                                  <span class="discount-badge">
                                    <c:choose>
                                      <c:when test="${voucher.type == 'PERCENTAGE'}">
                                        -${voucher.discountPercent}%
                                      </c:when>
                                      <c:otherwise>
                                        -<fmt:formatNumber value="${voucher.discountAmount}" pattern="#,###" />đ
                                      </c:otherwise>
                                    </c:choose>
                                  </span>
                                </div>
                              </div>
                            </div>
                          </c:forEach>
                        </div>
                      </div>
                    </c:if>

                    <c:if test="${empty availableVouchers}">
                      <div class="alert alert-info mt-3">
                        <i class="bi bi-info-circle"></i> Hiện tại không có voucher khả dụng
                      </div>
                    </c:if>
                  </div>
                  <!-- ========================================== -->
                  <!-- KẾT THÚC PHẦN CHỌN VOUCHER -->
                  <!-- ========================================== -->

                  <div class="loyalty-section">
                    <h5 class="mb-3"><i class="bi bi-star-fill"></i> Điểm tích lũy</h5>

                    <!-- Hiển thị số điểm hiện có -->
                    <div class="loyalty-balance-card">
                      <div class="d-flex justify-content-between align-items-center mb-3">
                        <div>
                          <span class="text-muted">Điểm tích lũy hiện có:</span>
                          <h4 class="mb-0 mt-1">
                            <span class="loyalty-points-display">${customer.loyaltyPoints}</span>
                            <small class="text-muted">điểm</small>
                          </h4>
                        </div>
                        <div class="text-end">
                          <span class="text-muted d-block" style="font-size: 0.85rem;">Giá trị quy đổi:</span>
                          <h5 class="mb-0 text-success">
                            <fmt:formatNumber value="${customer.loyaltyPoints * 1000}" type="number" pattern="#,###"/>đ
                          </h5>
                        </div>
                      </div>

                      <!-- Thông tin quy đổi -->
                      <div class="alert alert-info mb-3" style="font-size: 0.9rem;">
                        <i class="bi bi-info-circle"></i> 1 điểm = 1,000đ | Tối đa sử dụng đến khi tổng đơn hàng = 0đ
                      </div>
                    </div>

                    <!-- Input nhập số điểm muốn sử dụng -->
                    <div class="loyalty-input-group">
                      <label for="loyaltyPointsInput" class="form-label">
                        Nhập số điểm muốn sử dụng:
                      </label>
                      <div class="input-group">
                        <input type="number"
                               class="form-control"
                               id="loyaltyPointsInput"
                               name="loyaltyPoints"
                               min="0"
                               max="${customer.loyaltyPoints}"
                               placeholder="Nhập số điểm"
                               value="0">
                        <button type="button"
                                class="btn btn-outline-primary"
                                id="applyLoyaltyBtn">
                          Áp dụng
                        </button>
                        <button type="button"
                                class="btn btn-outline-success"
                                id="useAllLoyaltyBtn">
                          Dùng hết
                        </button>
                      </div>
                      <small class="text-muted mt-1 d-block">
                        Tối đa: ${customer.loyaltyPoints} điểm
                      </small>
                    </div>

                    <!-- Hiển thị điểm đang sử dụng -->
                    <div id="appliedLoyaltyDisplay" class="alert alert-success d-none mt-3">
                      <div class="d-flex justify-content-between align-items-center">
                        <div>
                          <i class="bi bi-check-circle"></i>
                          Đang sử dụng: <strong id="appliedLoyaltyPoints">0</strong> điểm
                          <span class="ms-2 text-muted">
                    (Giảm <strong id="appliedLoyaltyValue">0đ</strong>)
                </span>
                        </div>
                        <button type="button" class="btn-close" id="removeLoyaltyBtn"></button>
                      </div>
                    </div>

                    <!-- Cảnh báo nếu không đủ điểm -->
                    <div id="loyaltyWarning" class="alert alert-warning d-none mt-3">
                      <i class="bi bi-exclamation-triangle"></i>
                      <span id="loyaltyWarningMessage"></span>
                    </div>
                  </div>

                  <!-- Thêm input hidden để gửi về server -->
                  <input type="hidden" name="usedLoyaltyPoints" id="usedLoyaltyPoints" value="0">

                  <!-- Phương thức thanh toán -->
                  <div class="payment-method-section mb-4">
                    <label for="paymentMethod" class="form-label fw-bold">Chọn phương thức thanh toán:</label>
                    <select class="form-select form-select-lg" id="paymentMethod" name="paymentMethod" required>
                      <option value="" disabled selected>-- Vui lòng chọn --</option>
                      <option value="COD">Thanh toán khi nhận hàng (COD)</option>
                      <option value="VNPAY">VNPay</option>
                    </select>
                    <div class="payment-method-icons mt-2">
                      <small class="text-muted">
                        <i class="bi bi-truck me-1"></i>COD: Thanh toán khi nhận hàng
                      </small>
                    </div>
                  </div>

                  <div class="action-buttons-container">
                    <!-- Hàng trên: Nút quay lại và thêm địa chỉ -->
                    <div class="d-flex justify-content-between mb-3">
                      <a href="${pageContext.request.contextPath}/cart" class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> Quay lại giỏ hàng
                      </a>
                      <a href="${pageContext.request.contextPath}/profile/address" class="btn btn-outline-primary">
                        <i class="bi bi-plus-circle"></i> Thêm địa chỉ mới
                      </a>
                    </div>

                    <!-- Hàng dưới: Nút tiếp tục thanh toán chiếm toàn bộ chiều ngang -->
                    <button type="submit" class="btn btn-primary w-100 btn-lg">
                      Tiếp tục thanh toán <i class="bi bi-arrow-right"></i>
                    </button>
                  </div>
                </form>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Footer -->
<jsp:include page="../../footer.jsp" />

<script>
  // Biến toàn cục cho JavaScript - QUAN TRỌNG: PHẢI CÓ
  const contextPath = "${pageContext.request.contextPath}";
  const isLoggedIn = ${not empty sessionScope.user};
  const orderTotal = ${total}; // Tổng tiền đơn hàng
</script>

<!-- Script riêng cho popup login -->
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
<!-- Script riêng cho chọn địa chỉ -->
<script src="${pageContext.request.contextPath}/assets/js/address-dropdown.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/voucher-handler.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/checkout-calculator.js"></script>

</body>
</html>