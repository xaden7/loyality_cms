package md.akdev.loyality_cms.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionDetailDTO {
    private Integer id;
//    private String image;
    private String imageName;
    private String imageType;
    private Double productBonus;
    private Double productDiscount;
    private Integer productId;
    private String productName;
    private Double productPrice;
    private Double productOldPrice;

}
