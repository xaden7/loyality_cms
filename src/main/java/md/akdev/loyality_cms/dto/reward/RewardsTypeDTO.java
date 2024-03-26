package md.akdev.loyality_cms.dto.reward;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RewardsTypeDTO {
    private Integer id;
    private String rewardType;
    private String description;
    private Integer rewardMethod;
}
