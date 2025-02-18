package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleService roleService;
    RoleRepository roleRepository;
    public UserResponse create(UserCreationRequest userCreationRequest){
        if(userRepository.existsByUsername((userCreationRequest.getUsername()))){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(userCreationRequest);
//        Role role = roleRepository.findAllById();
//        user.set
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
    public UserResponse update(int id, UserUpdateRequest userUpdateRequest){
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, userUpdateRequest);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public void delete(int id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
    public UserResponse getUser(int id){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }
    public List<UserResponse> getAllUser(){
        //lỗi null
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> userMapper.toUserResponse(user)).toList();
    }
}
