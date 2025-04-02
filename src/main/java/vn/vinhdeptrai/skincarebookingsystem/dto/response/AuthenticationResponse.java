package vn.vinhdeptrai.skincarebookingsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.vinhdeptrai.skincarebookingsystem.entity.Role;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    boolean authenticated;
    String token;
    String fullName;
    int id;
    Set<RoleResponse> roles;
}
