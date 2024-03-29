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
    @Column(name = "client_uid")
    private String clientUid;
    @Column(name = "type_bonus")
    private typeBonus typeBonus;
    @Column
    private Integer bonus;
    @Column(name = "date_insert")
    @CreatedDate
    private LocalDateTime dateInsert;
    @Column
    private Boolean accured;
    @Column(name = "date_accured")
    private LocalDateTime dateAccured;


    @Override
    public String toString() {
        return "BonusModel{" +
                "id=" + id +
                ", clientUid='" + clientUid + '\'' +
                ", typeBonus=" + typeBonus +
                ", bonus=" + bonus +
                ", dateInsert=" + dateInsert +
                ", accured=" + accured +
                ", dateAccured=" + dateAccured +
                '}';
    }
}
