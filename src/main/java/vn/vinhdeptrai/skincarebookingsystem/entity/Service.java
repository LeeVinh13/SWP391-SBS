package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private long price;
    private String duration;
    private String thumbnail; // lưu URL ảnh từ cloudinary
    @ManyToOne
    @JoinColumn(name ="categoryId")
    private ServiceCategory category;
}
