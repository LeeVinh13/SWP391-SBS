package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Service;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    Service toService(ServiceRequest serviceRequest);
    ServiceResponse toServiceResponse(Service service);
    void updateService(@MappingTarget Service service, ServiceRequest serviceRequest);
}
