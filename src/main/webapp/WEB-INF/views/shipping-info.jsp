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
  <c:if test="${isBuyNow}">
    <div class="alert alert-info mb-4">
      <h5 class="alert-heading"><i class="bi bi-lightning-charge"></i> Đang mua ngay</h5>
      <div class="row mt-3">
        <div class="col-md-2">
          <img src="${buyNowItem.productImage}" alt="${buyNowItem.productName}"
               class="img-fluid rounded" style="max-height: 80px;">
        </div>
        <div class="col-md-6">
          <h6>${buyNowItem.productName}</h6>
          <p class="text-muted mb-0">Số lượng: ${buyNowItem.quantity}</p>
        </div>
        <div class="col-md-4 text-end">
          <h5 class="text-primary">
            <fmt:formatNumber value="${buyNowItem.totalPrice}" type="currency"
                              currencySymbol="₫" groupingUsed="true" />
          </h5>
        </div>
      </div>
    </div>
  </c:if>
  <c:if test="${not empty selectedCartItems}">
    <div class="alert alert-info mb-4">
      <h5 class="alert-heading"><i class="bi bi-cart-check"></i> Đơn hàng từ giỏ hàng</h5>
      <c:forEach var="item" items="${selectedCartItems}" varStatus="status">
        <div class="row mt-3">
          <div class="col-md-2">
            <img src="${item.productImage}" alt="${item.productName}"
                 class="img-fluid rounded" style="max-height: 80px;"
                 onerror="this.src='${pageContext.request.contextPath}/assets/images/default-product.jpg'">
          </div>
          <div class="col-md-6">
            <h6>${item.productName}</h6>
            <p class="text-muted mb-0">Số lượng: ${item.quantity}</p>
            <c:if test="${not empty item.variantDisplay}">
              <p class="text-muted mb-0">${item.variantDisplay}</p>
            </c:if>
          </div>
          <div class="col-md-4 text-end">
            <h5 class="text-primary">
              <fmt:formatNumber value="${item.price}" type="currency"
                                currencySymbol="₫" groupingUsed="true" />
            </h5>
          </div>
        </div>
        <c:if test="${not status.last}"><hr></c:if>
      </c:forEach>
      <div class="row mt-3">
        <div class="col-12 text-end">
          <h5>Tổng cộng:
            <fmt:formatNumber value="${total}" type="currency"
                              currencySymbol="₫" groupingUsed="true" />
          </h5>
        </div>
      </div>
    </div>
  </c:if>

  <div class="row">
    <div class="col-lg-8 mx-auto">
      <div class="card shadow">
        <div class="card-header bg-primary text-white">
          <h4 class="mb-0"><i class="bi bi-geo-alt"></i> Chọn địa chỉ giao hàng</h4>
        </div>
        <div class="card-body">
          <!-- Thông báo nếu không có địa chỉ nào -->
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

                <!-- Nút điều hướng -->
                <div class="d-flex justify-content-between">
                  <a href="${pageContext.request.contextPath}/cart" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> Quay lại giỏ hàng
                  </a>
                  <div>
                    <a href="${pageContext.request.contextPath}/address" class="btn btn-outline-primary me-2">
                      <i class="bi bi-plus-circle"></i> Thêm địa chỉ mới
                    </a>
                    <button type="submit" class="btn btn-primary">
                      Tiếp tục thanh toán <i class="bi bi-arrow-right"></i>
                    </button>
                  </div>
                </div>
              </form>
            </c:otherwise>
          </c:choose>
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
</script>

<!-- Script riêng cho popup login -->
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
<script>
  // Xử lý form submission
  document.getElementById('addressForm').addEventListener('submit', function(e) {
    var selectedAddress = document.querySelector('input[name="selectedAddressId"]:checked');
    if (!selectedAddress) {
      e.preventDefault();
      alert('Vui lòng chọn một địa chỉ giao hàng');
      return false;
    }
    return true;
  });

  // Thêm hiệu ứng khi chọn địa chỉ
  document.querySelectorAll('input[name="selectedAddressId"]').forEach(function(radio) {
    radio.addEventListener('change', function() {
      // Loại bỏ class border-primary từ tất cả các card
      document.querySelectorAll('.address-list .card').forEach(function(card) {
        card.classList.remove('border-primary');
        card.classList.add('border-light');
      });

      // Thêm class border-primary vào card được chọn
      var selectedCard = this.closest('.card');
      selectedCard.classList.remove('border-light');
      selectedCard.classList.add('border-primary');
    });
  });

  // Khởi tạo border cho địa chỉ được chọn ban đầu
  document.addEventListener('DOMContentLoaded', function() {
    var checkedRadio = document.querySelector('input[name="selectedAddressId"]:checked');
    if (checkedRadio) {
      var selectedCard = checkedRadio.closest('.card');
      selectedCard.classList.remove('border-light');
      selectedCard.classList.add('border-primary');
    }
  });
</script>
</body>
</html>
