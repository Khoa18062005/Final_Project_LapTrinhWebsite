<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/product-detail.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/variant-selector.css">

    <!-- JavaScript cho chọn variant -->
    <script src="${pageContext.request.contextPath}/assets/js/variant-selector.js" defer></script>
    <script src="${pageContext.request.contextPath}/assets/js/cart-ajax.js" defer></script>
    <script src="${pageContext.request.contextPath}/assets/js/product-detail.js" defer></script>
    <script>
        // Khởi tạo biến toàn cục từ dữ liệu JSP
        <c:if test="${not empty variants}">
        window.variantData = {
            productId: ${product.productId},
            basePrice: ${product.basePrice},
            variants: [
                <c:forEach items="${variants}" var="variant" varStatus="status">
                {
                    id: ${variant.variantId},
                    price: ${variant.finalPrice},
                    isActive: ${variant.active},
                    attributes: [
                        <c:forEach items="${variant.attributes}" var="attr" varStatus="attrStatus">
                        {
                            id: ${attr.attributeId},
                            name: "${attr.attributeName}",
                            value: "${attr.attributeValue}"
                        }<c:if test="${!attrStatus.last}">,</c:if>
                        </c:forEach>
                    ]
                }<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ]
        };
        </c:if>
    </script>
</head>

<body>
<jsp:include page="/header.jsp" />

<!-- Toast Container -->
<div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
    <div id="cartToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header bg-success text-white">
            <i class="bi bi-check-circle-fill me-2"></i>
            <strong class="me-auto">Thành công</strong>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
            <span id="toast-message">Đã thêm sản phẩm vào giỏ hàng!</span>
        </div>
    </div>
</div>

