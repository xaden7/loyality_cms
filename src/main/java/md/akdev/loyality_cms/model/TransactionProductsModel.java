package md.akdev.loyality_cms.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TransactionProductsModel implements Serializable {
    private String prodName;
    private Double qty;
    private Double price;

}
