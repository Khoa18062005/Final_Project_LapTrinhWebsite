package viettech.service;

import viettech.dao.ProductDAO;
import viettech.dao.ProductImageDAO;
import viettech.dao.VariantAttributeDAO;
import viettech.dao.VariantDAO;
import viettech.dto.*;
import viettech.entity.product.*;

import java.awt.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.text.similarity.LevenshteinDistance;


public class ProductService {
    private ProductDAO productDAO = new ProductDAO();

    // Constants cho category IDs
    public static final int CATEGORY_PHONE = 1;
    public static final int CATEGORY_LAPTOP = 3;
    public static final int CATEGORY_TABLET = 4;
    public static final int CATEGORY_HEADPHONE = 5;

    public List<ProductCardDTO> getHomeProducts() {
        List<Product> products = productDAO.findAll();
        return products.stream().map(p -> {
            ProductCardDTO dto = new ProductCardDTO();
            dto.setId(p.getProductId());
            dto.setName(p.getName());
            dto.setPrice(p.getBasePrice());
            dto.setRating(p.getAverageRating());
            dto.setPrimaryImage(getImagesById(p.getProductId()));
            dto.setMemberDiscount(0);
            return dto;
        }).toList();
    }

    public int getCategoryById(int productId) {
        Product product = productDAO.findById(productId);
        return product != null ? product.getCategoryId() : -1;
    }

    public String getImagesById(int productId) {
        ProductImageDAO imageDAO = new ProductImageDAO();
        return imageDAO.findPrimaryByProductId(productId).getUrl();
    }

    /**
     * Lấy product detail dựa trên ID và tự động phân loại theo category
     * @param productId ID của product
     * @return ProductDetailDTO tương ứng (Phone/Laptop/Tablet/Headphone)
     */
    public ProductDetailDTO getProductDetail(int productId) {
        Product product = productDAO.findById(productId);

        if (product == null) return null;

        int categoryId = product.getCategoryId();

        switch (categoryId) {
            case CATEGORY_PHONE:
                return convertPhoneToDTO((Phone) product);

            case CATEGORY_LAPTOP:
                return convertLaptopToDTO((Laptop) product);

            case CATEGORY_TABLET:
                return convertTabletToDTO((Tablet) product);

            case CATEGORY_HEADPHONE:
                return convertHeadphoneToDTO((Headphone) product);

            default:
                return null;
        }
    }

    /**
     * Convert Phone Entity sang PhoneDetailDTO
     */
    private PhoneDetailDTO convertPhoneToDTO(Phone phone) {
        if (phone == null) return null;

        PhoneDetailDTO dto = new PhoneDetailDTO();

        // Map từ Product (base class)
        dto.setProductId(phone.getProductId());
        dto.setVendorId(phone.getVendorId());
        dto.setCategoryId(phone.getCategoryId());
        dto.setName(phone.getName());
        dto.setSlug(phone.getSlug());
        dto.setBasePrice(phone.getBasePrice());
        dto.setDescription(phone.getDescription());
        dto.setBrand(phone.getBrand());
        dto.setSpecifications(phone.getSpecifications());
        dto.setStatus(phone.getStatus());
        dto.setWeight(phone.getWeight());
        dto.setDimensions(phone.getDimensions());
        dto.setUpdatedAt(phone.getUpdatedAt());
        dto.setAverageRating(phone.getAverageRating());
        dto.setTotalReviews(phone.getTotalReviews());
        dto.setTotalSold(phone.getTotalSold());
        dto.setViewCount(phone.getViewCount());
        dto.setFeatured(phone.isFeatured());
        dto.setPrimaryImageUrl(getImagesById(phone.getProductId()));

        // Map từ Phone (specific fields)
        dto.setScreenSize(phone.getScreenSize());
        dto.setScreenResolution(phone.getScreenResolution());
        dto.setScreenType(phone.getScreenType());
        dto.setRefreshRate(phone.getRefreshRate());
        dto.setBatteryCapacity(phone.getBatteryCapacity());
        dto.setChargerType(phone.getChargerType());
        dto.setChargingSpeed(phone.getChargingSpeed());
        dto.setProcessor(phone.getProcessor());
        dto.setGpu(phone.getGpu());
        dto.setRearCamera(phone.getRearCamera());
        dto.setFrontCamera(phone.getFrontCamera());
        dto.setVideoRecording(phone.getVideoRecording());
        dto.setOs(phone.getOs());
        dto.setOsVersion(phone.getOsVersion());
        dto.setSimType(phone.getSimType());
        dto.setNetworkSupport(phone.getNetworkSupport());
        dto.setConnectivity(phone.getConnectivity());
        dto.setNfc(phone.isNfc());
        dto.setWaterproofRating(phone.getWaterproofRating());
        dto.setDustproofRating(phone.getDustproofRating());
        dto.setFingerprintSensor(phone.isFingerprintSensor());
        dto.setFaceRecognition(phone.isFaceRecognition());
        dto.setWirelessCharging(phone.isWirelessCharging());
        dto.setReverseCharging(phone.isReverseCharging());
        dto.setAudioJack(phone.isAudioJack());

        return dto;
    }

