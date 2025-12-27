<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="section-header">
  <h2>Quản lý sản phẩm</h2>
  <button class="btn btn-primary" onclick="openAddProductModal()">
    <i class="fas fa-plus"></i> Thêm sản phẩm
  </button>
</div>

<div class="filter-section">
  <label><i class="fas fa-filter"></i> <strong>Lọc theo danh mục:</strong></label>
  <form action="${pageContext.request.contextPath}/admin" method="GET" id="filterForm" class="filter-form">
    <select name="category" onchange="document.getElementById('filterForm').submit()">
      <option value="">-- Tất cả sản phẩm --</option>
      <option value="1" ${currentCategory == 1 ? 'selected' : ''}>📱 Điện thoại</option>
      <option value="2" ${currentCategory == 2 ? 'selected' : ''}>💻 Laptop</option>
      <option value="3" ${currentCategory == 3 ? 'selected' : ''}>🖊 Tablet</option>
      <option value="4" ${currentCategory == 4 ? 'selected' : ''}>🎧 Tai nghe / Phụ kiện</option>
    </select>
  </form>
</div>

<c:if test="${not empty param.message}">
  <div class="alert alert-success">
    <i class="fas fa-check-circle"></i>
    <c:choose>
      <c:when test="${param.message == 'added_success'}">Thêm sản phẩm thành công!</c:when>
      <c:when test="${param.message == 'delete_success'}">Xóa sản phẩm thành công!</c:when>
      <c:when test="${param.message == 'init_success'}">Khởi tạo dữ liệu mẫu thành công!</c:when>
      <c:otherwise>Thao tác thành công!</c:otherwise>
    </c:choose>
  </div>
</c:if>
<c:if test="${not empty param.error}">
  <div class="alert alert-error">
    <i class="fas fa-exclamation-triangle"></i> Đã có lỗi xảy ra! Vui lòng thử lại.
  </div>
</c:if>

<div class="table-container">
  <table class="data-table">
    <thead>
    <tr>
      <th>Mã (ID)</th>
      <th>Tên sản phẩm</th>
      <th>Mã người bán</th>
      <th>Danh mục</th>
      <th>Giá gốc</th>
      <th>Trạng thái</th>
      <th>Thao tác</th>
    </tr>
    </thead>
    <tbody id="productsTable">
    <c:forEach var="p" items="${productList}">
      <tr>
        <td>#${p.productId}</td>
        <td>
          <strong>${p.name}</strong>
          <br><small class="text-muted">${p.slug}</small>
        </td>
        <td>
                    <span class="vendor-code">
                        Vendor #${p.vendorId}
                    </span>
        </td>
        <td>
          <c:choose>
            <c:when test="${p.categoryId == 1}"><span class="badge-type phone"><i class="fas fa-mobile-alt"></i> Điện thoại</span></c:when>
            <c:when test="${p.categoryId == 3}"><span class="badge-type laptop"><i class="fas fa-laptop"></i> Laptop</span></c:when>
            <c:when test="${p.categoryId == 4}"><span class="badge-type tablet"><i class="fas fa-tablet-alt"></i> Tablet</span></c:when>
            <c:when test="${p.categoryId == 5}"><span class="badge-type accessory"><i class="fas fa-headphones"></i> Tai nghe</span></c:when>
            <c:otherwise><span class="badge-type">Khác</span></c:otherwise>
          </c:choose>
        </td>
        <td>
          <fmt:formatNumber value="${p.basePrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
        </td>
        <td>
                    <span class="status-badge ${p.status == 'Active' ? 'delivered' : 'rejected'}">
                        ${p.status != null ? p.status : 'Active'}
                    </span>
        </td>
        <td>
          <div class="action-buttons">
            <button type="button" class="btn-icon view" onclick="showProductDetails('detail-${p.productId}')" title="Xem chi tiết">
              <i class="fas fa-eye"></i>
            </button>

            <button class="btn-icon edit" title="Sửa"><i class="fas fa-edit"></i></button>

            <form action="${pageContext.request.contextPath}/admin" method="POST" class="delete-form" onsubmit="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này?');">
              <input type="hidden" name="action" value="delete_product">
              <input type="hidden" name="id" value="${p.productId}">
              <button type="submit" class="btn-icon delete" title="Xóa"><i class="fas fa-trash"></i></button>
            </form>
          </div>

          <div id="detail-${p.productId}" class="product-detail-hidden">
            <div class="product-detail-header">
              <h3>${p.name}</h3>
              <p>ID: #${p.productId} | Vendor ID: ${p.vendorId}</p>
            </div>
            <h4 class="product-detail-title">📦 Thông tin quản lý</h4>
            <p>${p.description}</p>
          </div>
        </td>
      </tr>
    </c:forEach>

    <c:if test="${empty productList}">
      <tr>
        <td colspan="7" class="empty-state">
          <p>Không tìm thấy sản phẩm nào.</p>
          <form action="${pageContext.request.contextPath}/admin" method="POST">
            <input type="hidden" name="action" value="init_data">
            <button type="submit" class="btn btn-secondary btn-sm">Tạo dữ liệu mẫu</button>
          </form>
        </td>
      </tr>
    </c:if>
    </tbody>
  </table>
</div>

<div id="productModal" class="modal">
  <div class="modal-content">
    <div class="modal-header">
      <h2>Thêm sản phẩm mới</h2>
      <span class="close" onclick="closeModal('productModal')">&times;</span>
    </div>
    <div class="modal-body">
      <form id="productForm" action="${pageContext.request.contextPath}/admin" method="POST">
        <input type="hidden" name="action" value="add_product">
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" onclick="closeModal('productModal')">Hủy</button>
          <button type="submit" class="btn btn-primary">Lưu</button>
        </div>
      </form>
    </div>
  </div>
</div>

<div id="viewDetailModal" class="modal">
  <div class="modal-content">
    <div class="modal-header modal-header-bordered">
      <h2>Chi tiết sản phẩm</h2>
      <span class="close" onclick="closeModal('viewDetailModal')">&times;</span>
    </div>
    <div class="modal-body modal-body-scroll" id="viewDetailContent">
    </div>
    <div class="modal-footer modal-footer-bordered">
      <button type="button" class="btn btn-secondary" onclick="closeModal('viewDetailModal')">Đóng</button>
    </div>
  </div>
</div>