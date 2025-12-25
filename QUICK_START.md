# 🚀 Quick Start - Admin Dashboard

## Các bước để chạy Admin Dashboard

### 1. Build Project trong IntelliJ IDEA

1. **Clean project:**
   - Vào menu: `Build` → `Clean Project`

2. **Rebuild project:**
   - Vào menu: `Build` → `Rebuild Project`

### 2. Configure Tomcat Server (nếu chưa)

1. **Thêm Tomcat Configuration:**
   - Vào `Run` → `Edit Configurations`
   - Click `+` → `Tomcat Server` → `Local`
   
2. **Configure Deployment:**
   - Tab `Deployment` → Click `+` → `Artifact`
   - Chọn `Final_Project_LapTrinhWebsite:war exploded`
   - Đặt **Application context** là `/` (để truy cập root)
   - Hoặc để là `/Final_Project_LapTrinhWebsite`

3. **Configure Server:**
   - Tab `Server`
   - URL: `http://localhost:8080/`
   - HTTP port: `8080`

### 3. Start Server

1. Click nút **Run** (▶️) hoặc **Debug** (🐞)
2. Đợi server khởi động

### 4. Truy cập Admin Dashboard

**Nếu Application context = `/`:**
```
http://localhost:8080/admin
```

**Nếu Application context = `/Final_Project_LapTrinhWebsite`:**
```
http://localhost:8080/Final_Project_LapTrinhWebsite/admin
```

## ✅ Checklist nếu gặp lỗi 404

- [ ] Đã rebuild project?
- [ ] Servlet `/admin` có annotation `@WebServlet(urlPatterns = "/admin")`?
- [ ] File `web.xml` đã được update lên version 3.1?
- [ ] `metadata-complete="false"` trong web.xml?
- [ ] Tomcat deployment artifact đã được config?
- [ ] Check console xem servlet có được load không?

## 📂 Files đã tạo

```
✅ src/main/webapp/WEB-INF/views/admin.jsp       (Giao diện HTML)
✅ src/main/webapp/assets/css/admin.css          (Styles)
✅ src/main/webapp/assets/js/admin.js            (JavaScript + Data)
✅ src/main/webapp/WEB-INF/web.xml               (Updated to v3.1)
```

## 🎯 Tính năng có sẵn

### Navigation
- ✅ Dashboard - Thống kê tổng quan
- ✅ Sản phẩm - Quản lý sản phẩm (8 items mẫu)
- ✅ Người dùng - Quản lý users (5 items mẫu)
- ✅ Đơn hàng - Quản lý orders (5 items mẫu)
- ✅ Doanh thu - Báo cáo revenue
- ✅ Danh mục - 6 categories với icon
- ✅ Đánh giá - 4 reviews mẫu
- ✅ Cài đặt - Settings form

### CRUD Operations (với dữ liệu mẫu)
- ✅ **Thêm sản phẩm** - Form modal hoạt động
- ✅ **Xóa sản phẩm** - Confirm dialog
- ✅ **Xóa người dùng** - Confirm dialog
- ✅ **Xóa danh mục** - Confirm dialog
- ✅ **Xóa đánh giá** - Confirm dialog
- ⚠️ **Edit** - Hiển thị alert (chưa implement form)
- ⚠️ **View detail** - Hiển thị alert với info

## 🎨 UI Features

- **Responsive Design** - Hoạt động trên mobile/tablet/desktop
- **Sidebar Navigation** - Gradient đẹp với icons
- **Stats Cards** - 4 thẻ thống kê với màu sắc
- **Data Tables** - Hiển thị data dạng bảng
- **Charts** - Biểu đồ doanh thu đơn giản
- **Modal Forms** - Form thêm sản phẩm
- **Status Badges** - Trạng thái có màu
- **Action Buttons** - View/Edit/Delete

## 📊 Dữ liệu mẫu

File `admin.js` chứa:
- **8 sản phẩm**: iPhone 15 Pro, Samsung S24, MacBook Pro, Dell XPS, AirPods, iPad, Apple Watch, Sony
- **5 người dùng**: Mix Admin và Khách hàng
- **5 đơn hàng**: Các trạng thái khác nhau
- **6 danh mục**: Điện thoại, Laptop, Tablet, Phụ kiện, Smartwatch, TV
- **4 đánh giá**: Reviews với rating 4-5 sao

## 🔄 Refresh Data

Khi thêm/xóa items, trang tự động cập nhật:
- Stats numbers update
- Tables refresh
- No page reload needed

## 📱 Test Responsive

1. Mở Chrome DevTools (F12)
2. Click icon Toggle Device Toolbar (Ctrl+Shift+M)
3. Chọn device: iPhone, iPad, etc.
4. Sidebar tự động ẩn trên mobile

## 🐛 Troubleshooting

### Lỗi: Servlet không tìm thấy
```
Solution: Đảm bảo web.xml đã là version 3.1
```

### Lỗi: CSS/JS không load
```
Solution: Check đường dẫn trong browser DevTools > Network
URL phải là: http://localhost:8080/assets/css/admin.css
```

### Lỗi: 404 Not Found
```
Solution: 
1. Check Application context trong Tomcat config
2. Đảm bảo URL đúng: /admin hoặc /Final_Project_LapTrinhWebsite/admin
3. Rebuild project
```

## 📝 Notes

- ⚠️ Đây là **FRONTEND ONLY** - chưa kết nối database
- ⚠️ Data mẫu lưu trong JavaScript array
- ⚠️ Refresh page sẽ reset data về ban đầu
- ✅ Sẵn sàng tích hợp backend API
- ✅ Có thể customize màu sắc, icons, layout

---

**Happy Coding! 🎉**

