package md.akdev.loyality_cms.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import md.akdev.loyality_cms.model.sms.SmsApiResponse;
import md.akdev.loyality_cms.model.sms.SmsCodeLog;
import md.akdev.loyality_cms.model.sms.SmsCodeStorage;
import md.akdev.loyality_cms.model.sms.SmsRequest;
import md.akdev.loyality_cms.repository.sms.SmsCodeLogsRepository;
import md.akdev.loyality_cms.repository.sms.SmsCodeStorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    Logger logger = LoggerFactory.getLogger(SmsService.class);

    public SmsService(SmsCodeStorageRepository smsCodeStorageRepository, SmsCodeLogsRepository smsCodeLogsRepository) {
        this.smsCodeStorageRepository = smsCodeStorageRepository;
        this.smsCodeLogsRepository = smsCodeLogsRepository;
    }


    public ResponseEntity<?> sendDevinoSms(String phone, String messageToSend) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set("Authorization", "Bearer " + smsApiKey);

        smsApiUrl = smsApiUrl + "?sadr=" + smsSender + "&dadr=" + phone + "&text=" + messageToSend + "&translite=1";

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        return new RestTemplate().postForEntity(smsApiUrl, request, String.class);
    }

    @Deprecated
    public ResponseEntity<?> sendSms(String phone, String messageToSend) {


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

        return new RestTemplate().postForEntity(smsApiUrl, httpEntity, SmsApiResponse.class);
    }

    public ResponseEntity<?> verifySmsCode(String phone, Integer code) {
        SmsCodeStorage smsCodeStorage = smsCodeStorageRepository.findByPhone(phone);

        code = code != null ? code : 0;

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

        return new ResponseEntity<>(Map.of("reason", "Code is not valid"), org.springframework.http.HttpStatus.NOT_FOUND);
    }

    public Integer getRandomNumber() {
        int min = 100000;
        int max = 999999;
        return (int) ((Math.random() * (max - min)) + min);
//        return 111111; //for apple review
    }

    public void saveSmsCode(String phone, Integer code) {
        long EXPIRATION_TIME = System.currentTimeMillis() + 5 * 60 * 1000; // current time plus 5 minutes
        SmsCodeStorage smsCodeStorage = new SmsCodeStorage(phone, code.toString(), EXPIRATION_TIME);
        smsCodeStorageRepository.save(smsCodeStorage);
    }

    public void saveSmsLog(SmsCodeLog smsCodeLog) {

        smsCodeLogsRepository.save(smsCodeLog);
    }

    public List<SmsCodeStorage> getAllSmsCodeStorage() {
        return smsCodeStorageRepository.findAll();
    }
}

