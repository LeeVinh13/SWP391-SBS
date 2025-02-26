package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceCategoryRequest {
    String name;
    String description;
    boolean signature;

}
