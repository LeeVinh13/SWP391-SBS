package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceResponse;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.ServiceMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Service {
    ServiceRepository serviceRepository;
    ServiceMapper serviceMapper;
    public List<ServiceResponse> getAll() {
        return serviceRepository.findAll().stream().map(service -> serviceMapper.toServiceResponse(service)).toList();
    }
    public ServiceResponse getById(int id) {
        vn.vinhdeptrai.skincarebookingsystem.entity.Service service = serviceRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SERVICE_NOT_FOUND)
        );;
        return serviceMapper.toServiceResponse(service);
    }
    public ServiceResponse create(ServiceRequest serviceRequest) {
        return serviceMapper.toServiceResponse(serviceRepository.save(serviceMapper.toService(serviceRequest)));
    }
    public ServiceResponse update(ServiceRequest serviceRequest, int id) {
        vn.vinhdeptrai.skincarebookingsystem.entity.Service service = serviceRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SERVICE_NOT_FOUND)
        );
        serviceMapper.updateService(service, serviceRequest);
        return serviceMapper.toServiceResponse(serviceRepository.save(service));
    }
    public void delete(int id) {
        serviceRepository.deleteById(id);
    }
}
