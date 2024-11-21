package md.akdev.loyality_cms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import md.akdev.loyality_cms.dto.promotion.PromotionDetailDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerFullDTO {
    private String title;
    private String image;
    private List<PromotionDetailDTO> promotionDetailDTOList = new ArrayList<>();
}
