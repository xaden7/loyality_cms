package md.akdev.loyality_cms.dto;

import lombok.*;

import java.util.UUID;

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
