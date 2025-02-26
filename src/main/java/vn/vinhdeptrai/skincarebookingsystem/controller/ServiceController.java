package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.CategoryServiceRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.CategoryServiceResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.CategoryService;
import vn.vinhdeptrai.skincarebookingsystem.service.Service;

import java.util.List;
@RestController
@RequestMapping("/api/services")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ServiceController {
    Service service;
    @GetMapping
    public ApiResponse<List<ServiceResponse>> getAll() {
        return ApiResponse.<List<ServiceResponse>>builder()
                .result(service.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ServiceResponse> getById(@PathVariable int id) {
        return ApiResponse.<ServiceResponse>builder()
                .result(service.getById(id))
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<ServiceResponse> create(@RequestBody ServiceRequest request) {
        ApiResponse<ServiceResponse> apiRespone = new ApiResponse<>();
        apiRespone.setResult(service.create(request));
        return apiRespone;
    }
    @PutMapping("/update/{id}")
    ApiResponse<ServiceResponse> update(@PathVariable int id, @RequestBody ServiceRequest serviceRequest) {
        return ApiResponse.<ServiceResponse>builder()
                .result(service.update(serviceRequest,id))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    void deleteUser(@PathVariable int id) {
        this.service.delete(id);
    }
}

