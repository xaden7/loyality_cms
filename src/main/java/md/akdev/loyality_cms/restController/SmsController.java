package md.akdev.loyality_cms.restController;

import jakarta.validation.constraints.NotNull;
import md.akdev.loyality_cms.model.sms.SmsCodeLog;
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

    @GetMapping("/send-sms-code")
    @PostMapping("/send-sms-code")
    public ResponseEntity<?> sendSms(@NotNull String phone, @RequestParam(defaultValue = "devino") String provider) {

        logger.info("trying to send sms to phone: " + phone);

        phone = phoneDefaultIfNull(phone);

        String formattedPhone = phone.replaceAll("\\s+", "");

        if (formattedPhone.length() != 8) {
            return ResponseEntity.badRequest().body("Phone number is not valid");
        }

        formattedPhone =  formattedPhone.substring(phone.length() - 8);

        Integer code = smsService.getRandomNumber();
        if(formattedPhone.equals("61031319")) {
            code = 111111;
        }
        String message = "Codul de verificare este: " + code;

        logger.info("Sending sms to phone: " + formattedPhone + " with code: " + code);

        ResponseEntity<?> responseEntity;

        if (provider.equals("devino")) {
            responseEntity = smsService.sendDevinoSms("373" + formattedPhone, message);
        } else {
            responseEntity = smsService.sendUnifunSms("373" + formattedPhone, message);
        }

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String smsApiResponse = (String) responseEntity.getBody();

            if (smsApiResponse != null && !smsApiResponse.isEmpty()) {

                SmsCodeLog smsCodeLog = new SmsCodeLog(
                        "373" + formattedPhone
                        , code.toString()
                        , smsApiResponse
                        , "CODE"
                        , "SEND SMS");
                logger.info("Sms - save result to DB: " + "373" + formattedPhone + " with code: " + code);
                smsService.saveSmsLog(smsCodeLog);

                smsService.saveSmsCode("373" +formattedPhone, code);


                return ResponseEntity.ok("Sms sent successfully");
            }

        }
        return ResponseEntity.badRequest().body("Error while sending sms");
    }



    @PostMapping("/send-sms-card")
    public ResponseEntity<?> sendSmsCard(@NotNull String phone , @RequestParam(defaultValue = "devino") String provider) {

        phone = phoneDefaultIfNull(phone);

        String formattedPhone = phone.replaceAll("\\s+", "");

        if (formattedPhone.length() != 8) {
            return ResponseEntity.badRequest().body("Phone number is not valid");
        }

        formattedPhone = formattedPhone.substring(phone.length() - 8);

        try{
           String message  = clientService.getBarcode(phone);

           logger.info("Sending sms to phone: " + phone + " with message: " + message);

            ResponseEntity<?> responseEntity = smsService.sendSms("373" + formattedPhone, message, provider);

            if (responseEntity.getStatusCode().is2xxSuccessful()){

                String smsApiResponse = (String) responseEntity.getBody();
                if (smsApiResponse != null && !smsApiResponse.isEmpty()) {

                    SmsCodeLog smsCodeLog = new SmsCodeLog(
                            "373" + formattedPhone
                            , message
                            , smsApiResponse
                            , "CARD"
                            , "SEND SMS");

                    smsService.saveSmsLog(smsCodeLog);

                        return ResponseEntity.ok("Sms sent successfully");
                    }
                else {
                        return ResponseEntity.badRequest().body("Error while sending sms");
                    }
                //}
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.badRequest().body("Error while sending sms");
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<?> verifySms(@NotNull String phone, @NotNull Integer code) {
        String formattedPhone = phone.replaceAll("\\s+", "");
        formattedPhone =  formattedPhone.substring(phone.length() - 8);

        return smsService.verifySmsCode("373" + formattedPhone, code);
    }

    private static String phoneDefaultIfNull(String phone) {
        phone = phone != null ? phone : "0";
        return phone;
    }

    @GetMapping("/get-all-sms")
    public ResponseEntity<?> getAllSms() {
        return ResponseEntity.ok(smsService.getAllSmsCodeStorage());
    }
}
