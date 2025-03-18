package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceCategoryRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceCategoryResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.ServiceCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ServiceCategoryController {
    ServiceCategoryService categoryService;
    @GetMapping
    public ApiResponse<List<ServiceCategoryResponse>> getAll() {
        return ApiResponse.<List<ServiceCategoryResponse>>builder()
                .result(categoryService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ServiceCategoryResponse> getById(@PathVariable int id) {
        return ApiResponse.<ServiceCategoryResponse>builder()
                .result(categoryService.getById(id))
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<ServiceCategoryResponse> create(@RequestBody ServiceCategoryRequest request) {
        ApiResponse<ServiceCategoryResponse> apiRespone = new ApiResponse<>();
        apiRespone.setResult(categoryService.create(request));
        return apiRespone;
    }
    @PutMapping("/update/{id}")
    ApiResponse<ServiceCategoryResponse> update(@PathVariable int id, @RequestBody ServiceCategoryRequest serviceCategoryRequest) {
        return ApiResponse.<ServiceCategoryResponse>builder()
                .result(categoryService.update(id, serviceCategoryRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<ServiceCategoryResponse> delete(@PathVariable int id) {
        this.categoryService.delete(id);
        return ApiResponse.<ServiceCategoryResponse>builder()
                .build();
    }
}
