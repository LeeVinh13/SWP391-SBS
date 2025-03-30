package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.hibernate.sql.Delete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.vinhdeptrai.skincarebookingsystem.entity.Slot;
import vn.vinhdeptrai.skincarebookingsystem.entity.SlotDetail;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SlotDetailRepository extends JpaRepository<SlotDetail, Integer> {
    Set<SlotDetail> findBySlotAndStatus(Slot slot, SlotStatus status);
    Optional<SlotDetail> findByTherapistAndSlot(Therapist therapist, Slot slot);
    @Modifying
    @Query("DELETE FROM SlotDetail WHERE id = :id")
    void deleteById(@Param("id") int id);
    @Modifying
    @Query("DELETE FROM SlotDetail sd WHERE sd.therapist.id = :therapistId")
    void deleteAllByTherapistId(@Param("therapistId") int therapistId);

    List<SlotDetail> findByTherapistAndSlot_Date(Therapist therapist, LocalDate slotDate);
}
