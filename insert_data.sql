-- ============================================================================
-- VietTech Database - Sample Data Insert Script
-- ============================================================================
-- This script inserts sample data for all 41 tables in the viettech schema
use viettech;
-- ============================================================================
-- 1. ROLES TABLE (for user types)
-- ============================================================================
INSERT INTO roles (role_id, role_name) VALUES
(1, 'Admin'),
(2, 'Vendor'),
(3, 'Customer'),
(4, 'Shipper');

-- ============================================================================
-- 2. USERS TABLE (Base user table - parent for inheritance)
-- ============================================================================
INSERT INTO users (user_id, role_id, first_name, last_name, username, password, email, phone, avatar, date_of_birth, gender, created_at, updated_at, is_active,last_login_at) VALUES
(9, 1, 'Nguyen', 'Admin', 'admin_user', '$2a$10$abcdefg1234567890123456', 'admin@viettech.vn', '0912345678', '/avatars/admin.jpg', '1985-01-15', 'Male', NOW(), NOW(), 1, NOW()),
(10, 2, 'Tran', 'Vendor', 'vendor_001', '$2a$10$abcdefg1234567890123456', 'vendor1@viettech.vn', '0987654321', '/avatars/vendor1.jpg', '1990-03-20', 'Male', NOW(), NOW(), 1, NOW()),
(3, 2, 'Le', 'VendorTwo', 'vendor_002', '$2a$10$abcdefg1234567890123456', 'vendor2@viettech.vn', '0988888888', '/avatars/vendor2.jpg', '1992-05-10', 'Female', NOW(), NOW(), 1, NOW()),
(4, 3, 'Pham', 'Customer', 'customer_001', '$2a$10$abcdefg1234567890123456', 'customer1@viettech.vn', '0901234567', '/avatars/customer1.jpg', '1995-07-22', 'Male', NOW(), NOW(), 1, NOW()),
(5, 3, 'Hoang', 'CustomerTwo', 'customer_002', '$2a$10$abcdefg1234567890123456', 'customer2@viettech.vn', '0902345678', '/avatars/customer2.jpg', '1998-09-12', 'Female', NOW(), NOW(), 1, NOW()),
(6, 3, 'Vu', 'CustomerThree', 'customer_003', '$2a$10$abcdefg1234567890123456', 'customer3@viettech.vn', '0903456789', '/avatars/customer3.jpg', '1996-11-30', 'Male', NOW(), NOW(), 1, NOW()),
(7, 4, 'Dang', 'Shipper', 'shipper_001', '$2a$10$abcdefg1234567890123456', 'shipper1@viettech.vn', '0904567890', '/avatars/shipper1.jpg', '1993-04-18', 'Male', NOW(), NOW(), 1, NOW()),
(8, 4, 'Bui', 'ShipperTwo', 'shipper_002', '$2a$10$abcdefg1234567890123456', 'shipper2@viettech.vn', '0905678901', '/avatars/shipper2.jpg', '1994-06-25', 'Male', NOW(), NOW(), 1, NOW());

-- ============================================================================
-- 3. ADMIN TABLE
-- ============================================================================
INSERT INTO admins (user_id, access_level, department) VALUES
(1, 'Super Admin', 'Management');

-- ============================================================================
-- 4. VENDOR TABLE
-- ============================================================================
INSERT INTO vendors
(user_id, business_name, tax_id, rating, commission, is_verified, bank_account)
VALUES
    (2, 'TechStore Vietnam', 'TAX20230001', 4.8, 0.10, 1, 'VCB-0123456789'),
    (3, 'ElectroMart',       'TAX20230002', 4.5, 0.12, 1, 'ACB-9876543210');


-- ============================================================================
-- 5. CUSTOMER TABLE
INSERT INTO customers
(user_id, loyalty_points, membership_tier, total_spent)
VALUES
    (4, 5000, 'GOLD',   15000000),
    (5, 3200, 'SILVER',  8500000),
    (6, 1500, 'BRONZE',  3200000);

-- ============================================================================
-- 6. SHIPPER TABLE
-- ============================================================================
INSERT INTO shippers
(user_id, license_number, vehicle_type, vehicle_plate,
 is_available, current_latitude, current_longitude, rating, total_deliveries)
VALUES
    (7, 'DL-001-2023', 'Motorcycle', '59X1-12345',
     1, 10.8231, 106.6297, 4.9, 120),

    (8, 'DL-002-2023', 'Motorcycle', '43C1-67890',
     1, 16.0471, 108.2068, 4.7, 95);

-- ============================================================================
-- 7. CATEGORIES TABLE
-- ============================================================================
INSERT INTO categories
(category_id, name, slug, description,
 parent_category_id, icon, banner, sort_order, is_active, created_at)
VALUES
    (1, 'Smartphones', 'smartphones', 'Mobile phones and accessories',
     NULL, '/icons/smartphones.png', '/banners/smartphones.jpg', 1, 1, NOW()),

    (2, 'Laptops', 'laptops', 'Laptops and notebooks',
     NULL, '/icons/laptops.png', '/banners/laptops.jpg', 2, 1, NOW()),

    (3, 'Tablets', 'tablets', 'Tablets and iPad',
     NULL, '/icons/tablets.png', '/banners/tablets.jpg', 3, 1, NOW()),

    (4, 'Headphones', 'headphones', 'Audio devices and headphones',
     NULL, '/icons/headphones.png', '/banners/headphones.jpg', 4, 1, NOW()),

    (5, 'Accessories', 'accessories', 'Phone and computer accessories',
     NULL, '/icons/accessories.png', '/banners/accessories.jpg', 5, 1, NOW());

-- ============================================================================
-- 8. PRODUCTS TABLE (Base product - parent for inheritance)
-- ============================================================================
INSERT INTO products (
    product_id,
    vendor_id,
    category_id,
    name,
    slug,
    base_price,
    description,
    brand,
    specifications,
    status,
    weight,
    dimensions,
    created_at,
    updated_at,
    average_rating,
    total_reviews,
    total_sold,
    view_count,
    is_featured
) VALUES
-- 1
(1, 2, 1, 'iPhone 15 Pro Max', 'iphone-15-pro-max', 35000000,
 'Latest Apple flagship smartphone', 'Apple',
 'A17 Pro, 256GB, 12MP',
 'Active', 0.22, '160.9 x 77.8 x 8.25 mm',
 NOW(), NOW(), 4.8, 120, 350, 1500, 1),

