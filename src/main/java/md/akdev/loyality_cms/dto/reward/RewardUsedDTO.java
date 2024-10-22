package md.akdev.loyality_cms.dto.reward;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RewardUsedDTO {
    private Integer rewardId;
    private Integer rewardDetailId;
    private Integer rewardDetailMultimediaId;
    private Integer rewardDetailMultimediaRowId;
    private UUID clientId;
    private String text;

    @Override
    public String toString() {
        return "RewardUsedDTO{" +
                "rewardId=" + rewardId +
                ", rewardDetailId=" + rewardDetailId +
                ", rewardDetailMultimediaId=" + rewardDetailMultimediaId +
                ", rewardDetailMultimediaRowId=" + rewardDetailMultimediaRowId +
                ", clientId=" + clientId +
                ", text='" + text + '\'' +
                '}';
    }
}
