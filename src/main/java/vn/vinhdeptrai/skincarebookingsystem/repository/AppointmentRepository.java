package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Appointment;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;
import vn.vinhdeptrai.skincarebookingsystem.enums.AppointmentStatus;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByUser(User user);
    Optional<Appointment> findByTxnRef(String txnRef);
    List<Appointment> findByAppointmentStatusAndCancelAtBefore(AppointmentStatus appointmentStatus, LocalDateTime cancelAtBefore);
    List<Appointment> findByUserAndAppointmentStatusIn(User user, List<AppointmentStatus> appointmentStatuses);
}
