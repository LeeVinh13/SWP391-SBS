package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceCategoryRequest {
    @NotNull(message = "INVALID_CATEGORY_NAME")
    String name;
    String description;
    boolean signature;

}
