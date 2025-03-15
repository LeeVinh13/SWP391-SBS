package vn.vinhdeptrai.skincarebookingsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;//thuộc appointment nào, để check đã trải nghiệm chưa, đã feedback chưa

    private int stars;

    private String comment; //feedback đi kèm (có thể chỉ rating không cần feedback)

    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
