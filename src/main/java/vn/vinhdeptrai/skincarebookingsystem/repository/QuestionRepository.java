package vn.vinhdeptrai.skincarebookingsystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Question;
import vn.vinhdeptrai.skincarebookingsystem.entity.Quiz;

import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Set<Question> findByQuiz(Quiz quiz);
}
