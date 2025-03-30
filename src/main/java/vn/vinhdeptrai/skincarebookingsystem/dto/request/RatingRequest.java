package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingRequest {
    @Size(min = 1, max = 5, message = "INVALID_RATING_STAR")
    @NotNull(message = "INVALID_RATING_STAR")
    private int stars;

    @Size(max = 255, message = "INVALID_FEEDBACK")
    private String feedback;
}
