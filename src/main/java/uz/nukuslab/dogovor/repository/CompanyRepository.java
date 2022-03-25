package uz.nukuslab.dogovor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nukuslab.dogovor.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByName(String name);


}
