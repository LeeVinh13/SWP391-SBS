package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceRecommendationResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceRecommendation;

@Mapper(componentModel = "spring")
public interface ServiceRecommendationMapper {
    
    ServiceRecommendationResponse toServiceRecommendationResponse(ServiceRecommendation serviceRecommendation);
}
