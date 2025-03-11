package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.SlotRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.SlotResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Slot;

@Mapper(componentModel = "spring")
public interface SlotMapper {
    @Mapping(target = "therapists", ignore = true)
    Slot toSlot(SlotRequest slotRequest);
    SlotResponse toSlotResponse(Slot slot);
}
