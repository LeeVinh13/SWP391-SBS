package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import vn.vinhdeptrai.skincarebookingsystem.enums.AppointmentStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    LocalDate createAt = LocalDate.now();
    String note;
    @Enumerated(EnumType.STRING)
    AppointmentStatus status;
    @ManyToOne
    @JoinColumn(name ="userId")
    User user;

    @OneToOne
    SlotDetail slotDetail;
    @ManyToOne
    @JoinColumn(name = "serviceId")
    Service service;
}
