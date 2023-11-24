package md.akdev.loyality_cms.restController;

import jakarta.validation.constraints.NotNull;
import md.akdev.loyality_cms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    @Autowired
    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send-sms")
    public ResponseEntity<?> sendSms(@NotNull String phone) {
        return smsService.sendSms(phone);
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<?> verifySms(@NotNull String phone, @NotNull Integer code) {
        return smsService.verifySmsCode(phone, code);
    }

}
