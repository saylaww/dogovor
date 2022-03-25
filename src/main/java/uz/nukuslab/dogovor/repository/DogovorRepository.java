package uz.nukuslab.dogovor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nukuslab.dogovor.entity.Dogovor;

import java.sql.Timestamp;
import java.util.List;

public interface DogovorRepository extends JpaRepository<Dogovor, Long> {

    List<Dogovor> findByUserId(Integer user_id);

    List<Dogovor> findByCreatedAtBetween(Timestamp createdAt, Timestamp createdAt2);

}
