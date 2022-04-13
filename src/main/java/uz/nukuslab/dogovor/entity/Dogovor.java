package uz.nukuslab.dogovor.entity;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dogovor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    private User user;

    @ManyToOne
    private Company company;

    @CreationTimestamp
    private Timestamp createdAt;

    public Dogovor(double price, User user, Company company) {
        this.price = price;
        this.user = user;
        this.company = company;
    }
}
