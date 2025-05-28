INSERT INTO categories (category_name, description)
VALUES 
  ('Bút viết', 'Các loại bút như bút bi, bút chì, bút máy'),
  ('Giấy và sổ', 'Giấy in, giấy A4, sổ tay, sổ ghi chú'),
  ('Dụng cụ văn phòng', 'Kéo, bấm kim, đồ bấm ghim, dao rọc giấy'),
  ('Lưu trữ hồ sơ', 'Bìa hồ sơ, kẹp file, hộp tài liệu'),
  ('Văn phòng phẩm khác', 'Các loại văn phòng phẩm khác');

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
