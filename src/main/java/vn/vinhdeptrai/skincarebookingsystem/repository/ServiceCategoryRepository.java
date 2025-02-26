package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceCategory;
import vn.vinhdeptrai.skincarebookingsystem.service.ServiceCategoryService;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Integer> {
}
