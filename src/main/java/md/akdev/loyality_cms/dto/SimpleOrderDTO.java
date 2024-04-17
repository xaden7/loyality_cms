package md.akdev.loyality_cms.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SimpleOrderDTO {
    private UUID clientId;
    @Getter
    @Setter
    public static class Product {
        private String article;
        private BigDecimal qty;
        private BigDecimal price;
    }
    List<Product> products;
}
