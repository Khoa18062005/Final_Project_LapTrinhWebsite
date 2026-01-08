# Refactoring: Contact Message Service

## Tóm tắt thay đổi

Đã tái cấu trúc code xử lý contact messages từ khách hàng bằng cách tách logic ra khỏi `AdminServlet` và đưa vào một service chuyên biệt `ContactMessageService`.

## Các file thay đổi

### 1. **ContactMessageService.java** (MỚI)
- **Vị trí**: `src/main/java/viettech/service/ContactMessageService.java`
- **Chức năng**: Service tập trung xử lý tất cả logic liên quan đến contact messages

#### Các method chính:

##### `getContactMessages(int page, int pageSize, String statusFilter, Integer userIdFilter)`
- Lấy danh sách contact messages với phân trang
- Hỗ trợ lọc theo:
  - **statusFilter**: "read" | "unread" | null (lấy tất cả)
  - **userIdFilter**: ID của user gửi tin nhắn (null để lấy từ tất cả users)
- Tự động phân tích sentiment cho mỗi tin nhắn
- Trả về `ContactMessageResult` chứa:
  - `messages`: Danh sách notification
  - `sentimentMap`: Map<notificationId, sentiment>
  - `totalMessages`: Tổng số tin nhắn
  - `unreadMessages`: Số tin nhắn chưa đọc
  - `currentPage`: Trang hiện tại
  - `totalPages`: Tổng số trang

##### `markAsRead(int notificationId)`
- Đánh dấu contact message là đã đọc
- Trả về `true` nếu thành công, `false` nếu thất bại

##### `deleteMessage(int notificationId)`
- Xóa contact message
- Trả về `true` nếu thành công, `false` nếu thất bại

##### `replyToMessage(int originalNotificationId, int userId, String replyTitle, String replyMessage)`
- Trả lời contact message
- Tự động đánh dấu tin nhắn gốc là đã đọc
- Tạo notification mới gửi đến khách hàng
- Trả về `true` nếu thành công, `false` nếu thất bại

##### `analyzeSentiments(List<Notification> messages)` (private)
- Phân tích sentiment cho danh sách messages
- Sử dụng `SentimentAnalysis` API
- Trả về Map với key là notificationId, value là sentiment ("positive", "negative", "neutral")

### 2. **AdminServlet.java** (CẬP NHẬT)
- **Vị trí**: `src/main/java/viettech/controller/AdminServlet.java`

#### Thay đổi:

##### Import mới:
```java
import viettech.service.ContactMessageService;
```

##### Khởi tạo service:
```java
private final ContactMessageService contactMessageService = new ContactMessageService();
```

##### Refactored methods:

###### `loadContactMessages(HttpServletRequest req)`
- **Trước**: 100+ dòng code với logic phức tạp
- **Sau**: 38 dòng code gọn gàng, dễ đọc
- Chỉ xử lý:
  - Parse request parameters
  - Gọi `contactMessageService.getContactMessages()`
  - Set attributes cho JSP

###### `markContactAsRead(HttpServletRequest req, HttpServletResponse resp)`
- **Trước**: Tự xử lý update notification qua DAO
- **Sau**: Gọi `contactMessageService.markAsRead()`

###### `deleteContactMessage(HttpServletRequest req, HttpServletResponse resp)`
- **Trước**: Tự xử lý delete qua DAO
- **Sau**: Gọi `contactMessageService.deleteMessage()`

###### `replyContactMessage(HttpServletRequest req, HttpServletResponse resp)`
- **Trước**: Xử lý phức tạp với nhiều bước
- **Sau**: Gọi `contactMessageService.replyToMessage()`

## Lợi ích của refactoring

### 1. **Separation of Concerns**
- Controller chỉ xử lý HTTP request/response
- Business logic được đưa vào Service layer
- Dễ dàng test từng phần riêng biệt

### 2. **Reusability**
- `ContactMessageService` có thể được sử dụng ở nhiều nơi khác nhau
- Không cần duplicate code

### 3. **Maintainability**
- Code ngắn gọn, dễ đọc hơn
- Thay đổi logic chỉ cần sửa ở một nơi (service)
- Dễ dàng mở rộng thêm tính năng

### 4. **Testability**
- Service có thể được test độc lập
- Mock dễ dàng hơn khi viết unit test

### 5. **Consistency**
- Xử lý error nhất quán
- Logging thống nhất
- Return type rõ ràng với `ContactMessageResult`

## Cách sử dụng

### Trong Controller:

```java
// Khởi tạo service
private final ContactMessageService contactMessageService = new ContactMessageService();

// Lấy tất cả contact messages (trang 1, 10 items/trang)
ContactMessageService.ContactMessageResult result = 
    contactMessageService.getContactMessages(1, 10, null, null);

// Lấy contact messages chưa đọc
ContactMessageService.ContactMessageResult result = 
    contactMessageService.getContactMessages(1, 10, "unread", null);

// Lấy contact messages từ user ID = 123
ContactMessageService.ContactMessageResult result = 
    contactMessageService.getContactMessages(1, 10, null, 123);

// Đánh dấu đã đọc
boolean success = contactMessageService.markAsRead(notificationId);

// Xóa message
boolean success = contactMessageService.deleteMessage(notificationId);

// Trả lời message
boolean success = contactMessageService.replyToMessage(
    originalNotificationId, userId, replyTitle, replyMessage);
```

### Truy xuất kết quả:

```java
ContactMessageService.ContactMessageResult result = ...;

List<Notification> messages = result.getMessages();
Map<Integer, String> sentiments = result.getSentimentMap();
long total = result.getTotalMessages();
long unread = result.getUnreadMessages();
int currentPage = result.getCurrentPage();
int totalPages = result.getTotalPages();
```

## Tương lai

Có thể mở rộng thêm:
1. Bulk operations (mark multiple as read, delete multiple)
2. Search/filter theo title, message content
3. Export contact messages to CSV/Excel
4. Statistics về contact messages
5. Auto-reply với template

## Ghi chú kỹ thuật

- Contact messages được xác định bởi `actionUrl = "/admin/contact-messages"`
- Sentiment analysis sử dụng API external (POS/NEG/NEU)
- Phân trang được xử lý ở DAO level để tối ưu performance
- Filter được áp dụng sau khi query để linh hoạt hơn (có thể tối ưu thêm bằng cách đưa filter vào query)