<div class="container">
    <!-- Breadcrumb -->
    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/">Trang chủ</a>
        <span>›</span>
        <c:choose>
            <c:when test="${product.categoryId == 1}">
                <a href="${pageContext.request.contextPath}/category?id=1">Điện thoại</a>
            </c:when>
            <c:when test="${product.categoryId == 3}">
                <a href="${pageContext.request.contextPath}/category?id=3">Laptop</a>
            </c:when>
            <c:when test="${product.categoryId == 4}">
                <a href="${pageContext.request.contextPath}/category?id=4">Tablet</a>
            </c:when>
            <c:when test="${product.categoryId == 5}">
                <a href="${pageContext.request.contextPath}/category?id=5">Tai nghe</a>
            </c:when>
        </c:choose>
        <span>›</span>
        <span>${product.name}</span>
    </div>

    <!-- Product Main Info -->
    <div class="product-container">
        <!-- Product Images -->
        <div class="product-images">
            <div class="main-image">
                <img id="product-img" src="${product.primaryImageUrl}" alt="${product.name}">
                <div class="zoom-controls">
                    <button type="button" onclick="zoomIn()" class="btn-zoom" title="Phóng to">
                        <i class="bi bi-zoom-in"></i>
                    </button>
                    <button type="button" onclick="zoomOut()" class="btn-zoom" title="Thu nhỏ">
                        <i class="bi bi-zoom-out"></i>
                    </button>
                    <button type="button" onclick="resetZoom()" class="btn-zoom" title="Đặt lại">
                        <i class="bi bi-arrow-counterclockwise"></i>
                    </button>
                </div>
            </div>
        </div>

        <!-- Product Info -->
        <div class="product-info">
            <c:choose>
                <c:when test="${product.categoryId == 1}">
                    <span class="product-category">Điện thoại</span>
                </c:when>
                <c:when test="${product.categoryId == 3}">
                    <span class="product-category">Laptop</span>
                </c:when>
                <c:when test="${product.categoryId == 4}">
                    <span class="product-category">Tablet</span>
                </c:when>
                <c:when test="${product.categoryId == 5}">
                    <span class="product-category">Tai nghe</span>
                </c:when>
            </c:choose>

            <h1 class="product-title">${product.name}</h1>
            <p class="product-brand">Thương hiệu: <strong>${product.brand}</strong></p>

            <div class="product-rating">
                <span class="stars">
                    <c:forEach begin="1" end="5" var="i">
                        <c:choose>
                            <c:when test="${i <= product.averageRating}">★</c:when>
                            <c:otherwise>☆</c:otherwise>
                        </c:choose>
                    </c:forEach>
                </span>
                <span class="rating-text">
                    <fmt:formatNumber value="${product.averageRating}" pattern="#.#" /> / 5.0
                    (${product.totalReviews} đánh giá)
                </span>
            </div>

            <div class="product-price">
                <div class="price-value" id="dynamic-price">
                    <fmt:formatNumber value="${product.basePrice}" type="currency" currencySymbol="₫"
                                      groupingUsed="true" />
                </div>
                <div class="price-label">Giá chưa bao gồm VAT</div>
            </div>

            <!-- Variant Selection Section -->
            <c:if test="${not empty variants}">
                <div class="variant-section">
                    <h4>Chọn phiên bản:</h4>
                    <div class="variant-options">
                        <c:forEach items="${variants}" var="variant">
                            <button type="button" id = "variant-btn"
                                    class="btn btn-outline-primary variant-option"
                                    data-variant-id="${variant.variantId}"
                                    onclick="window.selectVariant(${variant.variantId})">
                                <c:forEach items="${variant.attributes}" var="attr" varStatus="attrStatus">
                                    ${attr.attributeValue}<c:if test="${!attrStatus.last}"> - </c:if>
                                </c:forEach>
                            </button>
                        </c:forEach>
                    </div>

                    <!-- Current Variant Info -->
                    <div class="variant-info" id="variant-info">
                        <!-- Dynamic content will be inserted here by JavaScript -->
                    </div>
                </div>
            </c:if>

            <div class="product-status">
                <div class="status-item">
                    <span class="status-label">Trạng thái:</span>
                    <c:choose>
                        <c:when test="${product.status == 'Available'}">
                            <span class="status-badge status-active">Đang bán</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge status-inactive">Ngừng bán</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="status-item">
                    <span class="status-label">Đã bán:</span>
                    <span class="status-value">${product.totalSold}</span>
                </div>
                <div class="status-item">
                    <span class="status-label">Lượt xem:</span>
                    <span class="status-value">${product.viewCount}</span>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons">
                <!-- Kiểm tra đăng nhập cho nút Thêm vào giỏ hàng -->
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <form action="${pageContext.request.contextPath}/cart" method="POST"
                              class="d-inline" id="add-to-cart-form">
                            <input type="hidden" name="productId" value="${product.productId}">
                            <input type="hidden" name="quantity" value="1">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="variantId" id="selected-variant-id" value="">
                            <button type="submit" class="btn btn-primary" id="add-to-cart-btn">
                                <i class="bi bi-cart-plus"></i> Thêm vào giỏ hàng
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <button type="button"
                                class="btn btn-primary"
                                id="add-to-cart-btn"
                                data-bs-toggle="modal"
                                data-bs-target="#smemberModal">
                            <i class="bi bi-cart-plus"></i> Thêm vào giỏ hàng
                        </button>
                    </c:otherwise>
                </c:choose>

                <!-- Kiểm tra đăng nhập cho nút Mua ngay -->
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <form action="${pageContext.request.contextPath}/checkout/buy-now" method="post" id="buy-now-form">
                            <input type="hidden" name="productId" value="${product.productId}">
                            <input type="hidden" name="quantity" value="1">
                            <input type="hidden" name="variantId" id="buy-now-variant-id" value="">
                            <button type="submit" class="btn btn-secondary" id="buy-now-btn">
                                Mua ngay
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <button type="button"
                                class="btn btn-secondary"
                                id="buy-now-btn"
                                data-bs-toggle="modal"
                                data-bs-target="#smemberModal">
                            Mua ngay
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Specifications Section với variant attributes -->
    <div class="specs-section">
        <h2 class="section-title">Thông số kỹ thuật</h2>

        <!-- Variant Attributes Section -->
        <div id="variant-specs-container" class="variant-specs-container">
            <!-- Thông tin variant sẽ được thêm vào đây -->
        </div>

        <!-- Standard Product Specifications -->
        <div id="standard-specs-container">
            <c:choose>
                <%-- PHONE SPECIFICATIONS --%>
                <c:when test="${product.categoryId == 1}">
                    <div class="specs-grid">
                        <div class="spec-item">
                            <span class="spec-label">Màn hình:</span>
                            <span class="spec-value">${product.screenSize}" - ${product.screenResolution}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Loại màn hình:</span>
                            <span class="spec-value">${product.screenType}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Tần số quét:</span>
                            <span class="spec-value">${product.refreshRate}Hz</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Dung lượng pin:</span>
                            <span class="spec-value">${product.batteryCapacity}mAh</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Sạc:</span>
                            <span class="spec-value">${product.chargerType} - ${product.chargingSpeed}W</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Chip xử lý:</span>
                            <span class="spec-value">${product.processor}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">GPU:</span>
                            <span class="spec-value">${product.gpu}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Camera sau:</span>
                            <span class="spec-value">${product.rearCamera}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Camera trước:</span>
                            <span class="spec-value">${product.frontCamera}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Quay video:</span>
                            <span class="spec-value">${product.videoRecording}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Hệ điều hành:</span>
                            <span class="spec-value">${product.os} ${product.osVersion}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">SIM:</span>
                            <span class="spec-value">${product.simType}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Mạng:</span>
                            <span class="spec-value">${product.networkSupport}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Kết nối:</span>
                            <span class="spec-value">${product.connectivity}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">NFC:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.nfc ? 'spec-yes' : 'spec-no'}">
                                        ${product.nfc ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Chống nước:</span>
                            <span class="spec-value">${product.waterproofRating}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Chống bụi:</span>
                            <span class="spec-value">${product.dustproofRating}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Vân tay:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.fingerprintSensor ? 'spec-yes' : 'spec-no'}">
                                        ${product.fingerprintSensor ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Nhận diện khuôn mặt:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.faceRecognition ? 'spec-yes' : 'spec-no'}">
                                        ${product.faceRecognition ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Sạc không dây:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.wirelessCharging ? 'spec-yes' : 'spec-no'}">
                                        ${product.wirelessCharging ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Sạc ngược:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.reverseCharging ? 'spec-yes' : 'spec-no'}">
                                        ${product.reverseCharging ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Jack tai nghe 3.5mm:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.audioJack ? 'spec-yes' : 'spec-no'}">
                                        ${product.audioJack ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                    </div>
                </c:when>

                <%-- LAPTOP SPECIFICATIONS --%>
                <c:when test="${product.categoryId == 3}">
                    <div class="specs-grid">
                        <div class="spec-item">
                            <span class="spec-label">CPU:</span>
                            <span class="spec-value">${product.cpu} ${product.cpuGeneration}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Tốc độ CPU:</span>
                            <span class="spec-value">${product.cpuSpeed}GHz</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">GPU:</span>
                            <span class="spec-value">${product.gpu}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Bộ nhớ GPU:</span>
                            <span class="spec-value">${product.gpuMemory}GB</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">RAM:</span>
                            <span class="spec-value">${product.ram}GB ${product.ramType}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">RAM tối đa:</span>
                            <span class="spec-value">${product.maxRam}GB</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Ổ cứng:</span>
                            <span class="spec-value">${product.storage}GB ${product.storageType}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Khe cắm thêm:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.additionalSlot ? 'spec-yes' : 'spec-no'}">
                                        ${product.additionalSlot ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Màn hình:</span>
                            <span class="spec-value">${product.screenSize}" - ${product.screenResolution}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Loại màn hình:</span>
                            <span class="spec-value">${product.screenType}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Tần số quét:</span>
                            <span class="spec-value">${product.refreshRate}Hz</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Gam màu:</span>
                            <span class="spec-value">${product.colorGamut}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Dung lượng pin:</span>
                            <span class="spec-value">${product.batteryCapacity}Wh</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Thời lượng pin:</span>
                            <span class="spec-value">${product.batteryLife} giờ</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Cổng kết nối:</span>
                            <span class="spec-value">${product.ports}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Hệ điều hành:</span>
                            <span class="spec-value">${product.os}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Bàn phím:</span>
                            <span class="spec-value">${product.keyboardType}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Đèn nền:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.keyboardBacklight ? 'spec-yes' : 'spec-no'}">
                                        ${product.keyboardBacklight ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Webcam:</span>
                            <span class="spec-value">${product.webcam}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Loa:</span>
                            <span class="spec-value">${product.speakers}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Micro:</span>
                            <span class="spec-value">${product.microphone}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Màn hình cảm ứng:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.touchScreen ? 'spec-yes' : 'spec-no'}">
                                        ${product.touchScreen ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Vân tay:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.fingerprintSensor ? 'spec-yes' : 'spec-no'}">
                                        ${product.fingerprintSensor ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">GPU rời:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.discreteGpu ? 'spec-yes' : 'spec-no'}">
                                        ${product.discreteGpu ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Thunderbolt:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.thunderbolt ? 'spec-yes' : 'spec-no'}">
                                        ${product.thunderbolt ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                    </div>
                </c:when>

                <%-- TABLET SPECIFICATIONS --%>
                <c:when test="${product.categoryId == 4}">
                    <div class="specs-grid">
                        <div class="spec-item">
                            <span class="spec-label">Màn hình:</span>
                            <span class="spec-value">${product.screenSize}" - ${product.screenResolution}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Loại màn hình:</span>
                            <span class="spec-value">${product.screenType}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Tần số quét:</span>
                            <span class="spec-value">${product.refreshRate}Hz</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Dung lượng pin:</span>
                            <span class="spec-value">${product.batteryCapacity}mAh</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Chip xử lý:</span>
                            <span class="spec-value">${product.processor}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">GPU:</span>
                            <span class="spec-value">${product.gpu}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Camera sau:</span>
                            <span class="spec-value">${product.rearCamera}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Camera trước:</span>
                            <span class="spec-value">${product.frontCamera}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Quay video:</span>
                            <span class="spec-value">${product.videoRecording}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Hệ điều hành:</span>
                            <span class="spec-value">${product.os} ${product.osVersion}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Hỗ trợ SIM:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.simSupport ? 'spec-yes' : 'spec-no'}">
                                        ${product.simSupport ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Mạng:</span>
                            <span class="spec-value">${product.networkSupport}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Kết nối:</span>
                            <span class="spec-value">${product.connectivity}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Hỗ trợ bút:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.stylusSupport ? 'spec-yes' : 'spec-no'}">
                                        ${product.stylusSupport ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Bút đi kèm:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.stylusIncluded ? 'spec-yes' : 'spec-no'}">
                                        ${product.stylusIncluded ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Hỗ trợ bàn phím:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.keyboardSupport ? 'spec-yes' : 'spec-no'}">
                                        ${product.keyboardSupport ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Loa:</span>
                            <span class="spec-value">${product.speakers}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Jack tai nghe:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.audioJack ? 'spec-yes' : 'spec-no'}">
                                        ${product.audioJack ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Chống nước:</span>
                            <span class="spec-value">${product.waterproofRating}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Nhận diện khuôn mặt:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.faceRecognition ? 'spec-yes' : 'spec-no'}">
                                        ${product.faceRecognition ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Vân tay:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.fingerprintSensor ? 'spec-yes' : 'spec-no'}">
                                        ${product.fingerprintSensor ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                    </div>
                </c:when>

                <%-- HEADPHONE SPECIFICATIONS --%>
                <c:when test="${product.categoryId == 5}">
                    <div class="specs-grid">
                        <div class="spec-item">
                            <span class="spec-label">Loại:</span>
                            <span class="spec-value">${product.type}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Kiểu đeo:</span>
                            <span class="spec-value">${product.formFactor}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Thời lượng pin:</span>
                            <span class="spec-value">${product.batteryLife} giờ</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Thời gian sạc:</span>
                            <span class="spec-value">${product.chargingTime} phút</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Cổng sạc:</span>
                            <span class="spec-value">${product.chargingPort}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Chống ồn:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.noiseCancellation ? 'spec-yes' : 'spec-no'}">
                                        ${product.noiseCancellation ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <c:if test="${product.noiseCancellation}">
                            <div class="spec-item">
                                <span class="spec-label">Loại chống ồn:</span>
                                <span class="spec-value">${product.noiseCancellationType}</span>
                            </div>
                        </c:if>
                        <div class="spec-item">
                            <span class="spec-label">Chế độ xuyên âm:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.ambientMode ? 'spec-yes' : 'spec-no'}">
                                        ${product.ambientMode ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Kích thước driver:</span>
                            <span class="spec-value">${product.driverSize}mm</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Loại driver:</span>
                            <span class="spec-value">${product.driverType}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Dải tần:</span>
                            <span class="spec-value">${product.frequencyResponse}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Trở kháng:</span>
                            <span class="spec-value">${product.impedance}Ω</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Độ nhạy:</span>
                            <span class="spec-value">${product.sensitivity}dB</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Micro:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.microphone ? 'spec-yes' : 'spec-no'}">
                                        ${product.microphone ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <c:if test="${product.microphone}">
                            <div class="spec-item">
                                <span class="spec-label">Loại micro:</span>
                                <span class="spec-value">${product.micType}</span>
                            </div>
                        </c:if>
                        <div class="spec-item">
                            <span class="spec-label">Chống nước:</span>
                            <span class="spec-value">${product.waterproofRating}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Kết nối:</span>
                            <span class="spec-value">${product.connectivity}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Phiên bản Bluetooth:</span>
                            <span class="spec-value">${product.bluetoothVersion}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Codec Bluetooth:</span>
                            <span class="spec-value">${product.bluetoothCodecs}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Kết nối dây:</span>
                            <span class="spec-value">${product.wiredConnection}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Multipoint:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.multipoint ? 'spec-yes' : 'spec-no'}">
                                        ${product.multipoint ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Chất âm:</span>
                            <span class="spec-value">${product.soundProfile}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Điều khiển qua app:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.appControl ? 'spec-yes' : 'spec-no'}">
                                        ${product.appControl ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">EQ tùy chỉnh:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.customEQ ? 'spec-yes' : 'spec-no'}">
                                        ${product.customEQ ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Âm thanh vòm:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.surroundSound ? 'spec-yes' : 'spec-no'}">
                                        ${product.surroundSound ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Gấp gọn:</span>
                            <span class="spec-value">
                                <span class="spec-boolean ${product.foldable ? 'spec-yes' : 'spec-no'}">
                                        ${product.foldable ? 'Có' : 'Không'}
                                </span>
                            </span>
                        </div>
                    </div>
                </c:when>
            </c:choose>
        </div>
    </div>

    <!-- Description Section -->
    <div class="description-section">
        <h2 class="section-title">Mô tả sản phẩm</h2>
        <div class="description-text">
            <c:choose>
                <c:when test="${not empty product.description}">
                    ${product.description}
                </c:when>
                <c:otherwise>
                    <p style="color: #999;">Chưa có mô tả chi tiết cho sản phẩm này.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Additional Info -->
    <div class="specs-section">
        <h2 class="section-title">Thông tin bổ sung</h2>
        <div class="specs-grid">
            <div class="spec-item">
                <span class="spec-label">Trọng lượng:</span>
                <span class="spec-value">${product.weight}g</span>
            </div>
            <div class="spec-item">
                <span class="spec-label">Kích thước:</span>
                <span class="spec-value">${product.dimensions}</span>
            </div>
            <div class="spec-item">
                <span class="spec-label">Tình trạng:</span>
                <span class="spec-value">${product.conditions}</span>
            </div>
            <div class="spec-item">
                <span class="spec-label">Ngày thêm:</span>
                <span class="spec-value">
                    <fmt:formatDate value="${product.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                </span>
            </div>
            <c:if test="${not empty product.updatedAt}">
                <div class="spec-item">
                    <span class="spec-label">Cập nhật lần cuối:</span>
                    <span class="spec-value">
                        <fmt:formatDate value="${product.updatedAt}" pattern="dd/MM/yyyy HH:mm" />
                    </span>
                </div>
            </c:if>
            <c:if test="${product.featured}">
                <div class="spec-item">
                    <span class="spec-label">Sản phẩm nổi bật:</span>
                    <span class="spec-value">
                        <span class="spec-boolean spec-yes">Có</span>
                    </span>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Reviews Section -->
    <div class="reviews-section" style="background: white; border-radius: 12px; padding: 2rem; margin-top: 2rem; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
        <h2 class="section-title" style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 1.5rem; padding-bottom: 1rem; border-bottom: 2px solid #e0e0e0;">
            <i class="bi bi-star-fill" style="color: #ffc107;"></i> Đánh giá sản phẩm
            <span class="review-count" style="font-size: 0.9rem; font-weight: 400; color: #6c757d;">(${fn:length(reviews)} đánh giá)</span>
        </h2>

        <!-- Review Summary -->
        <div class="review-summary">
            <div class="rating-overview">
                <div class="average-rating">
                    <span class="rating-number">
                        <fmt:formatNumber value="${product.averageRating}" pattern="#.#"/>
                    </span>
                    <span class="rating-max">/5</span>
                </div>
                <div class="rating-stars">
                    <c:forEach begin="1" end="5" var="i">
                        <c:choose>
                            <c:when test="${i <= product.averageRating}">
                                <i class="bi bi-star-fill text-warning"></i>
                            </c:when>
                            <c:when test="${i - 0.5 <= product.averageRating}">
                                <i class="bi bi-star-half text-warning"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-star text-warning"></i>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
                <div class="total-reviews">${product.totalReviews} đánh giá</div>
            </div>
        </div>

        <!-- Add Review Form -->
        <div class="add-review-section">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:choose>
                        <c:when test="${hasUserReviewed}">
                            <div class="alert alert-info">
                                <i class="bi bi-info-circle"></i> Bạn đã đánh giá sản phẩm này rồi.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="review-form-container" style="background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%); border-radius: 12px; padding: 1.75rem; border: 1px solid #e0e0e0; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                                <h4 style="margin-bottom: 1.25rem; color: #212529; display: flex; align-items: center; gap: 0.5rem; font-size: 1.15rem; font-weight: 700; padding-bottom: 0.75rem; border-bottom: 2px solid #0d6efd;"><i class="bi bi-pencil-square" style="color: #0d6efd;"></i> Viết đánh giá của bạn</h4>
                                <form id="reviewForm" class="review-form">
                                    <input type="hidden" name="productId" value="${product.productId}">

                                    <div class="form-group rating-input">
                                        <label>Đánh giá của bạn <span class="text-danger">*</span></label>
                                        <div class="star-rating-input">
                                            <input type="radio" id="star5" name="rating" value="5">
                                            <label for="star5" title="5 sao"><i class="bi bi-star-fill"></i></label>
                                            <input type="radio" id="star4" name="rating" value="4">
                                            <label for="star4" title="4 sao"><i class="bi bi-star-fill"></i></label>
                                            <input type="radio" id="star3" name="rating" value="3">
                                            <label for="star3" title="3 sao"><i class="bi bi-star-fill"></i></label>
                                            <input type="radio" id="star2" name="rating" value="2">
                                            <label for="star2" title="2 sao"><i class="bi bi-star-fill"></i></label>
                                            <input type="radio" id="star1" name="rating" value="1">
                                            <label for="star1" title="1 sao"><i class="bi bi-star-fill"></i></label>
                                        </div>
                                        <span id="ratingText" class="rating-text-display"></span>
                                    </div>

                                    <div class="form-group">
                                        <label for="reviewTitle">Tiêu đề</label>
                                        <input type="text" id="reviewTitle" name="title" class="form-control"
                                               placeholder="Nhập tiêu đề đánh giá (tùy chọn)" maxlength="200">
                                    </div>

                                    <div class="form-group">
                                        <label for="reviewComment">Nội dung đánh giá <span class="text-danger">*</span></label>
                                        <textarea id="reviewComment" name="comment" class="form-control" rows="4"
                                                  placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm..." required></textarea>
                                    </div>

                                    <div class="form-group text-end">
                                        <button type="submit" class="btn btn-submit-review" style="display: inline-flex; align-items: center; justify-content: center; gap: 0.5rem; padding: 0.85rem 2rem; font-weight: 600; font-size: 1rem; background: linear-gradient(135deg, #0d6efd 0%, #0b5ed7 100%); color: white; border: none; border-radius: 8px; cursor: pointer; box-shadow: 0 4px 15px rgba(13, 110, 253, 0.3);">
                                            <i class="bi bi-send-fill"></i> Gửi đánh giá
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <div class="login-to-review">
                        <p><i class="bi bi-person-circle"></i> Vui lòng
                            <a href="#" data-bs-toggle="modal" data-bs-target="#smemberModal">đăng nhập</a>
                            để viết đánh giá
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Reviews List -->
        <div class="reviews-list">
            <c:choose>
                <c:when test="${not empty reviews}">
                    <c:forEach items="${reviews}" var="review">
                        <div class="review-item" data-review-id="${review.reviewId}">
                            <div class="review-header">
                                <div class="reviewer-info">
                                    <div class="reviewer-avatar">
                                        <c:choose>
                                            <c:when test="${not empty review.customerAvatar}">
                                                <img src="${review.customerAvatar}" alt="Avatar">
                                            </c:when>
                                            <c:otherwise>
                                                <i class="bi bi-person-circle"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="reviewer-details">
                                        <span class="reviewer-name">${review.customerName}</span>
                                        <c:if test="${review.verifiedPurchase}">
                                            <span class="verified-badge">
                                                <i class="bi bi-patch-check-fill"></i> Đã mua hàng
                                            </span>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="review-date">
                                    <fmt:formatDate value="${review.reviewDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </div>
                            </div>

                            <div class="review-rating">
                                <c:forEach begin="1" end="5" var="i">
                                    <c:choose>
                                        <c:when test="${i <= review.rating}">
                                            <i class="bi bi-star-fill text-warning"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="bi bi-star text-warning"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>

                            <c:if test="${not empty review.title}">
                                <h5 class="review-title">${review.title}</h5>
                            </c:if>

                            <div class="review-content">
                                <p>${review.comment}</p>
                            </div>

                            <div class="review-actions">
                                <button class="btn btn-sm btn-outline-secondary btn-helpful"
                                        data-review-id="${review.reviewId}">
                                    <i class="bi bi-hand-thumbs-up"></i>
                                    Hữu ích <span class="helpful-count">(${review.helpfulCount})</span>
                                </button>
                            </div>

                            <c:if test="${not empty review.responseContent}">
                                <div class="vendor-response">
                                    <div class="response-header">
                                        <i class="bi bi-reply-fill"></i>
                                        <strong>Phản hồi từ người bán</strong>
                                        <span class="response-date">
                                            - <fmt:formatDate value="${review.responseDate}" pattern="dd/MM/yyyy"/>
                                        </span>
                                    </div>
                                    <div class="response-content">
                                        ${review.responseContent}
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="no-reviews">
                        <i class="bi bi-chat-square-text"></i>
                        <p>Chưa có đánh giá nào cho sản phẩm này.</p>
                        <p>Hãy là người đầu tiên đánh giá!</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp" />

<!-- Biến JavaScript để kiểm tra trạng thái đăng nhập -->
<script>
    const contextPath = "${pageContext.request.contextPath}";
    const isLoggedIn = ${not empty sessionScope.user};
</script>

<!-- Script riêng cho popup login -->
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/product-review.js"></script>
</body>
</html>