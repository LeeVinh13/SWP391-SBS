package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AppointmentRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.AppointmentResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Appointment toAppointment(AppointmentRequest appointmentRequest);
    @Mapping(target ="email", source = "user.email")
    @Mapping(target ="fullname",source = "user.fullname")
    @Mapping(target = "date", source = "slotDetail.slot.date")
    @Mapping(target = "time", source = "slotDetail.slot.time")
    @Mapping(target="therapist", source = "slotDetail.therapist")
    @Mapping(target="service", source = "service")
    AppointmentResponse toAppointmentResponse(Appointment appointment);
}
