package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceCategory;
import vn.vinhdeptrai.skincarebookingsystem.service.ServiceCategoryService;
@Repository

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Integer> {
}
