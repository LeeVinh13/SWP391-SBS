package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingRequest {
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private int stars;

    @Size(max = 255, message = "Feedback must be less than 255 characters")
    private String feedback;
}
