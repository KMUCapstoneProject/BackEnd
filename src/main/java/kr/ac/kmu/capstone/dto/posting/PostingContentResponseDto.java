package kr.ac.kmu.Capstone.dto.posting;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class PostingContentResponseDto {

    private Long postId;
    private Long userId;
    private Long categoryId;
    private String nickname;
    private String title;
    private String content;
    private LocalDate startTime;
    private LocalDate deadline;
    private int postHits;
    private Double latitude;
    private Double longitude;
}