    /**
     * Convert Laptop Entity sang LaptopDetailDTO
     */
    private LaptopDetailDTO convertLaptopToDTO(Laptop laptop) {
        if (laptop == null) return null;

        LaptopDetailDTO dto = new LaptopDetailDTO();

        // Map từ Product (base class)
        dto.setProductId(laptop.getProductId());
        dto.setVendorId(laptop.getVendorId());
        dto.setCategoryId(laptop.getCategoryId());
        dto.setName(laptop.getName());
        dto.setSlug(laptop.getSlug());
        dto.setBasePrice(laptop.getBasePrice());
        dto.setDescription(laptop.getDescription());
        dto.setBrand(laptop.getBrand());
        dto.setSpecifications(laptop.getSpecifications());
        dto.setStatus(laptop.getStatus());
        dto.setWeight(laptop.getWeight());
        dto.setDimensions(laptop.getDimensions());
        dto.setUpdatedAt(laptop.getUpdatedAt());
        dto.setAverageRating(laptop.getAverageRating());
        dto.setTotalReviews(laptop.getTotalReviews());
        dto.setTotalSold(laptop.getTotalSold());
        dto.setViewCount(laptop.getViewCount());
        dto.setFeatured(laptop.isFeatured());
        dto.setPrimaryImageUrl(getImagesById(laptop.getProductId()));

        // Map từ Laptop (specific fields)
        dto.setCpu(laptop.getCpu());
        dto.setCpuGeneration(laptop.getCpuGeneration());
        dto.setCpuSpeed(laptop.getCpuSpeed());
        dto.setGpu(laptop.getGpu());
        dto.setGpuMemory(laptop.getGpuMemory());
        dto.setRam(laptop.getRam());
        dto.setRamType(laptop.getRamType());
        dto.setMaxRam(laptop.getMaxRam());
        dto.setStorage(laptop.getStorage());
        dto.setStorageType(laptop.getStorageType());
        dto.setAdditionalSlot(laptop.isAdditionalSlot());
        dto.setScreenSize(laptop.getScreenSize());
        dto.setScreenResolution(laptop.getScreenResolution());
        dto.setScreenType(laptop.getScreenType());
        dto.setRefreshRate(laptop.getRefreshRate());
        dto.setColorGamut(laptop.getColorGamut());
        dto.setBatteryCapacity(laptop.getBatteryCapacity());
        dto.setBatteryLife(laptop.getBatteryLife());
        dto.setPorts(laptop.getPorts());
        dto.setOs(laptop.getOs());
        dto.setKeyboardType(laptop.getKeyboardType());
        dto.setKeyboardBacklight(laptop.isKeyboardBacklight());
        dto.setWebcam(laptop.getWebcam());
        dto.setSpeakers(laptop.getSpeakers());
        dto.setMicrophone(laptop.getMicrophone());
        dto.setTouchScreen(laptop.isTouchScreen());
        dto.setFingerprintSensor(laptop.isFingerprintSensor());
        dto.setDiscreteGpu(laptop.isDiscreteGpu());
        dto.setThunderbolt(laptop.isThunderbolt());

        return dto;
    }

