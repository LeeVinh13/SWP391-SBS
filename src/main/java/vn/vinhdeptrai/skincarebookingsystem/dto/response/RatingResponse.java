package vn.vinhdeptrai.skincarebookingsystem.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingResponse {
    private int id;
    private int stars;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
