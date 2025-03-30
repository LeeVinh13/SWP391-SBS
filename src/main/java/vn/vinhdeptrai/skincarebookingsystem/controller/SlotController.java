package vn.vinhdeptrai.skincarebookingsystem.controller;

import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.SlotRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.TherapistRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.SlotResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.TherapistResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;
import vn.vinhdeptrai.skincarebookingsystem.service.SlotService;
import vn.vinhdeptrai.skincarebookingsystem.service.TherapistService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SlotController {
    SlotService slotService;

    @GetMapping
    public ApiResponse<List<SlotResponse>> getAllSlots() {
        return ApiResponse.<List<SlotResponse>>builder()
                .result(slotService.getAllSlots())
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<SlotResponse> getSlot(@PathVariable int id) {
        return ApiResponse.<SlotResponse>builder()
                .result(slotService.getSlotsById(id))
                .build();
    }

    @GetMapping("/date-and-therapist")
    public ApiResponse<List<SlotResponse>> getSlotsByDateAndTherapist(@RequestParam("date") LocalDate date,
                                                                      @RequestParam("therapistId") int therapistId) {
        return ApiResponse.<List<SlotResponse>>builder()
                .result(slotService.getSlotsByDateAndTherapist(date,therapistId))
                .build();
    }
    @GetMapping("/available/date/{date}")
    public ApiResponse<List<SlotResponse>> getAvailableSlotsByDate(@PathVariable("date") LocalDate date) {
        return ApiResponse.<List<SlotResponse>>builder()
                .result(slotService.getAvailableSlotsByDate(date))
                .build();
    }
    @GetMapping("/available/therapist/{therapistId}")
    public ApiResponse<List<SlotResponse>> getAvailableSlotsByTherapist(@PathVariable("therapistId") int therapistId) {
        return ApiResponse.<List<SlotResponse>>builder()
                .result(slotService.getAvailableSlotsByTherapist(therapistId))
                .build();
    }
    @GetMapping("/therapist")
    public ApiResponse<List<SlotResponse>> getSlotForTherapist(@RequestParam("therapistId") int therapistId,
                                                         @RequestParam("startDate") LocalDate startDate,
                                                         @RequestParam("endDate") LocalDate endDate) {
        return ApiResponse.<List<SlotResponse>>builder()
                .result(slotService.getTherapistSchedule(therapistId,startDate,endDate))
                .build();
    }
    @GetMapping("/therapist/delete")
    public ApiResponse<Void> deleteScheduleByTherapistAndDate(@RequestParam("therapistId") int therapistId,
                                                              @RequestParam("date") LocalDate date) {
        slotService.deleteScheduleByTherapistAndDate(therapistId,date);
        return ApiResponse.<Void>builder()
                .build();
    }
    @GetMapping("/by-date")
    public ApiResponse<SlotResponse> getSlotsByDate(@RequestParam("date") LocalDate date) {
        return ApiResponse.<SlotResponse>builder()
                .result(slotService.getSlotsWithTherapistsByDate(date))
                .build();
    }

    @GetMapping("/by-date-range")
    public ApiResponse<List<SlotResponse>> getSlotsByDateRange(@RequestParam("startDate") LocalDate startDate,
                                                               @RequestParam("endDate") LocalDate endDate) {
        return ApiResponse.<List<SlotResponse>>builder()
                .result(slotService.getSlotsWithTherapistsByDateRange(startDate, endDate))
                .build();
    }

    @PostMapping("/generate/day")
    public ApiResponse<List<SlotResponse>> generateSlotForDay(@RequestBody SlotRequest slotRequest) {
        return ApiResponse.<List<SlotResponse>>builder()
                .result(slotService.generateSlotsForDate(slotRequest))
                .build();
    }

    @PostMapping("/generate/date-range")
    public ApiResponse<List<SlotResponse>> generateSlotForDateRange(@RequestParam("startDate") LocalDate startDate
            , @RequestParam("endDate") LocalDate endDate,@RequestParam("therapistIds") Set<Integer> therapistIds) {
        return ApiResponse.<List<SlotResponse>>builder()
                .result(slotService.generateSlotsForDateRange(startDate, endDate, therapistIds))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        slotService.delete(id);
        return ApiResponse.<Void>builder()
                .build();
    }

}
