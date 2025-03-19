package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Service;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceCategory;

import java.util.List;

@Repository

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findByCategory(ServiceCategory category);
}
