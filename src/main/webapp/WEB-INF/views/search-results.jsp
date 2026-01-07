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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/search-results.css">
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
  <div class="container-fluid px-4 px-lg-5">
    <!-- Tiêu đề kết quả tìm kiếm -->
    <h2 class="category-title text-center mb-4">
      <c:choose>
        <c:when test="${not empty keyword}">
          Kết quả tìm kiếm cho: <span style="color: #e74c3c;">"${keyword}"</span>
        </c:when>
        <c:otherwise>
          Tất cả sản phẩm
        </c:otherwise>
      </c:choose>
    </h2>

    <!-- Không có kết quả -->
    <c:if test="${empty products}">
      <div class="text-center py-5">
        <img src="${pageContext.request.contextPath}/assets/images/search-empty.png"
             alt="Không tìm thấy"
             class="mb-4"
             style="max-width: 300px;"
             onerror="this.style.display='none'">
        <h4 class="text-muted-filter">Không tìm thấy sản phẩm nào phù hợp</h4>
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
      <!-- FILTER & SORT BAR (kiểu Shopee) -->
      <div class="mb-4">
        <div class="row g-3 align-items-center">
          <!-- SẮP XẾP -->
          <div class="col-12 col-md-auto">
            <div class="d-flex flex-wrap gap-2 align-items-center">
              <span class="fw-bold text-secondary me-3">Sắp xếp theo</span>

              <!-- Liên quan (mặc định) -->
              <a href="?q=${keyword}&min_price=${currentMinPrice}&max_price=${currentMaxPrice}"
                 class="btn btn-sort ${currentSort == null || currentSort == '' ? 'btn-sort-active' : 'btn-sort-inactive'}">
                Liên quan
              </a>

              <!-- Giá cao → thấp -->
              <a href="?q=${keyword}&sort=price_desc&min_price=${currentMinPrice}&max_price=${currentMaxPrice}"
                 class="btn btn-sort ${currentSort == 'price_desc' ? 'btn-sort-active' : 'btn-sort-inactive'}">
                <i class="bi bi-arrow-down"></i> Giá cao
              </a>

              <!-- Giá thấp → cao -->
              <a href="?q=${keyword}&sort=price_asc&min_price=${currentMinPrice}&max_price=${currentMaxPrice}"
                 class="btn btn-sort ${currentSort == 'price_asc' ? 'btn-sort-active' : 'btn-sort-inactive'}">
                <i class="bi bi-arrow-up"></i> Giá thấp
              </a>

              <!-- === THÊM MỚI: Đánh giá cao nhất === -->
              <a href="?q=${keyword}&sort=rating_desc&min_price=${currentMinPrice}&max_price=${currentMaxPrice}"
                 class="btn btn-sort ${currentSort == 'rating_desc' ? 'btn-sort-active' : 'btn-sort-inactive'}">
                <i class="bi bi-star-fill text-warning"></i> Đánh giá cao
              </a>
            </div>
          </div>

          <!-- LỌC GIÁ -->
          <div class="col-12 col-md" id="filter-price">
            <form method="get" id="priceFilterForm" class="d-flex flex-wrap gap-2 align-items-end justify-content-md-end">
              <input type="hidden" name="q" value="${keyword}">
              <input type="hidden" name="sort" value="${currentSort}">

              <div style="min-width: 130px;" class="form-control-price">
                <input type="text"
                       id="minPriceInput"
                       class="form-control form-control-sm price-input"
                       placeholder="Giá từ"
                       value="<c:if test="${currentMinPrice != null}"><fmt:formatNumber value="${currentMinPrice}" pattern="#,###"/></c:if>">
                <input type="hidden" name="min_price" id="minPriceHidden">
              </div>

              <div class="text-secondary align-self-center">-</div>

              <div style="min-width: 130px;" class="form-control-price">
                <input type="text"
                       id="maxPriceInput"
                       class="form-control form-control-sm price-input"
                       placeholder="Giá đến"
                       value="<c:if test="${currentMaxPrice != null}"><fmt:formatNumber value="${currentMaxPrice}" pattern="#,###"/></c:if>">
                <input type="hidden" name="max_price" id="maxPriceHidden">
              </div>

              <button type="submit" class="btn btn-primary btn-sm">Lọc</button>

              <c:if test="${currentMinPrice != null || currentMaxPrice != null || currentSort != null}">
                <a href="?q=${keyword}" class="btn btn-outline-secondary btn-sm">Xóa lọc</a>
              </c:if>
            </form>
          </div>
        </div>
      </div>

      <!-- Số lượng sản phẩm -->
      <div class="text-muted-filter mb-4">
        Đã tìm thấy <strong>${products.size()}</strong> sản phẩm
      </div>

      <!-- DANH SÁCH SẢN PHẨM -->
      <div class="row justify-content-start">
        <c:forEach var="product" items="${products}">
          <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6 mb-4">
            <a href="${pageContext.request.contextPath}/product?id=${product.id}" class="product-link text-decoration-none">
              <div class="product position-relative">

                <!-- Badges -->
                <div class="product-badges">
                  <c:if test="${product.discountPercent > 0}">
                    <span class="badge discount">Giảm ${product.discountPercent}%</span>
                  </c:if>
                  <span class="badge installment" style="top: 10px; right: auto; left: 10px; background-color: #28a745; color: white; border-radius: 20px; font-size: 0.85rem; padding: 4px 12px;">Trả góp 0%</span>
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
                    <div class="product-image-placeholder text-center py-3 d-flex align-items-center justify-content-center bg-light">
                      <p class="text-muted mb-0">Chưa có ảnh sản phẩm</p>
                    </div>
                  </c:otherwise>
                </c:choose>

                <!-- Tên sản phẩm -->
                <h3 class="product-name mt-2">${product.name} | Chính hãng</h3>

                <!-- Giá -->
                <div class="product-price fw-bold text-danger fs-5">
                  <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/>đ
                  <c:if test="${product.oldPrice > product.price}">
                    <span class="old-price text-muted text-decoration-line-through fs-6 ms-2">
                      <fmt:formatNumber value="${product.oldPrice}" type="number" groupingUsed="true"/>đ
                    </span>
                  </c:if>
                </div>

                <!-- Member discount -->
                <c:if test="${product.memberDiscount > 0}">
                  <div class="member-discount text-success small">
                    Smember giảm đến <fmt:formatNumber value="${product.memberDiscount}" type="number" groupingUsed="true"/>đ
                  </div>
                </c:if>

                <!-- Thông tin trả góp -->
                <div class="installment-info small text-muted">
                  Trả góp 0% - 0đ phụ thu - Kỳ hạn đến 6 tháng
                </div>

                <!-- Footer: rating + yêu thích -->
                <div class="product-footer d-flex justify-content-between align-items-center mt-2">
                  <div class="rating">
                    <span class="stars text-warning">★ ${product.rating}</span>
                  </div>
                  <span class="like text-muted small">Yêu thích</span>
                </div>
              </div>
            </a>
          </div>
        </c:forEach>
      </div>
    </c:if>
  </div>
</section>

<%@ include file="/footer.jsp" %>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/chatbot.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/search-results.js"></script>

<!-- Định dạng giá tự động (file riêng) -->
<script src="${pageContext.request.contextPath}/assets/js/price-format.js"></script>
</body>
</html>