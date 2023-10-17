package md.akdev.loyality_cms.dto;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientDeviceDto {

    private UUID Id;
    private String ClientName;
    private String PhoneNumber;
    private String CodeCard;
    private String DeviceId;
    private Double Bonus;
}
