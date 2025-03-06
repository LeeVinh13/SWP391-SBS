package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Appointment;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;

import java.util.Optional;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
}
