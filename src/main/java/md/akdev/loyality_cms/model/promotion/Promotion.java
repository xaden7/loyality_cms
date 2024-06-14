package md.akdev.loyality_cms.model.promotion;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import md.akdev.loyality_cms.model.Tag;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotions")
public class Promotion {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "priority",columnDefinition = "INT DEFAULT 1")
    private Integer priority;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "name_ru")
    private String nameRu;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "description_ru")
    private String descriptionRu;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "up_to_discount")
    private Double upToDiscount;

    @Column(name = "up_to_bonus")
    private Double upToBonus;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    @Size(max = 100)
    @Column(name = "image_name", length = 100)
    private String imageName = "image.jpg";

    @Size(max = 255)
    @Column(name = "image_type")
    private String imageType = "image/jpeg";

    @Size(max = 255)
    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<PromotionDetail> promotionDetails;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "promotions_tags",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @Column(name = "created_at")
    private Instant createdAt;

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", priority=" + priority +
                ", name='" + name + '\'' +
                ", nameRu='" + nameRu + '\'' +
                ", description='" + description + '\'' +
                ", descriptionRu='" + descriptionRu + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", upToDiscount=" + upToDiscount +
                ", upToBonus=" + upToBonus +
                ", image='" + image + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageType='" + imageType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}