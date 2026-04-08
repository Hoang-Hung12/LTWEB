# Báo cáo kiểm thử API và bộ dữ liệu đề xuất

## 1. Tóm tắt đã sửa
- Cập nhật logic xóa `khách hàng` để chỉ chặn khi còn "đơn đặt sân đang hoạt động".
- Cập nhật logic xóa `sân` để chỉ chặn khi còn "đơn đặt sân đang hoạt động" và cho phép xóa khi chỉ còn đơn đã hoàn thành / đã hủy.
- Hiển thị `Mã khách` trong giao diện `Quản lý khách hàng` để admin có thể xóa đúng theo API `/api/taikhoan/{maTK}`.
- Thống nhất ảnh sân bằng cách dùng dữ liệu ảnh có sẵn của API `AnhSanController` thay vì nhập URL bên ngoài.
- Thêm endpoint mới: `GET /api/anhsan` (trả về tất cả ảnh sân có sẵn).

## 2. API chính cần test

### 2.1 Khách hàng
- `GET /api/taikhoan` — lấy tất cả khách.
- `GET /api/taikhoan/{maTK}` — lấy thông tin khách theo mã.
- `POST /api/taikhoan/register` — tạo khách mới.
- `PUT /api/taikhoan/{maTK}` — cập nhật thông tin khách.
- `DELETE /api/taikhoan/{maTK}` — xóa khách.

### 2.2 Sân
- `GET /api/san` — lấy tất cả sân.
- `GET /api/san/{maSan}` — lấy sân theo mã.
- `POST /api/san` — tạo sân mới.
- `PUT /api/san/{maSan}` — cập nhật sân.
- `DELETE /api/san/{maSan}` — xóa sân.

### 2.3 Ảnh sân
- `GET /api/anhsan` — lấy danh sách ảnh sân có sẵn.
- `GET /api/anhsan/san/{maSan}` — lấy ảnh cho 1 sân cụ thể.

### 2.4 Đơn đặt sân (để kiểm tra trạng thái)
- `GET /api/dondatsan` — lấy tất cả đơn đặt sân.
- `GET /api/dondatsan/lich-su/{maKH}` — lịch sử đặt sân của khách.
- `PATCH /api/dondatsan/{maDon}/trang-thai?trangThai={giatri}` — cập nhật trạng thái đơn.
- `PATCH /api/dondatsan/{maDon}/xac-nhan-thanh-toan?maAdmin={maAdmin}` — xác nhận thanh toán.

## 3. Bộ dữ liệu đề xuất dễ thao tác
Dùng các dữ liệu này giúp kiểm tra rõ ràng luồng xóa và báo cáo.

### 3.1 Khách hàng
- `TK1001` — Nguyễn Văn A — `0912000111` — `dung@gmail.com`
- `TK1002` — Phạm Thị Dung — `0912000222` — `dung2@gmail.com`

### 3.2 Sân
- `SAN001` — Sân Mini 5 - Khu B — loại `LS01` — đang trống/EMPTY
- `SAN002` — Sân B1 - Tiện ích — loại `LS02` — đang trống/EMPTY

### 3.3 Đơn đặt sân mẫu
- `ORD001` — khách `TK1002`, sân `SAN001`, trạng thái `Đã hoàn thành`
- `ORD002` — khách `TK1002`, sân `SAN002`, trạng thái `Đã hủy`
- `ORD003` — khách `TK1001`, sân `SAN001`, trạng thái `Đã xác nhận`

> Mục tiêu: dùng `TK1002` và `ORD001` / `ORD002` để kiểm chứng xóa khách được phép khi chỉ còn đơn đã hoàn thành / đã hủy.

## 4. Kịch bản test chi tiết

### 4.1 Kiểm tra xóa khách hàng
1. Bước 1: `GET /api/taikhoan` — lấy danh sách khách và xác nhận mã khách tồn tại.
2. Bước 2: `GET /api/dondatsan/lich-su/{maTK}` với `maTK=TK1002`.
   - Nếu chỉ còn trạng thái `Đã hoàn thành` hoặc `Đã hủy`, xóa phải thành công.
   - Nếu còn trạng thái `Chờ duyệt`, `Đã xác nhận`, `Chờ xác nhận thanh toán`, xóa phải báo lỗi.
3. Bước 3: `DELETE /api/taikhoan/TK1002`.
   - Kết quả mong muốn: thành công nếu chỉ có đơn đã hoàn thành / đã hủy.
   - Nếu không, kết quả mong muốn: lỗi rõ ràng nói rằng còn đơn đặt đang hoạt động.

### 4.2 Kiểm tra xóa sân
1. Bước 1: `GET /api/san` — xác nhận mã sân.
2. Bước 2: `GET /api/dondatsan/gio-da-dat/{maSan}?ngayDa=YYYY-MM-DD` hoặc `GET /api/dondatsan` để xem đơn liên quan.
3. Bước 3: `DELETE /api/san/SAN001`.
   - Kết quả mong muốn: thành công nếu chỉ có đơn đã hoàn thành / đã hủy.
   - Nếu còn đơn đặt còn mở, lỗi phải chỉ rõ là phải hoàn thành hoặc hủy các đơn đó trước.

### 4.3 Kiểm tra ảnh sân
1. Bước 1: `GET /api/anhsan` — nhận danh sách ảnh có sẵn.
2. Bước 2: `GET /api/anhsan/san/{maSan}` — kiểm tra ảnh gắn với sân.
3. Bước 3: Vào `quan-ly-san.html`, mở form thêm hoặc sửa sân.
   - `ẢNH CHÍNH` phải chọn từ danh sách ảnh có sẵn.
   - Không còn trường nhập URL tự do.

## 5. Hướng dẫn test bằng Swagger hoặc Postman
### 5.1 Ví dụ request `DELETE /api/taikhoan/TK1002`
- URL: `http://localhost:8080/api/taikhoan/TK1002`
- Method: `DELETE`

### 5.2 Ví dụ request `DELETE /api/san/SAN001`
- URL: `http://localhost:8080/api/san/SAN001`
- Method: `DELETE`

### 5.3 Ví dụ request `GET /api/anhsan`
- URL: `http://localhost:8080/api/anhsan`
- Method: `GET`

### 5.4 Ví dụ request `PATCH /api/dondatsan/ORD003/trang-thai?trangThai=Đã hoàn thành`
- URL: `http://localhost:8080/api/dondatsan/ORD003/trang-thai?trangThai=Đã hoàn thành`
- Method: `PATCH`

## 6. Ghi chú quan trọng
- Luồng xóa bây giờ chỉ cấm khi có đơn chưa về trạng thái cuối cùng.
- `Đã hoàn thành` và `Đã hủy` được coi là trạng thái terminal, cho phép xóa khách hoặc xóa sân nếu không còn đơn mở.
- `Mã khách` giờ đã hiển thị trên giao diện admin để việc lấy mã cho API trở nên rõ ràng.
- Ảnh sân giờ phải được chọn từ dữ liệu `GET /api/anhsan`, không còn nhập URL bên ngoài.
