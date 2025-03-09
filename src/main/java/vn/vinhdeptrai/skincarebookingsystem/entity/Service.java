package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String description;
    double price;
    String duration;
    String thumbnail; // lưu URL ảnh từ cloudinary
    @ManyToOne
    @JoinColumn(name ="categoryId")
    ServiceCategory category;
    @OneToMany(mappedBy = "service")
    List<Appointment> appointments;
}
