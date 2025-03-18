package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceCategoryRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceCategoryResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceCategory;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.ServiceCategoryMapper;
import vn.vinhdeptrai.skincarebookingsystem.mapper.ServiceMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.ServiceCategoryRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.ServiceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceCategoryService {
    ServiceCategoryMapper serviceCategoryMapper;
    ServiceCategoryRepository serviceCategoryRepository;
    ServiceRepository serviceRepository;
    public List<ServiceCategoryResponse> getAll() {
        List<ServiceCategory> serviceCategories = serviceCategoryRepository.findAll();
        if(serviceCategories.isEmpty()) {
            throw new AppException(ErrorCode.SERVICE_CATE_NOT_FOUND);
        }
        return serviceCategories.stream().map(serviceCategory ->
                serviceCategoryMapper.toServiceCategoryResponse(serviceCategory)).collect(Collectors.toList());
    }
    public ServiceCategoryResponse getById(int id) {
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SERVICE_CATE_NOT_FOUND)
        );
        return serviceCategoryMapper.toServiceCategoryResponse(serviceCategory);
    }
    public ServiceCategoryResponse create(ServiceCategoryRequest serviceCategoryRequest) {
        return serviceCategoryMapper.toServiceCategoryResponse(
                serviceCategoryRepository.save(serviceCategoryMapper.toServiceCategory(serviceCategoryRequest)));
    }
    public ServiceCategoryResponse update(int id, ServiceCategoryRequest serviceCategoryRequest) {
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SERVICE_CATE_NOT_FOUND)
        );
        serviceCategoryMapper.updateServiceCategory(serviceCategory, serviceCategoryRequest );
        return serviceCategoryMapper.toServiceCategoryResponse(serviceCategoryRepository.save(serviceCategory));
    }
    public void delete(int id) {
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SERVICE_CATE_NOT_FOUND)
        );
        List<vn.vinhdeptrai.skincarebookingsystem.entity.Service> services = serviceRepository.findByCategory(serviceCategory);
        if(services != null && !services.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_CONTAINING_SERVICE);
        }
        serviceCategoryRepository.deleteById(id);
    }
}
