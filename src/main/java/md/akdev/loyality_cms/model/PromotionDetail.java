package md.akdev.loyality_cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "promotion_details")
public class PromotionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    @Size(max = 255)
    @Column(name = "image_name")
    private String imageName = "image.jpg";

    @Size(max = 255)
    @Column(name = "image_type")
    private String imageType = "image/jpeg";

    @Column(name = "product_bonus")
    private Double productBonus;

    @Column(name = "product_discount")
    private Double productDiscount;

    @Column(name = "product_id")
    private Integer productId;

    @Size(max = 255)
    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private Double productPrice;

    @Column(name = "product_old_price")
    private Double productOldPrice;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Override
    public String toString() {
        return "PromotionDetail{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageType='" + imageType + '\'' +
                ", productBonus=" + productBonus +
                ", productDiscount=" + productDiscount +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productOldPrice=" + productOldPrice +
                '}';
    }
}