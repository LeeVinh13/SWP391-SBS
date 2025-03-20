package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceRecommendationResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceRecommendation;

@Mapper(componentModel = "spring")
public interface ServiceRecommendationMapper {

    @Mapping(source = "quiz.id", target = "quiz_id")
    @Mapping(source = "quiz.title", target = "quiz_title")
    @Mapping(source = "service.id", target = "service_id")
    @Mapping(source = "service.name", target = "service_name")
    @Mapping(source = "service.thumbnail", target = "thumbnail_url")
    ServiceRecommendationResponse toServiceRecommendationResponse(ServiceRecommendation serviceRecommendation);
}
