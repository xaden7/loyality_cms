package md.akdev.loyality_cms.dto;

import jakarta.persistence.Column;
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
