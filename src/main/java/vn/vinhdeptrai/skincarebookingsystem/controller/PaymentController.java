package vn.vinhdeptrai.skincarebookingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.PaymentRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.PaymentResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentController {
    PaymentService paymentService;
    @PostMapping("/vn-pay")
    public ApiResponse<PaymentResponse> pay(@RequestBody PaymentRequest paymentRequest, HttpServletRequest req) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.createVnPayPayment(paymentRequest, req))
                .build();
    }
    @GetMapping("/vn-pay-callback")
    public ApiResponse<PaymentResponse> payCallbackHandler(HttpServletRequest request) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.handlerVNPayCallback(request))
                .build();
    }
}
