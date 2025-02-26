package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.CategoryServiceRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.CategoryServiceResponse;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.CategoryServiceMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.CategoryServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryServiceRepository categoryServiceRepository;
    CategoryServiceMapper categoryServiceMapper;
    public List<CategoryServiceResponse> getAll() {
//        List<CategoryServiceResponse> categoryServiceResponseList = new ArrayList<>();
//        for(vn.vinhdeptrai.skincarebookingsystem.entity.CategoryService category : categoryServiceRepository.findAll()) {
//            categoryServiceResponseList.add(categoryServiceMapper.toCategoryServiceResponse(category));
//        }
        return categoryServiceRepository.findAll().stream().map(categoryService ->
                        categoryServiceMapper.toCategoryServiceResponse(categoryService)).collect(Collectors.toList());
    }
    public CategoryServiceResponse getById(int id) {
        return categoryServiceMapper.toCategoryServiceResponse(categoryServiceRepository.getOne(id));
    }
    public CategoryServiceResponse create(CategoryServiceRequest categoryServiceRequest) {
        return categoryServiceMapper.toCategoryServiceResponse(
                categoryServiceRepository.save(categoryServiceMapper.toCategoryService(categoryServiceRequest)));
    }
    public CategoryServiceResponse update(int id, CategoryServiceRequest categoryServiceRequest) {
        vn.vinhdeptrai.skincarebookingsystem.entity.CategoryService categoryService = categoryServiceRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.CATE_SERVICE_NOT_FOUND)
        );
        categoryServiceMapper.updateCategoryService(categoryServiceRequest, categoryService);
        return categoryServiceMapper.toCategoryServiceResponse(categoryServiceRepository.save(categoryService));
    }
    public void delete(int id) {
        categoryServiceRepository.deleteById(id);
    }
}
