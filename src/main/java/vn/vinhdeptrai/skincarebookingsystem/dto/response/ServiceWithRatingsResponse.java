package vn.vinhdeptrai.skincarebookingsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceWithRatingsResponse {
    int id;
    String name;
    String description;
    double price;
    String duration;
    String thumbnail;
    ServiceCategoryResponse category;
    List<RatingResponse> ratings;
}
