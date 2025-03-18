package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import vn.vinhdeptrai.skincarebookingsystem.entity.Appointment;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceCategory;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TherapistRequest {
    String username, password, fullname, phone, email,description;
    int experience;
//    MultipartFile image;
}
