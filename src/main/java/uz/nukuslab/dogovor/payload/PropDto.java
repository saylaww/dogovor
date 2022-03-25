package uz.nukuslab.dogovor.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PropDto {

    private String bankName;

    private String accountNumber;

    private Integer mfo;

    private Integer inn;


}
