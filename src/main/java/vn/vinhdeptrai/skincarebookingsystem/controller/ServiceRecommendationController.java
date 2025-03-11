package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import vn.vinhdeptrai.skincarebookingsystem.dto.request.CalculateRecommendationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceRecommendationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceRecommendationResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.ServiceRecommendationService;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceRecommendationController {
    ServiceRecommendationService serviceRecommendationService;

    @GetMapping
    public ApiResponse<List<ServiceRecommendationResponse>> getAll() {
        return ApiResponse.<List<ServiceRecommendationResponse>>builder()
                .result(serviceRecommendationService.getServiceRecommendationList())
                .build();
    }

   @GetMapping("/{serviceRecommendationId}")
   public ApiResponse<ServiceRecommendationResponse> getServiceRecommendationById(@PathVariable int serviceRecommendationId) {
       return ApiResponse.<ServiceRecommendationResponse>builder()
               .result(serviceRecommendationService.getServiceRecommendation(serviceRecommendationId))
               .build();
   }

   @PostMapping("/create")
   public ApiResponse<ServiceRecommendationResponse> create(@RequestBody ServiceRecommendationRequest request) {
       return ApiResponse.<ServiceRecommendationResponse>builder()
               .result(serviceRecommendationService.create(request))
               .build();
   }

   @PutMapping("/update/{serviceRecommendationId}")
   ApiResponse<ServiceRecommendationResponse> update(@PathVariable int serviceRecommendationId, @RequestBody ServiceRecommendationRequest request) {
       return ApiResponse.<ServiceRecommendationResponse>builder()
               .result(serviceRecommendationService.update(serviceRecommendationId, request))
               .build();
   }

   @DeleteMapping("/delete/{serviceRecommendationId}")
   void deleteServiceRecommendation(@PathVariable int serviceRecommendationId) {
       this.serviceRecommendationService.delete(serviceRecommendationId);
   }

   @PostMapping("/calculate-recommendation")
   public ApiResponse<Set<ServiceRecommendationResponse>> calculateRecommendation(@RequestBody CalculateRecommendationRequest request) {
       //nhận vào 1 list các id của các câu trả lời
       //trả về danh sách các dịch vụ được đề xuất dựa trên điểm số của các câu trả lời
       return ApiResponse.<Set<ServiceRecommendationResponse>>builder()
               .result(serviceRecommendationService.calculateRecommendation(request))
               .build();
   }
   
}
