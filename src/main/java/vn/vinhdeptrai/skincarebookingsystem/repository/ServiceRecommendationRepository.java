package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Quiz;
import vn.vinhdeptrai.skincarebookingsystem.entity.Service;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceRecommendation;

import java.util.Set;

@Repository
public interface ServiceRecommendationRepository extends JpaRepository<ServiceRecommendation, Integer> {
    Set<ServiceRecommendation> findByQuizAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(Quiz quiz, int minScore, int maxScore);

    boolean existsByQuizAndService(Quiz quiz, Service service);

    @Modifying
    @Query("DELETE FROM ServiceRecommendation sr WHERE sr.quiz.id = :quizId")
    void deleteByQuizId(@Param("quizId") int quizId);

    @Modifying
    @Query("DELETE FROM ServiceRecommendation sr WHERE sr.service.id = :serviceId")
    void deleteByServiceId(@Param("serviceId") int serviceId);
}
