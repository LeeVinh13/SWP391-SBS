package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RatingRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.RatingResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Rating;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    Rating toRating(RatingRequest request);

    @Mapping(source = "appointment.user.fullname", target = "userFullname")
    @Mapping(source = "appointment.slotDetail.therapist.fullname", target = "therapist")
    @Mapping(source = "appointment.service.name", target = "service")
    RatingResponse toRatingResponse(Rating rating);
}
