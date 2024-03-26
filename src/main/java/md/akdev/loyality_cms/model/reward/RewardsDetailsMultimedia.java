package md.akdev.loyality_cms.model.reward;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rewards_details_multimedia")
public class RewardsDetailsMultimedia{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "reward_details_id", referencedColumnName = "id")
    @JsonBackReference
    private RewardDetail rewardDetails;

    @OneToMany(mappedBy = "rewardDetailsMultimedia" , cascade = {CascadeType.ALL},  fetch = FetchType.EAGER)
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RewardsDetailsMultimediaRow> rewardsDetailsMultimediaRows;

    @Size(max = 255)
    @Column(name = "video_link")
    private String videoLink;

    @Size(max = 255)
    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "image_base64", length = Integer.MAX_VALUE)
    private String imageBase64;

    @Size(max = 255)
    @Column(name = "image_name")
    private String imageName;

    @Size(max = 255)
    @Column(name = "image_type")
    private String imageType;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;



}