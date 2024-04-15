package md.akdev.loyality_cms.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_for_site")
public class ProductForSite implements java.io.Serializable{
    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty("uid")
    private UUID id;

    @Column(name = "article", nullable = false)
    private String article;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "country")
    private String country;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "cod_med")
    @JsonProperty("cod_med")
    private String codMed;

    @Column(name = "parent_id")
    @JsonProperty("parent_id")
    private UUID parentId;

    @Column(name = "images")
    private String images;

    @Column(name = "changed_at")
    @UpdateTimestamp
    private Instant changedAt;

    @OneToMany(mappedBy = "productForSite", cascade = {CascadeType.ALL}, orphanRemoval = true,  fetch = FetchType.EAGER)
    private List<BarcodeForSite> barcodes ;

}