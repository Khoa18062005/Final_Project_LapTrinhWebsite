<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng | VietTech</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

    <!-- CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cart.css">
</head>

<body>
<jsp:include page="../../header.jsp" />

<div class="container my-5">
    <h1 class="mb-4">Giỏ hàng của bạn</h1>

    <c:choose>
        <c:when test="${empty cartItems}">
            <div class="empty-cart text-center py-5">
                <div class="empty-icon mb-3">
                    <i class="bi bi-cart-x" style="font-size: 4rem; color: #6c757d;"></i>
                </div>
                <h3 class="mb-3">Giỏ hàng trống</h3>
                <p class="text-muted mb-4">Bạn chưa có sản phẩm nào trong giỏ hàng.</p>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                    <i class="bi bi-house-door me-2"></i>Tiếp tục mua sắm
                </a>
            </div>
        </c:when>

        <c:otherwise>
            <div class="row">
                <!-- Danh sách sản phẩm -->
                <div class="col-lg-8">
                    <div class="card mb-4">
                        <div class="card-header bg-light">
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="select-all">
                                    <label class="form-check-label fw-bold" for="select-all">
                                        Chọn tất cả (${itemCount} sản phẩm)
                                    </label>
                                </div>
                                <button type="button" class="btn btn-link text-danger p-0"
                                        onclick="cartController.confirmClearCart()">
                                    <i class="bi bi-trash me-1"></i>Xóa tất cả
                                </button>
                            </div>
                        </div>

                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <tbody>
                                    <c:forEach items="${cartItems}" var="item" varStatus="status">
                                        <tr class="cart-item-row"
                                            data-product-id="${item.productId}"
                                            data-variant-id="${item.variantId}">
                                            <td style="width: 60px;">
                                                <div class="form-check">
                                                    <input class="form-check-input item-checkbox"
                                                           type="checkbox"
                                                           value="${item.productId}"
                                                           data-variant-id="${item.variantId}"
                                                           <c:if test="${item.selected}">checked</c:if>>
                                                </div>
                                            </td>
                                            <td style="width: 100px;">
                                                <img src="${item.imageUrl}"
                                                     alt="${item.productName}"
                                                     class="img-fluid rounded cart-item-image"
                                                     onerror="this.src='${pageContext.request.contextPath}/assets/images/default-product.jpg'">
                                            </td>
                                            <td>
                                                <div>
                                                    <h6 class="mb-1">${item.productName}</h6>
                                                    <c:if test="${not empty item.variantInfo}">
                                                        <small class="text-muted">
                                                            <i class="bi bi-tag me-1"></i>
                                                                ${item.variantDisplay}
                                                        </small>
                                                    </c:if>
                                                </div>
                                            </td>
                                            <td style="width: 150px;">
                                                <div class="input-group input-group-sm">
                                                    <input type="number"
                                                           class="form-control text-center quantity-input"
                                                           value="${item.quantity}"
                                                           min="1"
                                                           max="99"
                                                           data-product-id="${item.productId}"
                                                           data-variant-id="${item.variantId}"
                                                           onchange="cartController.updateQuantityFromInput(this)">
                                                </div>
                                            </td>
                                            <td style="width: 120px;">
                                                <div class="text-end">
                                                    <div class="fw-bold text-danger item-price">
                                                        <fmt:formatNumber value="${item.price}"
                                                                          type="currency"
                                                                          currencySymbol="₫"
                                                                          groupingUsed="true" />
                                                    </div>
                                                    <div class="text-muted small item-subtotal">
                                                        <fmt:formatNumber value="${item.subtotal}"
                                                                          type="currency"
                                                                          currencySymbol="₫"
                                                                          groupingUsed="true" />
                                                    </div>
                                                </div>
                                            </td>
                                            <td style="width: 50px;">
                                                <button type="button"
                                                        class="btn btn-link text-danger p-0"
                                                        onclick="cartController.removeItem(${item.productId}, ${item.variantId})">
                                                    <i class="bi bi-x-lg"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/" class="btn btn-outline-primary">
                            <i class="bi bi-arrow-left me-2"></i>Tiếp tục mua sắm
                        </a>
                        <button type="button" class="btn btn-danger" onclick="cartController.confirmClearCart()">
                            <i class="bi bi-trash me-2"></i>Xóa giỏ hàng
                        </button>
                    </div>
                </div>

                <!-- Thanh toán -->
                <div class="col-lg-4">
                    <div class="card sticky-top" style="top: 20px;">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">Tóm tắt đơn hàng</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-2">
                                <span>Tạm tính:</span>
                                <span id="subtotal-amount">
                                        <fmt:formatNumber value="${total}"
                                                          type="currency"
                                                          currencySymbol="₫"
                                                          groupingUsed="true" />
                                    </span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span>Phí vận chuyển:</span>
                                <span id="shipping-fee">₫0</span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span>Giảm giá:</span>
                                <span class="text-success" id="discount-amount">-₫0</span>
                            </div>
                            <hr>
                            <div class="d-flex justify-content-between mb-3">
                                <span class="fw-bold">Tổng cộng:</span>
                                <span class="fw-bold fs-5 text-danger" id="total-amount">
                                        <fmt:formatNumber value="${total}"
                                                          type="currency"
                                                          currencySymbol="₫"
                                                          groupingUsed="true" />
                                    </span>
                            </div>

                            <div class="mb-3">
                                <label for="voucher-code" class="form-label">Mã giảm giá</label>
                                <div class="input-group">
                                    <input type="text"
                                           class="form-control"
                                           id="voucher-code"
                                           placeholder="Nhập mã giảm giá">
                                    <button class="btn btn-outline-primary" type="button" id="apply-voucher">
                                        Áp dụng
                                    </button>
                                </div>
                                <div id="voucher-message" class="mt-1 small"></div>
                            </div>

                            <button type="button"
                                    class="btn btn-success w-100 py-3 fw-bold"
                                    id="checkout-btn"
                                    onclick="cartController.checkout()">
                                <i class="bi bi-credit-card me-2"></i>THANH TOÁN
                            </button>

                            <div class="mt-3 text-center">
                                <small class="text-muted">
                                    Bằng việc nhấn vào thanh toán, bạn đồng ý với
                                    <a href="#">Điều khoản dịch vụ</a> của chúng tôi
                                </small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../../footer.jsp" />

<!-- Modal xác nhận xóa -->
<div class="modal fade" id="confirmModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Xác nhận</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="modal-message">
                Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger" id="confirm-action">Xóa</button>
            </div>
        </div>
    </div>
</div>

<script>
    window.contextPath = '<%= request.getContextPath() %>';
</script>

<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/assets/js/cart-controller.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
</body>
</html>