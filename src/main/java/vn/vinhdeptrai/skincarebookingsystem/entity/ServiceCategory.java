package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="categoryservice")
public class ServiceCategory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String description;
    boolean signature;
    @OneToMany(mappedBy = "category")
    Set<Service> services;
    @ManyToMany(mappedBy = "categories")
    Set<Therapist> therapists;

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    Quiz quiz; // một category chỉ cần 1 quiz để đề xuất các service
}
