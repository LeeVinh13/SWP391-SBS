package vn.vinhdeptrai.skincarebookingsystem.controller;

import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AppointmentRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.AppointmentResponse;
import vn.vinhdeptrai.skincarebookingsystem.enums.AppointmentStatus;
import vn.vinhdeptrai.skincarebookingsystem.enums.PaymentStatus;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;
import vn.vinhdeptrai.skincarebookingsystem.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AppointmentController {
    AppointmentService appointmentService;
    @GetMapping
    public ApiResponse<List<AppointmentResponse>> getAllAppointments() {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAllAppointments())
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<AppointmentResponse> getAppointmentById(@PathVariable int id) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.getAppointment(id))
                .build();
    }
    @GetMapping("/my-upcoming-appointment")
    public ApiResponse<List<AppointmentResponse>> getMyAppointment(){
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.myUpcomingAppointment())
                .build();
    }
    @GetMapping("/my-historical-appointment")
    public ApiResponse<List<AppointmentResponse>> getMyHistoricalAppointment(){
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.myHistoricalAppointment())
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<AppointmentResponse> create(@RequestBody AppointmentRequest appointmentRequest) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.create(appointmentRequest))
                .build();
    }
    @PutMapping("/update/{id}")
    public ApiResponse<AppointmentResponse> update(@RequestBody AppointmentRequest appointmentRequest, @PathVariable int id) {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.update(appointmentRequest,id))
                .build();
    }
    @PutMapping("/update-status/{id}")
    public ApiResponse<AppointmentResponse> updateStatus(@PathVariable int id,
                                                         @RequestParam(value ="aptStatus", required = false) AppointmentStatus appointmentStatus,
                                                         @RequestParam(value ="ptStatus", required = false) PaymentStatus paymentStatus) {
        appointmentService.updateStatus(appointmentStatus,paymentStatus,id);
        return ApiResponse.<AppointmentResponse>builder()
                .build();
    }
    @PutMapping("/cancel/{id}")
    public ApiResponse<Void> cancel(@PathVariable int id) {
        appointmentService.cancelAppointment(id);
        return ApiResponse.<Void>builder()
                .build();
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        appointmentService.delete(id);
        return ApiResponse.<Void>builder()
                .build();
    }

}
