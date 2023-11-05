package kr.ac.kmu.Capstone.dto.posting;

import lombok.Builder;

import java.sql.Timestamp;
import java.time.LocalDate;

@Builder
public class PostingResponseDto {

    private Long postId;
    private Long categoryId;
    private Long userId;
    private String nickname;
    private String title;
    private LocalDate deadline;
    private int postHits;


}