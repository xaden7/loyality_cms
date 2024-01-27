package md.akdev.loyality_cms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PromotionDTO {

    private Integer id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double upToDiscount;
    private Double upToBonus;
//    private String image;
    private String imageName;
    private String imageType;
    private String status;
    private List<PromotionDetailDTO> promotionDetails;
    private List<TagDTO> tags;
}
