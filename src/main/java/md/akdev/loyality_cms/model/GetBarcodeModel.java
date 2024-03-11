package md.akdev.loyality_cms.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBarcodeModel {
    private Integer qtyBarcode;
    private String lastBarcode;

    @Override
    public String toString() {
        return "GetBarcodeModel{" +
                "qtyBarcode=" + qtyBarcode +
                ", lastBarcode='" + lastBarcode + '\'' +
                '}';
    }
}
