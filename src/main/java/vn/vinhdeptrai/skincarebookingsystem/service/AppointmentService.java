package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AppointmentRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.AppointmentResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.*;
import vn.vinhdeptrai.skincarebookingsystem.enums.AppointmentStatus;
import vn.vinhdeptrai.skincarebookingsystem.enums.PaymentStatus;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.AppointmentMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService {
    AppointmentRepository appointmentRepository;
    AppointmentMapper appointmentMapper;
    UserRepository userRepository;
    SlotRepository slotRepository;
    TherapistRepository therapistRepository;
    ServiceRepository serviceRepository;
    SlotDetailRepository slotDetailRepository;

    public List<AppointmentResponse> myUpcomingAppointment() {
        User user = getUser();
        List<AppointmentResponse> appointments = appointmentRepository.findByUserAndPaymentStatus(user, PaymentStatus.PARTIALLY_PAID)
                .stream()
                .filter(apt -> isUpcomingAppointment(apt.getSlotDetail().getSlot().getDate()))
                .map(apt -> {
                    return appointmentMapper.toAppointmentResponse(apt);
                })
                .toList();
        //stream không bgio trả về null, toList có thể trả về mảng rỗng nhưng cũng ko null
        if (appointments.isEmpty()) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }
        return appointments;
    }

    public List<AppointmentResponse> myHistoricalAppointment() {
        User user = getUser();
        List<AppointmentResponse> appointments = appointmentRepository.findByUserAndPaymentStatus(user, PaymentStatus.PAID).stream()
                .filter(apt -> !isUpcomingAppointment(apt.getSlotDetail().getSlot().getDate()))
                .map(apt -> {
                        return appointmentMapper.toAppointmentResponse(apt)
                    ;})
                .toList();
        if (appointments.isEmpty()) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }
        return appointments;
    }

    public AppointmentResponse getAppointment(int id) {
        Appointment appointment = getAppointmentById(id);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }
        return appointments.stream().map(
                appointment -> appointmentMapper.toAppointmentResponse(appointment)
        ).collect(Collectors.toList());
    }

    public AppointmentResponse create(AppointmentRequest appointmentRequest) {
        User user = getUser();
        vn.vinhdeptrai.skincarebookingsystem.entity.Service service = getService(appointmentRequest.getServiceId());
        SlotDetail slotDetail = getAvailableSlotDetail(appointmentRequest.getSlotId(), appointmentRequest.getTherapistId());
        Appointment appointment = appointmentMapper.toAppointment(appointmentRequest);
        appointment.setUser(user);
        appointment.setCreateAt(LocalDateTime.now());
        appointment.setService(service);
        appointment.setSlotDetail(slotDetail);
        calculateAndSetAmount(appointment, service);
        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    public AppointmentResponse update(AppointmentRequest appointmentRequest, int appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        SlotDetail newSlotDetail = getAvailableSlotDetail(appointmentRequest.getSlotId(), appointmentRequest.getTherapistId());
        updateSlotDetailStatus(appointment.getSlotDetail(), SlotStatus.AVAILABLE);
        appointment.setSlotDetail(newSlotDetail);
        appointment.setUpdateAt(LocalDateTime.now());
        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    public void cancelAppointment(int appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment.getSlotDetail().getStatus() == SlotStatus.AVAILABLE) {
            throw new AppException(ErrorCode.APPOINTMENT_CANCELLED);
        }
        appointment.setCancelAt(LocalDateTime.now());
        updateStatus(appointment.getAppointmentStatus(), appointment.getPaymentStatus(), appointmentId);
        updateSlotDetailStatus(appointment.getSlotDetail(), SlotStatus.AVAILABLE);
        appointmentRepository.save(appointment);
    }

    public void updateStatus(AppointmentStatus appointmentStatus, PaymentStatus paymentStatus, int appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointmentStatus != null) {
            appointment.setAppointmentStatus(appointmentStatus);
        }
        if (paymentStatus != null) {
            appointment.setPaymentStatus(paymentStatus);
        }
        appointmentRepository.save(appointment);
    }

    public void delete(int id) {
        Appointment appointment = getAppointmentById(id);
        updateSlotDetailStatus(appointment.getSlotDetail(), SlotStatus.AVAILABLE);
        appointment.getSlotDetail().setAppointment(null);
        appointmentRepository.deleteById(id);
    }

    private SlotDetail getAvailableSlotDetail(int slotId, int therapistId) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(
                () -> new AppException(ErrorCode.SLOT_NOT_FOUND)
        );
        Therapist therapist;
        if (therapistId <= 0) {
            Set<SlotDetail> availableSlots = slotDetailRepository.findBySlotAndStatus(slot, SlotStatus.AVAILABLE);
            if (availableSlots.isEmpty()) {
                throw new AppException(ErrorCode.SLOT_NOT_AVAILABLE);
            }
            therapist = availableSlots.iterator().next().getTherapist();
        } else {
            therapist = therapistRepository.findById(therapistId).orElseThrow(
                    () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
            );
        }
        SlotDetail slotDetail = slotDetailRepository.findByTherapistAndSlot(therapist, slot).orElseThrow(
                () -> new AppException(ErrorCode.SLOT_NOT_FOUND)
        );
        if (!slotDetail.getStatus().equals(SlotStatus.AVAILABLE)) {
            throw new AppException(ErrorCode.SLOT_NOT_AVAILABLE);
        }
        updateSlotDetailStatus(slotDetail, SlotStatus.UNAVAILABLE);
        return slotDetail;
    }

    private vn.vinhdeptrai.skincarebookingsystem.entity.Service getService(int serviceId) {
        return serviceRepository.findById(serviceId).orElseThrow(
                () -> new AppException(ErrorCode.SERVICE_NOT_FOUND)
        );
    }

    private void updateSlotDetailStatus(SlotDetail slotDetail, SlotStatus status) {
        slotDetail.setStatus(status);
        slotDetailRepository.save(slotDetail);
    }

    private Appointment getAppointmentById(int id) {
        return appointmentRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND)
        );
    }

    private User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private boolean isUpcomingAppointment(LocalDate appointmentDate) {
        return appointmentDate.isAfter(LocalDate.now());
    }

    private void calculateAndSetAmount(Appointment appointment, vn.vinhdeptrai.skincarebookingsystem.entity.Service service) {
        double price = service.getPrice();
        double deposit = price * 0.3;
        double remaining = price - deposit;
        appointment.setPrice(price);
        appointment.setDepositAmount(deposit);
        appointment.setRemainingAmount(remaining);
    }
    //tự động cập nhật appointment khi đang pending quá 15p định kỳ 5p
    @Scheduled(fixedRate = 300000)
    private void handlePendingAppointments() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
        List<Appointment> pendingAppointments = appointmentRepository.findByAppointmentStatusAndCreateAtBefore(AppointmentStatus.PENDING, fifteenMinutesAgo);
        //chi
        if (!pendingAppointments.isEmpty()) {
            for (Appointment appointment : pendingAppointments) {
                appointment.setCancelAt(LocalDateTime.now());
                updateStatus(AppointmentStatus.REJECTED, PaymentStatus.CANCELLED, appointment.getId());
                updateSlotDetailStatus(appointment.getSlotDetail(), SlotStatus.AVAILABLE);
                appointmentRepository.save(appointment);
            }
        }
    }
    //tự động xóa appointment bị cancel lúc 00:00
    @Scheduled(cron = "0 0 0 * * *")
    private void cleanupCancelledAppointments() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        List<Appointment> cancelledAppointment = appointmentRepository.findByAppointmentStatusAndCancelAtBefore(AppointmentStatus.REJECTED, oneDayAgo);
        for (Appointment appointment : cancelledAppointment) {
            delete(appointment.getId());
        }
    }
}
