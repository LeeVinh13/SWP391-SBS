package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

}
