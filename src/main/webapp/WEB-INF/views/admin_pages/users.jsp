<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!-- Section Header -->
<div class="section-header">
    <h2>Quản lý người dùng</h2>
</div>

<!-- Alert Messages -->
<c:if test="${not empty param.userMessage}">
    <div class="alert alert-success">
        <i class="fas fa-check-circle"></i>
        <c:choose>
            <c:when test="${param.userMessage == 'user_banned'}">Đã khóa tài khoản thành công!</c:when>
            <c:when test="${param.userMessage == 'user_unbanned'}">Đã mở khóa tài khoản thành công!</c:when>
            <c:when test="${param.userMessage == 'user_deleted'}">Đã xóa người dùng thành công!</c:when>
            <c:otherwise>${param.userMessage}</c:otherwise>
        </c:choose>
    </div>
</c:if>
<c:if test="${not empty param.userError}">
    <div class="alert alert-error">
        <i class="fas fa-exclamation-triangle"></i> Lỗi: ${param.userError}
    </div>
</c:if>

<!-- User Stats Cards -->
<div class="stats-grid" style="grid-template-columns: repeat(4, 1fr); margin-bottom: 24px;">
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon" style="background: linear-gradient(135deg, #EF4444, #DC2626);">
                <i class="fas fa-user-shield"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Admin</h3>
            <p class="stat-number">${totalAdmins != null ? totalAdmins : 0}</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon blue">
                <i class="fas fa-store"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Vendor</h3>
            <p class="stat-number">${totalVendors != null ? totalVendors : 0}</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon purple">
                <i class="fas fa-truck"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Shipper</h3>
            <p class="stat-number">${totalShippers != null ? totalShippers : 0}</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-card-header">
            <div class="stat-icon green">
                <i class="fas fa-users"></i>
            </div>
        </div>
        <div class="stat-details">
            <h3>Customer</h3>
            <p class="stat-number">${totalCustomers != null ? totalCustomers : 0}</p>
        </div>
    </div>
</div>

<!-- Filter Form -->
<div class="filter-section" style="margin-bottom: 20px;">
    <form action="${pageContext.request.contextPath}/admin" method="GET" style="display: flex; gap: 12px; flex-wrap: wrap; align-items: center;">
        <input type="hidden" name="section" value="users">

        <label style="display: flex; align-items: center; gap: 8px;">
            <i class="fas fa-filter"></i> Vai trò:
            <select name="roleFilter" onchange="this.form.submit()" style="padding: 8px; border-radius: 6px; border: 1px solid #ddd;">
                <option value="">Tất cả</option>
                <option value="1" ${param.roleFilter == '1' ? 'selected' : ''}>Admin</option>
                <option value="2" ${param.roleFilter == '2' ? 'selected' : ''}>Vendor</option>
                <option value="3" ${param.roleFilter == '3' ? 'selected' : ''}>Shipper</option>
                <option value="4" ${param.roleFilter == '4' ? 'selected' : ''}>Customer</option>
            </select>
        </label>

        <label style="display: flex; align-items: center; gap: 8px;">
            Trạng thái:
            <select name="statusFilter" onchange="this.form.submit()" style="padding: 8px; border-radius: 6px; border: 1px solid #ddd;">
                <option value="">Tất cả</option>
                <option value="active" ${param.statusFilter == 'active' ? 'selected' : ''}>Hoạt động</option>
                <option value="banned" ${param.statusFilter == 'banned' ? 'selected' : ''}>Bị khóa</option>
            </select>
        </label>
    </form>
</div>

<!-- Users Table -->
<div class="table-container">
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Họ tên</th>
                <th>Email</th>
                <th>SĐT</th>
                <th>Vai trò</th>
                <th>Trạng thái</th>
                <th>Ngày tạo</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${userList}">
                <tr>
                    <td><strong>#${user.userId}</strong></td>
                    <td>
                        <strong>${user.firstName} ${user.lastName}</strong>
                        <br><small style="color: #888;">@${user.username}</small>
                    </td>
                    <td>${user.email}</td>
                    <td>${user.phone}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.roleID == 1}">
                                <span class="badge badge-danger"><i class="fas fa-user-shield"></i> Admin</span>
                            </c:when>
                            <c:when test="${user.roleID == 2}">
                                <span class="badge badge-info"><i class="fas fa-store"></i> Vendor</span>
                            </c:when>
                            <c:when test="${user.roleID == 3}">
                                <span class="badge" style="background: #E9D5FF; color: #7C3AED;"><i class="fas fa-truck"></i> Shipper</span>
                            </c:when>
                            <c:when test="${user.roleID == 4}">
                                <span class="badge badge-success"><i class="fas fa-user"></i> Customer</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge">Unknown</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.active}">
                                <span class="status-badge active">
                                    <i class="fas fa-check-circle"></i> Hoạt động
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge inactive">
                                    <i class="fas fa-ban"></i> Bị khóa
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${user.createdAt != null}">
                            <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy"/>
                        </c:if>
                    </td>
                    <td>
                        <div class="action-buttons">
                            <!-- Ban/Unban -->
                            <c:if test="${user.roleID != 1}">
                                <c:choose>
                                    <c:when test="${user.active}">
                                        <form action="${pageContext.request.contextPath}/admin" method="POST" style="display: inline;"
                                              onsubmit="return confirm('Bạn có chắc muốn KHÓA tài khoản này?');">
                                            <input type="hidden" name="action" value="ban_user">
                                            <input type="hidden" name="userId" value="${user.userId}">
                                            <input type="hidden" name="userRole" value="${user.roleID}">
                                            <button type="submit" class="btn-icon" style="background: #FEF3C7; color: #D97706;" title="Khóa tài khoản">
                                                <i class="fas fa-user-lock"></i>
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="${pageContext.request.contextPath}/admin" method="POST" style="display: inline;"
                                              onsubmit="return confirm('Bạn có chắc muốn MỞ KHÓA tài khoản này?');">
                                            <input type="hidden" name="action" value="unban_user">
                                            <input type="hidden" name="userId" value="${user.userId}">
                                            <input type="hidden" name="userRole" value="${user.roleID}">
                                            <button type="submit" class="btn-icon" style="background: #D1FAE5; color: #059669;" title="Mở khóa">
                                                <i class="fas fa-user-check"></i>
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>

                                <!-- Delete -->
                                <form action="${pageContext.request.contextPath}/admin" method="POST" style="display: inline;"
                                      onsubmit="return confirm('Bạn có chắc muốn XÓA VĨNH VIỄN tài khoản này?');">
                                    <input type="hidden" name="action" value="delete_user">
                                    <input type="hidden" name="userId" value="${user.userId}">
                                    <input type="hidden" name="userRole" value="${user.roleID}">
                                    <button type="submit" class="btn-icon delete" title="Xóa người dùng">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </form>
                            </c:if>
                            <c:if test="${user.roleID == 1}">
                                <span style="color: #888; font-size: 12px;">Admin</span>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty userList}">
                <tr>
                    <td colspan="8" style="text-align: center; padding: 48px;">
                        <i class="fas fa-users" style="font-size: 48px; color: #ccc; margin-bottom: 16px; display: block;"></i>
                        <p style="color: #888;">Không có người dùng nào</p>
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<style>
    .badge-danger {
        background: linear-gradient(135deg, #EF4444, #DC2626);
        color: white;
        padding: 4px 10px;
        border-radius: 12px;
        font-size: 12px;
    }
    .stats-grid {
        display: grid;
        gap: 20px;
    }
</style>

