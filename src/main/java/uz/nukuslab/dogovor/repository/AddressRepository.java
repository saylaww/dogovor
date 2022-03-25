package uz.nukuslab.dogovor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nukuslab.dogovor.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByCityAndDistrictAndNumber(String city, String district, Integer number);


}
