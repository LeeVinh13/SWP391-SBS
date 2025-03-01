package vn.vinhdeptrai.skincarebookingsystem.controller;
import java.util.List;


import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserCreationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserUpdateRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.UserResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.UserService;


@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    @GetMapping
    public ApiResponse<List<UserResponse>> getAll() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable int userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<UserResponse> create(@RequestBody UserCreationRequest request) {
        ApiResponse<UserResponse> apiRespone = new ApiResponse<>();
        apiRespone.setResult(userService.create(request));
        return apiRespone;
    }
    @PutMapping("/update/{userId}")
    ApiResponse<UserResponse> update(@PathVariable int userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.update(userId,userUpdateRequest))
                .build();
    }

    @DeleteMapping("/delete/{userId}")
    void deleteUser(@PathVariable int userId) {
        this.userService.delete(userId);
    }
    @GetMapping("/myInfor")
    public ApiResponse<UserResponse> myInfor(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.myInfor())
                .build();
    }


}
