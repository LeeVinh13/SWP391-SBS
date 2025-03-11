package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlotDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name="slotId")
    Slot slot;

    @ManyToOne
    @JoinColumn(name="therapistId")
    Therapist therapist;

    @OneToOne(mappedBy = "slotDetail", cascade = CascadeType.REMOVE)
    Appointment appointment;

    @Enumerated(EnumType.STRING)
    SlotStatus status;
}
