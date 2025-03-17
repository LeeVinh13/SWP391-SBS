package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import vn.vinhdeptrai.skincarebookingsystem.dto.request.BlogRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.BlogResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Blog;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.BlogMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.BlogRepository;
import vn.vinhdeptrai.skincarebookingsystem.util.CloudinaryUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BlogService {
   CloudinaryUtil cloudinaryUtil;
   BlogRepository blogRepository;
   BlogMapper blogMapper;
   
   public List<BlogResponse> getAll() {
       return blogRepository.findAll().stream().map(blogMapper::toBlogResponse).toList();
   }

   public BlogResponse getById(int id) {
       Blog blog = blogRepository.findById(id).orElseThrow(
               () -> new AppException(ErrorCode.BLOG_NOT_FOUND)
       );
       return blogMapper.toBlogResponse(blog);
   }

   public BlogResponse create(BlogRequest blogRequest, MultipartFile file) throws IOException {
       Blog blog = blogMapper.toBlog(blogRequest);
       blog = Blog.builder()
                .title(blogRequest.getTitle())
                .content(blogRequest.getContent())
                .thumbnail(cloudinaryUtil.uploadImage(file))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
       return blogMapper.toBlogResponse(blogRepository.save(blog));
   }

   public BlogResponse update(BlogRequest blogRequest, int id, MultipartFile file) throws IOException {
       Blog blog = blogRepository.findById(id).orElseThrow(
               () -> new AppException(ErrorCode.BLOG_NOT_FOUND)
       );
       blog = Blog.builder()
            .title(blogRequest.getTitle())
            .content(blogRequest.getContent())
            .thumbnail(cloudinaryUtil.uploadImage(file))
            .updatedAt(LocalDateTime.now())
            .build();
       return blogMapper.toBlogResponse(blogRepository.save(blog));
   }

   public void delete(int id) {
       blogRepository.deleteById(id);
   }

   public List<BlogResponse> getAllWithSort(String field, String sortOrder) {
        return blogRepository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), field)).stream().map(blogMapper::toBlogResponse).toList();
   }

   public Page<BlogResponse> getAllWithPagination(int page, int size) {
        Page<Blog> blogs = blogRepository.findAll(PageRequest.of(page, size));
        return blogs.map(blogMapper::toBlogResponse);
   }

   public Page<BlogResponse> getAllWithPaginationAndSort(int page, int size, String field, String sortOrder) {
        Page<Blog> blogs = blogRepository.findAll(PageRequest.of(page, size)
            .withSort(Sort.by(Sort.Direction.fromString(sortOrder), field)));
        return blogs.map(blogMapper::toBlogResponse);
   }
}
