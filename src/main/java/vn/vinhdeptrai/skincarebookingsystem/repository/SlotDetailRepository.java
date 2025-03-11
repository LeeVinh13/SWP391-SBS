package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Slot;
import vn.vinhdeptrai.skincarebookingsystem.entity.SlotDetail;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SlotDetailRepository extends JpaRepository<SlotDetail, Integer> {
    Set<SlotDetail> findBySlotAndStatus(Slot slot, SlotStatus status);

    Optional<SlotDetail> findByTherapistAndSlot(Therapist therapist, Slot slot);
}
