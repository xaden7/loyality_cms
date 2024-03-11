package md.akdev.loyality_cms.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "questionary")
@Entity
public class QuestionaryModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "last_name")
    private String name;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "barcode")
    private String barcode;
    @Column(name = "language")
    private String language;
    @Column(name = "birth_date")
    private LocalDate birthday;
    @Column(name = "client_id")
    private UUID clientId;
    @Column(name = "sex")
    private String sex;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Override
    public String toString() {
        return "QuestionaryModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", barcode='" + barcode + '\'' +
                ", language='" + language + '\'' +
                ", birthday=" + birthday +
                ", clientId=" + clientId +
                ", sex='" + sex + '\'' +
                '}';
    }
}
