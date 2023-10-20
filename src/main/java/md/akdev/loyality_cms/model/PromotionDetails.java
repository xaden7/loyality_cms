package md.akdev.loyality_cms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PromotionDetails {
    //details class of promotion
    @Id
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "promotion", referencedColumnName = "id")
    @JsonBackReference
    private Promotion promotion;
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_price")
    private Double productPrice;
    @Column(name = "product_discount")
    private Double productDiscount;
    @Column(name = "product_bonus")
    private Double productBonus;
    @Column(name = "image_name")
    private String imageName;
    @Column(name = "image_type")
    private String imageType;
    @Column(name = "image")
    @Lob
    private String image;
}
