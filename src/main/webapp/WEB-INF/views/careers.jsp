<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tuyển Dụng - VietTech</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/careers.css">
</head>
<body>
<%@ include file="../../header.jsp" %>
<script>
    // Biến toàn cục cho JavaScript
    const contextPath = "${pageContext.request.contextPath}";
    const isLoggedIn = ${not empty sessionScope.user};
</script>
<div class="careers-hero">
    <div class="container">
        <h1><i class="bi bi-briefcase"></i> Tuyển Dụng</h1>
        <p class="lead">Gia nhập đội ngũ VietTech - Cơ hội phát triển không giới hạn</p>
    </div>
</div>

<div class="careers-content">
    <div class="container">

        <!-- Positions -->
        <div class="row mb-5">
            <!-- Vendor Position -->
            <div class="col-lg-6">
                <div class="position-card">
                    <div class="icon"><i class="bi bi-shop"></i></div>
                    <h3>Đối Tác Kinh Doanh (Vendor)</h3>
                    <p class="lead">Trở thành đối tác bán hàng trên nền tảng VietTech</p>

                    <h5 class="mt-4">Yêu cầu:</h5>
                    <ul class="benefits-list">
                        <li>Có giấy phép kinh doanh hợp pháp</li>
                        <li>Cung cấp sản phẩm công nghệ chính hãng</li>
                        <li>Có khả năng quản lý kho hàng và vận chuyển</li>
                        <li>Cam kết chất lượng dịch vụ</li>
                    </ul>

                    <h5 class="mt-4">Quyền lợi:</h5>
                    <ul class="benefits-list">
                        <li>Tiếp cận hàng triệu khách hàng tiềm năng</li>
                        <li>Hỗ trợ marketing và quảng bá sản phẩm</li>
                        <li>Hoa hồng hấp dẫn từ mỗi đơn hàng</li>
                        <li>Công cụ quản lý đơn hàng hiện đại</li>
                        <li>Thanh toán nhanh chóng, minh bạch</li>
                    </ul>
                </div>
            </div>

            <!-- Shipper Position -->
            <div class="col-lg-6">
                <div class="position-card">
                    <div class="icon"><i class="bi bi-truck"></i></div>
                    <h3>Tài Xế Giao Hàng (Shipper)</h3>
                    <p class="lead">Gia nhập đội ngũ giao hàng chuyên nghiệp của VietTech</p>

                    <h5 class="mt-4">Yêu cầu:</h5>
                    <ul class="benefits-list">
                        <li>Có bằng lái xe hợp lệ (A1, A2 hoặc B1, B2)</li>
                        <li>Có phương tiện giao hàng (xe máy/ô tô)</li>
                        <li>Am hiểu địa lý khu vực TP.HCM</li>
                        <li>Thái độ phục vụ tốt, trung thực</li>
                        <li>Có thể làm việc linh hoạt</li>
                    </ul>

                    <h5 class="mt-4">Quyền lợi:</h5>
                    <ul class="benefits-list">
                        <li>Thu nhập cao (8-15 triệu/tháng)</li>
                        <li>Hỗ trợ xăng xe, bảo hiểm</li>
                        <li>Thưởng theo hiệu suất làm việc</li>
                        <li>Làm việc linh hoạt, tự do thời gian</li>
                        <li>Ứng dụng quản lý đơn hàng thông minh</li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Application Forms -->
        <div class="application-form">
            <h4><i class="bi bi-file-earmark-text"></i> Đăng Ký Tuyển Dụng</h4>

            <div class="note-box">
                <strong><i class="bi bi-info-circle"></i> Lưu ý:</strong>
                <p class="mb-0">Vui lòng điền đầy đủ thông tin chính xác. Đơn đăng ký của bạn sẽ được xem xét trong vòng 3-5 ngày làm việc.</p>
            </div>

            <!-- Tabs -->
            <ul class="nav nav-tabs form-tabs" id="careerTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="vendor-tab" data-bs-toggle="tab" data-bs-target="#vendor" type="button" role="tab">
                        <i class="bi bi-shop me-2"></i>Đối Tác Kinh Doanh
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="shipper-tab" data-bs-toggle="tab" data-bs-target="#shipper" type="button" role="tab">
                        <i class="bi bi-truck me-2"></i>Tài Xế Giao Hàng
                    </button>
                </li>
            </ul>

            <!-- Tab Contents -->
            <div class="tab-content" id="careerTabsContent">

                <!-- Vendor Form -->
                <div class="tab-pane fade show active" id="vendor" role="tabpanel">
                    <form id="vendorForm" method="post" action="${pageContext.request.contextPath}/careers/vendor">
                        <div class="row g-3">
                            <!-- ✅ Họ - Tự động điền từ session -->
                            <div class="col-md-6">
                                <label class="form-label">Họ <span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control"
                                       name="firstName"
                                       value="${not empty sessionScope.user ? sessionScope.user.firstName : ''}"
                                       required>
                            </div>

                            <!-- ✅ Tên - Tự động điền từ session -->
                            <div class="col-md-6">
                                <label class="form-label">Tên <span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control"
                                       name="lastName"
                                       value="${not empty sessionScope.user ? sessionScope.user.lastName : ''}"
                                       required>
                            </div>

                            <!-- ✅ Email - Tự động điền và READONLY -->
                            <div class="col-md-6">
                                <label class="form-label">Email <span class="text-danger">*</span></label>
                                <input type="email"
                                       class="form-control"
                                       name="email"
                                       value="${not empty sessionScope.user ? sessionScope.user.email : ''}"
                                       readonly
                                       required>
                                <small class="text-muted">Email không thể thay đổi</small>
                            </div>

                            <!-- ✅ Số điện thoại - Tự động điền -->
                            <div class="col-md-6">
                                <label class="form-label">Số điện thoại <span class="text-danger">*</span></label>
                                <input type="tel"
                                       class="form-control"
                                       name="phone"
                                       value="${not empty sessionScope.user ? sessionScope.user.phone : ''}"
                                       pattern="[0-9]{10}"
                                       required>
                            </div>

                            <!-- ✅ Giới tính - Tự động chọn -->
                            <div class="col-md-6">
                                <label class="form-label">Giới tính <span class="text-danger">*</span></label>
                                <select class="form-select" name="gender" required>
                                    <option value="">-- Chọn --</option>
                                    <option value="Nam" ${sessionScope.user.gender == 'Male' ? 'selected' : ''}>Nam</option>
                                    <option value="Nữ" ${sessionScope.user.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                                    <option value="Khác" ${sessionScope.user.gender == 'Other' ? 'selected' : ''}>Khác</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Tên doanh nghiệp <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="businessName" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Mã số thuế <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="taxId" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Số tài khoản ngân hàng <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="bankAccount" required>
                            </div>
                            <div class="col-12">
                                <label class="form-label">Địa chỉ kho hàng</label>
                                <textarea class="form-control" name="address" rows="2"></textarea>
                            </div>
                            <div class="col-12">
                                <label class="form-label">Giới thiệu về sản phẩm/dịch vụ</label>
                                <textarea class="form-control" name="description" rows="4"></textarea>
                            </div>
                            <div class="col-12">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="vendorAgree" required>
                                    <label class="form-check-label" for="vendorAgree">
                                        Tôi đồng ý với <a href="${pageContext.request.contextPath}/policy/terms" target="_blank">Điều khoản sử dụng</a>
                                    </label>
                                </div>
                            </div>
                            <div class="col-12">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="bi bi-send me-2"></i>Gửi Đơn Đăng Ký
                                </button>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Shipper Form -->
                <div class="tab-pane fade" id="shipper" role="tabpanel">
                    <form id="shipperForm" method="post" action="${pageContext.request.contextPath}/careers/shipper">
                        <div class="row g-3">
                            <!-- ✅ Họ - Tự động điền -->
                            <div class="col-md-6">
                                <label class="form-label">Họ <span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control"
                                       name="firstName"
                                       value="${not empty sessionScope.user ? sessionScope.user.firstName : ''}"
                                       required>
                            </div>

                            <!-- ✅ Tên - Tự động điền -->
                            <div class="col-md-6">
                                <label class="form-label">Tên <span class="text-danger">*</span></label>
                                <input type="text"
                                       class="form-control"
                                       name="lastName"
                                       value="${not empty sessionScope.user ? sessionScope.user.lastName : ''}"
                                       required>
                            </div>

                            <!-- ✅ Email - Tự động điền và READONLY -->
                            <div class="col-md-6">
                                <label class="form-label">Email <span class="text-danger">*</span></label>
                                <input type="email"
                                       class="form-control"
                                       name="email"
                                       value="${not empty sessionScope.user ? sessionScope.user.email : ''}"
                                       readonly
                                       required>
                                <small class="text-muted">Email không thể thay đổi</small>
                            </div>

                            <!-- ✅ Số điện thoại - Tự động điền -->
                            <div class="col-md-6">
                                <label class="form-label">Số điện thoại <span class="text-danger">*</span></label>
                                <input type="tel"
                                       class="form-control"
                                       name="phone"
                                       value="${not empty sessionScope.user ? sessionScope.user.phone : ''}"
                                       pattern="[0-9]{10}"
                                       required>
                            </div>

                            <!-- ✅ Giới tính - Tự động chọn -->
                            <div class="col-md-6">
                                <label class="form-label">Giới tính <span class="text-danger">*</span></label>
                                <select class="form-select" name="gender" required>
                                    <option value="">-- Chọn --</option>
                                    <option value="Nam" ${sessionScope.user.gender == 'Male' ? 'selected' : ''}>Nam</option>
                                    <option value="Nữ" ${sessionScope.user.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                                    <option value="Khác" ${sessionScope.user.gender == 'Other' ? 'selected' : ''}>Khác</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Số bằng lái xe <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="licenseNumber" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Loại phương tiện <span class="text-danger">*</span></label>
                                <select class="form-select" name="vehicleType" required>
                                    <option value="">-- Chọn --</option>
                                    <option value="Xe máy">Xe máy</option>
                                    <option value="Xe tải nhỏ">Xe tải nhỏ</option>
                                    <option value="Ô tô">Ô tô</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Biển số xe <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="vehiclePlate" required>
                            </div>
                            <div class="col-12">
                                <label class="form-label">Địa chỉ hiện tại</label>
                                <textarea class="form-control" name="address" rows="2"></textarea>
                            </div>
                            <div class="col-12">
                                <label class="form-label">Kinh nghiệm làm việc</label>
                                <textarea class="form-control" name="experience" rows="3" placeholder="Mô tả ngắn về kinh nghiệm giao hàng của bạn..."></textarea>
                            </div>
                            <div class="col-12">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="shipperAgree" required>
                                    <label class="form-check-label" for="shipperAgree">
                                        Tôi đồng ý với <a href="${pageContext.request.contextPath}/policy/terms" target="_blank">Điều khoản sử dụng</a>
                                    </label>
                                </div>
                            </div>
                            <div class="col-12">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="bi bi-send me-2"></i>Gửi Đơn Đăng Ký
                                </button>
                            </div>
                        </div>
                    </form>
                </div>

            </div>
        </div>

    </div>
</div>

<jsp:include page="../../footer.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
</body>
</html>