package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Answer;
import vn.vinhdeptrai.skincarebookingsystem.entity.Question;

import java.util.Set;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Set<Answer> findByQuestion(Question question);
    Set<Answer> findByQuestion_Id(int id);
}