-- 2
(2, 2, 1, 'Samsung Galaxy S24 Ultra', 'samsung-galaxy-s24-ultra', 32000000,
 'Premium Samsung Android phone', 'Samsung',
 'Snapdragon 8 Gen 3, 256GB, 200MP',
 'Active', 0.23, '162.8 x 79.4 x 8.6 mm',
 NOW(), NOW(), 4.7, 95, 280, 1200, 1),

-- 3
(3, 3, 2, 'MacBook Pro 16', 'macbook-pro-16', 55000000,
 'Professional Apple laptop', 'Apple',
 'M3 Max, 512GB SSD, 18GB RAM',
 'Active', 2.15, '359.7 x 251.7 x 16.12 mm',
 NOW(), NOW(), 4.9, 75, 180, 980, 1),

-- 4
(4, 3, 2, 'Dell XPS 15', 'dell-xps-15', 45000000,
 'High performance Windows laptop', 'Dell',
 'Intel i9, RTX 4090, 1TB SSD, 32GB RAM',
 'Active', 2.12, '357 x 235 x 17.3 mm',
 NOW(), NOW(), 4.6, 60, 150, 870, 0),

-- 5
(5, 2, 4, 'Sony WH-1000XM5', 'sony-wh-1000xm5', 8500000,
 'Premium noise-canceling headphones', 'Sony',
 'Active Noise Cancellation, 30hr battery',
 'Active', 0.25, '250 x 200 x 100 mm',
 NOW(), NOW(), 4.8, 200, 500, 2100, 1),

-- 6
(6, 3, 3, 'iPad Pro 12.9', 'ipad-pro-12-9', 28000000,
 'Professional tablet device', 'Apple',
 'M2 chip, 256GB, Liquid Retina display',
 'Active', 0.68, '280.6 x 214.9 x 6.4 mm',
 NOW(), NOW(), 4.7, 85, 220, 1100, 1),

-- 7
(7, 2, 1, 'Google Pixel 8 Pro', 'google-pixel-8-pro', 28000000,
 'Google flagship with advanced AI', 'Google',
 'Tensor G3, 256GB, Exceptional camera',
 'Active', 0.22, '162.6 x 72 x 8.5 mm',
 NOW(), NOW(), 4.6, 70, 160, 900, 0),

-- 8
(8, 3, 2, 'ASUS VivoBook 16', 'asus-vivobook-16', 22000000,
 'Budget-friendly powerful laptop', 'ASUS',
 'AMD Ryzen 7, RTX 4050, 512GB, 16GB RAM',
 'Active', 1.98, '357.2 x 249.2 x 18.9 mm',
 NOW(), NOW(), 4.5, 55, 130, 760, 0),

-- 9
(9, 2, 4, 'Bose QuietComfort 45', 'bose-quietcomfort-45', 7500000,
 'Professional audio headphones', 'Bose',
 'Noise Cancellation, 24hr battery',
 'Active', 0.23, '240 x 210 x 95 mm',
 NOW(), NOW(), 4.7, 180, 420, 1950, 1),

-- 10
(10, 3, 5, 'USB-C Multi-port Hub', 'usb-c-hub', 1500000,
 'Universal connectivity hub', 'Anker',
 '7-in-1 ports, 100W power delivery',
 'Active', 0.15, '150 x 80 x 50 mm',
 NOW(), NOW(), 4.4, 90, 300, 1400, 0);
-- ============================================================================
-- 9. PHONE TABLE (inherited from products)
-- ============================================================================
INSERT INTO phones (
    product_id,
    screen_size,
    screen_resolution,
    screen_type,
    refresh_rate,
    battery_capacity,
    charger_type,
    charging_speed,
    processor,
    gpu,
    rear_camera,
    front_camera,
    video_recording,
    os,
    os_version,
    sim_type,
    network_support,
    connectivity,
    nfc,
    waterproof_rating,
    dustproof_rating,
    fingerprint_sensor,
    face_recognition,
    wireless_charging,
    reverse_charging,
    audio_jack
) VALUES
-- iPhone 15 Pro Max
(1,
 6.7,
 '2796 x 1290',
 'LTPO OLED',
 120,
 4685,
 'USB-C',
 27,
 'Apple A17 Pro',
 'Apple GPU',
 '48MP + 12MP + 12MP',
 '12MP',
 '4K@60fps',
 'iOS',
 '17',
 'eSIM',
 '5G',
 'Wi-Fi 6E, Bluetooth 5.3',
 1,
 'IP68',
 'IP6X',
 0,
 1,
 1,
 0,
 0
),

-- Samsung Galaxy S24 Ultra
(2,
 6.8,
 '3120 x 1440',
 'Dynamic AMOLED 2X',
 120,
 5000,
 'USB-C',
 45,
 'Snapdragon 8 Gen 3',
 'Adreno 750',
 '200MP + 50MP + 12MP + 10MP',
 '12MP',
 '8K@30fps',
 'Android',
 '14',
 'Nano SIM + eSIM',
 '5G',
 'Wi-Fi 7, Bluetooth 5.4',
 1,
 'IP68',
 'IP6X',
 1,
 1,
 1,
 1,
 0
),

-- Google Pixel 8 Pro
(7,
 6.7,
 '2992 x 1344',
 'LTPO OLED',
 120,
 5050,
 'USB-C',
 30,
 'Google Tensor G3',
 'Mali-G715',
 '50MP + 48MP + 48MP',
 '10.5MP',
 '4K@60fps',
 'Android',
 '14',
 'Nano SIM + eSIM',
 '5G',
 'Wi-Fi 7, Bluetooth 5.3',
 1,
 'IP68',
 'IP6X',
 1,
 1,
 1,
 0,
 0
);

-- ============================================================================
-- 10. LAPTOP TABLE (inherited from products)
-- ============================================================================
INSERT INTO laptops (
    product_id,
    cpu,
    cpu_generation,
    cpu_speed,
    gpu,
    gpu_memory,
    ram,
    ram_type,
    max_ram,
    storage,
    storage_type,
    additional_slot,
    screen_size,
    screen_resolution,
    screen_type,
    refresh_rate,
    color_gamut,
    battery_capacity,
    battery_life,
    ports,
    os,
    keyboard_type,
    keyboard_backlight,
    webcam,
    speakers,
    microphone,
    touch_screen,
    fingerprint_sensor,
    discrete_gpu,
    thunderbolt
) VALUES
-- MacBook Pro 16 M3 Max
(3,
 'Apple M3 Max',
 'M3',
 4.1,
 'Apple GPU',
 0,
 18,
 'Unified',
 64,
 512,
 'SSD',
 0,
 16.0,
 '3456 x 2234',
 'Liquid Retina XDR',
 120,
 'P3',
 100,
 18,
 'Thunderbolt 4, HDMI, SDXC',
 'macOS',
 'Magic Keyboard',
 1,
 '1080p',
 '6-speaker system',
 'Studio-quality',
 0,
 1,
 0,
 1
),

