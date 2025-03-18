package vn.vinhdeptrai.skincarebookingsystem.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RatingRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.RatingResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.RatingService;

@RestController
@RequestMapping("/api/ratings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RatingController {
    RatingService ratingService;

    @PostMapping("/create/appointment/{appointmentId}")
    public ApiResponse<RatingResponse> createRating(@PathVariable int appointmentId, @RequestBody RatingRequest request) {
        return ApiResponse.<RatingResponse>builder()
                .result(ratingService.createRating(appointmentId, request))
                .build();
    }

    @GetMapping("/appointment/{appointmentId}")
    public ApiResponse<RatingResponse> getRatingByAppointment(@PathVariable int appointmentId) {
        return ApiResponse.<RatingResponse>builder()
                .result(ratingService.getRatingByAppointment(appointmentId))
                .build();
    }

    @GetMapping("/service/{serviceId}")
    public ApiResponse<List<RatingResponse>> getRatingByService(@PathVariable int serviceId) {
        return ApiResponse.<List<RatingResponse>>builder()
                .result(ratingService.getRatingByService(serviceId))
                .build();
    }
    
    @GetMapping("/all")
    public ApiResponse<List<RatingResponse>> getAllRatings() {
        return ApiResponse.<List<RatingResponse>>builder()
                .result(ratingService.getAllRatings())
                .build();
    }

    @DeleteMapping("/delete/{ratingId}")
    public ApiResponse<Void> deleteRating(@PathVariable int ratingId) {
        ratingService.deleteRating(ratingId);
        return ApiResponse.<Void>builder().build();
    }
    
}