package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Service;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    @Mapping(target = "thumbnail", ignore = true)
    Service toService(ServiceRequest serviceRequest);
    ServiceResponse toServiceResponse(Service service);
    @Mapping(target = "thumbnail", ignore = true)
    void updateService(@MappingTarget Service service, ServiceRequest serviceRequest);
}
