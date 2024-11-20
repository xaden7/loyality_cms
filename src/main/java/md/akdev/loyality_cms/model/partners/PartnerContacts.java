package md.akdev.loyality_cms.model.partners;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "partner_contacts")
@Getter
@Setter
public class PartnerContacts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private PartnerContactTypes type;
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "partner_id", referencedColumnName = "id")
    private Partner partner;
}