    /**
     * Convert Tablet Entity sang TabletDetailDTO
     */
    private TabletDetailDTO convertTabletToDTO(Tablet tablet) {
        if (tablet == null) return null;

        TabletDetailDTO dto = new TabletDetailDTO();

        // Map từ Product (base class)
        dto.setProductId(tablet.getProductId());
        dto.setVendorId(tablet.getVendorId());
        dto.setCategoryId(tablet.getCategoryId());
        dto.setName(tablet.getName());
        dto.setSlug(tablet.getSlug());
        dto.setBasePrice(tablet.getBasePrice());
        dto.setDescription(tablet.getDescription());
        dto.setBrand(tablet.getBrand());
        dto.setSpecifications(tablet.getSpecifications());
        dto.setStatus(tablet.getStatus());
        dto.setWeight(tablet.getWeight());
        dto.setDimensions(tablet.getDimensions());
        dto.setUpdatedAt(tablet.getUpdatedAt());
        dto.setAverageRating(tablet.getAverageRating());
        dto.setTotalReviews(tablet.getTotalReviews());
        dto.setTotalSold(tablet.getTotalSold());
        dto.setViewCount(tablet.getViewCount());
        dto.setFeatured(tablet.isFeatured());
        dto.setPrimaryImageUrl(getImagesById(tablet.getProductId()));


        // Map từ Tablet (specific fields)
        dto.setScreenSize(tablet.getScreenSize());
        dto.setScreenResolution(tablet.getScreenResolution());
        dto.setScreenType(tablet.getScreenType());
        dto.setRefreshRate(tablet.getRefreshRate());
        dto.setBatteryCapacity(tablet.getBatteryCapacity());
        dto.setProcessor(tablet.getProcessor());
        dto.setGpu(tablet.getGpu());
        dto.setRearCamera(tablet.getRearCamera());
        dto.setFrontCamera(tablet.getFrontCamera());
        dto.setVideoRecording(tablet.getVideoRecording());
        dto.setOs(tablet.getOs());
        dto.setOsVersion(tablet.getOsVersion());
        dto.setSimSupport(tablet.isSimSupport());
        dto.setNetworkSupport(tablet.getNetworkSupport());
        dto.setConnectivity(tablet.getConnectivity());
        dto.setStylusSupport(tablet.isStylusSupport());
        dto.setStylusIncluded(tablet.isStylusIncluded());
        dto.setKeyboardSupport(tablet.isKeyboardSupport());
        dto.setSpeakers(tablet.getSpeakers());
        dto.setAudioJack(tablet.isAudioJack());
        dto.setWaterproofRating(tablet.getWaterproofRating());
        dto.setFaceRecognition(tablet.isFaceRecognition());
        dto.setFingerprintSensor(tablet.isFingerprintSensor());

        return dto;
    }

    /**
     * Convert Headphone Entity sang HeadphoneDetailDTO
     */
    private HeadphoneDetailDTO convertHeadphoneToDTO(Headphone headphone) {
        if (headphone == null) return null;

        HeadphoneDetailDTO dto = new HeadphoneDetailDTO();

        // Map từ Product (base class)
        dto.setProductId(headphone.getProductId());
        dto.setVendorId(headphone.getVendorId());
        dto.setCategoryId(headphone.getCategoryId());
        dto.setName(headphone.getName());
        dto.setSlug(headphone.getSlug());
        dto.setBasePrice(headphone.getBasePrice());
        dto.setDescription(headphone.getDescription());
        dto.setBrand(headphone.getBrand());
        dto.setSpecifications(headphone.getSpecifications());
        dto.setStatus(headphone.getStatus());

        dto.setWeight(headphone.getWeight());
        dto.setDimensions(headphone.getDimensions());
        dto.setUpdatedAt(headphone.getUpdatedAt());
        dto.setAverageRating(headphone.getAverageRating());
        dto.setTotalReviews(headphone.getTotalReviews());
        dto.setTotalSold(headphone.getTotalSold());
        dto.setViewCount(headphone.getViewCount());
        dto.setFeatured(headphone.isFeatured());
        dto.setPrimaryImageUrl(getImagesById(headphone.getProductId()));


        // Map từ Headphone (specific fields)
        dto.setType(headphone.getType());
        dto.setFormFactor(headphone.getFormFactor());
        dto.setBatteryLife(headphone.getBatteryLife());
        dto.setChargingTime(headphone.getChargingTime());
        dto.setChargingPort(headphone.getChargingPort());
        dto.setNoiseCancellation(headphone.isNoiseCancellation());
        dto.setNoiseCancellationType(headphone.getNoiseCancellationType());
        dto.setAmbientMode(headphone.isAmbientMode());
        dto.setDriverSize(headphone.getDriverSize());
        dto.setDriverType(headphone.getDriverType());
        dto.setFrequencyResponse(headphone.getFrequencyResponse());
        dto.setImpedance(headphone.getImpedance());
        dto.setSensitivity(headphone.getSensitivity());
        dto.setMicrophone(headphone.isMicrophone());
        dto.setMicType(headphone.getMicType());
        dto.setWaterproofRating(headphone.getWaterproofRating());
        dto.setConnectivity(headphone.getConnectivity());
        dto.setBluetoothVersion(headphone.getBluetoothVersion());
        dto.setBluetoothCodecs(headphone.getBluetoothCodecs());
        dto.setWiredConnection(headphone.getWiredConnection());
        dto.setMultipoint(headphone.isMultipoint());
        dto.setSoundProfile(headphone.getSoundProfile());
        dto.setAppControl(headphone.isAppControl());
        dto.setCustomEQ(headphone.isCustomEQ());
        dto.setSurroundSound(headphone.isSurroundSound());
        dto.setFoldable(headphone.isFoldable());

        return dto;
    }

