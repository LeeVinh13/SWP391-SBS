package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.vinhdeptrai.skincarebookingsystem.validator.ConfirmPasswordConstraint;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfirmPasswordConstraint
public class RegisterRequest {
    @Size(min = 4, message = "INVALID_USERNAME")
    String username;
    @Size(min = 3,message = "INVALID_PASSWORD")
    String password;
    @NotNull
    String confirmPassword;
    @Email
    String email;
    @NotNull
    String fullname;
    String phone;

}
