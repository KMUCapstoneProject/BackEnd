package kr.ac.kmu.Capstone.dto.posting;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kr.ac.kmu.Capstone.entity.Category;
import kr.ac.kmu.Capstone.entity.Posting;
import kr.ac.kmu.Capstone.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostingSaveDto {

    @NotBlank(message = "제목을 입력해주세요")
    @Pattern(regexp = "^.{2,50}$", message = "2 ~ 50 자리의 제목을 작성해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Pattern(regexp = "^.{2,500}$", message = "글자수는 2자 이상 500자 이하로 작성해주세요")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime deadline;

    private int postHits; //조회수

    // 아이콘을 보여줄 위치 정보
    private Double latitude; // x

    private Double longitude; // y


    public Posting toEntity(User user, Category category) {
        return Posting.builder()
                .user(user)
                .category(category)
                .title(title)
                .content(content)
                .startTime(startTime)
                .deadline(deadline)
                .postHits(0)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }


}
