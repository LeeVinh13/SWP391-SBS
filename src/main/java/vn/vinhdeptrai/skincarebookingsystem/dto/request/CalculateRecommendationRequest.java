package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateRecommendationRequest {
    int quizId;
    List<Integer> answersIds;
}
