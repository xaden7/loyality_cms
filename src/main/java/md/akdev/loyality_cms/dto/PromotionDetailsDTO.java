package md.akdev.loyality_cms.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PromotionDetailsDTO {
    private Integer id;
    private String productName;
    private Double productPrice;
    private Double productDiscount;
    private Double productBonus;
    private String imageName;
    private String imageType;
    private String image;
}
