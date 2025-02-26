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
        config.put("cloud_name", "dhiizkbjp");
        config.put("api_key", "534696958938899");
        config.put("api_secret", "Oxh22hfk3U4m9489y8gufww0p_8");
        Cloudinary cloudinary = new Cloudinary(config);
        return new Cloudinary(config);
    }
}
