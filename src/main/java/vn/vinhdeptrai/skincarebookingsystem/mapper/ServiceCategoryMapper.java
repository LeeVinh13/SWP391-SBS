package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceCategoryRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceCategoryResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceCategory;

@Mapper(componentModel = "spring")
public interface ServiceCategoryMapper {
    ServiceCategory toServiceCategory(ServiceCategoryRequest serviceCategoryRequest);
    ServiceCategoryResponse toServiceCategoryResponse(ServiceCategory serviceCategory);
    void updateServiceCategory(@MappingTarget ServiceCategory serviceCategory,ServiceCategoryRequest serviceCategoryRequest);

}
