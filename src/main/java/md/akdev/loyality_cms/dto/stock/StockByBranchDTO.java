package md.akdev.loyality_cms.dto.stock;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StockByBranchDTO {
    private String article;
    private String branch_code;
    private BigDecimal qty;
}
