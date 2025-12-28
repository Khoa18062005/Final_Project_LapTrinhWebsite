<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Địa Chỉ Của Tôi" />
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<!-- MAIN CONTENT -->
<div class="col-lg-10 col-md-9">
  <div class="profile-content">
    <div class="profile-header">
      <h4><i class="bi bi-geo-alt me-2"></i>Địa Chỉ Của Tôi</h4>
      <p class="text-muted mb-0">Quản lý địa chỉ giao hàng của bạn</p>
    </div>

    <!-- HIỂN THỊ DANH SÁCH ĐỊA CHỈ -->
    <div class="row mt-4">
      <c:choose>
        <c:when test="${empty addresses}">
          <!-- EMPTY STATE -->
          <div class="col-12">
            <div class="empty-state text-center py-5">
              <i class="bi bi-geo-alt fs-1 text-muted mb-3 d-block"></i>
              <h5>Chưa có địa chỉ nào</h5>
              <p class="text-muted">Bạn chưa thêm địa chỉ giao hàng nào</p>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <!-- DANH SÁCH ĐỊA CHỈ -->
          <c:forEach var="address" items="${addresses}">
            <div class="col-md-6 mb-4">
              <div class="address-card border rounded p-4 position-relative">
                <!-- Badge mặc định -->
                <c:if test="${address.isDefault()}">
                  <span class="badge bg-danger position-absolute top-0 end-0 m-3">Mặc Định</span>
                </c:if>

                <!-- Thông tin người nhận -->
                <div class="d-flex justify-content-between align-items-start mb-3">
                  <div>
                    <h5 class="mb-1">${address.receiverName}</h5>
                    <p class="text-muted mb-0">
                      <i class="bi bi-telephone me-1"></i> ${address.phone}
                    </p>
                  </div>
                </div>

                <!-- Địa chỉ -->
                <p class="mb-3">
                  <i class="bi bi-house-door me-2"></i>
                    ${address.street}, ${address.ward}, ${address.district}, ${address.city}
                </p>
              </div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>

<%@ include file="components/footer.jsp" %>