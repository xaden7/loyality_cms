package md.akdev.loyality_cms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "bonus_to_add")
public class BonusModel {
    public enum typeBonus{
        FIRST_LOGIN
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String clientUid;
    @Column
    private typeBonus typeBonus;
    @Column
    private Integer bonus;
    @Column
    @CreatedDate
    private LocalDateTime dateInsert;
    @Column
    private Boolean accured;
    @Column
    private LocalDateTime dateAccured;

}
