package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="categoryService")
public class CategoryService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private boolean signature;
    @OneToMany(mappedBy = "category")
    private Set<Service> services;
}
