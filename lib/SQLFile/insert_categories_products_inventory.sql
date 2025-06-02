INSERT INTO categories (category_name, description)
VALUES 
  ('Bút viết', 'Các loại bút như bút bi, bút chì, bút máy'),
  ('Giấy và sổ', 'Giấy in, giấy A4, sổ tay, sổ ghi chú'),
  ('Dụng cụ văn phòng', 'Kéo, bấm kim, đồ bấm ghim, dao rọc giấy'),
  ('Lưu trữ hồ sơ', 'Bìa hồ sơ, kẹp file, hộp tài liệu'),
  ('Văn phòng phẩm khác', 'Các loại văn phòng phẩm khác');
  ('Máy tính và phụ kiện', 'Máy tính bỏ túi, pin, sạc điện thoại'),
  ('Dụng cụ học tập', 'Thước kẻ, com-pa, bảng nhỏ, bút dạ quang'),
  ('Băng keo và keo dán', 'Băng keo trong, băng keo nâu, keo dán'),
  ('Nhãn và tem', 'Nhãn dán, tem thư, nhãn in'),
  ('Dụng cụ nghệ thuật', 'Bút màu, cọ vẽ, giấy vẽ, màu nước');

INSERT INTO products (product_name, description, price, category_id, image_url)
VALUES
  ('Bút bi Thiên Long TL-027', 'Bút bi mực xanh, trơn mượt', 3.500, 1, NULL),
  ('Bút chì gỗ HB', 'Bút chì gỗ chuốt truyền thống', 2.000, 1, NULL),
  ('Giấy A4 Double A 70gsm', '500 tờ/ream, dùng cho in ấn', 65.000, 2, NULL),
  ('Sổ tay lò xo A5', '80 trang, giấy trắng mịn', 18.000, 2, NULL),
  ('Kéo văn phòng 17cm', 'Lưỡi thép không gỉ, tay cầm nhựa', 12.000, 3, NULL),
  ('Bấm kim số 10', 'Dùng cho kim bấm số 10, bấm được 12 tờ', 25.000, 3, NULL),
  ('Bìa còng A4 2 lỗ', 'Chất liệu nhựa PP, có kẹp cứng', 19.000, 4, NULL),
  ('Kẹp giấy 32mm', 'Hộp 12 cái, kẹp chặt tài liệu', 9.000, 4, NULL),
  ('Dao rọc giấy nhỏ', 'Dùng thay lưỡi, tiện lợi và gọn nhẹ', 7.000, 3, NULL);

  -- Thêm bút viết (category_id = 1)
  ('Bút bi Pilot BPS-GP', 'Bút bi mực đen, grip cao su', 8.500, 1, NULL),
  ('Bút chì kim 0.5mm', 'Bút chì kim tự động Pentel', 15.000, 1, NULL),
  ('Bút máy Lamy Safari', 'Bút máy cao cấp, mực xanh', 450.000, 1, NULL),
  ('Bút gel UNI-ball', 'Mực gel mượt, không lem', 12.000, 1, NULL),

  -- Thêm giấy và sổ (category_id = 2)
  ('Giấy note 3M Post-it', 'Giấy note dán 76x76mm, vàng', 28.000, 2, NULL),
  ('Sổ lò xo A4 200 trang', 'Sổ ghi chú khổ lớn, bìa cứng', 35.000, 2, NULL),
  ('Giấy photocopy A3', '500 tờ/ream, 80gsm', 85.000, 2, NULL),
  ('Sổ nhật ký bìa da', 'Sổ tay cao cấp 120 trang', 65.000, 2, NULL),

  -- Thêm dụng cụ văn phòng (category_id = 3)
  ('Đồ bấm ghim lớn', 'Bấm được 25 tờ giấy', 45.000, 3, NULL),
  ('Máy cắt băng keo', 'Tặng kèm 1 cuộn băng keo', 22.000, 3, NULL),
  ('Thước nhựa 30cm', 'Thước trong suốt có chia độ', 5.000, 3, NULL),
  ('Gọt bút chì điện', 'Chạy pin AA, gọt nhanh', 85.000, 3, NULL),

  -- Thêm lưu trữ hồ sơ (category_id = 4)
  ('Hộp lưu trữ carton', 'Hộp đựng tài liệu 32x25x10cm', 35.000, 4, NULL),
  ('Bìa nhựa L A4', 'Bìa trong suốt, mở chữ L', 3.500, 4, NULL),
  ('Kẹp bướm 19mm', 'Hộp 12 cái, kim loại đen', 15.000, 4, NULL),
  ('Tủ hồ sơ 4 ngăn', 'Tủ nhựa để bàn, 4 ngăn kéo', 180.000, 4, NULL),

  -- Máy tính và phụ kiện (category_id = 6)
  ('Máy tính Casio FX-580VN', 'Máy tính học sinh cấp 3', 285.000, 6, NULL),
  ('Pin AA Panasonic', 'Vỉ 4 viên, thời gian sử dụng lâu', 32.000, 6, NULL),
  ('Cáp sạc USB-C', 'Dây sạc điện thoại 1m', 25.000, 6, NULL),
  ('Máy tính để bàn Citizen', 'Màn hình lớn 12 chữ số', 195.000, 6, NULL),

  -- Dụng cụ học tập (category_id = 7)
  ('Thước đo góc 180°', 'Thước nhựa trong suốt', 8.000, 7, NULL),
  ('Com-pa vẽ hình', 'Com-pa kim loại có bút chì', 28.000, 7, NULL),
  ('Bút dạ quang vàng', 'Bút highlight màu vàng', 9.500, 7, NULL),
  ('Bảng viết nhỏ A4', 'Bảng trắng có bút xóa', 45.000, 7, NULL),

  -- Băng keo và keo dán (category_id = 8)
  ('Băng keo trong 2cm', 'Cuộn 50m, trong suốt', 12.000, 8, NULL),
  ('Băng keo nâu 5cm', 'Cuộn 100m, dán thùng carton', 25.000, 8, NULL),
  ('Keo dán UHU stick', 'Keo dán khô 21g', 18.000, 8, NULL),
  ('Băng keo 2 mặt', 'Cuộn 3m, dày 1mm', 15.000, 8, NULL),

  -- Nhãn và tem (category_id = 9)
  ('Nhãn in A4 105x37mm', 'Giấy decal 16 nhãn/tờ', 65.000, 9, NULL),
  ('Tem thư 5000đ', 'Tem bưu chính Việt Nam', 5.000, 9, NULL),
  ('Nhãn tròn đỏ 19mm', 'Cuộn 1000 tem tròn', 28.000, 9, NULL),
  ('Nhãn số thứ tự', 'Cuộn 1000 số từ 1-1000', 35.000, 9, NULL),

  -- Dụng cụ nghệ thuật (category_id = 10)
  ('Bút màu 12 màu Thiên Long', 'Hộp bút màu cho trẻ em', 45.000, 10, NULL),
  ('Cọ vẽ số 6', 'Cọ lông tự nhiên cán gỗ', 18.000, 10, NULL),
  ('Giấy vẽ A3', 'Giấy dày 150gsm, 50 tờ', 55.000, 10, NULL),
  ('Màu nước 18 màu', 'Hộp màu nước Thiên Long', 85.000, 10, NULL);

