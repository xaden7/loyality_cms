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
    private String clientName;
    @Column
    @NotEmpty
    private String phoneNumber;
    @Column
    @NotEmpty
    private String codeCard;
    @Column
    private Double bonus;
    @Column
    private String uuid1c;

    @Override
    public String toString() {
        return "ClientsModel{" +
                "id=" + id.toString() +
                ", clientName='" + clientName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", codeCard='" + codeCard + '\'' +
                ", bonus=" + bonus +
                ", uuid1c='" + uuid1c + '\'' +
                '}';
    }
}
