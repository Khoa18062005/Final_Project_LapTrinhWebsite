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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/product-detail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/variant-selector.css">

    <!-- JavaScript cho chọn variant -->
    <script src="${pageContext.request.contextPath}/assets/js/variant-selector.js" defer></script>
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
<jsp:include page="/WEB-INF/views/layout/header.jsp" />

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
                <img src="${product.primaryImageUrl}">
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
                            <button type="button"
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
                        <c:when test="${product.status == 'active'}">
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
                <form action="${pageContext.request.contextPath}/cart/add" method="POST"
                      class="d-inline" id="add-to-cart-form">
                    <input type="hidden" name="productId" value="${product.productId}">
                    <input type="hidden" name="quantity" value="1">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="variantId" id="selected-variant-id" value="">
                    <button type="submit" class="btn btn-primary" id="add-to-cart-btn">
                        <i class="bi bi-cart-plus"></i> Thêm vào giỏ hàng
                    </button>
                </form>

                <form action="${pageContext.request.contextPath}/checkout/buy-now" method="post" id="buy-now-form">
                    <input type="hidden" name="productId" value="${product.productId}">
                    <input type="hidden" name="quantity" value="1">
                    <input type="hidden" name="variantId" id="buy-now-variant-id" value="">
                    <button type="submit" class="btn btn-secondary" id="buy-now-btn">
                        Mua ngay
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- Specifications Section với variant attributes -->
    <div class="specs-section">
        <h2 class="section-title">Thông số kỹ thuật</h2>

        <!-- Variant Attributes Section (sẽ được cập nhật bằng JavaScript) -->
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
                        <!-- THÊM CÁC TRƯỜNG SAU ĐÂY -->
                        <div class="spec-item">
                            <span class="spec-label">Chip xử lý:</span>
                            <span class="spec-value">${product.processor}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">RAM:</span>
                            <!-- THÊM TRƯỜNG RAM CHO TABLET -->
                            <c:choose>
                                <c:when test="${not empty product.ram}">
                                    <span class="spec-value">${product.ram}GB ${product.ramType}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="spec-value">Không có thông tin</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Bộ nhớ trong:</span>
                            <!-- THÊM TRƯỜNG STORAGE CHO TABLET -->
                            <c:choose>
                                <c:when test="${not empty product.storage}">
                                    <span class="spec-value">${product.storage}GB</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="spec-value">Không có thông tin</span>
                                </c:otherwise>
                            </c:choose>
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
                            <span class="specvalue"> <span
                                    class="spec-boolean ${product.multipoint ? 'spec-yes' : 'spec-no'}">
                                    ${product.multipoint ? 'Có' : 'Không'} </span> </span> </div>
                        <div class="spec-item"> <span class="spec-label">Chất âm:</span> <span
                                class="spec-value">${product.soundProfile}</span> </div>
                        <div class="spec-item"> <span class="spec-label">Điều khiển qua app:</span> <span
                                class="spec-value"> <span
                                class="spec-boolean ${product.appControl ? 'spec-yes' : 'spec-no'}">
                                ${product.appControl ? 'Có' : 'Không'} </span> </span> </div>
                        <div class="spec-item"> <span class="spec-label">EQ tùy chỉnh:</span> <span class="spec-value">
                                <span class="spec-boolean ${product.customEQ ? 'spec-yes' : 'spec-no'}">
                                        ${product.customEQ ? 'Có' : 'Không'} </span> </span> </div>
                        <div class="spec-item"> <span class="spec-label">Âm thanh vòm:</span> <span class="spec-value">
                                <span class="spec-boolean ${product.surroundSound ? 'spec-yes' : 'spec-no'}">
                                        ${product.surroundSound ? 'Có' : 'Không'} </span> </span> </div>
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
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
</body>
</html>