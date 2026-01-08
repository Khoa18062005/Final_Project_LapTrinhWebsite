<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="pageTitle" value="Đơn Mua" />
<%@ include file="components/header.jsp" %>

<div class="row">
    <%@ include file="components/sidebar.jsp" %>

    <!-- MAIN CONTENT -->
    <div class="col-lg-10 col-md-9">
        <div class="profile-content">
            <!-- Header -->
            <div class="profile-header orders-header">
                <div>
                    <h4><i class="bi bi-box-seam me-2"></i>Đơn Mua</h4>
                    <p class="text-muted mb-0">Quản lý và theo dõi đơn hàng của bạn</p>
                </div>
            </div>

            <!-- Tabs Navigation -->
            <ul class="nav nav-tabs orders-tabs mb-4" id="ordersTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${empty param.status or param.status == 'all' ? 'active' : ''}"
                            data-status="all"
                            type="button">
                        <i class="bi bi-list-ul"></i>
                        <span>Tất cả</span>
                        <span class="badge bg-secondary ms-1">${orderCounts['all']}</span>
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${param.status == 'pending' ? 'active' : ''}"
                            data-status="pending"
                            type="button">
                        <i class="bi bi-clock-history"></i>
                        <span>Chờ thanh toán</span>
                        <span class="badge bg-warning ms-1">${orderCounts['pending']}</span>
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${param.status == 'processing' ? 'active' : ''}"
                            data-status="processing"
                            type="button">
                        <i class="bi bi-arrow-repeat"></i>
                        <span>Đang xử lý</span>
                        <span class="badge bg-info ms-1">${orderCounts['processing']}</span>
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${param.status == 'shipping' ? 'active' : ''}"
                            data-status="shipping"
                            type="button">
                        <i class="bi bi-truck"></i>
                        <span>Đang giao hàng</span>
                        <span class="badge bg-primary ms-1">${orderCounts['shipping']}</span>
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${param.status == 'completed' ? 'active' : ''}"
                            data-status="completed"
                            type="button">
                        <i class="bi bi-check-circle"></i>
                        <span>Hoàn thành</span>
                        <span class="badge bg-success ms-1">${orderCounts['completed']}</span>
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${param.status == 'cancelled' ? 'active' : ''}"
                            data-status="cancelled"
                            type="button">
                        <i class="bi bi-x-circle"></i>
                        <span>Đã hủy</span>
                        <span class="badge bg-danger ms-1">${orderCounts['cancelled']}</span>
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link ${param.status == 'returned' ? 'active' : ''}"
                            data-status="returned"
                            type="button">
                        <i class="bi bi-arrow-return-left"></i>
                        <span>Trả hàng/Hoàn tiền</span>
                        <span class="badge bg-secondary ms-1">${orderCounts['returned']}</span>
                    </button>
                </li>
            </ul>

            <!-- Tab Content -->
            <div class="tab-content" id="ordersTabContent">
                <c:choose>
                    <%-- Empty State --%>
                    <c:when test="${empty orders}">
                        <div class="empty-state text-center py-5">
                            <i class="bi bi-box-seam fs-1 text-muted mb-3 d-block"></i>
                            <h5>Chưa có đơn hàng</h5>
                            <p class="text-muted mb-4">
                                <c:choose>
                                    <c:when test="${param.status == 'pending'}">Bạn chưa có đơn hàng nào đang chờ thanh toán</c:when>
                                    <c:when test="${param.status == 'processing'}">Bạn chưa có đơn hàng nào đang xử lý</c:when>
                                    <c:when test="${param.status == 'shipping'}">Bạn chưa có đơn hàng nào đang giao</c:when>
                                    <c:when test="${param.status == 'completed'}">Bạn chưa có đơn hàng nào hoàn thành</c:when>
                                    <c:when test="${param.status == 'cancelled'}">Bạn chưa có đơn hàng nào bị hủy</c:when>
                                    <c:when test="${param.status == 'returned'}">Bạn chưa có đơn hàng nào trả hàng/hoàn tiền</c:when>
                                    <c:otherwise>Bạn chưa có đơn hàng nào</c:otherwise>
                                </c:choose>
                            </p>
                            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                                <i class="bi bi-cart-plus me-2"></i>Mua sắm ngay
                            </a>
                        </div>
                    </c:when>

                    <%-- Order List --%>
                    <c:otherwise>
                        <div class="orders-list">
                            <c:forEach var="order" items="${orders}">
                                <div class="order-card">
                                        <%-- Order Header --%>
                                    <div class="order-header">
                                        <div class="order-info-left">
                                            <span class="order-id">
                                                <i class="bi bi-hash"></i>
                                                <strong>Mã đơn:</strong> ${order.orderNumber}
                                            </span>
                                            <span class="order-date">
                                                <i class="bi bi-calendar3"></i>
                                                <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                            </span>
                                        </div>
                                        <div class="order-status">
                                            <c:choose>
                                                <c:when test="${order.status == 'pending'}">
                                                    <span class="badge badge-warning">
                                                        <i class="bi bi-clock-history"></i> Chờ thanh toán
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.status == 'processing'}">
                                                    <span class="badge badge-info">
                                                        <i class="bi bi-arrow-repeat"></i> Đang xử lý
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.status == 'shipping'}">
                                                    <span class="badge badge-primary">
                                                        <i class="bi bi-truck"></i> Đang giao hàng
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.status == 'completed'}">
                                                    <span class="badge badge-success">
                                                        <i class="bi bi-check-circle"></i> Hoàn thành
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.status == 'cancelled'}">
                                                    <span class="badge badge-danger">
                                                        <i class="bi bi-x-circle"></i> Đã hủy
                                                    </span>
                                                </c:when>
                                                <c:when test="${order.status == 'returned'}">
                                                    <span class="badge badge-secondary">
                                                        <i class="bi bi-arrow-return-left"></i> Trả hàng
                                                    </span>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                    </div>

                                        <%-- Order Body --%>
                                    <div class="order-body">
                                        <div class="order-products">
                                            <c:forEach var="item" items="${order.items}">
                                                <%-- ✅ DEBUG: Kiểm tra productId --%>
                                                <!-- DEBUG: productId = ${item.productId} -->

                                                <div class="product-item">
                                                    <div class="product-image">
                                                        <img src="${item.productImage}"
                                                             alt="${item.productName}"
                                                             onerror="this.src='${pageContext.request.contextPath}/assets/images/no-image.png'">
                                                    </div>
                                                    <div class="product-details">
                                                        <h6 class="product-name">${item.productName}</h6>
                                                        <c:if test="${not empty item.variantInfo}">
                                                            <p class="product-variant text-muted mb-1">
                                                                Phân loại: ${item.variantInfo}
                                                            </p>
                                                        </c:if>
                                                        <p class="product-quantity mb-0">
                                                            <span class="text-muted">Số lượng:</span> x${item.quantity}
                                                        </p>

                                                            <%-- ✅ NÚT ĐÁNH GIÁ --%>
                                                        <c:if test="${order.status == 'completed'}">
                                                            <button class="btn btn-outline-warning btn-sm mt-2"
                                                                    onclick="reviewProduct(${item.productId})">
                                                                <i class="bi bi-star me-1"></i>Đánh giá
                                                            </button>
                                                        </c:if>
                                                    </div>
                                                    <div class="product-price">
                                                        <fmt:formatNumber value="${item.price}" type="number" groupingUsed="true"/>đ
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>

                                        <%-- Order Footer --%>
                                    <div class="order-footer">
                                        <div class="order-total">
                                            <span class="text-muted">Tổng tiền:</span>
                                            <h5 class="total-amount mb-0">
                                                <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true"/>đ
                                            </h5>
                                        </div>
                                        <div class="order-actions">
                                            <c:choose>
                                                <%-- Pending: Chi tiết + Thanh toán + Hủy --%>
                                                <c:when test="${order.status == 'pending'}">
                                                    <button class="btn btn-outline-primary btn-sm"
                                                            onclick="viewOrderDetail(${order.orderId})">
                                                        <i class="bi bi-eye me-1"></i>Chi tiết
                                                    </button>
                                                    <button class="btn btn-primary btn-sm"
                                                            onclick="payOrder(${order.orderId})">
                                                        <i class="bi bi-credit-card me-1"></i>Thanh toán
                                                    </button>
                                                    <button class="btn btn-outline-danger btn-sm"
                                                            onclick="cancelOrder(${order.orderId})">
                                                        <i class="bi bi-x-circle me-1"></i>Hủy đơn
                                                    </button>
                                                </c:when>

                                                <%-- Processing/Shipping: Chi tiết + Hủy --%>
                                                <c:when test="${order.status == 'processing' or order.status == 'shipping'}">
                                                    <button class="btn btn-outline-primary btn-sm"
                                                            onclick="viewOrderDetail(${order.orderId})">
                                                        <i class="bi bi-eye me-1"></i>Chi tiết
                                                    </button>
                                                    <button class="btn btn-outline-danger btn-sm"
                                                            onclick="cancelOrder(${order.orderId})">
                                                        <i class="bi bi-x-circle me-1"></i>Hủy đơn
                                                    </button>
                                                </c:when>

                                                <%-- Completed: Chi tiết + Mua lại --%>
                                                <c:when test="${order.status == 'completed'}">
                                                    <button class="btn btn-outline-primary btn-sm"
                                                            onclick="viewOrderDetail(${order.orderId})">
                                                        <i class="bi bi-eye me-1"></i>Chi tiết
                                                    </button>
                                                    <button class="btn btn-outline-secondary btn-sm"
                                                            onclick="reorder()">
                                                        <i class="bi bi-arrow-clockwise me-1"></i>Mua lại
                                                    </button>
                                                </c:when>

                                                <%-- Cancelled: Chi tiết + Mua lại --%>
                                                <c:when test="${order.status == 'cancelled'}">
                                                    <button class="btn btn-outline-primary btn-sm"
                                                            onclick="viewOrderDetail(${order.orderId})">
                                                        <i class="bi bi-eye me-1"></i>Chi tiết
                                                    </button>
                                                    <button class="btn btn-outline-secondary btn-sm"
                                                            onclick="reorder()">
                                                        <i class="bi bi-arrow-clockwise me-1"></i>Mua lại
                                                    </button>
                                                </c:when>

                                                <%-- Returned: Chi tiết --%>
                                                <c:when test="${order.status == 'returned'}">
                                                    <button class="btn btn-outline-primary btn-sm"
                                                            onclick="viewOrderDetail(${order.orderId})">
                                                        <i class="bi bi-eye me-1"></i>Chi tiết
                                                    </button>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<%-- ✅ MODAL CHI TIẾT ĐƠN HÀNG --%>
<div class="modal fade" id="orderDetailModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    <i class="bi bi-box-seam me-2"></i>Chi Tiết Đơn Hàng
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="orderDetailContent">
                <!-- Content will be loaded by JavaScript -->
                <div class="text-center py-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<c:set var="pageScript" value="orders.js" />
<%@ include file="components/footer.jsp" %>