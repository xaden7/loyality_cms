package md.akdev.loyality_cms.model.sms;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "sms_code_logs")
@Entity
public class SmsCodeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 100)
    @NotNull
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Size(max = 500)
    @NotNull
    @Column(name = "io_code", nullable = false, length = 200)
    private String ioCode;
    @Size(max = 20)
    @NotNull
    @Column(name = "operation", nullable = false, length = 20)
    private String operation;

    @Size(max = 20)
    @NotNull
    @Column(name = "message_id", nullable = false, length = 20)
    private String messageId;

    public SmsCodeLog(String phone, String code, String ioCode, String messageId, String operation) {
        this.phone = phone;
        this.code = code;
        this.ioCode = ioCode;
        this.operation = operation;
        this.messageId = messageId;
    }
}