INSERT INTO inventory (product_id, quantity_on_hand, last_stocked_date)
VALUES
  (1, 200, '2025-05-01'),
  (2, 150, '2025-05-01'),
  (3, 100, '2025-05-03'),
  (4, 80, '2025-05-04'),
  (5, 50, '2025-05-02'),
  (6, 40, '2025-05-02'),
  (7, 60, '2025-05-05'),
  (8, 90, '2025-05-06'),
  (9, 70, '2025-05-07');
  (10, 120, '2025-05-08'),  -- Bút bi Pilot
  (11, 85, '2025-05-09'),   -- Bút chì kim
  (12, 25, '2025-05-10'),   -- Bút máy Lamy
  (13, 95, '2025-05-08'),   -- Bút gel UNI-ball
  (14, 150, '2025-05-11'),  -- Giấy note 3M
  (15, 40, '2025-05-09'),   -- Sổ lò xo A4
  (16, 60, '2025-05-12'),   -- Giấy A3
  (17, 35, '2025-05-10'),   -- Sổ nhật ký
  (18, 30, '2025-05-11'),   -- Đồ bấm ghim lớn
  (19, 45, '2025-05-12'),   -- Máy cắt băng keo
  (20, 200, '2025-05-08'),  -- Thước 30cm
  (21, 20, '2025-05-13'),   -- Gọt bút chì điện
  (22, 25, '2025-05-09'),   -- Hộp lưu trữ
  (23, 300, '2025-05-14'),  -- Bìa nhựa L
  (24, 100, '2025-05-10'),  -- Kẹp bướm
  (25, 15, '2025-05-11'),   -- Tủ hồ sơ
  (26, 50, '2025-05-12'),   -- Máy tính Casio
  (27, 80, '2025-05-13'),   -- Pin AA
  (28, 65, '2025-05-14'),   -- Cáp sạc USB-C
  (29, 30, '2025-05-15'),   -- Máy tính để bàn
  (30, 75, '2025-05-12'),   -- Thước đo góc
  (31, 40, '2025-05-13'),   -- Com-pa
  (32, 110, '2025-05-14'),  -- Bút dạ quang
  (33, 35, '2025-05-15'),   -- Bảng viết nhỏ
  (34, 90, '2025-05-13'),   -- Băng keo trong
  (35, 55, '2025-05-14'),   -- Băng keo nâu
  (36, 70, '2025-05-15'),   -- Keo UHU
  (37, 85, '2025-05-16'),   -- Băng keo 2 mặt
  (38, 45, '2025-05-14'),   -- Nhãn in A4
  (39, 200, '2025-05-15'),  -- Tem thư
  (40, 60, '2025-05-16'),   -- Nhãn tròn đỏ
  (41, 40, '2025-05-17');   -- Nhãn số thứ tự