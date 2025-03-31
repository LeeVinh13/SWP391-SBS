package vn.vinhdeptrai.skincarebookingsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TherapistResponse {
    int id;
    String username, password, fullname, phone, email,description,image;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int experience;
    SlotStatus status;
}
