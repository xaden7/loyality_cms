package md.akdev.loyality_cms.dto.promotion;

import lombok.*;
import md.akdev.loyality_cms.dto.TagDTO;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PromotionDTO {

    private Integer id;
    private Integer priority;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double upToDiscount;
    private Double upToBonus;
    private String imageName;
    private String imageType;
    private String status;
    private List<PromotionDetailDTO> promotionDetails;
    private List<TagDTO> tags;
}
