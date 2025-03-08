package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String title; //này là tên bài quiz - EX: facial quiz (phụ thuộc theo service category)

    @OneToOne
    @JoinColumn(name = "category_id", unique = true) // mỗi category một bài quiz
    ServiceCategory category;

    @OneToMany
    @JoinColumn(name = "quiz_id")
    Set<Question> questions; // bộ câu hỏi cho quiz thuộc một category nhất định

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ServiceRecommendation> recommendations;
}
