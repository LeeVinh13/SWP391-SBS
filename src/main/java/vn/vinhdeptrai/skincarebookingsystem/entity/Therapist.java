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
    String description;
    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL,orphanRemoval = true)
    List<SlotDetail> slotDetails;

}
