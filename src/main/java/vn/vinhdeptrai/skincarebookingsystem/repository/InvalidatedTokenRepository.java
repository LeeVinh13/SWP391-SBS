package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.InvalidatedToken;
@Repository

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
