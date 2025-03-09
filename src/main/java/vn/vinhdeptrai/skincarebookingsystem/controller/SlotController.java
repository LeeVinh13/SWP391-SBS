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
import vn.vinhdeptrai.skincarebookingsystem.service.SlotService;
import vn.vinhdeptrai.skincarebookingsystem.service.TherapistService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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


    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        slotService.deleteSlot(id);
        return ApiResponse.<Void>builder()
                .build();
    }

}
