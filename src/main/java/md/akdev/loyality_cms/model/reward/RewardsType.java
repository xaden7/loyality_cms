package md.akdev.loyality_cms.model.reward;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rewards_type", uniqueConstraints = {
        @UniqueConstraint(columnNames = "reward_type")
})
public class RewardsType {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Integer id;

        @NotBlank(message = "reward type must be not empty")
        @Column(name = "reward_type", unique = true)
        private String rewardType;

        @Column(name = "reward_method", nullable = false)
        private Integer rewardMethod;

        @NotNull(message = "description must be not empty")
        @Column(name = "description")
        private String description;


        @Override
        public String toString() {
                return "RewardsType{" +
                        "id=" + id +
                        ", rewardType='" + rewardType + '\'' +
                        ", rewardMethod=" + rewardMethod +
                        ", description='" + description + '\'' +
                        '}';
        }
}
