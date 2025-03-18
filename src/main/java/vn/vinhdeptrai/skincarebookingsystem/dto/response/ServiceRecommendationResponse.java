package vn.vinhdeptrai.skincarebookingsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceRecommendationResponse {
    long id;
    int quiz_id;
    String quiz_title;
    int service_id;
    String service_name;
    int minScore;
    int maxScore;
}
