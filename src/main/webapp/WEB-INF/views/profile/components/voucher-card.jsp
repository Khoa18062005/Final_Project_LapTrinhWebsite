<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- ✅ Bean 'now', 'customerId', 'userUsageMap' đã được khai báo ở vouchers.jsp rồi --%>

<%-- ✅ LẤY USER USAGE TỪ MAP (KHÔNG GỌI DAO NỮA) --%>
<c:set var="userUsageCount" value="${userUsageMap[voucher.voucherId]}" />
<c:set var="userUsagePercent" value="${(userUsageCount / voucher.usageLimitPerUser) * 100}" />

<%-- ✅ Kiểm tra voucher expired hay user đã hết lượt --%>
<c:set var="isExpired" value="${voucher.expiryDate.before(now) or userUsageCount >= voucher.usageLimitPerUser}" />

<%-- ✅ Kiểm tra voucher active (bao gồm cả check user usage) --%>
<c:set var="isActive" value="${voucher.active and voucher.startDate.before(now) and voucher.expiryDate.after(now) and voucher.usageCount < voucher.usageLimit and userUsageCount < voucher.usageLimitPerUser}" />

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
        <c:when test="${voucher.expiryDate.before(now)}">
          <p class="text-danger mb-1">
            <i class="bi bi-x-circle me-1"></i>
            Đã hết hạn: <fmt:formatDate value="${voucher.expiryDate}" pattern="dd/MM/yyyy"/>
          </p>
        </c:when>
        <c:when test="${userUsageCount >= voucher.usageLimitPerUser}">
          <p class="text-danger mb-1">
            <i class="bi bi-x-circle me-1"></i>
            Bạn đã hết lượt sử dụng voucher này
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

      <%-- ✅ USER USAGE PROGRESS --%>
      <c:if test="${not isExpired}">
        <div class="progress mt-2" style="height: 6px;">
          <div class="progress-bar ${userUsagePercent >= 100 ? 'bg-danger' : userUsagePercent >= 50 ? 'bg-warning' : 'bg-success'}"
               role="progressbar"
               style="width: ${userUsagePercent}%">
          </div>
        </div>
        <small class="text-muted">
          Bạn đã dùng: <strong>${userUsageCount}/${voucher.usageLimitPerUser}</strong> lượt
        </small>
      </c:if>
    </div>

    <!-- RIGHT: Action -->
    <div class="col-md-3 voucher-right text-end">
      <c:choose>
        <c:when test="${isExpired}">
          <button class="btn btn-secondary btn-sm mb-2 w-100" disabled>
            <i class="bi bi-x-circle me-1"></i>
            <c:choose>
              <c:when test="${voucher.expiryDate.before(now)}">Hết Hạn</c:when>
              <c:otherwise>Hết Lượt</c:otherwise>
            </c:choose>
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

      <%-- ✅ Hiển thị server usage --%>
      <p class="text-muted small mt-2 mb-0">
        <i class="bi bi-people-fill"></i> Còn: <strong>${voucher.usageLimit - voucher.usageCount}</strong> lượt (toàn server)
      </p>
    </div>
  </div>
</div>