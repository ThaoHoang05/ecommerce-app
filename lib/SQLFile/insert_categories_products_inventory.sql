INSERT INTO categories (category_id, category_name, description)
VALUES
    (1, 'Bút viết', 'Các loại bút như bút bi, bút chì, bút máy'),
    (2, 'Giấy và sổ', 'Giấy in, giấy A4, sổ tay, sổ ghi chú'),
    (3, 'Dụng cụ văn phòng', 'Kéo, bấm kim, đồ bấm ghim, dao rọc giấy'),
    (4, 'Lưu trữ hồ sơ', 'Bìa hồ sơ, kẹp file, hộp tài liệu'),
    (5, 'Văn phòng phẩm khác', 'Các loại văn phòng phẩm khác'),
    (6, 'Máy tính và phụ kiện', 'Máy tính bỏ túi, pin, sạc điện thoại'),
    (7, 'Dụng cụ học tập', 'Thước kẻ, com-pa, bảng nhỏ, bút dạ quang'),
    (8, 'Băng keo và keo dán', 'Băng keo trong, băng keo nâu, keo dán'),
    (9, 'Nhãn và tem', 'Nhãn dán, tem thư, nhãn in'),
    (10, 'Dụng cụ nghệ thuật', 'Bút màu, cọ vẽ, giấy vẽ, màu nước');

