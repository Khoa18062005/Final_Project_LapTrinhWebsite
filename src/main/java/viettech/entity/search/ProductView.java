package viettech.entity.search;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product_views")
public class ProductView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private int viewId;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "session_id", length = 200)
    private String sessionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "view_date", nullable = false)
    private Date viewDate;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "referrer", length = 500)
    private String referrer;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public ProductView() {
        this.productId = 0;
        this.customerId = 0;
        this.duration = 0;

        this.sessionId = "";
        this.source = "";
        this.referrer = "";

        this.viewDate = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có viewId)
    public ProductView(int productId,
                       int customerId,
                       String sessionId,
                       int duration,
                       String source,
                       String referrer) {

        this.productId = productId;
        this.customerId = customerId;
        this.sessionId = sessionId != null ? sessionId : "";
        this.duration = duration;
        this.source = source != null ? source : "";
        this.referrer = referrer != null ? referrer : "";
        this.viewDate = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getViewId() {
        return viewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId != null ? sessionId : "";
    }

    public Date getViewDate() {
        return viewDate;
    }

    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source != null ? source : "";
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer != null ? referrer : "";
    }
}