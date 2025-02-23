package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
//import org.springframework.security.access.prepost.PostAuthorize;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserCreationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserUpdateRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.UserResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Role;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.UserMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.RoleRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.UserRepository;
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

    public UserResponse create(UserCreationRequest userCreationRequest)  {
        if(userRepository.existsByUsername((userCreationRequest.getUsername()))){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(userCreationRequest);
        Role role = roleRepository.findByName("USER").orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setRole(Set.of(role));
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
    public UserResponse update(int id, UserUpdateRequest userUpdateRequest){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, userUpdateRequest);
        user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        Set<Role> roles = userUpdateRequest.getRole().stream().map(roleName -> roleRepository.findByName(roleName).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND)
        )).collect(Collectors.toSet());
        user.setRole((roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(int id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
    public UserResponse getUser(int id){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUser(){
        //lỗi null
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> userMapper.toUserResponse(user)).toList();
    }
}
