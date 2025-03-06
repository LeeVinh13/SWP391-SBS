package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.TherapistRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.TherapistResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;

@Mapper(componentModel = "spring")
public interface TherapistMapper {
    @Mapping(target = "image", ignore = true)
    Therapist toTherapist(TherapistRequest therapistRequest);
    TherapistResponse toTherapistResponse(Therapist therapist);
    @Mapping(target = "image", ignore = true)
    void updateTherapist(@MappingTarget Therapist therapist, TherapistRequest therapistRequest);
}
