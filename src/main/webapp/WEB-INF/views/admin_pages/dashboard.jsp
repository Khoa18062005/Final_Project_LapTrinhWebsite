<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="stats-grid">
  <div class="stat-card">
    <div class="stat-card-header">
      <div class="stat-icon blue"><i class="fas fa-users"></i></div>
    </div>
    <div class="stat-details">
      <h3>Tổng người dùng</h3>
      <p class="stat-number">${totalUsers}</p>
    </div>
  </div>
  <div class="stat-card">
    <div class="stat-card-header">
      <div class="stat-icon green"><i class="fas fa-shopping-bag"></i></div>
    </div>
    <div class="stat-details">
      <h3>Tổng sản phẩm</h3>
      <p class="stat-number">${totalProducts}</p>
    </div>
  </div>
</div>