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

    private UUID id;
    private String clientName;
    private String phoneNumber;
    private String codeCard;
    private String deviceId;
    private Double bonus;
}