    /**
     * Lấy thông tin category name để hiển thị
     */
    public String getCategoryName(int categoryId) {
        switch (categoryId) {
            case CATEGORY_PHONE:
                return "Phone";
            case CATEGORY_LAPTOP:
                return "Laptop";
            case CATEGORY_TABLET:
                return "Tablet";
            case CATEGORY_HEADPHONE:
                return "Headphone";
            default:
                return "Unknown";
        }
    }
    /**
     * Phương thức chung: lấy tất cả sản phẩm thuộc một category
     * @param categoryId ID của category
     * @return List<ProductCardDTO> chứa toàn bộ sản phẩm của category đó
     */
    private List<ProductCardDTO> getAllProductsByCategory(int categoryId) {
        // CHÚ Ý: Cần sửa DAO để JOIN FETCH images (xem bước 2 bên dưới)
        List<Product> products = productDAO.findByCategoryIdWithImages(categoryId);

        return products.stream()
                .map(p -> {
                    ProductCardDTO dto = new ProductCardDTO();
                    dto.setId(p.getProductId());
                    dto.setName(p.getName());
                    dto.setPrice(p.getBasePrice());
                    dto.setRating(p.getAverageRating());
                    dto.setMemberDiscount(0); // có thể tính sau
                    dto.setOldPrice(p.getBasePrice()); // tạm thời
                    dto.setDiscountPercent(0); // tạm thời

                    // ===== LẤY ẢNH CHÍNH TỪ DANH SÁCH IMAGES =====
                    String primaryImageUrl = "";

                    if (p.getImages() != null && !p.getImages().isEmpty()) {
                        // Ưu tiên ảnh có isPrimary = true
                        primaryImageUrl = p.getImages().stream()
                                .filter(img -> img.isPrimary())
                                .map(ProductImage::getUrl)
                                .findFirst()
                                .orElse("");

                        // Nếu không có ảnh primary, lấy ảnh có sortOrder nhỏ nhất (thường là ảnh chính)
                        if (primaryImageUrl.isEmpty()) {
                            primaryImageUrl = p.getImages().stream()
                                    .min(java.util.Comparator.comparingInt(ProductImage::getSortOrder))
                                    .map(ProductImage::getUrl)
                                    .orElse("");
                        }
                    }

                    dto.setPrimaryImage(primaryImageUrl);
                    // ==========================================

                    return dto;
                })
                .toList();
    }

    /**
     * Lấy tất cả điện thoại
     */
    public List<ProductCardDTO> getAllPhones() {
        return getAllProductsByCategory(CATEGORY_PHONE);
    }

    /**
     * Lấy tất cả laptop
     */
    public List<ProductCardDTO> getAllLaptops() {
        return getAllProductsByCategory(CATEGORY_LAPTOP);
    }

    /**
     * Lấy tất cả tablet
     */
    public List<ProductCardDTO> getAllTablets() {
        return getAllProductsByCategory(CATEGORY_TABLET);
    }

    /**
     * Lấy tất cả tai nghe
     */
    public List<ProductCardDTO> getAllHeadphones() {
        return getAllProductsByCategory(CATEGORY_HEADPHONE);
    }
    // =================================================================================

