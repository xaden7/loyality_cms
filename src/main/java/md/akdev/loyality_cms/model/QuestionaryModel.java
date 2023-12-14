package md.akdev.loyality_cms.model;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class QuestionaryModel implements Serializable {
    private UUID id;
    private String name;
    private String firstName;
    private String phoneNumber;
    private String email;
    private String barcode;
    private String language;
    private LocalDateTime birthday;
    private String sex;
}
