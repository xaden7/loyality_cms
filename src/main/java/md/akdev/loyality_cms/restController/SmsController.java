package md.akdev.loyality_cms.restController;

import jakarta.validation.constraints.NotNull;
import md.akdev.loyality_cms.service.ClientService;
import md.akdev.loyality_cms.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;
    private final ClientService clientService;
    Logger logger = LoggerFactory.getLogger(SmsController.class);
    @Autowired
    public SmsController(SmsService smsService, ClientService clientService) {
        this.smsService = smsService;
        this.clientService = clientService;
    }

    @PostMapping("/send-sms-code")
    public ResponseEntity<?> sendSms(@NotNull String phone) {

        logger.info("trying to send sms to phone: " + phone);

        String formattedPhone = phone.replaceAll("\\s+", "");
        formattedPhone =  formattedPhone.substring(phone.length() - 8);

        if (formattedPhone.length() != 8) {
            return ResponseEntity.badRequest().body("Phone number is not valid");
        }

        String message = "Codul de verificare este: " ;
                //smsService.getRandomNumber();


        return smsService.sendSms("373" + formattedPhone, message);
    }

    @PostMapping("/send-sms-card")
    public ResponseEntity<?> sendSmsCard(@NotNull String phone) {

        String formattedPhone = phone.replaceAll("\\s+", "");
        formattedPhone = formattedPhone.substring(phone.length() - 8);

        if (formattedPhone.length() != 8) {
            return ResponseEntity.badRequest().body("Phone number is not valid");
        }

        try{
           String message  = clientService.getBarcode(phone);
           return smsService.sendSms("373" + formattedPhone, message);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/verify-sms")
    public ResponseEntity<?> verifySms(@NotNull String phone, @NotNull Integer code) {
        String formattedPhone = phone.replaceAll("\\s+", "");
        formattedPhone =  formattedPhone.substring(phone.length() - 8);

        return smsService.verifySmsCode("373" + formattedPhone, code);
    }

    @GetMapping("/get-all-sms")
    public ResponseEntity<?> getAllSms() {
        return ResponseEntity.ok(smsService.getAllSmsCodeStorage());
    }
}
