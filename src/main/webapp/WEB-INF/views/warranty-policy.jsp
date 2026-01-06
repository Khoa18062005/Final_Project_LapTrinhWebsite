<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chính Sách Bảo Hành - VietTech</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/PNG/AVT.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatar.css">
    <style>
        .policy-hero {
            background: linear-gradient(135deg, #0d6efd 0%, #0b5ed7 100%);
            color: white;
            padding: 60px 0;
            text-align: center;
        }
        .policy-hero h1 {
            font-size: 2.5rem;
            font-weight: 800;
            margin-bottom: 0.5rem;
        }
        .policy-content {
            padding: 60px 0;
            background-color: #f8f9fa;
        }
        .policy-section {
            background: white;
            border-radius: 12px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        }
        .policy-section h3 {
            color: #0d6efd;
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 1.5rem;
            padding-bottom: 0.75rem;
            border-bottom: 3px solid #0d6efd;
        }
        .policy-section h3 i {
            margin-right: 0.5rem;
        }
        .policy-list {
            list-style: none;
            padding: 0;
        }
        .policy-list li {
            padding: 0.75rem 0;
            padding-left: 2rem;
            position: relative;
            line-height: 1.6;
        }
        .policy-list li::before {
            content: '✓';
            position: absolute;
            left: 0;
            color: #0d6efd;
            font-weight: 700;
            font-size: 1.2rem;
        }
        .warranty-table {
            width: 100%;
            margin: 1.5rem 0;
            border-collapse: collapse;
        }
        .warranty-table th {
            background: linear-gradient(135deg, #0d6efd, #0b5ed7);
            color: white;
            padding: 1rem;
            font-weight: 600;
            text-align: left;
        }
        .warranty-table td {
            padding: 1rem;
            border: 1px solid #e9ecef;
        }
        .warranty-table tbody tr:nth-child(even) {
            background-color: #f8f9fa;
        }
        .warranty-table tbody tr:hover {
            background-color: #e7f3ff;
        }
        .note-box {
            background: #fff3cd;
            border-left: 4px solid #ffc107;
            padding: 1rem 1.5rem;
            border-radius: 8px;
            margin: 1.5rem 0;
        }
        .note-box strong {
            color: #856404;
        }
        .contact-box {
            background: linear-gradient(135deg, #0d6efd, #0b5ed7);
            color: white;
            padding: 2rem;
            border-radius: 12px;
            margin-top: 2rem;
        }
        .contact-box h4 {
            color: white;
            margin-bottom: 1rem;
        }
        .contact-box p {
            margin-bottom: 0.5rem;
        }
        .contact-box i {
            margin-right: 0.5rem;
        }
    </style>
</head>
<body>
<%@ include file="../../header.jsp" %>
<script>
    // Biến toàn cục cho JavaScript
    const contextPath = "${pageContext.request.contextPath}";
    const isLoggedIn = ${not empty sessionScope.user};
</script>
<div class="policy-hero">
    <div class="container">
        <h1><i class="bi bi-shield-check"></i> Chính Sách Bảo Hành</h1>
        <p class="lead">Cam kết bảo hành chính hãng, uy tín và tận tâm</p>
    </div>
</div>

<div class="policy-content">
    <div class="container">

        <!-- Tổng Quan -->
        <div class="policy-section">
            <h3><i class="bi bi-info-circle"></i>Tổng Quan</h3>
            <p>VietTech cam kết cung cấp dịch vụ bảo hành chất lượng cao cho tất cả sản phẩm được mua tại hệ thống. Chúng tôi là đối tác chính thức của các thương hiệu hàng đầu, đảm bảo quyền lợi bảo hành tốt nhất cho khách hàng.</p>
            <p>Tất cả sản phẩm đều được bảo hành theo chính sách của nhà sản xuất và tuân thủ quy định của pháp luật Việt Nam về bảo vệ quyền lợi người tiêu dùng.</p>
        </div>

        <!-- Thời Gian Bảo Hành -->
        <div class="policy-section">
            <h3><i class="bi bi-clock-history"></i>Thời Gian Bảo Hành</h3>
            <table class="warranty-table">
                <thead>
                <tr>
                    <th>Loại Sản Phẩm</th>
                    <th>Thời Gian Bảo Hành</th>
                    <th>Ghi Chú</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><strong>Điện thoại</strong></td>
                    <td>12 tháng</td>
                    <td>Bảo hành chính hãng tại trung tâm</td>
                </tr>
                <tr>
                    <td><strong>Laptop</strong></td>
                    <td>12-24 tháng</td>
                    <td>Tùy theo hãng (Apple 12 tháng, Dell/HP/Asus 24 tháng)</td>
                </tr>
                <tr>
                    <td><strong>Máy tính bảng</strong></td>
                    <td>12 tháng</td>
                    <td>Bảo hành chính hãng</td>
                </tr>
                <tr>
                    <td><strong>Tai nghe</strong></td>
                    <td>6-12 tháng</td>
                    <td>Tùy theo hãng và dòng sản phẩm</td>
                </tr>
                <tr>
                    <td><strong>Phụ kiện điện tử</strong></td>
                    <td>3-12 tháng</td>
                    <td>Sạc, cáp, chuột, bàn phím, v.v.</td>
                </tr>
                </tbody>
            </table>
            <div class="note-box">
                <strong><i class="bi bi-exclamation-triangle"></i> Lưu ý:</strong>
                <p class="mb-0">Thời gian bảo hành cụ thể sẽ được ghi rõ trên phiếu bảo hành kèm theo sản phẩm. Vui lòng giữ phiếu bảo hành và hóa đơn mua hàng để được hỗ trợ tốt nhất.</p>
            </div>
        </div>

        <!-- Điều Kiện Bảo Hành -->
        <div class="policy-section">
            <h3><i class="bi bi-check-circle"></i>Điều Kiện Bảo Hành</h3>
            <p>Sản phẩm được bảo hành khi đáp ứng <strong>TẤT CẢ</strong> các điều kiện sau:</p>
            <ul class="policy-list">
                <li>Sản phẩm còn trong thời gian bảo hành ghi trên phiếu bảo hành</li>
                <li>Tem bảo hành, Serial Number còn nguyên vẹn, không bị rách, mờ hoặc tẩy xóa</li>
                <li>Có phiếu bảo hành hoặc hóa đơn mua hàng hợp lệ từ VietTech</li>
                <li>Lỗi do nhà sản xuất (lỗi kỹ thuật, lỗi phần cứng, lỗi phần mềm)</li>
                <li>Sản phẩm chưa bị can thiệp, sửa chữa bởi bên thứ ba không được ủy quyền</li>
                <li>Không có dấu hiệu va đập mạnh, rơi vỡ, ngấm nước, cháy nổ</li>
            </ul>
        </div>

        <!-- Trường Hợp Không Bảo Hành -->
        <div class="policy-section">
            <h3><i class="bi bi-x-circle"></i>Trường Hợp Không Được Bảo Hành</h3>
            <p>VietTech <strong>KHÔNG</strong> chịu trách nhiệm bảo hành trong các trường hợp sau:</p>
            <ul class="policy-list">
                <li>Sản phẩm hết thời gian bảo hành</li>
                <li>Không có phiếu bảo hành, hóa đơn mua hàng hoặc chứng từ hợp lệ</li>
                <li>Tem bảo hành, Serial Number bị rách, mờ, tẩy xóa hoặc không xác định được</li>
                <li>Sản phẩm bị hư hỏng do lỗi người dùng: rơi vỡ, va đập, trầy xước nặng</li>
                <li>Sản phẩm bị ngấm nước, ẩm ướt, oxy hóa do bảo quản không đúng cách</li>
                <li>Sản phẩm bị cháy nổ do sự cố điện áp không ổn định</li>
                <li>Sản phẩm đã được sửa chữa bởi bên thứ ba không được ủy quyền</li>
                <li>Lỗi do sử dụng sai cách, không tuân thủ hướng dẫn sử dụng của nhà sản xuất</li>
                <li>Hư hỏng do thiên tai, hỏa hoạn, động đất, lũ lụt</li>
                <li>Sản phẩm bị thay đổi cấu trúc, tháo dỡ linh kiện</li>
            </ul>
        </div>

        <!-- Quy Trình Bảo Hành -->
        <div class="policy-section">
            <h3><i class="bi bi-gear"></i>Quy Trình Bảo Hành</h3>
            <p><strong>Bước 1: Mang sản phẩm đến bảo hành</strong></p>
            <ul class="policy-list">
                <li>Mang sản phẩm lỗi kèm theo phiếu bảo hành và hóa đơn mua hàng</li>
                <li>Đến trung tâm bảo hành chính hãng hoặc cửa hàng VietTech gần nhất</li>
            </ul>

            <p><strong>Bước 2: Kiểm tra và tiếp nhận</strong></p>
            <ul class="policy-list">
                <li>Nhân viên kiểm tra tình trạng sản phẩm và xác nhận điều kiện bảo hành</li>
                <li>Lập phiếu tiếp nhận bảo hành (khách hàng giữ 1 liên để nhận sản phẩm)</li>
                <li>Thông báo thời gian dự kiến hoàn thành (trung bình 7-15 ngày làm việc)</li>
            </ul>

            <p><strong>Bước 3: Xử lý bảo hành</strong></p>
            <ul class="policy-list">
                <li>Trung tâm bảo hành kiểm tra và xử lý lỗi sản phẩm</li>
                <li>Thay thế linh kiện hoặc sửa chữa theo quy chuẩn của nhà sản xuất</li>
                <li>Kiểm tra chất lượng sau sửa chữa</li>
            </ul>

            <p><strong>Bước 4: Nhận sản phẩm</strong></p>
            <ul class="policy-list">
                <li>VietTech thông báo cho khách hàng khi sản phẩm hoàn thành</li>
                <li>Khách hàng mang phiếu tiếp nhận đến nhận sản phẩm</li>
                <li>Kiểm tra sản phẩm trước khi ký nhận</li>
            </ul>

            <div class="note-box mt-3">
                <strong><i class="bi bi-clock"></i> Thời gian xử lý:</strong>
                <p class="mb-0">Thời gian bảo hành tùy thuộc vào tình trạng lỗi và chính sách của từng hãng. Trung bình từ <strong>7-15 ngày làm việc</strong>. Đối với các lỗi phức tạp có thể kéo dài hơn.</p>
            </div>
        </div>

        <!-- Chính Sách Đổi Mới -->
        <div class="policy-section">
            <h3><i class="bi bi-arrow-repeat"></i>Chính Sách Đổi Sản Phẩm Mới</h3>
            <p>Trong trường hợp sản phẩm bị lỗi <strong>từ nhà sản xuất</strong> và không thể sửa chữa được, VietTech cam kết:</p>
            <ul class="policy-list">
                <li>Đổi sản phẩm mới cùng model trong vòng <strong>30 ngày đầu</strong> kể từ ngày mua</li>
                <li>Sau 30 ngày: Đổi sản phẩm tương đương hoặc hoàn tiền theo quy định của nhà sản xuất</li>
                <li>Sản phẩm đổi mới được bảo hành như sản phẩm ban đầu</li>
            </ul>
        </div>

        <!-- Liên Hệ -->
        <div class="contact-box">
            <h4><i class="bi bi-headset"></i> Hỗ Trợ Bảo Hành 24/7</h4>
            <p><i class="bi bi-telephone-fill"></i> <strong>Hotline:</strong> 0866 448 892</p>
            <p><i class="bi bi-envelope-fill"></i> <strong>Email:</strong> support@viettech.vn</p>
            <p><i class="bi bi-clock-fill"></i> <strong>Giờ làm việc:</strong> 8:00 - 22:00 (Thứ 2 - Chủ nhật)</p>
            <p class="mb-0"><i class="bi bi-geo-alt-fill"></i> <strong>Địa chỉ:</strong> 01 Đ. Võ Văn Ngân, Linh Chiểu, Thủ Đức, TP.HCM</p>
        </div>

    </div>
</div>

<jsp:include page="../../footer.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/popup-login.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/notification.js"></script>
</body>
</html>