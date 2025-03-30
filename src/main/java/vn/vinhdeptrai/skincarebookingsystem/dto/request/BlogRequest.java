package vn.vinhdeptrai.skincarebookingsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogRequest {
    @Size(min = 1, max = 255, message = "INVALID_BLOG_TITLE")
    @NotNull(message = "INVALID_BLOG_TITLE")
    String title;
    @NotNull(message = "INVALID_BLOG_CONTENT")
    String content;
}
