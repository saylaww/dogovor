package uz.nukuslab.dogovor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nukuslab.dogovor.entity.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    boolean existsByPhoneNumber(String phoneNumber);



}
