package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder
@Table(name = "therapist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Therapist extends User{
    int experience;
    String image;
    String specialty;
    String description;
    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<SlotDetail> slotDetails;

    @ManyToMany
    @JoinTable(
            name = "therapist_servicecategory",
            joinColumns = @JoinColumn(name = "therapist_id"),
            inverseJoinColumns = @JoinColumn(name = "servicecategory_id")
    )
    List<ServiceCategory> categories;
}
