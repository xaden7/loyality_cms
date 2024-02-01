package md.akdev.loyality_cms.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RewardDetailDTO {
    private Integer id;
    private String qrCode;
    private BigDecimal bonusQty;
    private String description;
}