INSERT INTO products (product_id, product_name, description, price, category_id, image_url)
VALUES
    (1, 'Bút bi Thiên Long TL-027', 'Bút bi mực xanh, trơn mượt', 3.500, 1, NULL),
    (2, 'Bút chì gỗ HB', 'Bút chì gỗ chuốt truyền thống', 2.000, 1, NULL),
    (3, 'Giấy A4 Double A 70gsm', '500 tờ/ream, dùng cho in ấn', 65.000, 2, NULL),
    (4, 'Sổ tay lò xo A5', '80 trang, giấy trắng mịn', 18.000, 2, NULL),
    (5, 'Kéo văn phòng 17cm', 'Lưỡi thép không gỉ, tay cầm nhựa', 12.000, 3, NULL),
    (6, 'Bấm kim số 10', 'Dùng cho kim bấm số 10, bấm được 12 tờ', 25.000, 3, NULL),
    (7, 'Bìa còng A4 2 lỗ', 'Chất liệu nhựa PP, có kẹp cứng', 19.000, 4, NULL),
    (8, 'Kẹp giấy 32mm', 'Hộp 12 cái, kẹp chặt tài liệu', 9.000, 4, NULL),
    (9, 'Dao rọc giấy nhỏ', 'Dùng thay lưỡi, tiện lợi và gọn nhẹ', 7.000, 3, NULL),
    -- Thêm bút viết (category_id = 1)
    (10, 'Bút bi Pilot BPS-GP', 'Bút bi mực đen, grip cao su', 8.500, 1, NULL),
    (11, 'Bút chì kim 0.5mm', 'Bút chì kim tự động Pentel', 15.000, 1, NULL),
    (12, 'Bút máy Lamy Safari', 'Bút máy cao cấp, mực xanh', 450.000, 1, NULL),
    (13, 'Bút gel UNI-ball', 'Mực gel mượt, không lem', 12.000, 1, NULL),

    -- Thêm giấy và sổ (category_id = 2)
    (14, 'Giấy note 3M Post-it', 'Giấy note dán 76x76mm, vàng', 28.000, 2, NULL),
    (15, 'Sổ lò xo A4 200 trang', 'Sổ ghi chú khổ lớn, bìa cứng', 35.000, 2, NULL),
    (16, 'Giấy photocopy A3', '500 tờ/ream, 80gsm', 85.000, 2, NULL),
    (17, 'Sổ nhật ký bìa da', 'Sổ tay cao cấp 120 trang', 65.000, 2, NULL),

    -- Thêm dụng cụ văn phòng (category_id = 3)
    (18, 'Đồ bấm ghim lớn', 'Bấm được 25 tờ giấy', 45.000, 3, NULL),
    (19, 'Máy cắt băng keo', 'Tặng kèm 1 cuộn băng keo', 22.000, 3, NULL),
    (20, 'Thước nhựa 30cm', 'Thước trong suốt có chia độ', 5.000, 3, NULL),
    (21, 'Gọt bút chì điện', 'Chạy pin AA, gọt nhanh', 85.000, 3, NULL),

    -- Thêm lưu trữ hồ sơ (category_id = 4)
    (22, 'Hộp lưu trữ carton', 'Hộp đựng tài liệu 32x25x10cm', 35.000, 4, NULL),
    (23, 'Bìa nhựa L A4', 'Bìa trong suốt, mở chữ L', 3.500, 4, NULL),
    (24, 'Kẹp bướm 19mm', 'Hộp 12 cái, kim loại đen', 15.000, 4, NULL),
    (25, 'Tủ hồ sơ 4 ngăn', 'Tủ nhựa để bàn, 4 ngăn kéo', 180.000, 4, NULL),

    -- Máy tính và phụ kiện (category_id = 6)
    (26, 'Máy tính Casio FX-580VN', 'Máy tính học sinh cấp 3', 285.000, 6, NULL),
    (27, 'Pin AA Panasonic', 'Vỉ 4 viên, thời gian sử dụng lâu', 32.000, 6, NULL),
    (28, 'Cáp sạc USB-C', 'Dây sạc điện thoại 1m', 25.000, 6, NULL),
    (29, 'Máy tính để bàn Citizen', 'Màn hình lớn 12 chữ số', 195.000, 6, NULL),

    -- Dụng cụ học tập (category_id = 7)
    (30, 'Thước đo góc 180°', 'Thước nhựa trong suốt', 8.000, 7, NULL),
    (31, 'Com-pa vẽ hình', 'Com-pa kim loại có bút chì', 28.000, 7, NULL),
    (32, 'Bút dạ quang vàng', 'Bút highlight màu vàng', 9.500, 7, NULL),
    (33, 'Bảng viết nhỏ A4', 'Bảng trắng có bút xóa', 45.000, 7, NULL),

    -- Băng keo và keo dán (category_id = 8)
    (34, 'Băng keo trong 2cm', 'Cuộn 50m, trong suốt', 12.000, 8, NULL),
    (35, 'Băng keo nâu 5cm', 'Cuộn 100m, dán thùng carton', 25.000, 8, NULL),
    (36, 'Keo dán UHU stick', 'Keo dán khô 21g', 18.000, 8, NULL),
    (37, 'Băng keo 2 mặt', 'Cuộn 3m, dày 1mm', 15.000, 8, NULL),

    -- Nhãn và tem (category_id = 9)
    (38, 'Nhãn in A4 105x37mm', 'Giấy decal 16 nhãn/tờ', 65.000, 9, NULL),
    (39, 'Tem thư 5000đ', 'Tem bưu chính Việt Nam', 5.000, 9, NULL),
    (40, 'Nhãn tròn đỏ 19mm', 'Cuộn 1000 tem tròn', 28.000, 9, NULL),
    (41, 'Nhãn số thứ tự', 'Cuộn 1000 số từ 1-1000', 35.000, 9, NULL),

    -- Dụng cụ nghệ thuật (category_id = 10)
    (42, 'Bút màu 12 màu Thiên Long', 'Hộp bút màu cho trẻ em', 45.000, 10, NULL),
    (43, 'Cọ vẽ số 6', 'Cọ lông tự nhiên cán gỗ', 18.000, 10, NULL),
    (44, 'Giấy vẽ A3', 'Giấy dày 150gsm, 50 tờ', 55.000, 10, NULL),
    (45, 'Màu nước 18 màu', 'Hộp màu nước Thiên Long', 85.000, 10, NULL),
    (46, 'Bút xóa trắng Tipp-Ex', 'Bút xóa chữ viết sai', 18.000, 5, NULL),
    (47, 'Dây thun buộc hồ sơ', 'Dây thun đen 20cm', 8.000, 5, NULL),
    (48, 'Giấy than A4', 'Giấy carbon màu xanh', 25.000, 5, NULL),
    (49, 'Hồ dán nhãn', 'Lọ hồ dán 50ml có cọ', 15.000, 5, NULL),
    (50, 'Mực dấu xanh', 'Lọ mực cho con dấu 30ml', 22.000, 5, NULL);

