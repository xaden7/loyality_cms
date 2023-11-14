package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.JwtResponse;
import md.akdev.loyality_cms.service.ClientService;
import md.akdev.loyality_cms.service.JwtAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class ClientDeviceDtoController {
   private final ClientService clientService;
   private final JwtAuthService jwtAuthService;

    public ClientDeviceDtoController(ClientService clientService, JwtAuthService jwtAuthService) {
        this.clientService = clientService;
        this.jwtAuthService = jwtAuthService;
    }

    @GetMapping("login/phone={phone}&barcode={barcode}")
    public ResponseEntity<?> getClientDeviceDto(@PathVariable String phone, @PathVariable String barcode){

        ClientDeviceDto inputClient = new ClientDeviceDto();
        inputClient.setPhoneNumber(phone);
        inputClient.setCodeCard(barcode);

        ClientsModel clientsModel = clientService.mapToClientsModel(inputClient);
        try {
            ClientsModel getClient = clientService.getClientByPhoneNumberAndCodeCard(clientsModel);

            inputClient.setId(getClient.getId());
            inputClient.setBonus(getClient.getBonus());
            inputClient.setClientName(getClient.getClientName());

            final JwtResponse token = jwtAuthService.login(phone, barcode);
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

}
