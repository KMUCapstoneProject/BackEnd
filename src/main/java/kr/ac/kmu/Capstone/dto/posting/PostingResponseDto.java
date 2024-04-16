package kr.ac.kmu.Capstone.dto.posting;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostingResponseDto {

    private Long postId;
    private Long userId;
    private Long categoryId;
    private String nickname;
    private String title;
    private String content;
    private String startTime;
    private String deadline;
    private int postHits;
    private Double latitude;
    private Double longitude;
    private int status;
    private String details;
    private List<String> imgUrl;
}