package viettech.service;

import viettech.dao.ProductDAO;
import viettech.dto.BuyNowDTO;
import viettech.entity.product.Product;
import java.util.ArrayList;
import java.util.List;

public class BuyNowService {
    private ProductDAO productDAO;

    // Constructor khởi tạo
    public BuyNowService() {
        this.productDAO = new ProductDAO(); // Khởi tạo productDAO
    }

    /**
     * Lấy thông tin sản phẩm để mua ngay
     */
    public BuyNowDTO getProductForBuyNow(int productId) {
        try {
            Product product = productDAO.findById(productId);
            if (product == null) {
                throw new RuntimeException("Không tìm thấy sản phẩm");
            }

            // Tạo DTO - Sửa theo cấu trúc thực tế của Product
            return new BuyNowDTO(
                    product.getProductId(),
                    product.getBasePrice(),
                    product.getName(),
                    product.getImages() != null ? "" : "" // Xử lý null
            );
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xử lý mua ngay: " + e.getMessage(), e);
        }
    }

    /**
     * Tạo danh sách BuyNow từ nhiều sản phẩm
     */
    public List<BuyNowDTO> createBuyNowList(int productId) {
        List<BuyNowDTO> buyNowList = new ArrayList<>();
        buyNowList.add(getProductForBuyNow(productId));
        return buyNowList;
    }
}