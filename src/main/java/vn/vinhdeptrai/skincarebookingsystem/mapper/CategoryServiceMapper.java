package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.CategoryServiceRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.CategoryServiceResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.CategoryService;

@Mapper(componentModel = "spring")
public interface CategoryServiceMapper {
    CategoryService toCategoryService(CategoryServiceRequest categoryServiceRequest);
    CategoryServiceResponse toCategoryServiceResponse(CategoryService categoryService);
    void updateCategoryService(@MappingTarget CategoryServiceRequest categoryServiceRequest, CategoryService categoryService);
}
