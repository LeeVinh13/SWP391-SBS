package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.vinhdeptrai.skincarebookingsystem.dto.request.BlogRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.BlogResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.BlogService;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BlogController {
   BlogService blogService;

   @GetMapping
   public ApiResponse<List<BlogResponse>> getAll() {
       return ApiResponse.<List<BlogResponse>>builder()
               .result(blogService.getAll())
               .build();
   }

   @GetMapping("/{id}")
   public ApiResponse<BlogResponse> getById(@PathVariable int id) {
       return ApiResponse.<BlogResponse>builder()
               .result(blogService.getById(id))
               .build();
   }

   @PostMapping(value="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ApiResponse<BlogResponse> create(@RequestPart("blog") BlogRequest blogRequest,
                                              @RequestPart(value="thumbnail", required = false) MultipartFile thumbnail) throws IOException {

       return ApiResponse.<BlogResponse>builder()
               .result(blogService.create(blogRequest,thumbnail))
               .build();
   }

   @PutMapping("/update/{id}")
   ApiResponse<BlogResponse> update(@PathVariable int id,@RequestPart("blog") BlogRequest blogRequest,
                                       @RequestPart("thumbnail") MultipartFile thumbnail) throws IOException {
       return ApiResponse.<BlogResponse>builder()
               .result(blogService.update(blogRequest,id, thumbnail))
               .build();
   }

   @DeleteMapping("/delete/{id}")
   void deleteUser(@PathVariable int id) {
       this.blogService.delete(id);
   }
}

