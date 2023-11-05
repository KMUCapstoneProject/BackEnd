package kr.ac.kmu.Capstone.dto.posting;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
public class PostingUpdateDto {

    // posthits 제외하고

    private Long categoryId;

    @NotBlank(message = "제목을 입력해주세요")
    @Pattern(regexp = "^.{2,50}$", message = "2 ~ 50 자리의 제목을 작성해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Pattern(regexp = "^.{2,500}$", message = "글자수는 2자 이상 500자 이하로 작성해주세요")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deadline;

    // 아이콘을 보여줄 위치 정보
    private Double latitude; // x

    private Double longitude; // y

    public PostingUpdateDto(String title, String content, LocalDate startTime, LocalDate deadline, Double latitude, Double longitude) {
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.deadline = deadline;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
