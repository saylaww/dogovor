package uz.nukuslab.dogovor.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    private String name;

    private Long addressId;

    private String directorName;

    private Long propId;

}
