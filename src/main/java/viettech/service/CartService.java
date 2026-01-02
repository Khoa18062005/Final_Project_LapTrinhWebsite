package viettech.service;

import viettech.dao.CartDAO;
import viettech.dao.CartItemDAO;
import viettech.dto.CartItemDTO;
import viettech.dto.ProductDetailDTO;
import viettech.dto.VariantDTO;
import viettech.entity.cart.Cart;
import viettech.entity.cart.CartItem;
import viettech.entity.user.User;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartService {

    private ProductService productService;
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;

    public CartService() {
        productService = new ProductService();
        cartDAO = new CartDAO();
        cartItemDAO = new CartItemDAO();
    }

    public List<CartItemDTO> getCartItems(HttpSession session) {
        int cartId = getOrCreateCartId(session);
        List<CartItem> items = cartItemDAO.findByCartId(cartId);
        List<CartItemDTO> cartItems = new ArrayList<>();

        for (CartItem item : items) {
            cartItems.add(convertToDTO(item));
        }

        return cartItems;
    }

    public void addToCart(HttpSession session, int productId, int variantId, int quantity) {
        int cartId = getOrCreateCartId(session);

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        CartItem existingItem = cartItemDAO.findByCartIdAndVariantIdandProductId(cartId, variantId, productId);

        if (existingItem != null) {
            // Tăng số lượng nếu đã có
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemDAO.update(existingItem);
        } else {
            // Thêm mới
            CartItem newItem = createCartItem(cartId, productId, variantId, quantity);
            if (newItem != null) {
                cartItemDAO.insert(newItem);
            }
        }
    }

    public void updateCartItem(HttpSession session, int productId, int variantId, int quantity) {
        int cartId = getOrCreateCartId(session);

        CartItem item = cartItemDAO.findByCartIdAndVariantIdandProductId(cartId, variantId, productId);
        if (item != null) {
            if (quantity <= 0) {
                removeCartItem(session, productId, variantId);
            } else {
                item.setQuantity(quantity);
                cartItemDAO.update(item);
            }
        }
    }

    public void removeCartItem(HttpSession session, int productId, int variantId) {
        int cartId = getOrCreateCartId(session);

        CartItem item = cartItemDAO.findByCartIdAndVariantIdandProductId(cartId, productId, variantId);
        if (item != null) {
            cartItemDAO.delete(item.getCartItemId());
        }
    }

    public void clearCart(HttpSession session) {
        int cartId = getOrCreateCartId(session);
        cartItemDAO.deleteByCartId(cartId);
    }

    public int getCartCount(HttpSession session) {
        int cartId = getOrCreateCartId(session);
        return cartDAO.getTotalQuantityByCartId(cartId);
    }

    public double calculateTotal(List<CartItemDTO> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private int getOrCreateCartId(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in"); // Hoặc redirect to login ở controller
        }

        int customerId = user.getUserId();
        Cart cart = cartDAO.findByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
            // Set các trường khác nếu cần, ví dụ: cart.setCreatedAt(new Date());
            cartDAO.insert(cart);
            // Refresh để lấy cartId sau insert (giả sử entity có @GeneratedValue)
            cart = cartDAO.findByCustomerId(customerId);
        }

        return cart.getCartId();
    }

    private CartItem createCartItem(int cartId, int productId, int variantId, int quantity) {
        try {
            ProductDetailDTO product = productService.getProductDetail(productId);

            double price = product.getBasePrice();
            VariantDTO variant = null;

            if (variantId > 0) {
                List<VariantDTO> variants = productService.getAllVariantsById(productId);
                Optional<VariantDTO> foundVariant = variants.stream()
                        .filter(v -> v.getVariantId() == variantId)
                        .findFirst();
                if (foundVariant.isPresent()) {
                    variant = foundVariant.get();
                    price = variant.getFinalPrice();
                }
            }

            return new CartItem(cartId, productId, variantId, quantity, price);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private CartItemDTO convertToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(item.getProductId());
        dto.setVariantId(item.getVariantId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPriceAtAdd());

        try {
            ProductDetailDTO product = productService.getProductDetail(item.getProductId());
            dto.setProductName(product.getName());
            dto.setImageUrl(product.getPrimaryImageUrl());

            if (item.getVariantId() > 0) {
                List<VariantDTO> variants = productService.getAllVariantsById(item.getProductId());
                Optional<VariantDTO> variant = variants.stream()
                        .filter(v -> v.getVariantId() == item.getVariantId())
                        .findFirst();
                variant.ifPresent(dto::setVariantInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }
}