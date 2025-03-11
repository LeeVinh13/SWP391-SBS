package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="slot")
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    LocalDate date;
    LocalTime time;
    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
    Set<SlotDetail> slotDetails;

}
