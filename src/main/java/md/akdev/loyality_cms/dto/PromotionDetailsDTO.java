package md.akdev.loyality_cms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
