package vn.vinhdeptrai.skincarebookingsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.jshell.Snippet;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.vinhdeptrai.skincarebookingsystem.enums.AppointmentStatus;
import vn.vinhdeptrai.skincarebookingsystem.enums.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentResponse {
    int id;
    String fullname, email,note;
    double price;
    double depositAmount;
    double remainingAmount;
    LocalDate date;
    LocalTime time;
    ServiceResponse service;
    TherapistResponse therapist;
    AppointmentStatus appointmentStatus;
    PaymentStatus paymentStatus;
}
