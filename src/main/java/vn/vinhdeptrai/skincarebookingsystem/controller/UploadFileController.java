package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.vinhdeptrai.skincarebookingsystem.service.UploadImageFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadFileController {
   UploadImageFile uploadImageFile;
   @PostMapping("/image")
    public String uploadImageFile(@RequestParam("file") MultipartFile file) throws IOException {
      return uploadImageFile.uploadImage(file);
   }
}
