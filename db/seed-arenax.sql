-- ARENAX — Xóa dữ liệu cũ và nạp bộ mẫu (chạy trên MySQL, database demoweb hoặc tên DB của bạn)
-- Ảnh sân: đặt file san1.jpg … san10.jpg vào: src/main/resources/static/images/san/
-- Cột KhuyenMai khớp entity (không có TenKM / NgayBatDau trong code)

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM HoaDon;
DELETE FROM DonDat_DichVu;
DELETE FROM DonDatSan;
DELETE FROM AnhSan;
DELETE FROM San;
DELETE FROM LoaiSan;
DELETE FROM TaiKhoan;
DELETE FROM KhuyenMai;
DELETE FROM DichVu;

SET FOREIGN_KEY_CHECKS = 1;

INSERT IGNORE INTO LoaiSan VALUES
('LS01', 'Sân 5 người',  5),
('LS02', 'Sân 7 người',  7),
('LS03', 'Sân 11 người', 11);

INSERT IGNORE INTO TaiKhoan (MaTK, HoTen, Email, SDT, MatKhau, VaiTro, DiemTichLuy, HangThanhVien) VALUES
('TK001', 'Admin',       'admin@arenax.vn', '0900000000', 'admin123', 'ADMIN', 0,    'Vàng'),
('TK002', 'Nguyễn An',   'an@gmail.com',    '0912333444', '123456',   'USER',  350,  'Đồng'),
('TK003', 'Trần Bình',   'binh@gmail.com',  '0988777666', '123456',   'USER',  810,  'Bạc'),
('TK004', 'Lê Cường',    'cuong@gmail.com', '0977123456', '123456',   'USER',  1500, 'Vàng'),
('TK005', 'Phạm Dung',   'dung@gmail.com',  '0912000111', '123456',   'USER',  200,  'Đồng'),
('TK006', 'Hoàng Đức',   'duc@gmail.com',   '0966111222', '123456',   'USER',  0,    'Đồng'),
('TK007', 'Vũ Hoa',      'hoa@gmail.com',   '0911222333', '123456',   'USER',  650,  'Bạc'),
('TK008', 'Đặng Khoa',   'khoa@gmail.com',  '0999888777', '123456',   'USER',  100,  'Đồng'),
('TK009', 'Bùi Lan',     'lan@gmail.com',   '0988123456', '123456',   'USER',  450,  'Đồng'),
('TK010', 'Trịnh Mạnh',  'manh@gmail.com',  '0912999888', '123456',   'USER',  220,  'Đồng'),
('TK011', 'Ngô Nhung',   'nhung@gmail.com', '0966555444', '123456',   'USER',  0,    'Đồng'),
('TK012', 'Tạ Phong',    'phong@gmail.com', '0977456789', '123456',   'USER',  880,  'Bạc');

INSERT IGNORE INTO San VALUES
('SAN001','Sân A1','Cầu Giấy, Hà Nội',    'Sân 5 người cỏ nhân tạo.', 'Đèn LED, Wifi, Gửi xe', 'san1.jpg',  300000,'EMPTY','LS01'),
('SAN002','Sân A2','Quận 9, TP.HCM',      'Sân 7 người cỏ nhân tạo.', 'Đèn LED, Wifi, Gửi xe', 'san2.jpg',  450000,'EMPTY','LS02'),
('SAN003','Sân B1','Thủ Đức, TP.HCM',     'Sân 11 người tiêu chuẩn.', 'Wifi, Gửi xe, Khán đài','san3.jpg',  700000,'EMPTY','LS03'),
('SAN004','Sân B2','Bình Thạnh, TP.HCM',  'Sân 5 người có mái che.',  'Mái che, Wifi, Gửi xe', 'san4.jpg',  250000,'EMPTY','LS01'),
('SAN005','Sân C1','Quận 10, TP.HCM',     'Sân 7 người trung tâm.',   'Đèn LED, Wifi, Gửi xe', 'san5.jpg',  400000,'EMPTY','LS02'),
('SAN006','Sân C2','Quận 9, TP.HCM',      'Đang bảo trì.',            'Đèn LED, Wifi',          'san6.jpg',  350000,'MAINTENANCE','LS02'),
('SAN007','Sân D1','Cầu Giấy, Hà Nội',    'Sân 7 người cao cấp.',     'Wifi, Gửi xe VIP',       'san7.jpg',  550000,'EMPTY','LS02'),
('SAN008','Sân D2','Nam Từ Liêm, Hà Nội', 'Sân 11 người chuẩn FIFA.', 'Khán đài, Wifi, Gửi xe','san8.jpg',  900000,'EMPTY','LS03'),
('SAN009','Sân E1','TP. Bắc Ninh',         'Sân 5 người giá rẻ.',      'Đèn LED, Gửi xe',        'san9.jpg',  200000,'EMPTY','LS01'),
('SAN010','Sân E2','Hải Châu, Đà Nẵng',   'Sân 7 người gần biển.',    'Đèn LED, Wifi, Gửi xe', 'san10.jpg', 380000,'EMPTY','LS02');

