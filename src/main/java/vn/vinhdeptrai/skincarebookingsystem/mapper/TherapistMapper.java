package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.*;
import org.springframework.context.annotation.Bean;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.TherapistRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.TherapistResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;

@Mapper(componentModel = "spring")
public interface TherapistMapper {
    @Mapping(target = "image", ignore = true)
    Therapist toTherapist(TherapistRequest therapistRequest);
    TherapistResponse toTherapistResponse(Therapist therapist);
    @Mapping(target = "image", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTherapist(@MappingTarget Therapist therapist, TherapistRequest therapistRequest);
}
