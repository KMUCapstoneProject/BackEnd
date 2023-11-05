package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "posting")
public class Posting { // 비교과랑 행사랑 카테고리 나눠서 구분??

    // 첨부파일 등록은 따로 만들어야할듯
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long postId; //시퀀스

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER) //Many = Posting, One = user, 한 계정에 여러 개 글 작성
    @JoinColumn(name = "user_id")
    private User user; // FK

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category; // FK

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    private LocalDate startTime; // start date가 나을까,,?

    private LocalDate deadline;

    private int postHits; //조회수

    // 아이콘을 보여줄 위치 정보
    // 따로따로? 아니면 건물별? -> 나중에 수정
    @Column(nullable = false, length = 500)
    private Double latitude; // x

    @Column(nullable = false, length = 500)
    private Double longitude; // y



    @Builder
    public Posting(Long postId, User user, Category category, String title, String content, LocalDate startTime, LocalDate deadline, int postHits, Double latitude, Double longitude) {
        this.postId = postId;
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.deadline = deadline;
        this.postHits = postHits;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPostHits(int postHits) {
        this.postHits = postHits;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}