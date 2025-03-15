package vn.vinhdeptrai.skincarebookingsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    boolean existsByAppointmentId(int appointmentId);
    Optional<Rating> findByAppointmentId(int appointmentId);
}