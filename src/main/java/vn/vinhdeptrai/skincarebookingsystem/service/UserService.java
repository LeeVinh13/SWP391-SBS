package vn.vinhdeptrai.skincarebookingsystem.service;

import com.nimbusds.jose.proc.SecurityContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
//import org.springframework.security.access.prepost.PostAuthorize;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vinhdeptrai.skincarebookingsystem.constant.PredefinedRole;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ChangePasswordRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserCreationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserUpdateRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.UserResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.*;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.UserMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.*;
import vn.vinhdeptrai.skincarebookingsystem.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleService roleService;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    private final SlotDetailRepository slotDetailRepository;
    private final TherapistRepository therapistRepository;
    private final AppointmentRepository appointmentRepository;

    public UserResponse create(UserCreationRequest userCreationRequest) {
        if (userRepository.existsByUsername((userCreationRequest.getUsername()))) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(userCreationRequest);
        Role role = roleRepository.findByName(PredefinedRole.USER_ROLE).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRole(Set.of(role));
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public UserResponse update(int id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, userUpdateRequest);
//        if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty()) {
//            user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
//        }
        if (userUpdateRequest.getRole() != null) {
            Set<Role> roles = userUpdateRequest.getRole().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                    .collect(Collectors.toSet());
            user.setRole((roles));
        }
        return userMapper.toUserResponse(userRepository.save(user));
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getRole().stream().anyMatch(role -> role.getName().equals(PredefinedRole.THERAPIST_ROLE))) {
            Therapist therapist = therapistRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.THERAPIST_NOT_FOUND));
            appointmentRepository.deleteAppointmentsByTherapistId(therapist.getId());
            slotDetailRepository.deleteAllByTherapistId(therapist.getId());
            therapistRepository.delete(therapist);
        }
        userRepository.delete(user);
    }

    public UserResponse myInfor() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public UserResponse getById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public List<UserResponse> getAllUser() {
        //lỗi null
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return users.stream().map(user -> userMapper.toUserResponse(user)).toList();
    }

    public void changePassword(ChangePasswordRequest request) {
        // current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // check old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }

        // check new password and confirm password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCH);
        }

        // update new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
