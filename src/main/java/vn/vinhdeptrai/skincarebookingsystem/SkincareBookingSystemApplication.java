package vn.vinhdeptrai.skincarebookingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SkincareBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkincareBookingSystemApplication.class, args);
    }

}
