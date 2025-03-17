package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.SlotRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.SlotResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.TherapistResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Slot;
import vn.vinhdeptrai.skincarebookingsystem.entity.SlotDetail;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SlotMapper {
    Slot toSlot(SlotRequest slotRequest);
    @Mapping(target = "therapists", expression = "java(mapTherapists(slot.getSlotDetails()))")
    SlotResponse toSlotResponse(Slot slot);
    default Set<TherapistResponse> mapTherapists(Set<SlotDetail> slotDetails) {
        if (slotDetails == null) {
            return Set.of();
        }
        return slotDetails.stream()
                .map(slotDetail -> TherapistResponse.builder()
                        .id(slotDetail.getTherapist().getId())
                        .fullname(slotDetail.getTherapist().getFullname())
                        .status(slotDetail.getStatus())
                        .build())
                .collect(Collectors.toSet());
    }

}
