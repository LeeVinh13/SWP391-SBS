package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.CategoryServiceRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserCreationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserUpdateRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.CategoryServiceResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.UserResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categoryServices")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryServiceController {
    CategoryService categoryService;
    @GetMapping
    public ApiResponse<List<CategoryServiceResponse>> getAll() {
        return ApiResponse.<List<CategoryServiceResponse>>builder()
                .result(categoryService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryServiceResponse> getById(@PathVariable int id) {
        return ApiResponse.<CategoryServiceResponse>builder()
                .result(categoryService.getById(id))
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<CategoryServiceResponse> create(@RequestBody CategoryServiceRequest request) {
        ApiResponse<CategoryServiceResponse> apiRespone = new ApiResponse<>();
        apiRespone.setResult(categoryService.create(request));
        return apiRespone;
    }
    @PutMapping("/update/{id}")
    ApiResponse<CategoryServiceResponse> update(@PathVariable int id, @RequestBody CategoryServiceRequest categoryServiceRequest) {
        return ApiResponse.<CategoryServiceResponse>builder()
                .result(categoryService.update(id,categoryServiceRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    void deleteUser(@PathVariable int id) {
        this.categoryService.delete(id);
    }
}