INSERT INTO inventory (inventory_id, product_id, quantity_on_hand, last_stocked_date)
VALUES
    (1, 1, 200, '2025-05-01'),
    (2, 2, 150, '2025-05-01'),
    (3, 3, 100, '2025-05-03'),
    (4, 4, 80, '2025-05-04'),
    (5, 5, 50, '2025-05-02'),
    (6, 6, 40, '2025-05-02'),
    (7, 7, 60, '2025-05-05'),
    (8, 8, 90, '2025-05-06'),
    (9, 9, 70, '2025-05-07'),
-- Sản phẩm từ ID 10-41 (32 sản phẩm mới)
    (10, 10, 120, '2025-05-08'),  -- Bút bi Pilot
    (11, 11, 85, '2025-05-09'),   -- Bút chì kim
    (12, 12, 25, '2025-05-10'),   -- Bút máy Lamy
    (13, 13, 95, '2025-05-08'),   -- Bút gel UNI-ball
    (14, 14, 150, '2025-05-11'),  -- Giấy note 3M
    (15, 15, 40, '2025-05-09'),   -- Sổ lò xo A4
    (16, 16, 60, '2025-05-12'),   -- Giấy A3
    (17, 17, 35, '2025-05-10'),   -- Sổ nhật ký
    (18, 18, 30, '2025-05-11'),   -- Đồ bấm ghim lớn
    (19, 19, 45, '2025-05-12'),   -- Máy cắt băng keo
    (20, 20, 200, '2025-05-08'),  -- Thước 30cm
    (21, 21, 20, '2025-05-13'),   -- Gọt bút chì điện
    (22, 22, 25, '2025-05-09'),   -- Hộp lưu trữ
    (23, 23, 300, '2025-05-14'),  -- Bìa nhựa L
    (24, 24, 100, '2025-05-10'),  -- Kẹp bướm
    (25, 25, 15, '2025-05-11'),   -- Tủ hồ sơ
    (26, 26, 50, '2025-05-12'),   -- Máy tính Casio
    (27, 27, 80, '2025-05-13'),   -- Pin AA
    (28, 28, 65, '2025-05-14'),   -- Cáp sạc USB-C
    (29, 29, 30, '2025-05-15'),   -- Máy tính để bàn
    (30, 30, 75, '2025-05-12'),   -- Thước đo góc
    (31, 31, 40, '2025-05-13'),   -- Com-pa
    (32, 32, 110, '2025-05-14'),  -- Bút dạ quang
    (33, 33, 35, '2025-05-15'),   -- Bảng viết nhỏ
    (34, 34, 90, '2025-05-13'),   -- Băng keo trong
    (35, 35, 55, '2025-05-14'),   -- Băng keo nâu
    (36, 36, 70, '2025-05-15'),   -- Keo UHU
    (37, 37, 85, '2025-05-16'),   -- Băng keo 2 mặt
    (38, 38, 45, '2025-05-14'),   -- Nhãn in A4
    (39, 39, 200, '2025-05-15'),  -- Tem thư
    (40, 40, 60, '2025-05-16'),   -- Nhãn tròn đỏ
    (41, 41, 40, '2025-05-17'),   -- Nhãn số thứ tự
    (42, 42, 80, '2025-05-21'),   -- Bút màu 12 màu Thiên Long
    (43, 43, 35, '2025-05-22'),   -- Cọ vẽ số 6
    (44, 44, 60, '2025-05-23'),   -- Giấy vẽ A3
    (45, 45, 45, '2025-05-21'),   -- Màu nước 18 màu
    (46, 46, 75, '2025-05-18'),   -- Bút xóa trắng
    (47, 47, 180, '2025-05-20'),  -- Dây thun buộc
    (48, 48, 45, '2025-05-18'),   -- Giấy than A4
    (49, 49, 65, '2025-05-19'),   -- Hồ dán nhãn
    (50, 50, 40, '2025-05-20');   -- Mực dấu xanh
