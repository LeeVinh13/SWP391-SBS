package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceRequest {
    String name;
    String description;
    double price;
    String duration;
    MultipartFile thumbnail;
    int categoryId;
}
