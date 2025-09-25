package md.akdev.loyality_cms.model.order1c;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRow {
    private String product;
    private int qty;
    private BigDecimal sum;
}
