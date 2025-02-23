package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RoleRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserCreationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserUpdateRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.RoleResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.UserResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleController {
    RoleService roleService;
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRole() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRole())
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<RoleResponse> createUser(@RequestBody RoleRequest roleRequest) {
        ApiResponse<RoleResponse> apiRespone = new ApiResponse<>();
        apiRespone.setResult(roleService.create(roleRequest));
        return apiRespone;
    }
    @DeleteMapping("/delete/{roleId}")
    public ApiResponse<RoleResponse> deleteUser(@PathVariable int roleId) {
        roleService.delete(roleId);
        return ApiResponse.<RoleResponse>builder()
                .build();
    }

}
