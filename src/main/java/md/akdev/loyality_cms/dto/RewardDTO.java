package md.akdev.loyality_cms.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RewardDTO {
    private Integer id;
    private RewardsTypeDTO rewardType;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String imageName;
    private String imageType;
    List<RewardDetailDTO> rewardDetails;
}
