package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RatingRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.RatingResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Rating;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    Rating toRating(RatingRequest request);
    RatingResponse toRatingResponse(Rating rating);
}