-- Dell XPS 15
(4,
 'Intel Core i9',
 '13th Gen',
 5.4,
 'NVIDIA RTX 4090',
 16,
 32,
 'DDR5',
 64,
 1024,
 'SSD',
 1,
 15.6,
 '3456 x 2160',
 'OLED',
 120,
 'DCI-P3',
 86,
 5,
 'Thunderbolt 4, USB-C',
 'Windows 11',
 'Backlit Keyboard',
 1,
 '720p',
 'Stereo speakers',
 'Dual-array',
 0,
 1,
 1,
 1
),

-- ASUS VivoBook 16
(8,
 'AMD Ryzen 7 7840HS',
 'Zen 4',
 3.8,
 'NVIDIA RTX 4050',
 6,
 16,
 'DDR5',
 32,
 512,
 'SSD',
 1,
 16.0,
 '1920 x 1200',
 'IPS',
 144,
 '100% sRGB',
 70,
 10,
 'USB-C, USB-A, HDMI',
 'Windows 11',
 'Chiclet Keyboard',
 1,
 '720p',
 'Harman Kardon',
 'Built-in',
 0,
 1,
 1,
 0
);

-- ============================================================================
-- 11. TABLET TABLE (inherited from products)
-- ============================================================================
INSERT INTO tablets (
    product_id,
    screen_size,
    screen_resolution,
    screen_type,
    refresh_rate,
    battery_capacity,
    processor,
    gpu,
    rear_camera,
    front_camera,
    video_recording,
    os,
    os_version,
    sim_support,
    network_support,
    connectivity,
    stylus_support,
    stylus_included,
    keyboard_support,
    speakers,
    audio_jack,
    waterproof_rating,
    face_recognition,
    fingerprint_sensor
) VALUES
-- iPad Pro 12.9 (M2)
(6,
 12.9,
 '2732 x 2048',
 'Liquid Retina XDR',
 120,
 10308,
 'Apple M2',
 'Apple GPU',
 '12MP Wide',
 '12MP Ultra Wide',
 '4K@60fps',
 'iPadOS',
 '17',
 0,
 'Wi-Fi',
 'Wi-Fi 6E, Bluetooth 5.3',
 1,
 0,
 1,
 '4-speaker system',
 0,
 'IP68',
 1,
 0
);

-- ============================================================================
-- 12. VARIANTS TABLE
-- ============================================================================
INSERT INTO variants (
    variant_id,
    product_id,
    sku,
    base_price,
    final_price,
    weight,
    is_active,
    created_at
) VALUES
-- iPhone 15 Pro Max
(1, 1, 'SKU-IP15PM-001', 35000000, 35000000, 0.22, 1, NOW()),
(2, 1, 'SKU-IP15PM-002', 35000000, 35000000, 0.22, 1, NOW()),
(3, 1, 'SKU-IP15PM-003', 38000000, 38000000, 0.22, 1, NOW()),

-- Samsung Galaxy S24 Ultra
(4, 2, 'SKU-SGS24U-001', 32000000, 32000000, 0.23, 1, NOW()),
(5, 2, 'SKU-SGS24U-002', 32000000, 32000000, 0.23, 1, NOW()),

-- MacBook Pro 16
(6, 3, 'SKU-MBP16-001', 55000000, 55000000, 2.15, 1, NOW()),
(7, 3, 'SKU-MBP16-002', 55000000, 55000000, 2.15, 1, NOW()),

-- Dell XPS 15
(8, 4, 'SKU-XPS15-001', 45000000, 45000000, 2.12, 1, NOW()),

-- Sony WH-1000XM5
(9, 5, 'SKU-SONY-001', 8500000, 8500000, 0.25, 1, NOW()),

-- iPad Pro 12.9
(10, 6, 'SKU-IPAD-001', 28000000, 28000000, 0.68, 1, NOW());

-- ============================================================================
-- 13. VARIANT_ATTRIBUTES TABLE
-- ============================================================================
INSERT INTO variant_attributes (
    attribute_id,
    variant_id,
    attribute_name,
    attribute_value,
    price_adjustment,
    sort_order
) VALUES
      (1, 1, 'Color', 'Space Black', 0, 1),
      (2, 1, 'Storage', '256GB', 0, 2),

      (3, 2, 'Color', 'Titanium Blue', 0, 1),
      (4, 2, 'Storage', '256GB', 0, 2),

      (5, 3, 'Color', 'Titanium Gold', 3000000, 1),
      (6, 3, 'Storage', '512GB', 3000000, 2),

      (7, 4, 'Color', 'Phantom Black', 0, 1),
      (8, 4, 'Storage', '256GB', 0, 2),

      (9, 5, 'Color', 'Phantom Violet', 0, 1),
      (10, 5, 'Storage', '256GB', 0, 2),

      (11, 6, 'Color', 'Silver', 0, 1),
      (12, 6, 'Storage', '512GB', 0, 2),

      (13, 7, 'Color', 'Space Gray', 0, 1),
      (14, 7, 'Storage', '512GB', 0, 2),

      (15, 8, 'Color', 'Silver', 0, 1),
      (16, 8, 'Storage', '1TB', 0, 2),

      (17, 9, 'Color', 'Black', 0, 1),

      (18, 10, 'Color', 'Silver', 0, 1),
      (19, 10, 'Storage', '256GB', 0, 2);
