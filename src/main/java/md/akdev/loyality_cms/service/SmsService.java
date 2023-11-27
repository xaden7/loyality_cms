package md.akdev.loyality_cms.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import md.akdev.loyality_cms.model.SmsApiResponse;
import md.akdev.loyality_cms.model.SmsCodeLog;
import md.akdev.loyality_cms.model.SmsCodeStorage;
import md.akdev.loyality_cms.model.SmsRequest;
import md.akdev.loyality_cms.repository.SmsCodeLogsRepository;
import md.akdev.loyality_cms.repository.SmsCodeStorageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class SmsService {
    @Value("${devino.sms.url}")
     String smsApiUrl;

    @Value("${devino.sms.api-key}")
     String smsApiKey;

    @Value("${devino.sms.sender}")
     String smsSender;

    final String priority = "HIGH";

    private final SmsCodeStorageRepository smsCodeStorageRepository;

    private final SmsCodeLogsRepository smsCodeLogsRepository;

    public SmsService(SmsCodeStorageRepository smsCodeStorageRepository, SmsCodeLogsRepository smsCodeLogsRepository) {
        this.smsCodeStorageRepository = smsCodeStorageRepository;
        this.smsCodeLogsRepository = smsCodeLogsRepository;
    }

    public ResponseEntity<?> sendSms(String phone){

        Integer code = getRandomNumber();

        String messageToSend = "Codul de verificare este: " + code;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Key " + smsApiKey);

        SmsRequest smsRequest = new SmsRequest(Collections.singletonList(
                                        new SmsRequest.Message(smsSender, phone, messageToSend, 0, priority)));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(smsRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while parsing json body");
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, httpHeaders);

        ResponseEntity<SmsApiResponse> response = new RestTemplate().postForEntity(smsApiUrl, httpEntity, SmsApiResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {

            SmsApiResponse smsApiResponse = response.getBody();

            if (smsApiResponse != null && smsApiResponse.getResult() != null && !smsApiResponse.getResult().isEmpty()) {

                SmsApiResponse.Result result = smsApiResponse.getResult().get(0);

                SmsCodeLog smsCodeLog = new SmsCodeLog(phone
                        , result.getCode()
                        , code.toString(), result.getMessageId(), "SEND SMS");

                saveSmsLog(smsCodeLog);

                if ("OK".equals(result.getCode())) {
                    saveSmsCode(phone, code);
                    return ResponseEntity.ok("Sms sent successfully");
                } else {
                    return ResponseEntity.badRequest().body("Error while sending sms");
                }
            }
        }

      return ResponseEntity.badRequest().body("Error while sending sms");

    }

    public ResponseEntity<?> verifySmsCode(String phone, Integer code) {
        SmsCodeStorage smsCodeStorage = smsCodeStorageRepository.findByPhone(phone);
        if (smsCodeStorage != null) {
            if (smsCodeStorage.getCode().equals(code.toString())) {

                SmsCodeLog smsCodeLog = new SmsCodeLog(phone
                        , "OK"
                        , code.toString(), "0", "VERIFY CODE");

                saveSmsLog(smsCodeLog);

                if (System.currentTimeMillis() <= smsCodeStorage.getExpirationTime()) {
                    smsCodeStorageRepository.delete(smsCodeStorage);  // delete code from storage
                    return ResponseEntity.ok("Code is valid");
                } else {
                    smsCodeStorageRepository.delete(smsCodeStorage);  // delete code from storage
                    return ResponseEntity.badRequest().body("Code is expired");
                }
            }
        }
        return ResponseEntity.badRequest().body("Code is not valid");
    }

    private Integer getRandomNumber() {
        int min = 100000;
        int max = 999999;
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void saveSmsCode(String phone, Integer code) {
        long EXPIRATION_TIME = System.currentTimeMillis() + 5 * 60 * 1000; ; // current time plus 5 minutes
        SmsCodeStorage smsCodeStorage = new SmsCodeStorage(phone, code.toString(), EXPIRATION_TIME);
        smsCodeStorageRepository.save(smsCodeStorage);
    }

    private void saveSmsLog(SmsCodeLog smsCodeLog) {
        //  SmsCodeLog smsCodeLog = new SmsCodeLog(phone, result.getCode(), result.getMessageId(), operation);
        smsCodeLogsRepository.save(smsCodeLog);
    }

    public List<SmsCodeStorage> getAllSmsCodeStorage() {
        return smsCodeStorageRepository.findAll();
    }
}

