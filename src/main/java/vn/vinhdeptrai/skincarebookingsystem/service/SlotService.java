package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.SlotRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.SlotResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Slot;
import vn.vinhdeptrai.skincarebookingsystem.entity.SlotDetail;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.SlotMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.SlotDetailRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.SlotRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.TherapistRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SlotService {
    SlotRepository slotRepository;
    SlotMapper slotMapper;
    TherapistRepository therapistRepository;
    SlotDetailRepository slotDetailRepository;
    List<LocalTime> WORKING_HOURS = List.of(
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

    public List<SlotResponse> getAllSlots() {
        return slotRepository.findAll().stream()
                .map(slot -> slotMapper.toSlotResponse(slot))
                .collect(Collectors.toList());
    }

    public SlotResponse getSlotsById(int id) {
        return slotMapper.toSlotResponse(slotRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SLOT_NOT_FOUND)));
    }

    public List<SlotResponse> getAvailableSlotsByTherapist(int therapistId) {
        Therapist therapist = therapistRepository.findById(therapistId).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        //Get slot with slotInfor
        return slotRepository.findBySlotDetails_TherapistAndSlotDetails_Status(therapist, SlotStatus.AVAILABLE)
                .stream().map(slot -> {
                    return slotMapper.toSlotResponse(slot);
                }).toList();
    }

    public List<SlotResponse> getAvailableSlotsByDate(LocalDate date) {

        List<Slot> slots = slotRepository.findByDate(date);
        //slot  2 2  2 2 2 2
        for (Slot slot : slots) {
            Set<SlotDetail> slotDetails = slotDetailRepository.findBySlotAndStatus(slot, SlotStatus.AVAILABLE);
            slot.setSlotDetails(slotDetails);
        }
        return slots.stream().map(slot -> slotMapper.toSlotResponse(slot)).collect(Collectors.toList());

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
    public List<SlotResponse> generateSlotsForDay(SlotRequest slotRequest) {
        List<Therapist> therapists = therapistRepository.findAllById(slotRequest.getTherapists());
        // Tạo danh sách Slot từ giờ + therapists
        List<Slot> slots = WORKING_HOURS.stream()
                //lọc ngày giờ KHÔNG trong db
                .filter(time -> !slotRepository.existsByDateAndTime(slotRequest.getDate(), time))
                .map(time -> {
                    Slot slot = slotMapper.toSlot(slotRequest);
                    slot.setTime(time);
                    return slotRepository.save(slot);
                }).toList();
        //create SlotInfor
        List<SlotDetail> slotDetails = createSlotInfor(slots, therapists);

        for(Slot slot : slots) {
            Set<SlotDetail> slotDetail = slotDetails.stream()
                    .filter(sd -> sd.getSlot().getId() == slot.getId())
                    .collect(Collectors.toSet());
            slot.setSlotDetails(slotDetail);
        }
        slotRepository.saveAll(slots);

        return slots.stream()
                .map(slot -> slotMapper.toSlotResponse(slot))
                .collect(Collectors.toList());
    }
    private List<SlotDetail> createSlotInfor(List<Slot> slots, List<Therapist> therapists) {
        List<SlotDetail> slotDetails = new ArrayList<>();
        for (Slot slot : slots) {
            for (Therapist therapist : therapists) {
                SlotDetail slotDetail = SlotDetail.builder()
                        .slot(slot)
                        .therapist(therapist)
                        .status(SlotStatus.AVAILABLE)
                        .build();
                slotDetails.add(slotDetail);
            }
        }
        slotDetailRepository.saveAll(slotDetails);
        return slotDetails;
    }
    //    public List<SlotResponse> generateSlotsForDateRange(
//            LocalDate startDate,
//            LocalDate endTime,
//            Set<Integer> therapistId) {
//        List<SlotResponse> allSlots = new ArrayList<>();
//        SlotRequest request = SlotRequest.builder()
//                .therapists(therapistId)
//                .build();
//        startDate.datesUntil(endTime.plusDays(1)).forEach((date) -> {
//            request.setDate(date);
//            List<SlotResponse> slotsForDay = generateSlotsForDay(request);
//            allSlots.addAll(slotsForDay);
//        });
//        return allSlots;
//    }

}
