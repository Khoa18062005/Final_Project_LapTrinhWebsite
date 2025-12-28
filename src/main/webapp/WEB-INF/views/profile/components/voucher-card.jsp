<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- ❌ XÓA DÒNG NÀY --%>
<%-- <jsp:useBean id="now" class="java.util.Date" /> --%>

<%-- ✅ Bean 'now' đã được khai báo ở vouchers.jsp rồi --%>
<c:set var="isExpired" value="${voucher.expiryDate.before(now)}" />
<c:set var="isActive" value="${voucher.active and voucher.startDate.before(now) and voucher.expiryDate.after(now) and voucher.usageCount < voucher.usageLimit}" />

<div class="voucher-card mb-3 ${isExpired ? 'voucher-expired' : ''}">
  <div class="row align-items-center">
    <!-- LEFT: Icon + Discount -->
    <div class="col-md-3 voucher-left">
      <div class="voucher-icon">
        <c:choose>
          <c:when test="${voucher.discountPercent > 0}">
            <i class="bi bi-percent fs-1 ${isActive ? 'text-success' : 'text-secondary'}"></i>
          </c:when>
          <c:otherwise>
            <i class="bi bi-gift-fill fs-1 ${isActive ? 'text-primary' : 'text-secondary'}"></i>
          </c:otherwise>
        </c:choose>
      </div>
      <div class="voucher-discount">
        <c:choose>
          <c:when test="${voucher.discountPercent > 0}">
            <h3 class="mb-0 ${isExpired ? 'text-secondary' : ''}">${voucher.discountPercent}%</h3>
            <small ${isExpired ? 'class="text-secondary"' : ''}>Giảm giá</small>
          </c:when>
          <c:otherwise>
            <h3 class="mb-0 ${isExpired ? 'text-secondary' : ''}">
              <fmt:formatNumber value="${voucher.discountAmount / 1000}" pattern="#"/>K
            </h3>
            <small ${isExpired ? 'class="text-secondary"' : ''}>Giảm tối đa</small>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <!-- MIDDLE: Info -->
    <div class="col-md-6 voucher-middle ${isExpired ? 'opacity-75' : ''}">
      <h5 class="mb-2 ${isExpired ? 'text-secondary' : ''}">${voucher.name}</h5>

      <c:choose>
        <c:when test="${isExpired}">
          <p class="text-danger mb-1">
            <i class="bi bi-x-circle me-1"></i>
            Đã hết hạn: <fmt:formatDate value="${voucher.expiryDate}" pattern="dd/MM/yyyy"/>
          </p>
        </c:when>
        <c:otherwise>
          <p class="text-muted mb-1">
            <i class="bi bi-calendar-check me-1"></i>
            HSD: <fmt:formatDate value="${voucher.expiryDate}" pattern="dd/MM/yyyy"/>
          </p>
        </c:otherwise>
      </c:choose>

      <p class="text-muted mb-0">
        <i class="bi bi-tag me-1"></i>
        <c:choose>
          <c:when test="${voucher.minOrderValue > 0}">
            Đơn từ <fmt:formatNumber value="${voucher.minOrderValue}" type="number" groupingUsed="true"/>đ
          </c:when>
          <c:otherwise>
            Áp dụng cho tất cả sản phẩm
          </c:otherwise>
        </c:choose>
      </p>

      <!-- Usage Progress -->
      <c:if test="${not isExpired}">
        <div class="progress mt-2" style="height: 6px;">
          <div class="progress-bar bg-success" role="progressbar"
               style="width: ${(voucher.usageCount * 100.0) / voucher.usageLimit}%">
          </div>
        </div>
        <small class="text-muted">
          Còn ${voucher.usageLimitPerUser - voucher.usageCount}/${voucher.usageLimitPerUser} lượt
        </small>
      </c:if>
    </div>

    <!-- RIGHT: Action -->
    <div class="col-md-3 voucher-right text-end">
      <c:choose>
        <c:when test="${isExpired}">
          <button class="btn btn-secondary btn-sm mb-2 w-100" disabled>
            <i class="bi bi-x-circle me-1"></i> Hết Hạn
          </button>
        </c:when>
        <c:when test="${not isActive}">
          <button class="btn btn-warning btn-sm mb-2 w-100" disabled>
            <i class="bi bi-exclamation-triangle me-1"></i> Không Khả Dụng
          </button>
        </c:when>
        <c:otherwise>
          <button class="btn btn-primary btn-sm mb-2 w-100">
            <i class="bi bi-bag-check me-1"></i> Dùng Ngay
          </button>
        </c:otherwise>
      </c:choose>

      <p class="text-muted mb-1 small">Mã: <strong>${voucher.code}</strong></p>

      <button class="btn btn-outline-info btn-sm w-100"
              onclick="showVoucherDetail(${voucher.voucherId})">
        <i class="bi bi-info-circle me-1"></i> Chi Tiết
      </button>
    </div>
  </div>
</div>