-- 14.ProductImage ============================================================================
INSERT INTO product_images (
    image_id,
    product_id,
    variant_id,
    url,
    alt_text,
    sort_order,
    is_primary,
    uploaded_at
) VALUES
      (1, 1, NULL, '/images/products/iphone15pm-1.jpg', 'iPhone 15 Pro Max front', 1, 1, NOW()),
      (2, 1, NULL, '/images/products/iphone15pm-2.jpg', 'iPhone 15 Pro Max back', 2, 0, NOW()),

      (3, 2, NULL, '/images/products/galaxy-s24u-1.jpg', 'Galaxy S24 Ultra front', 1, 1, NOW()),
      (4, 2, NULL, '/images/products/galaxy-s24u-2.jpg', 'Galaxy S24 Ultra back', 2, 0, NOW()),

      (5, 3, NULL, '/images/products/macbook-pro-1.jpg', 'MacBook Pro 16 open', 1, 1, NOW()),
      (6, 3, NULL, '/images/products/macbook-pro-2.jpg', 'MacBook Pro 16 keyboard', 2, 0, NOW()),

      (7, 4, NULL, '/images/products/dell-xps-1.jpg', 'Dell XPS 15 open', 1, 1, NOW()),
      (8, 4, NULL, '/images/products/dell-xps-2.jpg', 'Dell XPS 15 side', 2, 0, NOW()),

      (9, 5, NULL, '/images/products/sony-wh-1.jpg', 'Sony WH-1000XM5', 1, 1, NOW()),

      (10, 6, NULL, '/images/products/ipad-pro-1.jpg', 'iPad Pro 12.9', 1, 1, NOW()),

      (11, 7, NULL, '/images/products/pixel-8-1.jpg', 'Google Pixel 8 Pro', 1, 1, NOW()),

      (12, 8, NULL, '/images/products/asus-vivobook-1.jpg', 'ASUS VivoBook 16', 1, 1, NOW()),

      (13, 9, NULL, '/images/products/bose-qc45-1.jpg', 'Bose QuietComfort 45', 1, 1, NOW()),

      (14, 10, NULL, '/images/products/usb-hub-1.jpg', 'USB-C Multi-port Hub', 1, 1, NOW());

-- ============================================================================
-- 15. ADDRESSES TABLE
-- ============================================================================
INSERT INTO addresses (
    address_id,
    user_id,
    type,
    recipient_name,
    phone,
    address_line,
    province,
    district,
    ward,
    latitude,
    longitude,
    is_default,
    created_at,
    updated_at
) VALUES
      (1, 4, 'HOME', 'Pham Customer', '0901234567',
       '123 Nguyen Hue Street',
       'Ho Chi Minh City', 'District 1', 'Ben Thanh Ward',
       10.775658, 106.700424,
       1, NOW(), NOW()),

      (2, 4, 'WORK', 'Pham Customer', '0901234567',
       '456 Tran Hung Dao Avenue',
       'Ho Chi Minh City', 'District 1', 'Dakao Ward',
       10.780889, 106.693321,
       0, NOW(), NOW()),

      (3, 5, 'HOME', 'Hoang Customer', '0902345678',
       '789 Pham Ngu Lao Street',
       'Ho Chi Minh City', 'District 1', 'Pham Ngu Lao Ward',
       10.767123, 106.693456,
       1, NOW(), NOW()),

      (4, 6, 'HOME', 'Vu Customer', '0903456789',
       '321 Dong Khoi Road',
       'Ho Chi Minh City', 'District 1', 'Tan Dinh Ward',
       10.787456, 106.704321,
       1, NOW(), NOW()),

      (5, 2, 'BUSINESS', 'Tran Vendor', '0987654321',
       '654 Le Duan Boulevard',
       'Ho Chi Minh City', 'Tan Binh District', 'Tan Binh Ward',
       10.801234, 106.652345,
       1, NOW(), NOW()),

      (6, 3, 'BUSINESS', 'Le Vendor', '0988888888',
       '987 Nguyen Van Troi Street',
       'Hanoi', 'Thanh Xuan District', 'Nhan Chinh Ward',
       21.001234, 105.807654,
       1, NOW(), NOW());

-- ============================================================================
-- 16. WAREHOUSES TABLE
-- ============================================================================
INSERT INTO warehouses (
    warehouse_id,
    vendor_id,
    name,
    code,
    address_line,
    ward,
    district,
    province,
    latitude,
    longitude,
    capacity,
    used_capacity,
    manager_id,
    phone,
    email,
    operating_hours,
    is_active,
    created_at
) VALUES
      (1, 2, 'Main Warehouse HCM', 'WH-HCM-001',
       '123 Nguyen Huu Canh Street', 'Tan Phong Ward', 'District 7', 'Ho Chi Minh City',
       10.746, 106.704, 100000, 45000, 7, '0901234567', 'hcm-warehouse@viettech.vn', '08:00-18:00', 1, NOW()),
      (2, 3, 'Warehouse Hanoi', 'WH-HN-001',
       '456 Tran Phu Street', 'Ba Dinh Ward', 'Ba Dinh District', 'Hanoi',
       21.033, 105.849, 80000, 38000, 8, '0912345678', 'hanoi-warehouse@viettech.vn', '08:00-18:00', 1, NOW()),
      (3, 2, 'Warehouse Da Nang', 'WH-DN-001',
       '789 Nguyen Van Linh Street', 'Hai Chau Ward', 'Hai Chau District', 'Da Nang',
       16.067, 108.214, 50000, 22000, 7, '0923456789', 'danang-warehouse@viettech.vn', '08:00-18:00', 1, NOW());

-- ============================================================================
-- 17. INVENTORY TABLE
-- ============================================================================
INSERT INTO inventories (
    inventory_id,
    warehouse_id,
    variant_id,
    stock_quantity,
    reserved_quantity,
    available_quantity,
    damaged_quantity,
    last_updated,
    last_restocked_at,
    reorder_level,
    reorder_quantity,
    location
) VALUES
      (1, 1, 1, 50, 5, 45, 0, NOW(), NOW(), 10, 20, 'A1-01'),
      (2, 1, 2, 40, 3, 37, 0, NOW(), NOW(), 10, 15, 'A1-02'),
      (3, 1, 3, 20, 2, 18, 0, NOW(), NOW(), 5, 10, 'B1-01'),
      (4, 2, 4, 15, 1, 14, 0, NOW(), NOW(), 5, 10, 'B2-01'),
      (5, 1, 5, 25, 2, 23, 0, NOW(), NOW(), 5, 10, 'C1-01'),
      (6, 2, 6, 22, 2, 20, 0, NOW(), NOW(), 5, 10, 'C2-01'),
      (7, 1, 7, 30, 4, 26, 0, NOW(), NOW(), 5, 10, 'A1-03'),
      (8, 2, 3, 18, 1, 17, 0, NOW(), NOW(), 5, 10, 'B2-02'),
      (9, 2, 8, 28, 3, 25, 0, NOW(), NOW(), 5, 10, 'B2-03'),
      (10, 1, 9, 20, 1, 19, 0, NOW(), NOW(), 5, 10, 'C1-02'),
      (11, 1, 10, 100, 10, 90, 0, NOW(), NOW(), 20, 50, 'D1-01'),
      (12, 2, 1, 35, 3, 32, 0, NOW(), NOW(), 10, 20, 'A2-01');

