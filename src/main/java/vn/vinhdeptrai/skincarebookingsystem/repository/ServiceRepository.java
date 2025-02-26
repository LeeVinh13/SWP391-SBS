package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
