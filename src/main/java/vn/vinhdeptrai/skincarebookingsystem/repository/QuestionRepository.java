package vn.vinhdeptrai.skincarebookingsystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Question;
import vn.vinhdeptrai.skincarebookingsystem.entity.Quiz;

import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query("SELECT q FROM Question q JOIN q.quizzes quiz WHERE quiz.id = :quizId")
    Set<Question> findQuestionsByQuizId(@Param("quizId") int quizId);

}
