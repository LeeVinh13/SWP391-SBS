package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.BlogRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.BlogResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Blog;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    @Mapping(target = "thumbnail", ignore = true)
    Blog toBlog(BlogRequest blogRequest);
    BlogResponse toBlogResponse(Blog blog);
}
