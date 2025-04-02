package vn.vinhdeptrai.skincarebookingsystem.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.SlotRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.SlotResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.TherapistResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Slot;
import vn.vinhdeptrai.skincarebookingsystem.entity.SlotDetail;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.SlotMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.AppointmentRepository;
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
            LocalTime.of(9, 0), LocalTime.of(10, 0),
            LocalTime.of(11, 0), LocalTime.of(12, 0),
            LocalTime.of(13, 0), LocalTime.of(14, 0),
            LocalTime.of(15, 0), LocalTime.of(16, 0),
            LocalTime.of(17, 0), LocalTime.of(18, 0)
    );
    AppointmentRepository appointmentRepository;

    public List<SlotResponse> getAllSlots() {
        List<Slot> slots = slotRepository.findAll();
        if (slots.isEmpty()) {
            throw new AppException(ErrorCode.SLOT_NOT_FOUND);
        }
        return slots.stream().map(slot -> slotMapper.toSlotResponse(slot)).collect(Collectors.toList());
    }

    public SlotResponse getSlotsById(int id) {
        return slotMapper.toSlotResponse(slotRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.SLOT_NOT_FOUND))
        );
    }
    public SlotResponse getSlotsWithTherapistsByDate(LocalDate date) {
        List<Slot> slots = slotRepository.findByDate(date);

        if (slots.isEmpty()) {
            throw new AppException(ErrorCode.SLOT_NOT_FOUND);
        }

        Set<Integer> therapistIds = slots.stream()
                .flatMap(slot -> slot.getSlotDetails().stream())
                .filter(slotDetail -> slotDetail.getStatus() == SlotStatus.AVAILABLE)
                .map(slotDetail -> slotDetail.getTherapist().getId())
                .collect(Collectors.toSet());

        Set<TherapistResponse> therapistResponses = therapistIds.stream()
                .map(id -> TherapistResponse.builder().id(id).build())
                .collect(Collectors.toSet());

        return SlotResponse.builder()
                .date(date)
                .therapists(therapistResponses)
                .build();
    }
    public List<SlotResponse> getSlotByTherapist(int therapistId) {
        Therapist therapist = therapistRepository.findById(therapistId).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        List<Slot> slots = slotRepository.findBySlotDetails_Therapist(therapist);
        System.out.println("assss" + slots);
        return slots.stream().map(slot -> slotMapper.toSlotResponse(slot)).collect(Collectors.toList());
    }
    public List<SlotResponse> getSlotsWithTherapistsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        List<SlotResponse> slotResponses = new ArrayList<>();
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            try {
                SlotResponse slotsForDate = getSlotsWithTherapistsByDate(date);
                slotResponses.add(slotsForDate);
            } catch (AppException e) {
                if (e.getErrorCode() != ErrorCode.SLOT_NOT_FOUND) {
                    throw e;
                }
            }
            date = date.plusDays(1);
        }

        return slotResponses;
    }

    public List<SlotResponse> getAvailableSlotsByDate(LocalDate date) {
        List<Slot> slots = slotRepository.findByDate(date);
        if(date.equals(LocalDate.now())) {
            LocalTime time = LocalTime.now();
            slots = slots.stream()
                    .filter(slot -> !slot.getTime().isBefore(time))
                    .toList();
        }
        List<Slot> availableSlots = slots.stream().map(
                        slot -> {
                            Set<SlotDetail> slotDetails = slotDetailRepository.findBySlotAndStatus(slot, SlotStatus.AVAILABLE);
                            slot.setSlotDetails(slotDetails);
                            return slot;
                        })
                .filter(slot -> !slot.getSlotDetails().isEmpty())
                .toList();
        return availableSlots.stream().map(slot -> slotMapper.toSlotResponse(slot)).collect(Collectors.toList());
    }
    public List<SlotResponse> getAvailableSlotsByTherapist(int therapistId) {
        Therapist therapist = therapistRepository.findById(therapistId).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        List<Slot> slots = slotRepository.findBySlotDetails_TherapistAndSlotDetails_Status(therapist, SlotStatus.AVAILABLE);
        return slots.stream()
                .map(slot -> {
                    slot.setSlotDetails(filteredSlotDetailsByTherapist(slot, therapistId));
                    return slotMapper.toSlotResponse(slot);
                })
                .collect(Collectors.toList());
    }
    public List<SlotResponse> getSlotsByDateAndTherapist(LocalDate date, int therapistId) {
        Therapist therapist = therapistRepository.findById(therapistId).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        List<Slot> slots = slotRepository.findByDateAndSlotDetails_Therapist(date, therapist);
        return slots.stream().map(
                slot -> {
                    slot.setSlotDetails(filteredSlotDetailsByTherapist(slot, therapistId));
                    return slotMapper.toSlotResponse(slot);
                }
        ).collect(Collectors.toList());
    }

    public List<SlotResponse> generateSlotsForDate(SlotRequest slotRequest) {
        LocalDate date = slotRequest.getDate();
        Set<Integer> therapistIds = slotRequest.getTherapistIds();
        if(isValidDate(date)) {
            throw new AppException(ErrorCode.INVALID_START_DATE);
        }
        List<Therapist> therapists = therapistRepository.findAllById(therapistIds);
        if(therapists.isEmpty()) {
            throw new AppException(ErrorCode.THERAPIST_NOT_FOUND);
        }
        List<Slot> slots = createSlotForDate(date);
        createMissingSlotDetail(slots, therapists);

        return slots.stream().map(slot -> slotMapper.toSlotResponse(slot)).collect(Collectors.toList());
    }
    public List<SlotResponse> generateSlotsForDateRange(LocalDate startDate, LocalDate endDate, Set<Integer> therapistIds) {
        if (isValidDate(startDate)) {
            throw new AppException(ErrorCode.INVALID_START_DATE);
        }

        if (endDate.isBefore(startDate.plusDays(7))) {
            throw new AppException(ErrorCode.INVALID_END_DATE);
        }

        List<SlotResponse> allSlots = new ArrayList<>();
        startDate.datesUntil(endDate.plusDays(1)).forEach(date -> {
            SlotRequest request = SlotRequest.builder()
                    .therapistIds(therapistIds)
                    .date(date)
                    .build();
            allSlots.addAll(generateSlotsForDate(request));
        });

        return allSlots;
    }

    public void delete(int slotId) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new AppException(ErrorCode.SLOT_NOT_FOUND));
        Set<SlotDetail> slotDetailsCopy = slot.getSlotDetails();
        for (SlotDetail detail : slotDetailsCopy) {
            detail.setSlot(null);
            slotDetailRepository.deleteById(detail.getId());
        }
        slotRepository.delete(slot);
    }

    //lấy lịch làm vệc của therapist trong khoảng thời gian
    public List<SlotResponse> getTherapistSchedule(int therapistId, LocalDate startDate, LocalDate endDate){
        if(endDate.isBefore(startDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        Therapist therapist = therapistRepository.findById(therapistId).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        List<Slot> slots = slotRepository.findBySlotDetails_TherapistAndDateBetween(therapist, startDate, endDate);
        return slots.stream()
                .map(slot -> {slot.setSlotDetails(filteredSlotDetailsByTherapist(slot, therapistId));
                            return slotMapper.toSlotResponse(slot);}).toList();
    }
    //delete slot for therapist cụ thể
    public void deleteScheduleByTherapistAndDate(int therapistId, LocalDate date) {
        Therapist therapist = therapistRepository.findById(therapistId).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        List<SlotDetail> slotDetails = slotDetailRepository.findByTherapistAndSlot_Date(therapist,date);
        if(!slotDetails.isEmpty()) {
            slotDetailRepository.deleteAll(slotDetails);
        }
    }
    private void createMissingSlotDetail(List<Slot> slots,List<Therapist> therapists) {
        List<SlotDetail> newSlotDetails = new ArrayList<>();
        for(Slot slot : slots) {
            if(slot.getSlotDetails() == null){
                slot.setSlotDetails(new HashSet<>());
            }
            //lấy danh sách Therapist đã có Slot
            Set<Integer> existingTherapistIds = slot.getSlotDetails().stream()
                    .map(sd -> sd.getTherapist().getId())
                    .collect(Collectors.toSet());
            for(Therapist therapist : therapists) {
                if(!existingTherapistIds.contains(therapist.getId())) {
                    SlotDetail newSlotDetail = SlotDetail.builder()
                            .slot(slot)
                            .therapist(therapist)
                            .status(SlotStatus.AVAILABLE)
                            .build();
                    newSlotDetails.add(newSlotDetail);
                }
            }

        }
        //Lưu và kết nối các newSlotDetail
        if(!newSlotDetails.isEmpty()){
            slotDetailRepository.saveAll(newSlotDetails);
            for (Slot slot : slots) {
                // Duyệt qua tất cả SlotDetail mới
                boolean slotUpdated = false;
                for (SlotDetail slotDetail : newSlotDetails) {
                    // Nếu SlotDetail thuộc về slot hiện tại
                    if (slotDetail.getSlot().getId() == slot.getId()) {
                        // Thêm vào collection của slot
                        slot.getSlotDetails().add(slotDetail);
                        slotUpdated = true;
                    }
                }
                // Chỉ lưu slot nếu có thay đổi
                if (slotUpdated) {
                    slotRepository.save(slot);
                }
            }
        }
    }


    private List<Slot> createSlotForDate(LocalDate date) {
        //20-3-2025
        List<Slot> existingSlots = slotRepository.findByDate(date);
        if(existingSlots.size() == WORKING_HOURS.size()) {
            return existingSlots;
        }
        //size = 9
        // 9 11 12 15
        Set<LocalTime> existingTimes = existingSlots.stream()
                .map(slot -> slot.getTime()).collect(Collectors.toSet());
        List<Slot> newSlots = WORKING_HOURS.stream()
                .filter(time -> !existingTimes.contains(time))
                .map(time -> {Slot newSlot = Slot.builder()
                        .date(date)
                        .time(time)
                        .build();
                    return slotRepository.save(newSlot);
                }).toList();
        List<Slot> allSlots = new ArrayList<>(existingSlots);
        allSlots.addAll(newSlots);
        return allSlots;
    }
    private Set<SlotDetail> filteredSlotDetailsByTherapist(Slot slot, int therapistId) {
        return slot.getSlotDetails().stream().filter(
                slotDetail -> slotDetail.getTherapist().getId() == therapistId
        ).collect(Collectors.toSet());
    }

    private boolean isValidDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }
}
