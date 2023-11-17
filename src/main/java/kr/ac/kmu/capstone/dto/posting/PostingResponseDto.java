package kr.ac.kmu.Capstone.dto.posting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostingResponseDto {

    private Long postId;
    private Long categoryId;
    private Long userId;
    private String nickname;
    private String title;
    // localdatetime을 제대로 넘기지 못하는 것으로 추정.. 이게 문제가 아닌가..
    //private LocalDateTime deadline; // "2023-11-22T01:01:00"
    private String deadline; //"2023-11-22 01:01:00"
    //private DateTime deadline;  // "deadline": {"year": 2023,"month": 11,"day": 22,"hour": 1,"minute": 1}
    private int postHits;


}