package md.akdev.loyality_cms.model.partners;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="partner_details_ru")
public class PartnerDetailsRu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "partner_id", referencedColumnName = "id")
    private Partner partner;

}
