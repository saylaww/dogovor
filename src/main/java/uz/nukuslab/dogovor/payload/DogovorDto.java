package uz.nukuslab.dogovor.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DogovorDto {

    private double price;

    private Integer userId;

    private Long companyId;

}
