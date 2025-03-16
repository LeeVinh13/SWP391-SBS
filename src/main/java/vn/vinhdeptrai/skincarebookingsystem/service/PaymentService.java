package vn.vinhdeptrai.skincarebookingsystem.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.config.VNPayConfig;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.PaymentRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.PaymentResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Appointment;
import vn.vinhdeptrai.skincarebookingsystem.enums.AppointmentStatus;
import vn.vinhdeptrai.skincarebookingsystem.enums.PaymentStatus;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.repository.AppointmentRepository;
import vn.vinhdeptrai.skincarebookingsystem.util.VNPayUtil;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    VNPayConfig vnPayConfig;
    private final AppointmentRepository appointmentRepository;

    public PaymentResponse createVnPayPayment(PaymentRequest paymentRequest, HttpServletRequest httpServletRequest) {
        Appointment appointment = appointmentRepository.findById(paymentRequest.getAppointmentId()).orElseThrow(
                () ->  new AppException(ErrorCode.APPOINTMENT_NOT_FOUND)
        );
        long amount = (long)appointment.getDepositAmount()*100L;
        String bankCode = paymentRequest.getBankCode();
        String vnp_TxnRef = UUID.randomUUID().toString();
        Map<String, String> vnPayParams = vnPayConfig.getVNPayConfig();
        vnPayParams.put("vnp_Amount", String.valueOf(amount));
        if(bankCode != null && !bankCode.isEmpty()) {
            vnPayParams.put("vnp_BankCode", bankCode);
        }
        //Mã tham chiếu của giao dịch tại hệ thống của merchant. Mã này là duy nhất dùng để phân biệt các đơn hàng gửi sang VNPAY
        vnPayParams.put("vnp_TxnRef", vnp_TxnRef);
        vnPayParams.put("vnp_IpAddr", VNPayUtil.getIpAddress(httpServletRequest));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnPayParams, true);
        String hashData = VNPayUtil.getPaymentURL(vnPayParams, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        appointment.setTxnRef(vnp_TxnRef);
        appointmentRepository.save(appointment);
        return PaymentResponse.builder()
                .code("00")
                .message("Payment URL created successfully")
                .paymentUrl(paymentUrl)
                .build();
    }
    public PaymentResponse handlerVNPayCallback(HttpServletRequest httpServletRequest) {
        Appointment appointment = appointmentRepository.findByTxnRef(httpServletRequest.getParameter("vnp_TxnRef")).orElseThrow(
                () -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND)
        );
        String vnp_ResponseCode = httpServletRequest.getParameter("vnp_ResponseCode");
        if(vnp_ResponseCode.equals("00")){
            appointment.setAppointmentStatus(AppointmentStatus.APPROVED);
            appointment.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
            appointmentRepository.save(appointment);
            return PaymentResponse.builder()
                    .code("00")
                    .message("Payment successfully")
                    .build();
        }
        appointment.getSlotDetail().setStatus(SlotStatus.AVAILABLE);
        appointment.setPaymentStatus(PaymentStatus.FAILED);
        appointment.setAppointmentStatus(AppointmentStatus.REJECTED);
        appointmentRepository.save(appointment);
        return PaymentResponse.builder()
                .code(vnp_ResponseCode)
                .message("Payment failed")
                .build();

    }
}