package kr.ac.kmu.Capstone.dto.posting;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostingResponseDto {

    private Long postId;
    private Long categoryId;
    private Long userId;
    private String nickname;
    private String title;
    private LocalDateTime deadline;
    private int postHits;


}