package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Service;
@Repository

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
