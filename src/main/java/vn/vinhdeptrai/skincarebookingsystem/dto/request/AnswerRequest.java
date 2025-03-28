package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerRequest {
    @NotNull(message = "ANSWER_NOT_NULL")
    String answer;
    @NotNull(message = "ANSWER_SCORE_NOT_NULL")
    int score;

}
