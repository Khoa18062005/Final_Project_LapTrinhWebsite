<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>${not empty pageTitle ? pageTitle : 'Tìm kiếm sản phẩm'} | VietTech</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">

  <!-- CSS riêng -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/chatbot.css">
</head>
<body>
<%@ include file="/header.jsp" %>

<!-- Biến JS chung -->
<script>
  const contextPath = "${pageContext.request.contextPath}";
  const isLoggedIn = ${not empty sessionScope.user};
</script>

<!-- SEARCH RESULTS SECTION -->
<section class="products py-5">
  <div class="container">
    <!-- Tiêu đề kết quả tìm kiếm -->
    <h2 class="category-title text-center mb-4">
      <c:choose>
        <c:when test="${not empty keyword}">
          Kết quả tìm kiếm cho: <span style="color: #e74c3c;">"${keyword}"</span>
          <c:if test="${not empty products}">
            <small class="text-muted d-block mt-2">${products.size()} sản phẩm được tìm thấy</small>
          </c:if>
        </c:when>
        <c:otherwise>
          Tất cả sản phẩm
        </c:otherwise>
      </c:choose>
    </h2>

    <!-- Trường hợp không tìm thấy -->
    <c:if test="${empty products}">
      <div class="text-center py-5">
        <img src="${pageContext.request.contextPath}/assets/images/search-empty.png"
             alt="Không tìm thấy"
             class="mb-4"
             style="max-width: 300px;"
             onerror="this.style.display='none'">
        <h4 class="text-muted">Không tìm thấy sản phẩm nào phù hợp</h4>
        <p class="text-muted">
          Gợi ý:<br>
          • Kiểm tra lại chính tả<br>
          • Thử từ khóa ngắn hơn hoặc từ đồng nghĩa<br>
          • Tìm theo hãng: iPhone, Samsung, MacBook, Sony...
        </p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary mt-3">Quay về trang chủ</a>
      </div>
    </c:if>

    <!-- Có kết quả -->
    <c:if test="${not empty products}">
      <div class="row justify-content-start">
        <c:forEach var="product" items="${products}">
          <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-4">
            <a href="${pageContext.request.contextPath}/product?id=${product.id}" class="product-link">
              <div class="product position-relative">

                <!-- Badges -->
                <div class="product-badges">
                  <c:if test="${product.discountPercent > 0}">
                    <span class="badge discount">Giảm ${product.discountPercent}%</span>
                  </c:if>
                  <span class="badge installment">Trả góp 0%</span>
                </div>

                <!-- Ảnh sản phẩm -->
                <c:choose>
                  <c:when test="${not empty product.primaryImage}">
                    <div class="product-image">
                      <img src="${product.primaryImage}"
                           alt="${product.name}"
                           class="w-100 h-100"
                           style="object-fit: contain;"
                           onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.png';">
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="product-image-placeholder text-center py-3 d-flex align-items-center justify-content-center">
                      <p class="text-muted mb-0">Chưa có ảnh sản phẩm</p>
                    </div>
                  </c:otherwise>
                </c:choose>

                <!-- Tên sản phẩm -->
                <h3 class="product-name">${product.name} | Chính hãng</h3>

                <!-- Giá -->
                <div class="product-price">
                  <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/>đ
                  <c:if test="${product.oldPrice > product.price}">
                                            <span class="old-price">
                                                <fmt:formatNumber value="${product.oldPrice}" type="number" groupingUsed="true"/>đ
                                            </span>
                  </c:if>
                </div>

                <!-- Member discount -->
                <c:if test="${product.memberDiscount > 0}">
                  <div class="member-discount">
                    Smember giảm đến <fmt:formatNumber value="${product.memberDiscount}" type="number" groupingUsed="true"/>đ
                  </div>
                </c:if>

                <!-- Thông tin trả góp -->
                <div class="installment-info">
                  Trả góp 0% - 0đ phụ thu - 0đ trả trước - Kỳ hạn đến 6 tháng
                </div>

                <!-- Footer: rating + yêu thích -->
                <div class="product-footer">
                  <div class="rating">
                    <span class="stars">★ ${product.rating}</span>
                    <span class="like">Yêu thích</span>
                  </div>
                </div>
              </div>
            </a>
          </div>
        </c:forEach>
      </div>
    </c:if>
  </div>
</section>

<jsp:include page="/footer.jsp" />

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/chatbot.js"></script>
</body>
</html>