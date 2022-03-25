package uz.nukuslab.dogovor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nukuslab.dogovor.entity.Prop;

public interface PropRepository extends JpaRepository<Prop, Long> {

    boolean existsByAccountNumberAndMfoAndInn(String accountNumber, Integer mfo, Integer inn);

}
