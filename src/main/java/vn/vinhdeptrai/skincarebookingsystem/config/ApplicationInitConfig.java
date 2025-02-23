package vn.vinhdeptrai.skincarebookingsystem.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.vinhdeptrai.skincarebookingsystem.entity.Role;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;
import vn.vinhdeptrai.skincarebookingsystem.repository.RoleRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.UserRepository;

import java.util.HashSet;

@Slf4j
@Configuration
public class ApplicationInitConfig {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner initApplication() {
        return args -> {
            if(!userRepository.existsByUsername("admin")) {
                HashSet<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Admin role not found")));
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin account added ");
            }
        };
    }
}
