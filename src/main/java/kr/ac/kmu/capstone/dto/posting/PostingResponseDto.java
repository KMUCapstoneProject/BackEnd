package kr.ac.kmu.Capstone.dto.posting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostingResponseDto {

    private Long postId;
    private Long userId;
    private Long categoryId;
    private String nickname;
    private String title;
    private String content;
    //private LocalDateTime startTime;
    //private LocalDateTime deadline;
    private String startTime;
    private String deadline;
    private int postHits;
    private Double latitude;
    private Double longitude;
    private String details;
}