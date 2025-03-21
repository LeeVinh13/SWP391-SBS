package vn.vinhdeptrai.skincarebookingsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.PaymentRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.PaymentResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.PaymentService;

import java.io.IOException;

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
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PaymentResponse paymentResponse = paymentService.handlerVNPayCallback(request);
        if (paymentResponse.getRedirectUrl() != null) {
            response.sendRedirect(paymentResponse.getRedirectUrl());
        }
    }
}
