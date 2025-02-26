package vn.vinhdeptrai.skincarebookingsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceResponse {
    String name;
    String description;
    long price;
    String duration;
    String image;

}
