package md.akdev.loyality_cms.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TransactionModel implements Serializable {
    private String pharmCode;
    private String address;
    private String docDate;
    private Double docSum;
    private Double bonusReceived;
    private Double bonusUsed;
    private List<TransactionProductsModel> products;

}
