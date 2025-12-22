package viettech.entity.search;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "search_histories")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private int searchId;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    @Column(name = "keyword", length = 500, nullable = false)
    private String keyword;

    @Column(name = "filters", columnDefinition = "TEXT")
    private String filters;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "search_date", nullable = false)
    private Date searchDate;

    @Column(name = "results_count", nullable = false)
    private int resultsCount;

    @Column(name = "clicked_product_id")
    private int clickedProductId;

    @Column(name = "clicked_position")
    private int clickedPosition;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public SearchHistory() {
        this.customerId = 0;
        this.resultsCount = 0;
        this.clickedProductId = 0;
        this.clickedPosition = 0;

        this.keyword = "";
        this.filters = "";

        this.searchDate = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có searchId)
    public SearchHistory(int customerId,
                         String keyword,
                         String filters,
                         int resultsCount,
                         int clickedProductId,
                         int clickedPosition) {

        this.customerId = customerId;
        this.keyword = keyword != null ? keyword : "";
        this.filters = filters != null ? filters : "";
        this.resultsCount = resultsCount;
        this.clickedProductId = clickedProductId;
        this.clickedPosition = clickedPosition;
        this.searchDate = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getSearchId() {
        return searchId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword != null ? keyword : "";
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters != null ? filters : "";
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public int getResultsCount() {
        return resultsCount;
    }

    public void setResultsCount(int resultsCount) {
        this.resultsCount = resultsCount;
    }

    public int getClickedProductId() {
        return clickedProductId;
    }

    public void setClickedProductId(int clickedProductId) {
        this.clickedProductId = clickedProductId;
    }

    public int getClickedPosition() {
        return clickedPosition;
    }

    public void setClickedPosition(int clickedPosition) {
        this.clickedPosition = clickedPosition;
    }
}