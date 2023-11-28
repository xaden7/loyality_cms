package md.akdev.loyality_cms.restController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import md.akdev.loyality_cms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

        String formattedPhone = phone.replaceAll("\\s+", "");
        formattedPhone =  formattedPhone.substring(phone.length() - 8);

        if (formattedPhone.length() != 8) {
            return ResponseEntity.badRequest().body("Phone number is not valid");
        }


        return smsService.sendSms("373" + formattedPhone);
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<?> verifySms(@NotNull String phone, @NotNull Integer code) {
        return smsService.verifySmsCode(phone, code);
    }

    @GetMapping("/get-all-sms")
    public ResponseEntity<?> getAllSms() {
        return ResponseEntity.ok(smsService.getAllSmsCodeStorage());
    }

}
