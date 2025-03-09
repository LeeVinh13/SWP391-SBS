package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.SlotDetailResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.SlotDetail;

@Mapper(componentModel="spring")
public interface SlotDetailMapper {
    SlotDetailResponse toSlotDetailResponse(SlotDetail slotDetail);

}
