package md.akdev.loyality_cms.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RewardUsedDTO {
    private Integer rewardId;
    private UUID clientId;
}
