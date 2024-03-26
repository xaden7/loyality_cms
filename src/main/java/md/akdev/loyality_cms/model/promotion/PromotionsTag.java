package md.akdev.loyality_cms.model.promotion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import md.akdev.loyality_cms.model.Tag;

@Getter
@Setter
@Entity
@Table(name = "promotions_tags")
public class PromotionsTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Override
    public String toString() {
        return "PromotionsTag{" +
                "id=" + id +
                '}';
    }
}