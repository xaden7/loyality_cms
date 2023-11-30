package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.JwtRefreshRequest;
import md.akdev.loyality_cms.model.JwtResponse;
import md.akdev.loyality_cms.model.QuestionaryModel;
import md.akdev.loyality_cms.service.ClientService;
import md.akdev.loyality_cms.service.JwtAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/login")
public class AuthRestController {
    private final ClientService clientService;
    private final JwtAuthService jwtAuthService;

    public AuthRestController(ClientService clientService, JwtAuthService jwtAuthService) {
        this.clientService = clientService;
        this.jwtAuthService = jwtAuthService;
    }

    @GetMapping("phone={phone}&barcode={barcode}")
    public ResponseEntity<?> getClientDeviceDto(@PathVariable String phone, @PathVariable String barcode){

        ClientDeviceDto inputClient = new ClientDeviceDto();
        inputClient.setPhoneNumber(phone.substring(phone.length()-8));
        inputClient.setCodeCard(barcode);

        ClientsModel clientsModel = clientService.mapToClientsModel(inputClient);
        try {
            ClientsModel getClient = clientService.getClientByPhoneNumberAndCodeCard(clientsModel);

            inputClient.setId(getClient.getId());
            inputClient.setBonus(getClient.getBonus());
            inputClient.setClientName(getClient.getClientName());

            final JwtResponse token = jwtAuthService.login(inputClient.getPhoneNumber(), inputClient.getCodeCard());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("accessToken", token.getAccessToken());
            responseHeaders.set("refreshToken", token.getRefreshToken());
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(inputClient);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("newAccessToken")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody JwtRefreshRequest request){
        final JwtResponse token = jwtAuthService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("newRefreshToken")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody JwtRefreshRequest request){
        final JwtResponse token = jwtAuthService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("newClient")
    public ResponseEntity<?> newClient(@RequestBody QuestionaryModel questionaryModel){
        try{
            QuestionaryModel postQustionaryModel = clientService.newClient(questionaryModel);
            ClientsModel clientsModel = clientService.mapQuestionaryToClientsModel(postQustionaryModel);
            ClientsModel client = clientService.getClientByPhoneNumberAndCodeCard(clientsModel);
            final JwtResponse token = jwtAuthService.login(client.getPhoneNumber(), client.getCodeCard());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("accessToken", token.getAccessToken());
            responseHeaders.set("refreshToken", token.getRefreshToken());
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(postQustionaryModel);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