-- ============================================================================
-- 18. STOCK_MOVEMENTS TABLE
-- ============================================================================
INSERT INTO stock_movements (
    movement_id,
    inventory_id,
    type,
    quantity,
    from_warehouse_id,
    to_warehouse_id,
    reason,
    performed_by,
    timestamp,
    reference_id
) VALUES
      (1, 1, 'IN', 50, NULL, 1, 'Initial stock', 'System', NOW(), 'REF-001'),
      (2, 2, 'IN', 40, NULL, 1, 'Initial stock', 'System', NOW(), 'REF-002'),
      (3, 1, 'OUT', 5, 1, NULL, 'Order #ORD-001', 'Pham Customer', NOW(), 'ORD-001'),
      (4, 3, 'OUT', 2, 1, NULL, 'Order #ORD-002', 'Hoang Customer', NOW(), 'ORD-002'),
      (5, 5, 'IN', 25, NULL, 1, 'Restock', 'Vendor', NOW(), 'REF-003'),
      (6, 5, 'OUT', 2, 1, NULL, 'Order #ORD-003', 'Vu Customer', NOW(), 'ORD-003'),
      (7, 10, 'IN', 20, NULL, 1, 'Initial stock', 'System', NOW(), 'REF-004'),
      (8, 10, 'OUT', 1, 1, NULL, 'Order #ORD-004', 'Pham Customer', NOW(), 'ORD-004');

