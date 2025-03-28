package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @Size(min = 3,message = "INVALID_PASSWORD")
    @NotNull(message = "INVALID_PASSWORD")
    String oldPassword;

    @Size(min = 3,message = "INVALID_PASSWORD")
    @NotNull(message = "INVALID_PASSWORD")
    String newPassword;

    @Size(min = 3,message = "INVALID_PASSWORD")
    @NotNull(message = "INVALID_PASSWORD")
    String confirmPassword;
} 