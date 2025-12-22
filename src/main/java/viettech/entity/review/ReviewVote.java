package viettech.entity.review;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "review_votes")
public class ReviewVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private int voteId;

    @Column(name = "review_id", nullable = false)
    private int reviewId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "vote_type", length = 20, nullable = false)
    private String voteType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "voted_at", nullable = false)
    private Date votedAt;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public ReviewVote() {
        this.reviewId = 0;
        this.userId = 0;

        this.voteType = "";

        this.votedAt = new Date();
    }

    // Constructor đầy đủ tham số (KHÔNG có voteId)
    public ReviewVote(int reviewId,
                      int userId,
                      String voteType) {

        this.reviewId = reviewId;
        this.userId = userId;
        this.voteType = voteType != null ? voteType : "";
        this.votedAt = new Date();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getVoteId() {
        return voteId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType != null ? voteType : "";
    }

    public Date getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(Date votedAt) {
        this.votedAt = votedAt;
    }
}