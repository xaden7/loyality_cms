package md.akdev.loyality_cms.dto.reward;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
    @Setter
public class RewardsDetailsMultimediaDTO implements Serializable {
    private Integer id;
    private String videoLink;

    private String imageLink;
    private String imageBase64;
    private String imageName;
    private String imageType;
    private String description;
    private String descriptionRu;
    private List<RewardsDetailsMultimediaRowDTO> rewardsDetailsMultimediaRows;
}