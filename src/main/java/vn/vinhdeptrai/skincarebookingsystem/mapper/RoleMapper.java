package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RoleRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.RoleResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest roleRequest);
    RoleResponse toRoleResponse(Role role);
}
