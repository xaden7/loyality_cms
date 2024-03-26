package md.akdev.loyality_cms.model.transaction;

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

    @Override
    public String toString() {
        return "TransactionProductsModel{" +
                "prodName='" + prodName + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
