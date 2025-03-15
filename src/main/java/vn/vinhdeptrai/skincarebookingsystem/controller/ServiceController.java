package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.Service;

import java.io.IOException;
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
    @PostMapping(value="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ServiceResponse> create(@RequestPart("service") ServiceRequest serviceRequest,
                                               @RequestPart(value="thumbnail", required = false) MultipartFile thumbnail) throws IOException {

        return ApiResponse.<ServiceResponse>builder()
                .result(service.create(serviceRequest,thumbnail))
                .build();
    }
    @PutMapping("/update/{id}")
    ApiResponse<ServiceResponse> update(@PathVariable int id,@RequestPart("service") ServiceRequest serviceRequest,
                                        @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        return ApiResponse.<ServiceResponse>builder()
                .result(service.update(serviceRequest,id, thumbnail))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<ServiceResponse> deleteUser(@PathVariable int id) {
        this.service.delete(id);
        return ApiResponse.<ServiceResponse>builder()
                .build();
    }
}

