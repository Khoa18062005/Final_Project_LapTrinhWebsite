package viettech.dto;

import viettech.entity.product.Product;
import viettech.entity.order.Order;
import viettech.entity.user.Vendor;
import viettech.entity.storage.Warehouse;
import java.util.List;

public class Vendor_dto {
    // 1. Thông tin Vendor
    private Vendor vendorInfo;
    private Warehouse warehouseInfo;  // Thông tin kho hàng

    // 2. Thống kê (Statistics)
    private long totalProducts;
    private long newOrdersCount;      // Đơn mới (Pending)
    private long pendingApprovals;    // Sản phẩm chờ duyệt

    // 3. Danh sách dữ liệu (Tables)
    private List<Product> productList;          // Danh sách sản phẩm
    private List<Order> recentOrders;           // Đơn hàng gần đây
    private List<Order> pendingShippingOrders;  // Đơn chờ giao cho Shipper

    // --- Constructor mặc định ---
    public Vendor_dto() {}

    public Vendor getVendorInfo() { return vendorInfo; }
    public void setVendorInfo(Vendor vendorInfo) { this.vendorInfo = vendorInfo; }

    public Warehouse getWarehouseInfo() { return warehouseInfo; }
    public void setWarehouseInfo(Warehouse warehouseInfo) { this.warehouseInfo = warehouseInfo; }

    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }

    public long getNewOrdersCount() { return newOrdersCount; }
    public void setNewOrdersCount(long newOrdersCount) { this.newOrdersCount = newOrdersCount; }

    public long getPendingApprovals() { return pendingApprovals; }
    public void setPendingApprovals(long pendingApprovals) { this.pendingApprovals = pendingApprovals; }

    public List<Product> getProductList() { return productList; }
    public void setProductList(List<Product> productList) { this.productList = productList; }

    public List<Order> getRecentOrders() { return recentOrders; }
    public void setRecentOrders(List<Order> recentOrders) { this.recentOrders = recentOrders; }

    public List<Order> getPendingShippingOrders() { return pendingShippingOrders; }
    public void setPendingShippingOrders(List<Order> pendingShippingOrders) { this.pendingShippingOrders = pendingShippingOrders; }
}