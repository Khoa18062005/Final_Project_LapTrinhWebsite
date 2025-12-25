# Admin Dashboard - Hướng dẫn sử dụng

## 📋 Tổng quan

Giao diện Admin Dashboard hoàn chỉnh với các tính năng quản lý:
- **Dashboard**: Thống kê tổng quan về sản phẩm, người dùng, đơn hàng, doanh thu
- **Quản lý sản phẩm**: Thêm, sửa, xóa, xem chi tiết sản phẩm
- **Quản lý người dùng**: Quản lý thông tin khách hàng và admin
- **Quản lý đơn hàng**: Theo dõi và cập nhật trạng thái đơn hàng
- **Báo cáo doanh thu**: Xem thống kê doanh thu theo thời gian
- **Quản lý danh mục**: Thêm, sửa, xóa danh mục sản phẩm
- **Quản lý đánh giá**: Duyệt và xóa đánh giá khách hàng
- **Cài đặt hệ thống**: Cấu hình thông tin cửa hàng

## 🚀 Cách truy cập

Sau khi chạy server Tomcat, truy cập:

```
http://localhost:8080/admin
```

## 📁 Cấu trúc files

```
src/main/webapp/
├── WEB-INF/
│   └── views/
│       └── admin.jsp          # Giao diện admin
└── assets/
    ├── css/
    │   └── admin.css          # Styles cho admin
    └── js/
        └── admin.js           # Logic và dữ liệu mẫu
```

## 🎨 Tính năng

### 1. Dashboard
- Hiển thị 4 thẻ thống kê: Sản phẩm, Người dùng, Đơn hàng, Doanh thu
- Biểu đồ doanh thu theo tháng
- Top 5 sản phẩm bán chạy

### 2. Quản lý Sản phẩm
- Xem danh sách sản phẩm dạng bảng
- Thêm sản phẩm mới qua modal form
- Sửa, xóa, xem chi tiết sản phẩm
- Hiển thị trạng thái và số lượng tồn kho

### 3. Quản lý Người dùng
- Xem danh sách người dùng
- Phân biệt vai trò: Admin / Khách hàng
- Trạng thái: Hoạt động / Khóa
- CRUD operations

### 4. Quản lý Đơn hàng
- Xem danh sách đơn hàng
- Trạng thái: Hoàn thành, Đang xử lý, Chờ xử lý, Đã hủy
- Theo dõi thanh toán
- Cập nhật trạng thái đơn hàng

### 5. Báo cáo Doanh thu
- Thống kê doanh thu tháng
- Số đơn hàng hoàn thành
- Giá trị đơn trung bình
- Biểu đồ chi tiết

### 6. Quản lý Danh mục
- Hiển thị dạng grid với icon
- Đếm số sản phẩm trong mỗi danh mục
- Thêm, sửa, xóa danh mục

### 7. Quản lý Đánh giá
- Xem danh sách đánh giá từ khách hàng
- Hiển thị rating (1-5 sao)
- Duyệt hoặc xóa đánh giá

## 💾 Dữ liệu mẫu

File `admin.js` đã có sẵn dữ liệu mẫu:
- **8 sản phẩm**: iPhone, Samsung, MacBook, Dell, AirPods, iPad, Apple Watch, Sony
- **5 người dùng**: Bao gồm cả Admin và Khách hàng
- **5 đơn hàng**: Với nhiều trạng thái khác nhau
- **6 danh mục**: Điện thoại, Laptop, Tablet, Phụ kiện, Smartwatch, TV & Audio
- **4 đánh giá**: Review từ khách hàng

## 🎯 Các chức năng đang hoạt động

### ✅ Hoàn thành
- Navigation giữa các section
- Hiển thị dữ liệu mẫu
- Thêm sản phẩm mới
- Xóa sản phẩm, người dùng, danh mục, đánh giá
- Cập nhật thống kê real-time
- Responsive design (mobile-friendly)
- Modal form cho thêm sản phẩm
- Biểu đồ doanh thu đơn giản

### 🔄 Chưa implement (để dành cho backend)
- Kết nối database
- API calls
- Form sửa sản phẩm/người dùng
- Upload hình ảnh
- Export báo cáo
- Phân quyền thật
- Authentication & Authorization

## 🎨 Design Features

- **Sidebar navigation**: Màu gradient đẹp mắt
- **Top navbar**: Search box, notifications, user profile
- **Responsive**: Hoạt động tốt trên mobile
- **Smooth animations**: Fade in, hover effects
- **Color-coded stats**: Mỗi loại thống kê có màu riêng
- **Status badges**: Hiển thị trạng thái rõ ràng
- **Action buttons**: Icon buttons cho các thao tác

## 🔧 Tùy chỉnh

### Thêm section mới
1. Thêm nav item trong sidebar (admin.jsp)
2. Thêm content section với ID tương ứng
3. Thêm case trong `showSection()` function (admin.js)

### Thêm dữ liệu mẫu
Sửa các array trong `admin.js`:
```javascript
let products = [...];
let users = [...];
let orders = [...];
```

### Thay đổi màu sắc
Sửa file `admin.css`, tìm các gradient:
```css
.stat-icon.blue { background: linear-gradient(...); }
```

## 📱 Responsive Breakpoints

- Desktop: > 1200px
- Tablet: 768px - 1200px  
- Mobile: < 768px

## 🔐 Security Note

Đây chỉ là giao diện frontend với dữ liệu mẫu. Khi tích hợp backend:
- Cần implement authentication
- Kiểm tra quyền truy cập
- Validate dữ liệu
- Bảo mật API endpoints
- Xử lý XSS và CSRF

## 🚀 Next Steps

1. Tích hợp với backend API
2. Kết nối database
3. Implement real authentication
4. Thêm upload image
5. Export reports (PDF, Excel)
6. Real-time notifications
7. Advanced charts (Chart.js hoặc D3.js)
8. Search và filter cho các bảng

## 📞 Support

Nếu có vấn đề, kiểm tra:
1. Console browser (F12) xem có lỗi JS không
2. Network tab xem các file CSS/JS đã load chưa
3. Đảm bảo servlet `/admin` đã được map đúng

---

**Version**: 1.0  
**Last Updated**: 25/12/2025  
**Status**: ✅ Ready for use (Frontend only)

