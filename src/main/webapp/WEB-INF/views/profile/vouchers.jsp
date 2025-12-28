<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- ✅ KHAI BÁO BEAN Ở ĐÂY (chỉ 1 lần) --%>
<jsp:useBean id="now" class="java.util.Date" />

<c:set var="pageTitle" value="Kho Voucher" />

<script>
  console.log('🎫 VOUCHER DEBUG:');
  console.log('All vouchers size:', ${allVouchers.size()});
  console.log('Active vouchers size:', ${activeVouchers.size()});
  console.log('Expired vouchers size:', ${expiredVouchers.size()});
  console.log('All vouchers:', ${allVouchers});
</script>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<!-- MAIN CONTENT -->
<div class="col-lg-10 col-md-9">
  <div class="profile-content">
    <div class="profile-header">
      <h4><i class="bi bi-ticket-perforated me-2"></i>Kho Voucher</h4>
      <p class="text-muted">Quản lý các voucher và mã giảm giá của bạn</p>
    </div>

    <!-- Tabs Filter -->
    <ul class="nav nav-tabs mb-4" id="voucherTabs" role="tablist">
      <li class="nav-item" role="presentation">
        <button class="nav-link active" id="all-tab" data-bs-toggle="tab"
                data-bs-target="#all" type="button" role="tab">
          <i class="bi bi-grid me-1"></i> Tất Cả
          <span class="badge bg-secondary ms-1">${allVouchers.size()}</span>
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button class="nav-link" id="active-tab" data-bs-toggle="tab"
                data-bs-target="#active" type="button" role="tab">
          <i class="bi bi-check-circle me-1"></i> Có Thể Dùng
          <span class="badge bg-success ms-1">${activeVouchers.size()}</span>
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button class="nav-link" id="expired-tab" data-bs-toggle="tab"
                data-bs-target="#expired" type="button" role="tab">
          <i class="bi bi-x-circle me-1"></i> Hết Hạn
          <span class="badge bg-danger ms-1">${expiredVouchers.size()}</span>
        </button>
      </li>
    </ul>

    <!-- Tab Content -->
    <div class="tab-content" id="voucherTabContent">

      <!-- TAB: TẤT CẢ -->
      <div class="tab-pane fade show active" id="all" role="tabpanel">
        <c:choose>
          <c:when test="${empty allVouchers}">
            <div class="empty-state text-center py-5">
              <i class="bi bi-ticket-perforated fs-1 text-muted mb-3 d-block"></i>
              <h5>Chưa có voucher nào</h5>
              <p class="text-muted">Khám phá các ưu đãi hấp dẫn từ VietTech</p>
              <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                <i class="bi bi-arrow-left me-2"></i> Về Trang Chủ
              </a>
            </div>
          </c:when>
          <c:otherwise>
            <c:forEach var="voucher" items="${allVouchers}">
              <%@ include file="components/voucher-card.jsp" %>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>

      <!-- TAB: CÓ THỂ DÙNG -->
      <div class="tab-pane fade" id="active" role="tabpanel">
        <c:choose>
          <c:when test="${empty activeVouchers}">
            <div class="empty-state text-center py-5">
              <i class="bi bi-ticket-perforated fs-1 text-muted mb-3 d-block"></i>
              <h5>Không có voucher khả dụng</h5>
              <p class="text-muted">Hãy quay lại sau để nhận ưu đãi mới</p>
            </div>
          </c:when>
          <c:otherwise>
            <c:forEach var="voucher" items="${activeVouchers}">
              <%@ include file="components/voucher-card.jsp" %>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>

      <!-- TAB: HẾT HẠN -->
      <div class="tab-pane fade" id="expired" role="tabpanel">
        <c:choose>
          <c:when test="${empty expiredVouchers}">
            <div class="empty-state text-center py-5">
              <i class="bi bi-ticket-perforated fs-1 text-muted mb-3 d-block"></i>
              <h5>Không có voucher hết hạn</h5>
            </div>
          </c:when>
          <c:otherwise>
            <c:forEach var="voucher" items="${expiredVouchers}">
              <%@ include file="components/voucher-card.jsp" %>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>
</div>

<!-- MODAL CHI TIẾT VOUCHER -->
<div class="modal fade" id="voucherDetailModal" tabindex="-1">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">
          <i class="bi bi-ticket-detailed me-2"></i>Chi Tiết Voucher
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body" id="voucherDetailContent">
        <!-- Content will be loaded by JavaScript -->
      </div>
    </div>
  </div>
</div>

<!-- DATA SCRIPT -->
<script>
  window.voucherData = [
    <c:forEach var="v" items="${allVouchers}" varStatus="status">
    {
      id: ${v.voucherId},
      code: '${v.code}',
      name: '${v.name}',
      description: '${v.description}',
      type: '${v.type}',
      discountPercent: ${v.discountPercent},
      discountAmount: ${v.discountAmount},
      maxDiscount: ${v.maxDiscount},
      minOrderValue: ${v.minOrderValue},
      startDate: '<fmt:formatDate value="${v.startDate}" pattern="dd/MM/yyyy HH:mm"/>',
      expiryDate: '<fmt:formatDate value="${v.expiryDate}" pattern="dd/MM/yyyy HH:mm"/>',
      usageCount: ${v.usageCount},
      usageLimit: ${v.usageLimit},
      isActive: ${v.active}
    }<c:if test="${!status.last}">,</c:if>
    </c:forEach>
  ];
</script>

<c:set var="pageScript" value="vouchers.js" />
<%@ include file="components/footer.jsp" %>