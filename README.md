# 🇻🇳 VietTech - Siêu Thị Công Nghệ Trực Tuyến

<div align="center">

![VietTech Logo](src/main/webapp/assets/PNG/LogoVT.png)

**"Mua công nghệ - Chọn VietTech"**

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Servlet](https://img.shields.io/badge/Servlet-6.0-007396?style=for-the-badge&logo=java&logoColor=white)](https://jakarta.ee/specifications/servlet/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

[🌐 Demo](#) | [📖 Documentation](#) | [🐛 Bug Report](#) | [✨ Feature Request](#)

</div>

---

## 📋 Mục Lục

- [Giới Thiệu](#-giới-thiệu)
- [Tính Năng](#-tính-năng)
- [Công Nghệ Sử Dụng](#-công-nghệ-sử-dụng)
- [Kiến Trúc Hệ Thống](#-kiến-trúc-hệ-thống)
- [Cài Đặt](#-cài-đặt)
- [Cấu Hình](#-cấu-hình)
- [Sử Dụng](#-sử-dụng)
- [Database Schema](#-database-schema)
- [API Documentation](#-api-documentation)
- [Deployment](#-deployment)
- [Screenshots](#-screenshots)
- [Contributing](#-contributing)
- [Team](#-team)
- [License](#-license)

---

## 🎯 Giới Thiệu

**VietTech** là hệ thống thương mại điện tử chuyên cung cấp các sản phẩm công nghệ chính hãng tại Việt Nam.

### Chuyên phân phối:
- 📱 **Điện thoại thông minh**: iPhone, Samsung, Xiaomi, OPPO...
- 💻 **Laptop, Macbook**: Dell, HP, Asus, Lenovo, Apple...
- 📲 **Tablet, iPad**: iPad Pro, Samsung Tab, Xiaomi Pad...
- 🖥️ **PC Gaming & Linh kiện**: CPU, GPU, RAM, Mainboard...
- 🎧 **Tai nghe, Loa, Phụ kiện**: AirPods, Sony, JBL...

### Cam kết:
✅ 100% hàng chính hãng  
✅ Giá tốt nhất - Hoàn tiền nếu rẻ hơn  
✅ Giao hàng toàn quốc  
✅ Bảo hành tận tâm  

---

## ✨ Tính Năng

### 👤 Dành cho Khách hàng (Customer)
- 🔍 Tìm kiếm và lọc sản phẩm nâng cao
- 🛒 Giỏ hàng thông minh với lưu trữ persistent
- ❤️ Wishlist và so sánh sản phẩm
- 📦 Đặt hàng và theo dõi đơn hàng real-time
- 💳 Thanh toán đa dạng (COD, VNPay, MoMo, ZaloPay)
- ⭐ Đánh giá và review sản phẩm
- 🎟️ Áp dụng voucher và tích điểm
- 🔔 Nhận thông báo khuyến mãi và trạng thái đơn hàng
- 📍 Quản lý nhiều địa chỉ giao hàng
- 📊 Xem lịch sử mua hàng và hoàn tiền

### 🏪 Dành cho Vendor
- 📦 Quản lý sản phẩm và kho hàng
- 💰 Quản lý đơn hàng và doanh thu
- 📊 Thống kê và báo cáo bán hàng
- 🚚 Phân công shipper giao hàng
- 💬 Phản hồi đánh giá khách hàng
- 🏭 Quản lý nhiều kho hàng
- 📈 Xem analytics và insights

### 🚚 Dành cho Shipper
- 📍 Nhận và quản lý đơn giao hàng
- 🗺️ Cập nhật vị trí real-time
- ✅ Xác nhận giao hàng thành công
- 💵 Theo dõi thu nhập
- ⭐ Xem đánh giá và feedback

### 👨‍💼 Dành cho Admin
- 👥 Quản lý người dùng và phân quyền
- 🏷️ Quản lý danh mục và sản phẩm
- 🎫 Tạo và quản lý voucher, flash sale
- 📊 Thống kê toàn hệ thống
- ⚖️ Xử lý tranh chấp và khiếu nại
- 🔒 Quản lý bảo mật hệ thống

---

## 🛠️ Công Nghệ Sử Dụng

### Backend
- **Java 17+** - Ngôn ngữ lập trình chính
- **Java Servlet 6.0** - Web framework
- **JSP/JSTL** - Server-side rendering
- **EL (Expression Language)** - Template engine
- **JPA (Hibernate)** - ORM framework
- **MySQL 8.0** - Database chính
- **HikariCP** - Connection pooling (tối ưu hiệu suất)
- **Apache Commons** - Utility libraries
- **Jackson** - JSON processing
- **BCrypt** - Password hashing

### Frontend
- **HTML5/CSS3** - Markup & Styling
- **JavaScript (ES6+)** - Client-side scripting
- **Bootstrap 5** - UI Framework
- **jQuery** - DOM manipulation
- **AJAX** - Asynchronous requests
- **Chart.js** - Data visualization

### DevOps & Deployment
- **Aiven** - Database hosting & connection management
- **Render** - Web application deployment
- **Git/GitHub** - Version control
- **Maven** - Build tool & dependency management
- **Tomcat 10** - Application server

### Payment Integration
- **VNPay API** - Vietnamese payment gateway
- **MoMo API** - E-wallet integration
- **ZaloPay API** - E-wallet integration

### Other Tools
- **Log4j2** - Logging framework
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **Selenium** - E2E testing

---

## 🏗️ Kiến Trúc Hệ Thống

### Architecture Pattern
```
┌─────────────────────────────────────────────────────────┐
│                     CLIENT LAYER                        │
│         (Browser - HTML/CSS/JS/Bootstrap)               │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP/HTTPS
┌────────────────────▼────────────────────────────────────┐
│                  PRESENTATION LAYER                     │
│              (JSP/JSTL/Servlet Controllers)             │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                   SERVICE LAYER                         │
│           (Business Logic - Java Services)              │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                     DAO LAYER                           │
│              (JPA/Hibernate Repositories)               │
└────────────────────┬────────────────────────────────────┘
                     │ HikariCP
┌────────────────────▼────────────────────────────────────┐
│                  DATABASE LAYER                         │
│              (MySQL on Aiven Cloud)                     │
└─────────────────────────────────────────────────────────┘
```

### Project Structure
```
viettech/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/viettech/
│   │   │       ├── controller/     # Servlet Controllers
│   │   │       ├── service/        # Business Logic
│   │   │       ├── dao/            # Data Access Objects
│   │   │       ├── entity/         # JPA Entities
│   │   │       ├── dto/            # Data Transfer Objects
│   │   │       ├── filter/         # Security & Auth Filters
│   │   │       ├── util/           # Utility Classes
│   │   │       └── config/         # Configuration Classes
│   │   ├── resources/
│   │   │   ├── META-INF/
│   │   │   │   └── persistence.xml # JPA Configuration
│   │   │   ├── log4j2.xml          # Logging Config
│   │   │   └── application.properties
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml         # Deployment Descriptor
│   │       │   └── views/          # JSP Views
│   │       ├── assets/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       └── index.jsp
│   └── test/                       # Unit & Integration Tests
├── docs/                           # Documentation
├── database/
│   ├── schema.sql                  # Database Schema
│   ├── data.sql                    # Sample Data
│   └── class-diagram.puml          # Class Diagram
├── pom.xml                         # Maven Configuration
├── .gitignore
├── README.md
└── LICENSE
```

---

## 🚀 Cài Đặt

### Prerequisites
- **Java JDK 17+** ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Apache Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
- **MySQL 8.0+** (hoặc sử dụng Aiven)
- **Apache Tomcat 10** ([Download](https://tomcat.apache.org/download-10.cgi))
- **Git** ([Download](https://git-scm.com/downloads))

### Clone Repository
```bash
git clone https://github.com/yourusername/viettech.git
cd viettech
```

### Build Project
```bash
mvn clean install
```

### Chạy Tests
```bash
mvn test
```

---

## ⚙️ Cấu Hình

### 1. Database Configuration

#### Sử dụng Local MySQL
Tạo file `src/main/resources/application.properties`:
```properties
# Database Configuration
db.url=jdbc:mysql://localhost:3306/viettech_db?useSSL=false&serverTimezone=UTC
db.username=root
db.password=your_password

# HikariCP Configuration
hikari.maximumPoolSize=10
hikari.minimumIdle=5
hikari.connectionTimeout=30000
hikari.idleTimeout=600000
hikari.maxLifetime=1800000
```

#### Sử dụng Aiven Cloud Database
```properties
# Aiven Database Configuration
db.url=jdbc:mysql://your-aiven-host:port/viettech_db?ssl-mode=REQUIRED
db.username=avnadmin
db.password=your_aiven_password

# HikariCP Configuration (Production)
hikari.maximumPoolSize=20
hikari.minimumIdle=10
hikari.connectionTimeout=30000
hikari.idleTimeout=600000
hikari.maxLifetime=1800000
```

### 2. JPA Configuration
File `src/main/resources/META-INF/persistence.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
             http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd"
             version="2.2">
    
    <persistence-unit name="VietTechPU" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        
        <!-- Entity Classes -->
        <class>com.viettech.entity.User</class>
        <class>com.viettech.entity.Product</class>
        <class>com.viettech.entity.Order</class>
        <!-- Add all entity classes here -->
        
        <properties>
            <!-- Hibernate Properties -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            
            <!-- HikariCP Properties -->
            <property name="hibernate.hikari.maximumPoolSize" value="20"/>
            <property name="hibernate.hikari.minimumIdle" value="10"/>
            <property name="hibernate.connection.provider_class" 
                      value="org.hibernate.hikaricp.internal.HikariCPConnectionProvider"/>
        </properties>
    </persistence-unit>
</persistence>
```

### 3. Payment Gateway Configuration
```properties
# VNPay Configuration
vnpay.api.url=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.tmnCode=YOUR_TMN_CODE
vnpay.hashSecret=YOUR_HASH_SECRET
vnpay.returnUrl=https://yourapp.com/payment/vnpay/return

# MoMo Configuration
momo.partnerCode=YOUR_PARTNER_CODE
momo.accessKey=YOUR_ACCESS_KEY
momo.secretKey=YOUR_SECRET_KEY
momo.endpoint=https://test-payment.momo.vn/v2/gateway/api/create

# ZaloPay Configuration
zalopay.appId=YOUR_APP_ID
zalopay.key1=YOUR_KEY1
zalopay.key2=YOUR_KEY2
zalopay.endpoint=https://sb-openapi.zalopay.vn/v2/create
```

### 4. Import Database Schema
```bash
# Sử dụng MySQL CLI
mysql -u root -p viettech_db < database/schema.sql

# Import sample data
mysql -u root -p viettech_db < database/data.sql
```

---

## 💻 Sử Dụng

### Development Mode
1. Import project vào IDE (IntelliJ IDEA hoặc Eclipse)
2. Configure Tomcat server trong IDE
3. Run project:
   ```bash
   mvn tomcat7:run
   # hoặc
   mvn clean package
   # Deploy file WAR vào Tomcat
   ```
4. Truy cập: `http://localhost:8080/viettech`

### Production Mode
```bash
# Build production WAR
mvn clean package -Pprod

# Deploy to Tomcat
cp target/viettech.war $TOMCAT_HOME/webapps/
```

### Default Accounts
```
Admin:
- Username: admin@viettech.vn
- Password: Admin@123

Vendor:
- Username: vendor@viettech.vn
- Password: Vendor@123

Customer:
- Username: customer@viettech.vn
- Password: Customer@123

Shipper:
- Username: shipper@viettech.vn
- Password: Shipper@123
```

---

## 🗄️ Database Schema

Chi tiết database schema được định nghĩa trong file PlantUML:
- 📄 [Class Diagram](./database/class-diagram.puml)
- 📊 [ER Diagram](./docs/er-diagram.png)

### Main Tables
- **users** - Quản lý người dùng (Customer, Vendor, Shipper, Admin)
- **addresses** - Địa chỉ giao hàng (Tỉnh/Quận/Phường)
- **products** - Sản phẩm (Phone, Laptop, Tablet, PC, Headphone, Accessory)
- **variants** - Biến thể sản phẩm
- **variant_attributes** - Thuộc tính biến thể (màu, RAM, storage...)
- **categories** - Danh mục sản phẩm
- **inventories** - Quản lý kho hàng
- **warehouses** - Kho hàng
- **orders** - Đơn hàng
- **order_details** - Chi tiết đơn hàng
- **payments** - Thanh toán
- **deliveries** - Giao hàng
- **vouchers** - Mã giảm giá
- **flash_sales** - Flash sale
- **reviews** - Đánh giá sản phẩm
- **wishlists** - Danh sách yêu thích

---

## 🛣️ Servlet Endpoints

### User Management (Authentication)
```
GET    /register                - Trang đăng ký
POST   /register                - Xử lý đăng ký (form data)
GET    /login                   - Trang đăng nhập
POST   /login                   - Xử lý đăng nhập (form data)
GET    /logout                  - Đăng xuất
GET    /forgot-password         - Trang quên mật khẩu
POST   /forgot-password         - Xử lý quên mật khẩu
GET    /profile                 - Xem thông tin cá nhân
POST   /profile/update          - Cập nhật thông tin (form data)
```

### Product & Category
```
GET    /products                - Danh sách sản phẩm (pagination, filters via query params)
GET    /product-detail          - Chi tiết sản phẩm (?id=xxx)
GET    /search                  - Tìm kiếm sản phẩm (?keyword=xxx)
GET    /categories              - Danh sách danh mục
GET    /category                - Sản phẩm theo danh mục (?id=xxx)
```

### Shopping Cart
```
GET    /cart                    - Xem giỏ hàng
POST   /cart/add                - Thêm vào giỏ (form: productId, variantId, quantity)
POST   /cart/update             - Cập nhật số lượng (form: cartItemId, quantity)
POST   /cart/remove             - Xóa sản phẩm (form: cartItemId)
POST   /cart/clear              - Xóa toàn bộ giỏ hàng
```

### Order Management
```
GET    /checkout                - Trang thanh toán
POST   /checkout                - Xử lý đặt hàng (form data)
GET    /order-history           - Lịch sử đơn hàng
GET    /order-detail            - Chi tiết đơn hàng (?orderId=xxx)
POST   /order/cancel            - Hủy đơn hàng (form: orderId, reason)
GET    /order/track             - Theo dõi đơn hàng (?orderId=xxx)
```

### Payment
```
GET    /payment                 - Trang chọn phương thức thanh toán
POST   /payment/vnpay           - Tạo giao dịch VNPay
GET    /payment/vnpay/return    - Callback VNPay (query params)
POST   /payment/momo            - Tạo giao dịch MoMo
GET    /payment/momo/return     - Callback MoMo
POST   /payment/cod             - Xác nhận COD (form data)
```

### Review & Rating
```
GET    /reviews                 - Xem đánh giá (?productId=xxx)
POST   /review/add              - Thêm đánh giá (form: productId, rating, comment)
POST   /review/edit             - Sửa đánh giá (form: reviewId, rating, comment)
POST   /review/delete           - Xóa đánh giá (form: reviewId)
```

### Wishlist
```
GET    /wishlist                - Xem danh sách yêu thích
POST   /wishlist/add            - Thêm vào wishlist (form: productId)
POST   /wishlist/remove         - Xóa khỏi wishlist (form: wishlistItemId)
POST   /wishlist/move-to-cart   - Chuyển sang giỏ hàng (form: wishlistItemId)
```

### Vendor Panel
```
GET    /vendor/dashboard        - Dashboard vendor
GET    /vendor/products         - Quản lý sản phẩm
GET    /vendor/product/add      - Trang thêm sản phẩm
POST   /vendor/product/add      - Xử lý thêm sản phẩm (form data)
GET    /vendor/product/edit     - Trang sửa sản phẩm (?id=xxx)
POST   /vendor/product/edit     - Xử lý sửa sản phẩm (form data)
POST   /vendor/product/delete   - Xóa sản phẩm (form: productId)
GET    /vendor/orders           - Quản lý đơn hàng
POST   /vendor/order/update     - Cập nhật trạng thái đơn (form: orderId, status)
GET    /vendor/inventory        - Quản lý kho hàng
POST   /vendor/inventory/update - Cập nhật tồn kho (form: variantId, quantity)
```

### Shipper Panel
```
GET    /shipper/dashboard       - Dashboard shipper
GET    /shipper/deliveries      - Danh sách đơn giao hàng
POST   /shipper/delivery/accept - Nhận đơn (form: deliveryId)
POST   /shipper/delivery/reject - Từ chối đơn (form: deliveryId, reason)
POST   /shipper/delivery/update - Cập nhật trạng thái (form: deliveryId, status)
POST   /shipper/delivery/complete - Hoàn thành giao hàng (form: deliveryId)
```

### Admin Panel
```
GET    /admin/dashboard         - Dashboard admin
GET    /admin/users             - Quản lý người dùng
POST   /admin/user/ban          - Khóa user (form: userId, reason)
POST   /admin/user/unban        - Mở khóa user (form: userId)
GET    /admin/products          - Quản lý sản phẩm
POST   /admin/product/approve   - Duyệt sản phẩm (form: productId)
GET    /admin/vouchers          - Quản lý voucher
GET    /admin/voucher/add       - Trang thêm voucher
POST   /admin/voucher/add       - Xử lý thêm voucher (form data)
GET    /admin/statistics        - Thống kê hệ thống
```

### Query Parameters Examples
```
# Pagination
/products?page=1&limit=20

# Filtering
/products?category=laptop&brand=dell&minPrice=10000000&maxPrice=30000000

# Sorting
/products?sortBy=price&order=asc

# Search
/search?keyword=iphone&category=phone

# Combined
/products?category=laptop&brand=asus&sortBy=price&order=desc&page=2
```

---

## 🌐 Deployment

### Deploy to Render

#### 1. Chuẩn bị
- Tạo tài khoản trên [Render](https://render.com)
- Push code lên GitHub

#### 2. Tạo Web Service
```yaml
# render.yaml
services:
  - type: web
    name: viettech
    env: java
    buildCommand: mvn clean package
    startCommand: java -jar target/viettech.war
    envVars:
      - key: DB_URL
        value: your_aiven_db_url
      - key: DB_USERNAME
        value: your_db_username
      - key: DB_PASSWORD
        value: your_db_password
```

#### 3. Environment Variables
Cấu hình trên Render Dashboard:
```
DB_URL=jdbc:mysql://your-aiven-host:port/viettech_db
DB_USERNAME=avnadmin
DB_PASSWORD=your_password
VNPAY_TMN_CODE=your_code
VNPAY_HASH_SECRET=your_secret
```

#### 4. Deploy
```bash
git push origin main
# Render tự động deploy
```

### Aiven Database Setup

#### 1. Tạo Database trên Aiven
- Truy cập [Aiven Console](https://console.aiven.io)
- Tạo MySQL service
- Chọn region gần Việt Nam (Singapore)

#### 2. Cấu hình Connection
```properties
db.url=jdbc:mysql://your-project-your-service.aivencloud.com:12345/defaultdb?ssl-mode=REQUIRED
db.username=avnadmin
db.password=generated_password
```

#### 3. Download SSL Certificate
```bash
# Download CA certificate từ Aiven Console
wget https://your-aiven-host/ca.pem -O aiven-ca.pem
```

#### 4. Import Schema
```bash
mysql --host=your-host \
      --port=12345 \
      --user=avnadmin \
      --password=your_password \
      --ssl-ca=aiven-ca.pem \
      defaultdb < database/schema.sql
```

---

## 📸 Screenshots

### Homepage
![Homepage](./docs/screenshots/homepage.png)

### Product Listing
![Products](./docs/screenshots/products.png)

### Product Detail
![Product Detail](./docs/screenshots/product-detail.png)

### Shopping Cart
![Cart](./docs/screenshots/cart.png)

### Checkout
![Checkout](./docs/screenshots/checkout.png)

### Admin Dashboard
![Admin](./docs/screenshots/admin-dashboard.png)

### Vendor Dashboard
![Vendor](./docs/screenshots/vendor-dashboard.png)

---

## 🤝 Contributing

Chúng tôi rất hoan nghênh mọi đóng góp! 

### Quy trình contribute:
1. Fork repository này
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

### Coding Standards
- Tuân thủ Java Code Conventions
- Viết unit tests cho code mới
- Comment code rõ ràng (tiếng Việt hoặc tiếng Anh)
- Update documentation khi thay đổi API

---

## 👥 Team

### Development Team
- **Nguyễn Văn A** - *Full Stack Developer* - [GitHub](https://github.com/username1)
- **Trần Thị B** - *Backend Developer* - [GitHub](https://github.com/username2)
- **Lê Văn C** - *Frontend Developer* - [GitHub](https://github.com/username3)
- **Phạm Thị D** - *Database Administrator* - [GitHub](https://github.com/username4)

### Contact
- 📧 Email: contact@viettech.vn
- 🌐 Website: https://viettech.vn
- 📱 Hotline: 1900-xxxx
- 📍 Address: 123 Nguyễn Huệ, Quận 1, TP.HCM

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- [Bootstrap](https://getbootstrap.com/) - UI Framework
- [Hibernate](https://hibernate.org/) - ORM Framework
- [HikariCP](https://github.com/brettwooldridge/HikariCP) - Connection Pool
- [Aiven](https://aiven.io/) - Database Hosting
- [Render](https://render.com/) - Web Hosting
- [Font Awesome](https://fontawesome.com/) - Icons
- [Chart.js](https://www.chartjs.org/) - Charts

---

## 📊 Project Stats

![GitHub stars](https://img.shields.io/github/stars/yourusername/viettech?style=social)
![GitHub forks](https://img.shields.io/github/forks/yourusername/viettech?style=social)
![GitHub issues](https://img.shields.io/github/issues/yourusername/viettech)
![GitHub pull requests](https://img.shields.io/github/issues-pr/yourusername/viettech)

---

<div align="center">

**⭐ Nếu project này hữu ích, hãy cho chúng tôi một star! ⭐**

Made with ❤️ in Vietnam 🇻🇳

</div>
