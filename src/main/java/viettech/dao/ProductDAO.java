package viettech.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.config.JPAConfig;
import viettech.entity.product.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Product DAO - Data Access Object for Product entity (base class)
 * @author VietTech Team
 */
public class ProductDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    // CREATE
    public void insert(Product product) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(product);
            trans.commit();
            logger.info("✓ Inserted product: {}", product.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to insert product: {}", product.getName(), e);
            throw new RuntimeException("Failed to insert product", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo ID
    public Product findById(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Product product = em.find(Product.class, productId);
            if (product != null) {
                logger.debug("✓ Found product by ID: {}", productId);
            } else {
                logger.warn("✗ Product not found with ID: {}", productId);
            }
            return product;
        } catch (Exception e) {
            logger.error("✗ Error finding product by ID: {}", productId, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Slug
    public Product findBySlug(String slug) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p WHERE p.slug = :slug";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("slug", slug);
            Product product = query.getSingleResult();
            logger.debug("✓ Found product by slug: {}", slug);
            return product;
        } catch (NoResultException e) {
            logger.debug("✗ Product not found with slug: {}", slug);
            return null;
        } catch (Exception e) {
            logger.error("✗ Error finding product by slug: {}", slug, e);
            return null;
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Category ID
    public List<Product> findByCategoryId(int categoryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p WHERE p.categoryId = :categoryId ORDER BY p.createdAt DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("categoryId", categoryId);
            List<Product> products = query.getResultList();
            logger.debug("✓ Found {} product(s) for category ID: {}", products.size(), categoryId);
            return products;
        } catch (Exception e) {
            logger.error("✗ Error finding products by category ID: {}", categoryId, e);
            throw new RuntimeException("Failed to find products by category", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Vendor ID
    public List<Product> findByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p WHERE p.vendorId = :vendorId ORDER BY p.createdAt DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("vendorId", vendorId);
            List<Product> products = query.getResultList();
            logger.debug("✓ Found {} product(s) for vendor ID: {}", products.size(), vendorId);
            return products;
        } catch (Exception e) {
            logger.error("✗ Error finding products by vendor ID: {}", vendorId, e);
            throw new RuntimeException("Failed to find products by vendor", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Brand
    public List<Product> findByBrand(String brand) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p WHERE p.brand = :brand ORDER BY p.createdAt DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("brand", brand);
            List<Product> products = query.getResultList();
            logger.debug("✓ Found {} product(s) for brand: {}", products.size(), brand);
            return products;
        } catch (Exception e) {
            logger.error("✗ Error finding products by brand: {}", brand, e);
            throw new RuntimeException("Failed to find products by brand", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo Status
    public List<Product> findByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p WHERE p.status = :status ORDER BY p.createdAt DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("status", status);
            List<Product> products = query.getResultList();
            logger.debug("✓ Found {} product(s) with status: {}", products.size(), status);
            return products;
        } catch (Exception e) {
            logger.error("✗ Error finding products by status: {}", status, e);
            throw new RuntimeException("Failed to find products by status", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm featured products
    public List<Product> findFeatured() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p WHERE p.isFeatured = true ORDER BY p.createdAt DESC";
            List<Product> products = em.createQuery(jpql, Product.class).getResultList();
            logger.debug("✓ Found {} featured product(s)", products.size());
            return products;
        } catch (Exception e) {
            logger.error("✗ Error finding featured products", e);
            throw new RuntimeException("Failed to find featured products", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm theo khoảng giá
    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p WHERE p.basePrice BETWEEN :minPrice AND :maxPrice ORDER BY p.basePrice";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            List<Product> products = query.getResultList();
            logger.debug("✓ Found {} product(s) in price range {} - {}", products.size(), minPrice, maxPrice);
            return products;
        } catch (Exception e) {
            logger.error("✗ Error finding products by price range", e);
            throw new RuntimeException("Failed to find products by price range", e);
        } finally {
            em.close();
        }
    }

    // READ - Tìm kiếm theo tên
    public List<Product> searchByName(String keyword) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(:keyword) ORDER BY p.createdAt DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("keyword", "%" + keyword + "%");
            List<Product> products = query.getResultList();
            logger.debug("✓ Found {} product(s) matching: {}", products.size(), keyword);
            return products;
        } catch (Exception e) {
            logger.error("✗ Error searching products by name: {}", keyword, e);
            throw new RuntimeException("Failed to search products", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy tất cả
    public List<Product> findAll() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Product> products = em.createQuery("SELECT p FROM Product p ORDER BY p.createdAt DESC", Product.class).getResultList();
            logger.debug("✓ Retrieved {} product(s)", products.size());
            return products;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all products", e);
            throw new RuntimeException("Failed to retrieve products", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy top bán chạy
    public List<Product> findTopSelling(int limit) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p ORDER BY p.totalSold DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setMaxResults(limit);
            List<Product> products = query.getResultList();
            logger.debug("✓ Found {} top selling product(s)", products.size());
            return products;
        } catch (Exception e) {
            logger.error("✗ Error finding top selling products", e);
            throw new RuntimeException("Failed to find top selling products", e);
        } finally {
            em.close();
        }
    }

    // READ - Lấy sản phẩm mới nhất
    public List<Product> findNewest(int limit) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p ORDER BY p.createdAt DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setMaxResults(limit);
            List<Product> products = query.getResultList();
            logger.debug("✓ Found {} newest product(s)", products.size());
            return products;
        } catch (Exception e) {
            logger.error("✗ Error finding newest products", e);
            throw new RuntimeException("Failed to find newest products", e);
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void update(Product product) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(product);
            trans.commit();
            logger.info("✓ Updated product: {}", product.getName());
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to update product: {}", product.getName(), e);
            throw new RuntimeException("Failed to update product", e);
        } finally {
            em.close();
        }
    }

    // DELETE
    public void delete(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Product product = em.find(Product.class, productId);
            if (product != null) {
                em.remove(product);
                logger.info("✓ Deleted product ID: {} ({})", productId, product.getName());
            } else {
                logger.warn("✗ Cannot delete - product not found with ID: {}", productId);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to delete product ID: {}", productId, e);
            throw new RuntimeException("Failed to delete product", e);
        } finally {
            em.close();
        }
    }

    // COUNT
    public long count() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();
            logger.debug("✓ Total products: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting products", e);
            throw new RuntimeException("Failed to count products", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm theo category
    public long countByCategoryId(int categoryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM Product p WHERE p.categoryId = :categoryId";
            Long count = em.createQuery(jpql, Long.class).setParameter("categoryId", categoryId).getSingleResult();
            logger.debug("✓ Products in category {}: {}", categoryId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting products by category ID: {}", categoryId, e);
            throw new RuntimeException("Failed to count products", e);
        } finally {
            em.close();
        }
    }

    // COUNT - Đếm theo vendor
    public long countByVendorId(int vendorId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM Product p WHERE p.vendorId = :vendorId";
            Long count = em.createQuery(jpql, Long.class).setParameter("vendorId", vendorId).getSingleResult();
            logger.debug("✓ Products by vendor {}: {}", vendorId, count);
            return count;
        } catch (Exception e) {
            logger.error("✗ Error counting products by vendor ID: {}", vendorId, e);
            throw new RuntimeException("Failed to count products", e);
        } finally {
            em.close();
        }
    }

    // INCREMENT view count
    public void incrementViewCount(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "UPDATE Product p SET p.viewCount = p.viewCount + 1 WHERE p.productId = :productId";
            em.createQuery(jpql).setParameter("productId", productId).executeUpdate();
            trans.commit();
            logger.debug("✓ Incremented view count for product ID: {}", productId);
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            logger.error("✗ Failed to increment view count for product ID: {}", productId, e);
        } finally {
            em.close();
        }
    }
    /**
     * Lấy danh sách sản phẩm theo category và JOIN FETCH images để load ảnh cùng lúc
     */
    public List<Product> findByCategoryIdWithImages(int categoryId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT DISTINCT p FROM Product p " +
                    "LEFT JOIN FETCH p.images " +
                    "WHERE p.categoryId = :categoryId " +
                    "ORDER BY p.createdAt DESC";

            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("categoryId", categoryId);

            List<Product> products = query.getResultList();

            logger.debug("Found {} product(s) with images for category ID: {}", products.size(), categoryId);
            return products;
        } catch (Exception e) {
            logger.error("Error finding products with images by category ID: {}", categoryId, e);
            throw new RuntimeException("Failed to find products with images", e);
        } finally {
            em.close();
        }
    }
    /**
     * Tìm kiếm sản phẩm theo tên + JOIN FETCH images để load ảnh cùng lúc
     */
    public List<Product> searchByNameWithImages(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Normalize keyword đầy đủ: bỏ dấu + đ→d + lowercase
        String normalizedKeyword = normalizeVietnamese(keyword.trim());
        String searchPattern = "%" + normalizedKeyword + "%";

        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.images i
            WHERE LOWER(p.name) LIKE :pattern
               OR LOWER(p.brand) LIKE :pattern
            ORDER BY p.createdAt DESC
            """;

            List<Product> products = em.createQuery(jpql, Product.class)
                    .setParameter("pattern", searchPattern)
                    .setMaxResults(100)
                    .getResultList();

            logger.debug("✓ Found {} product(s) for keyword '{}' (normalized: '{}')",
                    products.size(), keyword, normalizedKeyword);

            // Log thêm để debug (tạm thời để lại, sau xóa cũng được)
            for (Product p : products) {
                logger.debug("   → Matched: {} | Brand: {}", p.getName(), p.getBrand());
            }

            return products;

        } catch (Exception e) {
            logger.error("✗ Error searching products for keyword: {}", keyword, e);
            throw new RuntimeException("Failed to search products", e);
        } finally {
            em.close();
        }
    }

    // Thêm method normalizeVietnamese vào ProductDAO (copy từ Service)
    private String normalizeVietnamese(String input) {
        if (input == null || input.isEmpty()) return "";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        temp = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(temp).replaceAll("");
        temp = temp.replaceAll("đ", "d").replaceAll("Đ", "d");
        return temp.toLowerCase();
    }

    /**
     * Lấy tất cả sản phẩm + JOIN FETCH images (dùng khi không có keyword)
     */
    public List<Product> findAllWithImages() {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT DISTINCT p FROM Product p " +
                    "LEFT JOIN FETCH p.images " +
                    "ORDER BY p.createdAt DESC";
            List<Product> products = em.createQuery(jpql, Product.class).getResultList();
            logger.debug("✓ Retrieved {} product(s) with images loaded", products.size());
            return products;
        } catch (Exception e) {
            logger.error("✗ Error retrieving all products with images", e);
            throw new RuntimeException("Failed to retrieve products with images", e);
        } finally {
            em.close();
        }
    }

    public Product findByIdWithImages(int productId) {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT p FROM Product p " +
                    "LEFT JOIN FETCH p.images " +
                    "WHERE p.productId = :productId";

            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("productId", productId);

            List<Product> results = query.getResultList();

            if (!results.isEmpty()) {
                Product product = results.get(0);
                logger.debug("✓ Found product with {} image(s)",
                        product.getImages() != null ? product.getImages().size() : 0);
                return product;
            }

            logger.warn("✗ Product not found with ID: {}", productId);
            return null;

        } catch (Exception e) {
            logger.error("✗ Error finding product with images: {}", productId, e);
            return null;
    /**
     * Tìm sản phẩm gợi ý theo từ khóa (dùng cho search suggestions)
     */
    public List<Product> findSimpleSuggestions(String keyword, int limit) {
        if (keyword == null || keyword.trim().length() < 2) {
            return new ArrayList<>();
        }

        String search = "%" + keyword.trim().toLowerCase() + "%";

        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.images
            WHERE p.status = 'ACTIVE'
              AND (LOWER(p.name) LIKE :search 
                   OR LOWER(p.brand) LIKE :search)
            ORDER BY 
                CASE 
                    WHEN LOWER(p.name) LIKE :startWith THEN 0
                    WHEN LOWER(p.brand) LIKE :startWith THEN 1
                    ELSE 2
                END,
                p.totalSold DESC
            """;

            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("search", search);
            query.setParameter("startWith", keyword.trim().toLowerCase() + "%"); // ưu tiên tên bắt đầu bằng từ khóa
            query.setMaxResults(limit);

            return query.getResultList();

        } catch (Exception e) {
            logger.error("Lỗi tìm gợi ý đơn giản: {}", keyword, e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }


    public List<Product> findSuggestionsSimple(String keyword, int limit) {
        System.out.println("DAO findSuggestionsSimple: " + keyword);

        String search = "%" + keyword.trim().toLowerCase() + "%";

        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        try {
            // Query cực kỳ đơn giản, chỉ lấy thông tin cơ bản
            String jpql = """
            SELECT p FROM Product p
            WHERE p.status = 'ACTIVE'
              AND (LOWER(p.name) LIKE :search 
                   OR LOWER(p.brand) LIKE :search)
            ORDER BY 
                CASE 
                    WHEN LOWER(p.name) LIKE :startWith THEN 0
                    WHEN LOWER(p.brand) LIKE :startWith THEN 1
                    ELSE 2
                END,
                p.viewCount DESC
            """;

            return em.createQuery(jpql, Product.class)
                    .setParameter("search", search)
                    .setParameter("startWith", keyword.trim().toLowerCase() + "%")
                    .setMaxResults(limit)
                    .getResultList();

        } catch (Exception e) {
            System.out.println("DAO Error: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
}

