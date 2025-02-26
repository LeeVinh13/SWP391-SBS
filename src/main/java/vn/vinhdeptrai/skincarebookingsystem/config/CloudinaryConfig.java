package vn.vinhdeptrai.skincarebookingsystem.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "skincare");
        config.put("cloud_secret", "skincare");
        config.put("cloud_url", "https://skincare.com/");
        Cloudinary cloudinary = new Cloudinary(config);
        return new Cloudinary(config);
    }
}
