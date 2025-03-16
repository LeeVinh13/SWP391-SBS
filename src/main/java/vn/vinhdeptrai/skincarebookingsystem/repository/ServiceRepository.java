package vn.vinhdeptrai.skincarebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vinhdeptrai.skincarebookingsystem.entity.Service;

import java.util.List;

@Repository

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<vn.vinhdeptrai.skincarebookingsystem.entity.Service> findByCategory_Signature(boolean categorySignature);
}
