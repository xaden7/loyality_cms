package md.akdev.loyality_cms.dto.promotion;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionDetailDTO {
    private Integer id;
    private String imageName;
    private String imageType;
    private Double productBonus;
    private Double productDiscount;
    private Integer productId;
    private String productName;
    private Double productPrice;
    private Double productOldPrice;

}