-- ============================================================================
-- 19. CARTS TABLE
-- ============================================================================
INSERT INTO carts (
    cart_id,
    customer_id,
    created_at,
    updated_at,
    expires_at
) VALUES
      (1, 4, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
      (2, 5, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
      (3, 6, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY));

-- ============================================================================
-- 20. CART_ITEMS TABLE
-- ============================================================================
INSERT INTO cart_items (
    cart_item_id,
    cart_id,
    variant_id,
    quantity,
    price_at_add,
    subtotal,
    added_at
) VALUES
      (1, 1, 1, 1, 35000000, 35000000, NOW()),
      (2, 1, 9, 1, 8500000, 8500000, NOW()),
      (3, 2, 6, 1, 55000000, 55000000, NOW()),
      (4, 3, 4, 2, 32000000, 64000000, NOW()),
      (5, 3, 10, 3, 1500000, 4500000, NOW());

-- ============================================================================
-- 21. ORDERS TABLE
-- ============================================================================
-- ============================================================================
-- 21. ORDERS TABLE
-- ============================================================================
INSERT INTO orders (
    order_id, order_number, customer_id, vendor_id, address_id, order_date, status,
    subtotal, shipping_fee, discount, tax, voucher_discount, loyalty_points_used,
    loyalty_points_discount, total_price, notes, cancel_reason, cancelled_at,
    cancelled_by, estimated_delivery, completed_at
) VALUES
      -- (1, 'ORD-2025-00001', 4, 2, 1, NOW(), 'Pending', 35000000, 50000, 0, 1400000, 0, 0, 0, 36450000, NULL, NULL, NULL, NULL, DATE_ADD(NOW(), INTERVAL 5 DAY), NULL),
      -- (2, 'ORD-2025-00002', 5, 3, 3, NOW(), 'Confirmed', 55000000, 75000, 5000000, 2000000, 0, 0, 0, 52075000, NULL, NULL, NULL, NULL, DATE_ADD(NOW(), INTERVAL 7 DAY), NULL),
      (3, 'ORD-2025-00003', 6, 2, 4, NOW(), 'Shipping', 64000000, 50000, 0, 2560000, 500000, 1000, 0, 65610000, NULL, NULL, NULL, NULL, DATE_ADD(NOW(), INTERVAL 3 DAY), NULL),
      (4, 'ORD-2025-00004', 4, 3, 1, DATE_SUB(NOW(), INTERVAL 5 DAY), 'Delivered', 28000000, 50000, 0, 1120000, 0, 0, 0, 29170000, NULL, NULL, NULL, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
      (5, 'ORD-2025-00005', 5, 2, 3, DATE_SUB(NOW(), INTERVAL 10 DAY), 'Delivered', 8500000, 50000, 0, 340000, 0, 500, 0, 8890500, NULL, NULL, NULL, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
      (6, 'ORD-2025-00006', 6, 3, 4, NOW(), 'Processing', 1500000, 30000, 0, 60000, 0, 0, 0, 1590000, NULL, NULL, NULL, NULL, DATE_ADD(NOW(), INTERVAL 3 DAY), NULL);

-- ============================================================================
-- 22. ORDER_DETAILS TABLE
-- ============================================================================
INSERT INTO order_details (
    order_detail_id, order_id, product_id, variant_id, product_name, variant_info,
    quantity, unit_price, discount, subtotal, status
) VALUES
      (1, 1, 1, 1, 'iPhone 15 Pro Max', 'Space Black, 256GB, 8GB RAM', 1, 35000000, 0, 35000000, 'Pending'),
      (2, 2, 3, 6, 'MacBook Pro 16', 'Silver, 512GB, 18GB RAM', 1, 55000000, 0, 55000000, 'Confirmed'),
      (3, 3, 2, 4, 'Samsung Galaxy S24 Ultra', 'Phantom Black, 256GB, 12GB RAM', 2, 32000000, 0, 64000000, 'Shipping'),
      (4, 4, 6, 10, 'iPad Pro 12.9', 'Silver, 256GB, 8GB RAM', 1, 28000000, 0, 28000000, 'Delivered'),
      (5, 5, 5, 9, 'Sony WH-1000XM5', 'Black', 1, 8500000, 0, 8500000, 'Delivered'),
      (6, 6, 10, 1, 'USB-C Multi-port Hub', NULL, 3, 1500000, 0, 4500000, 'Processing');

-- ============================================================================
-- 23. ORDER_STATUS TABLE
-- ============================================================================
INSERT INTO order_statuses (
    status_id, order_id, status, timestamp, note
) VALUES
      (1, 1, 'Pending', NOW(), 'Order received'),
      (2, 2, 'Confirmed', NOW(), 'Vendor confirmed'),
      (3, 3, 'Shipping', NOW(), 'Out for delivery'),
      (4, 4, 'Delivered', DATE_SUB(NOW(), INTERVAL 2 DAY), 'Delivered to customer'),
      (5, 5, 'Delivered', DATE_SUB(NOW(), INTERVAL 5 DAY), 'Delivered to customer'),
      (6, 6, 'Processing', NOW(), 'Being prepared');

-- ============================================================================
-- 24. PAYMENTS TABLE
-- ============================================================================
INSERT INTO payments (
    payment_id, order_id, method, provider, amount, status, transaction_id, payment_date, paid_at, gateway, bank_code, response_code, error_message, ip_address, metadata
) VALUES
      (1,1,'Credit Card','VISA',36450000,'Pending','TXN-2024-00001','2025-12-20 09:20:00',NULL,'Stripe',NULL,NULL,NULL,'203.113.0.1',NULL),
      (2,2,'Bank Transfer','BankX',52075000,'Completed','TXN-2024-00002','2025-12-20 10:10:00','2025-12-20 10:30:00','BankTransfer','BKX','00',NULL,'203.113.0.2',NULL),
      (3,3,'Credit Card','MasterCard',65610000,'Completed','TXN-2024-00003','2025-12-21 11:05:00','2025-12-21 11:06:00','Stripe',NULL,'00',NULL,'203.113.0.3',NULL),
      (4,4,'E-Wallet','E-WalletX',29170000,'Completed','TXN-2024-00004','2025-12-18 08:00:00','2025-12-18 08:05:00','EWallet',NULL,'00',NULL,'203.113.0.4',NULL),
      (5,5,'Credit Card','VISA',8890500,'Completed','TXN-2024-00005','2025-12-10 08:35:00','2025-12-10 08:40:00','Stripe',NULL,'00',NULL,'203.113.0.5',NULL),
      (6,6,'Bank Transfer','BankY',1590000,'Pending','TXN-2024-00006','2025-12-25 08:05:00',NULL,'BankTransfer','BKY',NULL,NULL,'203.113.0.6',NULL);

-- ============================================================================
-- 25. REFUNDS TABLE
-- ============================================================================
INSERT INTO refunds (
    refund_id, payment_id, order_id, customer_id, type, reason, amount, status, request_date, approved_date, processed_date, completed_date, approved_by, processed_by, notes, customer_notes, admin_notes, refund_method, transaction_id
) VALUES
      (1,4,4,4,'Full','Customer request',29170000,'Completed','2025-12-22 10:00:00','2025-12-22 12:00:00','2025-12-23 09:00:00','2025-12-23 11:00:00','admin1','finance1','Refund for returned headphones','Wrong color','Approved','E-Wallet','RFND-2025-0001'),
      (2,5,5,5,'Partial','Quality issue',8890500,'Processing','2025-12-12 10:00:00',NULL,NULL,NULL,NULL,NULL,'Under review','Sound issue',NULL,NULL,NULL);

-- ============================================================================
-- 26. DELIVERIES TABLE
-- ============================================================================
-- ========== DELIVERIES ==========
INSERT INTO deliveries (
    delivery_id, order_id, tracking_number, shipping_method, carrier, warehouse_id, address_id,
    estimated_pickup, actual_pickup, estimated_delivery, actual_delivery, shipping_fee, insurance_fee,
    current_location, current_latitude, current_longitude, status, attempt_count, signature, proof_of_delivery, notes
) VALUES
      (1,3,'TRK-2025-0001','Standard','VendorShipper',1,3,'2025-12-21 09:00:00','2025-12-21 09:15:00','2025-12-28 12:00:00',NULL,75000,0,'District 1, HCM',10.7731,106.7034,'In Transit',0,NULL,NULL,'Delivery in progress'),
      (2,4,'TRK-2025-0002','Express','VendorShipper',1,1,'2025-12-15 08:00:00','2025-12-15 08:30:00','2025-12-20 12:00:00','2025-12-20 10:00:00',40000,0,'Customer address',10.7731,106.7034,'Delivered',1,'signed-by-customer','https://cdn.example.com/proofs/proof-ord4.jpg','Delivered successfully'),
      (3,5,'TRK-2025-0003','Standard','VendorShipper',1,2,'2025-12-10 09:00:00','2025-12-10 09:10:00','2025-12-15 12:00:00','2025-12-15 09:00:00',30000,0,'Customer address',10.7758,106.7010,'Delivered',1,'signed-by-customer','https://cdn.example.com/proofs/proof-ord5.jpg','Delivered successfully'),
      (4,1,'TRK-2025-0004','Pickup','VendorShipper',1,10,NULL,NULL,NULL,NULL,50000,0,'Warehouse',10.7890,106.6510,'Pending',0,NULL,NULL,'Awaiting pickup');

-- ========== DELIVERY ASSIGNMENTS ==========
INSERT INTO delivery_assignments (assignment_id, delivery_id, shipper_id, assigned_at, accepted_at, started_at, completed_at, status, earnings, rating, feedback) VALUES
                                                                                                                                                                      (1,1,7,'2025-12-21 09:10:00','2025-12-21 09:12:00','2025-12-21 09:20:00',NULL,'Active',120000,NULL,NULL),
                                                                                                                                                                      (2,2,7,'2025-12-15 08:10:00','2025-12-15 08:12:00','2025-12-15 08:20:00','2025-12-20 10:00:00','Completed',80000,5,'On time'),
                                                                                                                                                                      (3,3,8,'2025-12-10 09:05:00','2025-12-10 09:06:00','2025-12-10 09:15:00','2025-12-15 09:00:00','Completed',70000,5,'Good'),
                                                                                                                                                                      (4,4,7,'2025-12-24 08:00:00',NULL,NULL,NULL,'Pending',0,NULL,NULL);

-- ============================================================================
-- 28. REVIEWS TABLE
-- ============================================================================
-- ========== REVIEWS ==========
INSERT INTO reviews (review_id, product_id, variant_id, customer_id, order_detail_id, rating, title, comment, review_date, updated_at, images, videos, likes, dislikes, helpful_count, is_verified_purchase, status, moderated_by, moderated_at) VALUES
                                                                                                                                                                                                                                                     (1,1,1,4,1,5,'Excellent product!','iPhone 15 Pro Max is amazing! Great camera and performance.','2025-12-22 09:00:00','2025-12-22 09:00:00',NULL,NULL,2,0,2,1,'Published',NULL,NULL),
                                                                                                                                                                                                                                                     (2,3,2,5,2,4,'Good laptop','MacBook Pro is powerful, a bit expensive but worth it.','2025-12-23 10:00:00','2025-12-23 10:00:00',NULL,NULL,1,0,1,1,'Published',NULL,NULL),
                                                                                                                                                                                                                                                     (3,5,1,4,4,5,'Best headphones','Sony WH-1000XM5 provides excellent sound quality and noise cancellation.','2025-12-24 08:00:00','2025-12-24 08:00:00',NULL,NULL,1,0,1,1,'Published',NULL,NULL),
                                                                                                                                                                                                                                                     (4,2,4,6,3,4,'Very good phone','Samsung Galaxy S24 Ultra is a great phone with excellent features.','2025-12-24 12:00:00','2025-12-24 12:00:00',NULL,NULL,0,0,0,0,'Published',NULL,NULL),
                                                                                                                                                                                                                                                     (5,6,5,5,5,5,'Perfect tablet','iPad Pro is perfect for work and entertainment.','2025-12-24 12:30:00','2025-12-24 12:30:00',NULL,NULL,0,0,0,1,'Published',NULL,NULL);

-- ========== REVIEW RESPONSES ==========
INSERT INTO review_responses (response_id, review_id, vendor_id, response, response_date, updated_at) VALUES
                                                                                                          (1,1,2,'Thank you for your review! We are glad you are satisfied.','2025-12-23 10:00:00','2025-12-23 10:00:00'),
                                                                                                          (2,2,3,'Appreciate your feedback. Enjoy your MacBook!','2025-12-24 09:00:00','2025-12-24 09:00:00'),
                                                                                                          (3,3,2,'Thanks for choosing our Sony headphones!','2025-12-24 12:45:00','2025-12-24 12:45:00');

-- ========== REVIEW VOTES ==========
INSERT INTO review_votes (vote_id, review_id, user_id, vote_type, voted_at) VALUES
                                                                                (1,1,5,'Helpful','2025-12-23 09:00:00'),
                                                                                (2,1,6,'Helpful','2025-12-24 09:00:00'),
                                                                                (3,2,4,'Helpful','2025-12-24 10:30:00'),
                                                                                (4,3,5,'Helpful','2025-12-24 12:00:00'),
                                                                                (5,4,4,'Not Helpful','2025-12-24 12:30:00');

-- ========== WISHLISTS & WISHLIST ITEMS ==========
INSERT INTO wishlists (wishlist_id, customer_id, name, is_default, is_public, created_at, updated_at) VALUES
                                                                                                          (1,4,'Default Wishlist',1,0,'2025-12-20 09:00:00','2025-12-24 09:00:00'),
                                                                                                          (2,5,'Default Wishlist',1,0,'2025-12-20 09:00:00','2025-12-24 09:00:00'),
                                                                                                          (3,6,'Default Wishlist',1,0,'2025-12-20 09:00:00','2025-12-24 09:00:00');

INSERT INTO wishlist_items (item_id, wishlist_id, product_id, variant_id, added_date, notify_on_discount, notify_on_restock, priority, notes) VALUES
                                                                                                                                                  (1,1,2,4,'2025-12-20 09:15:00',0,0,1,NULL),
                                                                                                                                                  (2,1,6,10,'2025-12-20 09:16:00',0,0,2,NULL),
                                                                                                                                                  (3,2,4,8,'2025-12-20 09:20:00',0,0,1,NULL),
                                                                                                                                                  (4,2,7,1,'2025-12-20 09:21:00',0,0,2,NULL),
                                                                                                                                                  (5,3,1,2,'2025-12-20 09:25:00',0,0,1,NULL),
                                                                                                                                                  (6,3,8,6,'2025-12-20 09:26:00',0,0,2,NULL);
-- ============================================================================
-- 33. VOUCHERS TABLE
-- ============================================================================
INSERT INTO vouchers (
    voucher_id, code, name, description, type, scope, discount_percent, discount_amount, max_discount, min_order_value,
    applicable_products, applicable_categories, start_date, expiry_date, usage_limit, usage_limit_per_user, usage_count, is_active, is_public, created_by, created_at
) VALUES
      (1,'TECHSUMMER2024','Tech Summer 2024','10% off','Percentage','Sitewide',10,NULL,NULL,1000000,NULL,NULL,'2025-11-25 00:00:00','2026-01-24 00:00:00',1000,100,250,1,1,'admin1','2025-11-25 00:00:00'),
      (2,'NEWUSER20','New User 20%','20% for new users','Percentage','NewCustomers',20,NULL,NULL,2000000,NULL,NULL,'2025-12-10 00:00:00','2026-01-24 00:00:00',500,1,120,1,0,'admin1','2025-12-10 00:00:00'),
      (3,'SAVE50K','Save 50K','50,000 VND off','Fixed','Sitewide',NULL,50000,50000,500000,NULL,NULL,'2025-12-15 00:00:00','2026-01-04 00:00:00',300,20,85,1,1,'admin1','2025-12-15 00:00:00'),
      (4,'FREESHIP','Free Shipping','Free shipping voucher','Percentage','Shipping',100,NULL,NULL,3000000,NULL,NULL,'2025-12-25 00:00:00','2026-02-23 00:00:00',2000,50,890,1,1,'admin1','2025-12-25 00:00:00'),
      (5,'VIPTECHCLUB','VIP Tech Club','15% for VIP','Percentage','VIP',15,NULL,NULL,5000000,NULL,NULL,'2025-12-25 00:00:00','2026-03-25 00:00:00',100,100,45,1,0,'admin1','2025-12-25 00:00:00');

-- ========== VOUCHER USAGES ==========
INSERT INTO voucher_usages (usage_id, voucher_id, customer_id, order_id, used_date, order_value, discount_applied) VALUES
                                                                                                                       (1,1,4,3,'2025-12-21 11:05:00',65610000,500000),
                                                                                                                       (2,2,5,2,'2025-12-20 10:10:00',52075000,5000000),
                                                                                                                       (3,1,6,1,'2025-12-21 11:05:30',0,0),
                                                                                                                       (4,3,4,4,'2025-12-22 11:05:30',0,0),
                                                                                                                       (5,4,5,5,'2025-12-15 09:00:00',8890500,0);
-- ============================================================================
-- 35. FLASH_SALES TABLE
-- ============================================================================
-- ========== FLASH SALES ==========
INSERT INTO flash_sales (sale_id, name, description, product_id, variant_id, original_price, sale_price, discount_percent, start_time, end_time, total_quantity, sold_count, limit_per_customer, is_active, created_at) VALUES
                                                                                                                                                                                                                            (1,'New Year Flash Sale','Get up to 30% off on selected items',1,NULL,36450000,25515000,30,'2025-12-25 00:00:00','2025-12-26 00:00:00',500,120,1,1,'2025-12-20 00:00:00'),
                                                                                                                                                                                                                            (2,'Weekend Special','Special weekend discounts on electronics',3,NULL,52075000,39056250,25,'2025-12-27 00:00:00','2025-12-28 00:00:00',200,40,2,1,'2025-12-20 00:00:00'),
                                                                                                                                                                                                                            (3,'Clearance Sale','Clear old stock with great discounts',4,8,22000000,11000000,50,'2025-12-30 00:00:00','2026-01-01 00:00:00',100,10,1,1,'2025-12-20 00:00:00');

-- ========== TRANSACTION HISTORIES ==========
-- ========================== TRANSACTION_HISTORY ==========================
INSERT INTO transaction_histories
(history_id, entity_type, entity_id, action, timestamp, performed_by, ip_address, user_agent, before_data, after_data, details) VALUES
                                                                                                                                    (1,'Order','ORD-2024-00001','Purchase','2025-12-20 09:20:00','cust4','203.113.0.1','Mozilla/5.0',NULL,NULL,'Purchase Order #ORD-2024-00001'),
                                                                                                                                    (2,'Order','ORD-2024-00002','Purchase','2025-12-20 10:30:00','cust5','203.113.0.2','Mozilla/5.0',NULL,NULL,'Purchase Order #ORD-2024-00002'),
                                                                                                                                    (3,'Order','ORD-2024-00003','Purchase','2025-12-21 11:06:00','cust6','203.113.0.3','Mozilla/5.0',NULL,NULL,'Purchase Order #ORD-2024-00003'),
                                                                                                                                    (4,'Order','ORD-2024-00004','Purchase','2025-12-18 08:05:00','cust4','203.113.0.4','Mozilla/5.0',NULL,NULL,'Purchase Order #ORD-2024-00004'),
                                                                                                                                    (5,'Order','ORD-2024-00005','Purchase','2025-12-10 08:40:00','cust5','203.113.0.5','Mozilla/5.0',NULL,NULL,'Purchase Order #ORD-2024-00005'),
                                                                                                                                    (6,'Refund','ORD-2024-00004','Refund','2025-12-23 09:00:00','admin1','203.113.0.4','Mozilla/5.0',NULL,NULL,'Refund for Order #ORD-2024-00004'),
                                                                                                                                    (7,'Loyalty','cust5','EarnPoints','2025-12-20 11:00:00','system',NULL,NULL,NULL,NULL,'Earned 5000 points'),
                                                                                                                                    (8,'Loyalty','cust6','RedeemPoints','2025-12-25 08:10:00','cust6','203.113.0.6','Mozilla/5.0',NULL,NULL,'Redeemed 1500 points');

-- ========================== STATISTICS ==========================
INSERT INTO statistics
(stat_id, vendor_id, period, start_date, end_date, revenue, sales_count, order_count, average_order_value, conversion_rate, return_rate, top_products, top_categories, new_customers, returning_customers, cancelled_orders, completed_orders, generated_at) VALUES
                                                                                                                                                                                                                                                                 -- (1,2,'monthly','2025-12-01 00:00:00','2025-12-31 23:59:59',156110000,4,5,31222000,2.5,0.2,'iPhone 15 Pro Max, Sony WH-1000XM5','Phones, Headphones',3,2,1,4,'2025-12-25 00:00:00'),
                                                                                                                                                                                                                                                                 (2,3,'monthly','2025-12-01 00:00:00','2025-12-31 23:59:59',144625000,3,4,36156250,1.8,0.1,'MacBook Pro','Laptops',2,1,0,3,'2025-12-25 00:00:00');

-- ========================== PRODUCT_VIEWS ==========================
INSERT INTO product_views
(view_id, product_id, customer_id, session_id, view_date, duration, source, referrer) VALUES
                                                                                          (1,1,4,'sess-111','2025-12-24 10:00:00',120,'search',NULL),
                                                                                          (2,1,5,'sess-112','2025-12-22 09:00:00',90,'recommendation',NULL),
                                                                                          (3,3,5,'sess-113','2025-12-24 11:00:00',45,'ads',NULL),
                                                                                          (4,2,6,'sess-114','2025-12-23 12:00:00',30,'search',NULL),
                                                                                          (5,5,4,'sess-115','2025-12-24 08:00:00',200,'search',NULL);

-- ========================== SEARCH_HISTORIES ==========================
INSERT INTO search_histories
(search_id, customer_id, keyword, filters, search_date, results_count, clicked_product_id, clicked_position) VALUES
                                                                                                                 (1,4,'iPhone',NULL,'2025-12-24 09:00:00',20,1,1),
                                                                                                                 (2,4,'headphones',NULL,'2025-12-24 09:05:00',12,5,1),
                                                                                                                 (3,5,'laptop',NULL,'2025-12-23 10:00:00',30,3,2);

-- ========================== NOTIFICATIONS ==========================
INSERT INTO notifications
(notification_id, user_id, type, title, message, action_url, is_read, created_at) VALUES
                                                                                      (1,4,'Order','Order Confirmed','Your order ORD-2024-00001 has been confirmed.','/orders/1',0,'2025-12-20 09:20:00'),
                                                                                      (2,5,'Payment','Payment Received','Payment for order ORD-2024-00002 has been received.','/orders/2',0,'2025-12-20 10:35:00'),
                                                                                      (3,6,'Delivery','Shipping Update','Your order ORD-2024-00003 is on the way.','/orders/3',1,'2025-12-23 09:00:00'),
                                                                                      (4,4,'Order','Order Delivered','Your order ORD-2024-00004 has been delivered.','/orders/4',1,'2025-12-23 11:00:00'),
                                                                                      (5,5,'Refund','Refund Processed','Your refund for order ORD-2024-00005 has been processed.','/orders/5',1,'2025-12-16 09:00:00'),
                                                                                      (6,2,'Order','New Order','You received a new order from customer.','/vendor/orders',0,'2025-12-20 09:25:00');


-- ============================================================================
-- 41. NOTIFICATIONS Table (Additional) - For system-wide notifications
-- ============================================================================
-- Already inserted above, this is just a note that Notifications is table #41

-- ============================================================================
-- COMMIT CHANGES
-- ============================================================================
COMMIT;

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================
-- Run these queries to verify data insertion:

-- SELECT COUNT(*) as total_users FROM users;
-- SELECT COUNT(*) as total_products FROM products;
-- SELECT COUNT(*) as total_orders FROM orders;
-- SELECT COUNT(*) as total_revenue, SUM(total_amount) as total_amount FROM orders;
-- SELECT COUNT(*) as total_customers FROM customer;
-- SELECT * FROM orders ORDER BY order_date DESC;
-- SELECT p.name, v.business_name, COUNT(od.order_detail_id) as sales FROM products p
-- JOIN order_details od ON p.product_id = od.product_id
-- JOIN vendor v ON p.vendor_id = v.user_id
-- GROUP BY p.product_id;

