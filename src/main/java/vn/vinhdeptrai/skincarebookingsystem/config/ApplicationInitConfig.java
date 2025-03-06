package vn.vinhdeptrai.skincarebookingsystem.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.vinhdeptrai.skincarebookingsystem.constant.PredefinedRole;
import vn.vinhdeptrai.skincarebookingsystem.entity.Role;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;
import vn.vinhdeptrai.skincarebookingsystem.repository.RoleRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
public class ApplicationInitConfig {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner initApplication() {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()) {
                List<User> users = new ArrayList<>();
                //admin
                Role adminRole = roleRepository.save(Role.builder().name(PredefinedRole.ADMIN_ROLE).build());
                User admin = User.builder()
                        .username("admin")
//                        .password(passwordEncoder.encode("admin"))
                        .password("admin")
                        .role(Set.of(adminRole))
                        .build();
                users.add(admin);
                //staff
                Role staffRole = roleRepository.save(Role.builder().name(PredefinedRole.STAFF_ROLE).build());
                User staff = User.builder()
                        .username("staff")
//                        .password(passwordEncoder.encode("staff"))
                        .password("staff")
                        .role(Set.of(staffRole))
                        .build();
                users.add(staff);
                //therapist
                Role therapistRole = roleRepository.save(Role.builder().name(PredefinedRole.THERAPIST_ROLE).build());
                User therapist = User.builder()
                        .username("therapist")
//                        .password(passwordEncoder.encode("therapist"))
                        .password("therapist")
                        .role(Set.of(therapistRole))
                        .build();
                users.add(therapist);
                //user
                Role userRole = roleRepository.save(Role.builder().name(PredefinedRole.USER_ROLE).build());
                User user = User.builder()
                        .username("user")
//                        .password(passwordEncoder.encode("user"))
                        .password("user")
                        .role(Set.of(userRole))
                        .build();
                users.add(user);
                userRepository.saveAll(users);
            }
        };
    }
}
