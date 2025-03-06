package vn.vinhdeptrai.skincarebookingsystem.controller;

import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.TherapistRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.TherapistResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.TherapistService;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/therapists")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TherapistController {
    TherapistService therapistService;

    @GetMapping
    public ApiResponse<List<TherapistResponse>> getAllTherapists() {
        return ApiResponse.<List<TherapistResponse>>builder()
                .result(therapistService.getAll())
                .build();
    }
    @PostMapping(value="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TherapistResponse> create(@RequestPart("therapist") TherapistRequest therapistRequest,
                                                 @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        return ApiResponse.<TherapistResponse>builder()
                .result(therapistService.create(therapistRequest,thumbnail))
                .build();
    }
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TherapistResponse> update(@RequestPart("therapist") TherapistRequest therapistRequest,
                                                 @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                                 @PathVariable("id") int id) throws IOException {
        return ApiResponse.<TherapistResponse>builder()
                .result(therapistService.update(therapistRequest,thumbnail,id))
                .build();
    }
    @DeleteMapping(value = "/delete/{id}")
    public ApiResponse<Void> update(@PathVariable("id") int id) throws IOException {
        therapistService.delete(id);
        return ApiResponse.<Void>builder()
                .build();
    }
}