INSERT IGNORE INTO KhuyenMai (MaKM, MaCode, LoaiKhuyenMai, GiaTri, NgayHetHan, TrangThai) VALUES
('KM001','ARENAX10',   'PhanTram', 10,     '2026-12-31', 1),
('KM002','ARENAX50K',  'SoTien',   50000,  '2026-06-30', 1),
('KM003','NEWUSER',    'PhanTram', 15,     '2026-12-31', 1),
('KM004','SUMMER100K', 'SoTien',   100000, '2026-08-31', 1),
('KM005','OLDCODE',    'PhanTram', 20,     '2025-12-31', 0);

INSERT IGNORE INTO DonDatSan (MaDon, NgayDa, GioBatDau, GioKetThuc, TienSan, TienCoc, DiemSuDung, DiemThuong, TrangThai, MaSan, MaKH) VALUES
('ORD001','2026-04-07','17:30:00','19:00:00', 300000, 90000,  0, 30, 'Chờ duyệt',    'SAN001','TK002'),
('ORD002','2026-04-07','19:00:00','20:30:00', 450000, 135000, 0, 45, 'Đã xác nhận',  'SAN002','TK003'),
('ORD003','2026-04-08','06:00:00','07:30:00', 700000, 210000, 0, 70, 'Chờ duyệt',    'SAN003','TK004'),
('ORD004','2026-04-08','16:00:00','17:30:00', 250000, 75000,  0, 25, 'Đã xác nhận',  'SAN004','TK005'),
('ORD005','2026-04-06','17:30:00','19:00:00', 400000, 120000, 0, 40, 'Đã hoàn thành','SAN005','TK002'),
('ORD006','2026-04-06','19:00:00','20:30:00', 300000, 90000,  0, 30, 'Đã hủy',       'SAN001','TK006'),
('ORD007','2026-04-09','10:30:00','12:00:00', 550000, 165000, 0, 55, 'Chờ duyệt',    'SAN007','TK007'),
('ORD008','2026-04-09','14:30:00','16:00:00', 900000, 270000, 0, 90, 'Đã xác nhận',  'SAN008','TK004'),
('ORD009','2026-04-10','07:30:00','09:00:00', 200000, 60000,  0, 20, 'Chờ duyệt',    'SAN009','TK008'),
('ORD010','2026-04-10','17:30:00','19:00:00', 380000, 114000, 0, 38, 'Chờ duyệt',    'SAN010','TK009'),
('ORD011','2026-04-05','16:00:00','17:30:00', 450000, 135000, 0, 45, 'Đã hoàn thành','SAN002','TK003'),
('ORD012','2026-04-05','19:00:00','20:30:00', 300000, 90000,  0, 30, 'Đã hoàn thành','SAN004','TK010'),
('ORD013','2026-04-11','09:00:00','10:30:00', 550000, 165000, 0, 55, 'Chờ duyệt',    'SAN007','TK011'),
('ORD014','2026-04-11','17:30:00','19:00:00', 400000, 120000, 0, 40, 'Chờ duyệt',    'SAN005','TK012'),
('ORD015','2026-04-04','19:00:00','20:30:00', 700000, 210000, 0, 70, 'Đã hoàn thành','SAN003','TK004');

INSERT IGNORE INTO DichVu VALUES
('DV001','Nước Aquafina',  10000, 'Chai'),
('DV002','Nước Monster',   25000, 'Lon'),
('DV003','Thuê giày',      30000, 'Đôi'),
('DV004','Thuê áo',        15000, 'Bộ'),
('DV005','Bơm bóng',        5000, 'Lần');

INSERT IGNORE INTO HoaDon (MaHoaDon, MaDon, TongTienDichVu, TongThanhToan, MaQLLap) VALUES
('HD001','ORD005', 20000, 420000, 'TK001'),
('HD002','ORD011', 30000, 480000, 'TK001'),
('HD003','ORD012',  5000, 305000, 'TK001'),
('HD004','ORD015', 50000, 750000, 'TK001');

-- Ảnh chọn trong form admin (đường dẫn hiển thị trên web)
INSERT IGNORE INTO AnhSan (MaAnh, DuongDanAnh, MaSan) VALUES
('AS01','/images/san/san1.jpg','SAN001'),
('AS02','/images/san/san2.jpg','SAN002'),
('AS03','/images/san/san3.jpg','SAN003'),
('AS04','/images/san/san4.jpg','SAN004'),
('AS05','/images/san/san5.jpg','SAN005'),
('AS06','/images/san/san6.jpg','SAN006'),
('AS07','/images/san/san7.jpg','SAN007'),
('AS08','/images/san/san8.jpg','SAN008'),
('AS09','/images/san/san9.jpg','SAN009'),
('AS10','/images/san/san10.jpg','SAN010');