    public List<VariantDTO> getAllVariantsById(int productId) {
        List<VariantDTO> list = new ArrayList<>();
        VariantDAO variantDAO = new VariantDAO();
        VariantAttributeDAO variantAttributeDAO = new VariantAttributeDAO();

        List<Variant> variants = variantDAO.findByProductId(productId);
        for(Variant variant : variants) {
            VariantDTO dto = new VariantDTO();
            dto.setVariantId(variant.getVariantId());
            dto.setActive(variant.isActive());
            dto.setFinalPrice(variant.getFinalPrice());
            List<AttributeDTO> attributeDTO = new ArrayList<>();
            List<VariantAttribute> variantAttributes = variantAttributeDAO.findByVariantId(variant.getVariantId());
            for(VariantAttribute variantAttribute : variantAttributes) {
                AttributeDTO attr = new AttributeDTO();
                attr.setAttributeId(variantAttribute.getAttributeId());
                attr.setAttributeName(variantAttribute.getAttributeName());
                attr.setAttributeValue(variantAttribute.getAttributeValue());
                attributeDTO.add(attr);
            }
            dto.setAttributes(attributeDTO);
            list.add(dto);
        }
        return list;
    }

//    Tìm kiếm sản phẩm bằng fuzzi search
    public List<ProductCardDTO> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getFeaturedOrNewestAsCardDTO(20);
        }

        String logic = normalizeVietnamese(keyword.trim());

        List<Product> candidates = productDAO.searchByNameWithImages(keyword);

        // Fallback: Nếu LIKE không match (do sai chính tả), fetch tất cả rồi apply fuzzy
        if (candidates.isEmpty()) {
            candidates = productDAO.findAllWithImages();
        }

        List<Product> results = candidates.stream()
                .filter(p -> {
                    String normalizedName = normalizeVietnamese(p.getName());

                    // 1. Nếu tên chứa chính xác từ khóa → ưu tiên cao nhất
                    if (normalizedName.contains(logic)) {
                        return true;
                    }

                    // 2. Tách tên sản phẩm thành các từ riêng lẻ
                    String[] nameWords = normalizedName.split("\\s+");

                    // 3. Kiểm tra từng từ trong tên sản phẩm với từ khóa
                    for (String nameWord : nameWords) {
                        if (nameWord.length() < 3) continue; // bỏ qua từ quá ngắn như "pro", "gb"

                        // Nếu từ trong tên chứa từ khóa → match tốt
                        if (nameWord.contains(logic) || logic.contains(nameWord)) {
                            return true;
                        }

                        // Levenshtein: chỉ so sánh từ khóa với từng từ riêng lẻ
                        int distance = LEVENSHTEIN.apply(logic, nameWord);

                        int threshold = switch (Math.min(logic.length(), nameWord.length())) {
                            case 0, 1, 2, 3 -> 1;
                            case 4, 5, 6, 7 -> 2;
                            default -> 3;
                        };

                        if (distance <= threshold) {
                            return true;
                        }
                    }

                    return false;
                })
                .sorted(Comparator.comparingInt(p -> {
                    String normalizedName = normalizeVietnamese(p.getName());
                    String[] nameWords = normalizedName.split("\\s+");

                    // Tính khoảng cách nhỏ nhất đến bất kỳ từ nào trong tên
                    int minDistance = Integer.MAX_VALUE;
                    for (String word : nameWords) {
                        if (word.length() >= 3) {
                            int dist = LEVENSHTEIN.apply(logic, word);
                            if (dist < minDistance) minDistance = dist;
                        }
                    }
                    return minDistance == Integer.MAX_VALUE ? 100 : minDistance;
                }))
                .limit(50)
                .toList();

        return results.stream()
                .map(this::convertProductToCardDTO)
                .toList();
    }

    // Thêm constant này vào đầu class ProductService
    private static final LevenshteinDistance LEVENSHTEIN = new LevenshteinDistance();

    private String normalizeVietnamese(String input) {
        if (input == null || input.isEmpty()) return "";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        temp = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(temp).replaceAll("");
        temp = temp.replaceAll("đ", "d").replaceAll("Đ", "d");
        return temp.toLowerCase();
    }

    private String getPrimaryImageUrl(Product p) {
        if (p.getImages() == null || p.getImages().isEmpty()) {
            return "";
        }
        return p.getImages().stream()
                .filter(ProductImage::isPrimary)
                .map(ProductImage::getUrl)
                .findFirst()
                .orElse(p.getImages().stream()
                        .min(Comparator.comparingInt(ProductImage::getSortOrder))
                        .map(ProductImage::getUrl)
                        .orElse(""));
    }

    private ProductCardDTO convertProductToCardDTO(Product p) {
        ProductCardDTO dto = new ProductCardDTO();
        dto.setId(p.getProductId());
        dto.setName(p.getName());
        dto.setPrice(p.getBasePrice());
        dto.setRating(p.getAverageRating()); // ← Đã fix lỗi, đơn giản và đúng
        dto.setMemberDiscount(0);
        dto.setOldPrice(p.getBasePrice());
        dto.setDiscountPercent(0);
        dto.setPrimaryImage(getPrimaryImageUrl(p));
        return dto;
    }

    private List<ProductCardDTO> getFeaturedOrNewestAsCardDTO(int limit) {
        List<Product> products = productDAO.findFeatured();
        if (products.isEmpty()) {
            products = productDAO.findNewest(limit);
        }
        return products.stream()
                .limit(limit)
                .map(this::convertProductToCardDTO)
                .toList();
    }
}
