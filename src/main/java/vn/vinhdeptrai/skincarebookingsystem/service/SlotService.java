package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.SlotRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.SlotResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Slot;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.SlotMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.SlotRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.TherapistRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SlotService {
    SlotRepository slotRepository;
    SlotMapper slotMapper;
    TherapistRepository therapistRepository;

    public List<SlotResponse> getAllSlots() {
        return slotRepository.findAll().stream()
                .map(slot -> slotMapper.toSlotResponse(slot))
                .collect(Collectors.toList());
    }
    public SlotResponse getSlotsById(int id) {
        return slotMapper.toSlotResponse(slotRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SLOT_NOT_FOUND)));
    }
    public List<SlotResponse> getSlotsByTherapist(int therapistId) {
        Therapist therapist = therapistRepository.findById(therapistId).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        return slotRepository.findByTherapists(therapist).stream().map(
                slot -> slotMapper.toSlotResponse(slot)
        ).toList();
    }
    public void deleteSlot(int slotId) {
        slotRepository.deleteById(slotId);
    }
    public List<SlotResponse> getSlotsByDate(LocalDate date) {
        return slotRepository.findByDate(date).stream()
                .map(slot -> slotMapper.toSlotResponse(slot))
                .collect(Collectors.toList());
    }
    //Tạo slot làm việc từ 9:00 đến 18:00 theo ngày
    public List<SlotResponse> generateSlotsForDay(LocalDate date, Set<Integer> therapist) {
        // Danh sách giờ cố định (9h - 18h)
        List<LocalTime> workingHours = List.of(
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                LocalTime.of(17, 0),
                LocalTime.of(18, 0)
        );
        // Lấy danh sách Therapist từ database
        List<Therapist> therapists = therapistRepository.findAllById(therapist);
        // Tạo danh sách Slot từ giờ + therapists
        List<Slot> slots = workingHours.stream()
                .filter(time -> !slotRepository.existsByDateAndTime(date, time))
                .map(time -> {
                    Slot slot = new Slot();
                    slot.setDate(date);
                    slot.setTime(time);
                    slot.setTherapists(therapists);
                    return slotRepository.save(slot);
                }).toList();

        return slots.stream()
                .map((slot) -> slotMapper.toSlotResponse(slot))
                .collect(Collectors.toList());
//        List<Slot> slots = new ArrayList<>();
//        for (LocalTime time : workingHours) {
//            Slot slot = slotMapper.toSlot(slotRequest);
//            slot.setTime(time);
//            slot.setTherapists(therapists);
//            slotRepository.save(slot);
//            slots.add(slot);
//        }
//        List<SlotResponse> slotResponses = slots.stream().map((slot) -> {
//            return slotMapper.toSlotResponse(slot);
//        }).toList();
//        return slotResponses;
    }

    public List<SlotResponse> generateSlotsForDateRange(SlotRequest slotRequest) {
        List<SlotResponse> allSlots = new ArrayList<>();
        slotRequest.getStartDate().datesUntil(slotRequest.getEndDate().plusDays(1)).forEach((date) ->{
            List<SlotResponse> slotsForDay = generateSlotsForDay(date, slotRequest.getTherapists());
            allSlots.addAll(slotsForDay);
        });
        return allSlots;
    }


}
