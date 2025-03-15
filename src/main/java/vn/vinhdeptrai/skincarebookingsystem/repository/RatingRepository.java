package vn.vinhdeptrai.skincarebookingsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.vinhdeptrai.skincarebookingsystem.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    boolean existsByAppointmentId(int appointmentId);
    Optional<Rating> findByAppointmentId(int appointmentId);
    @Query("SELECT r FROM Rating r " +
           "JOIN r.appointment a " +
           "JOIN a.service s " +
           "WHERE s.id = :serviceId")
    List<Rating> findByServiceId(@Param("serviceId") int serviceId);
}