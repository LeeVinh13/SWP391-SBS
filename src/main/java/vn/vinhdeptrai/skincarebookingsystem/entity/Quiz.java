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

    String title; //này là tên bài quiz - Ex: facial quiz (phụ thuộc theo service category)

    @OneToOne
    @JoinColumn(name = "category_id", unique = true, nullable = false) // mỗi category một bài quiz
    ServiceCategory category;

    @ManyToMany // một câu hỏi được dùng chung cho nhiều quiz
    @JoinTable(
            name = "quiz_question",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    Set<Question> questions; // một quiz có nhiều câu hỏi
}
