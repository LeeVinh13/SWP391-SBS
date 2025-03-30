package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.SlotResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Slot;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Repository

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    List<Slot> findByDate(LocalDate date);
    List<Slot> findBySlotDetails_TherapistAndSlotDetails_Status(Therapist slotDetailsTherapist, SlotStatus slotDetailsStatus);
    List<Slot> findByDateAndSlotDetails_Therapist(LocalDate date, Therapist slotDetailsTherapist);
    List<Slot> findBySlotDetails_TherapistAndDateBetween(Therapist slotDetailsTherapist, LocalDate  dateBefored, LocalDate ateAfter);

    void deleteBySlotDetails_Therapist(Therapist slotDetailsTherapist);

    void deleteBySlotDetails_TherapistAndDate(Therapist slotDetailsTherapist, LocalDate date);
}
