package md.akdev.loyality_cms.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductForSiteDTO  implements java.io.Serializable{
    private String id;
    private String article;
    private String name;
    private String description;
    private String price;
    private String country;
    private String manufacturer;
    private String cod_med;
    private String parent_id;
    private String images;
    private List<BarcodeForSiteDTO> barcodes;
}
