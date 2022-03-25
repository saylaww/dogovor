package uz.nukuslab.dogovor.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {

    private String phoneNumber;

    private String description;

    private Long companyId;


}
