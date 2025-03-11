package vn.vinhdeptrai.skincarebookingsystem.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.constant.PredefinedRole;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AppointmentRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserCreationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.UserUpdateRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.AppointmentResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.UserResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.*;
import vn.vinhdeptrai.skincarebookingsystem.enums.AppointmentStatus;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.AppointmentMapper;
import vn.vinhdeptrai.skincarebookingsystem.mapper.UserMapper;
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

    public AppointmentResponse getAppointment(int id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        return appointmentMapper.toAppointmentResponse(appointment);
    }
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(
                appointment -> appointmentMapper.toAppointmentResponse(appointment)
        ).collect(Collectors.toList());
    }
    public AppointmentResponse create(AppointmentRequest appointmentRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        vn.vinhdeptrai.skincarebookingsystem.entity.Service service = getService(appointmentRequest.getServiceId());
        SlotDetail slotDetail = getAvailableSlotDetail(appointmentRequest.getSlotId(), appointmentRequest.getTherapistId());

        Appointment appointment = appointmentMapper.toAppointment(appointmentRequest);
        appointment.setUser(user);
        appointment.setCreateAt(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setService(service);
        appointment.setSlotDetail(slotDetail);


        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }
    public AppointmentResponse update(AppointmentRequest appointmentRequest, int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND)
        );

            vn.vinhdeptrai.skincarebookingsystem.entity.Service newService = getService(appointmentRequest.getServiceId());
            SlotDetail newSlotDetail = getAvailableSlotDetail(appointmentRequest.getSlotId(), appointmentRequest.getTherapistId());

            updateSlotDetailStatus(appointment.getSlotDetail(), SlotStatus.AVAILABLE);

            appointment.setNote(appointmentRequest.getNote());
            appointment.setUpdateAt(LocalDateTime.now());
            appointment.setService(newService);
            appointment.setSlotDetail(newSlotDetail);

            return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
        }
    public void cancelAppointment(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND)
        );
        if(appointment.getSlotDetail().getStatus() == SlotStatus.AVAILABLE) {
            throw new AppException(ErrorCode.APPOINTMENT_CANCELLED);
        }
        appointment.setStatus(AppointmentStatus.REJECTED);
        updateSlotDetailStatus(appointment.getSlotDetail(), SlotStatus.AVAILABLE);
        appointmentRepository.save(appointment);
    }
    public void delete(int id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        updateSlotDetailStatus(appointment.getSlotDetail(), SlotStatus.AVAILABLE);
        appointment.getSlotDetail().setAppointment(null);
        appointmentRepository.deleteById(id);
    }

    private SlotDetail getAvailableSlotDetail(int slotId, int therapistId) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(
                () -> new AppException(ErrorCode.SLOT_NOT_FOUND)
        );
        Therapist therapist = therapistRepository.findById(therapistId).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        SlotDetail slotDetail = slotDetailRepository.findByTherapistAndSlot(therapist, slot).orElseThrow(
                () -> new AppException(ErrorCode.SLOT_NOT_FOUND)
        );
        if(!slotDetail.getStatus().equals(SlotStatus.AVAILABLE)){
            throw new AppException(ErrorCode.SLOT_NOT_AVAILABLE);
        }
        updateSlotDetailStatus(slotDetail, SlotStatus.UNAVAILABLE);

        return  slotDetail;
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

}
