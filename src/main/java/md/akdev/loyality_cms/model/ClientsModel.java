package md.akdev.loyality_cms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.UUID;
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CLIENTS")
public class ClientsModel {
    @Id
    @Column
    @GeneratedValue
    private UUID id;
    @Column
    @NotEmpty
    private String ClientName;
    @Column
    @NotEmpty
    private String PhoneNumber;
    @Column
    @NotEmpty
    private String CodeCard;
    @Column
    private Double Bonus;
}
