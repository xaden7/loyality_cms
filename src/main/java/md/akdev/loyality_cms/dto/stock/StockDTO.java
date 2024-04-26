package md.akdev.loyality_cms.dto.stock;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StockDTO {
    private String article;
    private String name;
    private BigDecimal qty;
}
