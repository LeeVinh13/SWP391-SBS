package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Quiz;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceCategory;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Optional<Quiz> findByCategory(ServiceCategory serviceCategory);
}
