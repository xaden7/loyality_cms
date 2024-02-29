package md.akdev.loyality_cms.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class QuestionaryDTO implements Serializable {
    private UUID id;
    private String name;
    private String firstName;
    private String phoneNumber;
    private String email;
    private String barcode;
    private String language;
    private LocalDate birthday;

    private String sex;
}
