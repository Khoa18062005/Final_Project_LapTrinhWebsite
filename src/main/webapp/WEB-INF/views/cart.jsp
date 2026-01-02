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
<jsp:include page="/header.jsp" />

<div class="container my-5">
    <h1 class="mb-4 text-center">Giỏ hàng của bạn (${itemCount} sản phẩm)</h1>

    <c:choose>
        <c:when test="${empty cartItems}">
            <!-- Giỏ hàng trống -->
            <div class="card text-center py-5">
                <div class="card-body">
                    <i class="bi bi-cart-x" style="font-size: 5rem; color: #6c757d;"></i>
                    <h3 class="mt-3">Giỏ hàng đang trống</h3>
                    <p class="text-muted">Hãy thêm sản phẩm để tiếp tục mua sắm!</p>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                        <i class="bi bi-shop me-2"></i>Khám phá sản phẩm
                    </a>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <!-- Danh sách sản phẩm dạng card -->
            <div class="row g-4">
                <div class="col-12">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="select-all" checked>
                            <label class="form-check-label fw-bold" for="select-all">
                                Chọn tất cả
                            </label>
                        </div>
                        <button type="button" class="btn btn-outline-danger" onclick="cartController.confirmClearCart()">
                            <i class="bi bi-trash me-1"></i>Xóa toàn bộ
                        </button>
                    </div>

                    <c:forEach items="${cartItems}" var="item" varStatus="status">
                        <div class="card mb-3 cart-item-card" data-product-id="${item.productId}" data-variant-id="${item.variantId}">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <div class="col-1">
                                        <div class="form-check">
                                            <input class="form-check-input item-checkbox" type="checkbox"
                                                   value="${item.productId}" data-variant-id="${item.variantId}"
                                                   <c:if test="${item.selected}">checked</c:if>>
                                        </div>
                                    </div>
                                    <div class="col-2">
                                        <img src="${item.imageUrl}" alt="${item.productName}"
                                             class="img-fluid rounded" style="max-height: 100px;"
                                             onerror="this.src='${pageContext.request.contextPath}/assets/images/default-product.jpg'">
                                    </div>
                                    <div class="col-4">
                                        <h5 class="card-title">${item.productName}</h5>
                                        <c:if test="${not empty item.variantInfo}">
                                            <small class="text-muted">
                                                <i class="bi bi-tags me-1"></i>${item.variantInfo.variantName}
                                            </small>
                                        </c:if>
                                    </div>
                                    <div class="col-2">
                                        <div class="input-group input-group-sm">
                                            <button class="btn btn-outline-secondary" type="button"
                                                    onclick="cartController.decreaseQuantity(${item.productId}, ${item.variantId})">
                                                <i class="bi bi-dash"></i>
                                            </button>
                                            <input type="number" class="form-control text-center quantity-input"
                                                   value="${item.quantity}" min="1" max="99"
                                                   data-product-id="${item.productId}" data-variant-id="${item.variantId}"
                                                   onchange="cartController.updateQuantityFromInput(this)">
                                            <button class="btn btn-outline-secondary" type="button"
                                                    onclick="cartController.increaseQuantity(${item.productId}, ${item.variantId})">
                                                <i class="bi bi-plus"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="col-2 text-end">
                                        <div class="fw-bold text-danger">
                                            <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫" groupingUsed="true" />
                                        </div>
                                        <small class="text-muted">
                                            Tiểu tổng: <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" groupingUsed="true" />
                                        </small>
                                    </div>
                                    <div class="col-1 text-end">
                                        <button type="button" class="btn btn-link text-danger"
                                                onclick="cartController.removeItem(${item.productId}, ${item.variantId})">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Tóm tắt đơn hàng -->
            <div class="row mt-4">
                <div class="col-md-8">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">
                        <i class="bi bi-arrow-left me-2"></i>Tiếp tục mua sắm
                    </a>
                </div>
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header bg-success text-white">
                            <h5 class="mb-0">Tóm tắt</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-2">
                                <span>Tạm tính</span>
                                <span id="subtotal-amount">
                                    <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫" groupingUsed="true" />
                                </span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span>Phí vận chuyển</span>
                                <span id="shipping-fee">₫0</span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span>Giảm giá</span>
                                <span class="text-success" id="discount-amount">-₫0</span>
                            </div>
                            <hr>
                            <div class="d-flex justify-content-between fw-bold">
                                <span>Tổng cộng</span>
                                <span class="text-danger" id="total-amount">
                                    <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫" groupingUsed="true" />
                                </span>
                            </div>
                            <div class="mt-3">
                                <label for="voucher-code" class="form-label">Mã giảm giá</label>
                                <div class="input-group mb-3">
                                    <input type="text" class="form-control" id="voucher-code" placeholder="Nhập mã">
                                    <button class="btn btn-outline-success" type="button" id="apply-voucher">Áp dụng</button>
                                </div>
                            </div>
                            <button type="button" class="btn btn-success w-100 mt-2" id="checkout-btn" onclick="cartController.checkout()">
                                <i class="bi bi-cart-check me-2"></i>Đặt hàng ngay
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/footer.jsp" />

<!-- Modal xác nhận xóa -->
<div class="modal fade" id="confirmModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Xác nhận xóa</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                Bạn chắc chắn muốn xóa?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger" id="confirm-action">Xóa</button>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/assets/js/cart-controller.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
</body>
</html>