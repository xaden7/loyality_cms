package md.akdev.loyality_cms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "promotions")
public class Promotion {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "up_to_discount")
    private Double upToDiscount;

    @Column(name = "up_to_bonus")
    private Double upToBonus;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    @Size(max = 100)
    @Column(name = "image_name", length = 100)
    private String imageName;

    @Size(max = 255)
    @Column(name = "image_type")
    private String imageType;

    @Size(max = 255)
    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "promotion", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<PromotionDetail> promotionDetails;

    @ManyToMany
    @JoinTable(
            name = "promotions_tags",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @Column(name = "created_at")
    private Instant createdAt;

}