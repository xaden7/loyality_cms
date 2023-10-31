package md.akdev.loyality_cms.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private Double upToDiscount;
    private Double upToBonus;
    private String imageName;
    private String imageType;
    private String image;
    private List<PromotionDetailsDTO> promotionDetails;

}
