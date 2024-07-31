package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.ClientDeviceDto;

import md.akdev.loyality_cms.exception.CstErrorResponse;
import md.akdev.loyality_cms.exception.JwtAuthException;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.jwt.JwtRefreshRequest;
import md.akdev.loyality_cms.model.jwt.JwtResponse;
import md.akdev.loyality_cms.model.QuestionaryModel;
import md.akdev.loyality_cms.service.ClientService;
import md.akdev.loyality_cms.service.JwtAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth/login")
public class AuthRestController {
    private final ClientService clientService;
    private final JwtAuthService jwtAuthService;

    private final Logger logger = LoggerFactory.getLogger(AuthRestController.class);

    public AuthRestController(ClientService clientService, JwtAuthService jwtAuthService) {
        this.clientService = clientService;
        this.jwtAuthService = jwtAuthService;
    }

    @GetMapping("phone={phone}&barcode={barcode}")
    public ResponseEntity<?> getClientDeviceDto(@PathVariable String phone, @PathVariable String barcode){

        phone = phoneDefaultIfNull(phone);

        logger.info("AuthRestController | getClientDeviceDto | inputValues: \u001B[32m" + phone +  "\u001B[0m - " + barcode);
        if (phone.length() != 8) {
            return ResponseEntity.badRequest().body("Phone number is not valid");
        }

        ClientDeviceDto inputClient = new ClientDeviceDto();
        inputClient.setPhoneNumber(phone);
        inputClient.setCodeCard(barcode);

        ClientsModel clientsModel = clientService.mapToClientsModel(inputClient);

        try {
            ClientsModel getClient = clientService.getClientByPhoneNumberAndCodeCard(clientsModel);

            inputClient.setId(getClient.getId());
            inputClient.setBonus(getClient.getBonus());
            inputClient.setClientName(getClient.getClientName());

            //final JwtResponse token = jwtAuthService.login(inputClient.getPhoneNumber(), inputClient.getCodeCard());
            final JwtResponse token = jwtAuthService.login(getClient);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("accessToken", token.getAccessToken());
            responseHeaders.set("refreshToken", token.getRefreshToken());

//            clientService.addBonusForFirstLogin(getClient);
            logger.info("AuthRestController | getClientDeviceDto: \u001B[32m" + inputClient.getPhoneNumber() + "\u001B[0m - " + inputClient.getCodeCard() + " - " + inputClient.getBonus() + " bonus");
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(inputClient);
        } catch (NotFoundException e) {
            Map<String, String> errorMessage = new HashMap<>();
            errorMessage.put("reason", e.getMessage());
            logger.error("AuthRestController | getClientDeviceDto: " + e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("AuthRestController | getClientDeviceDto: " + e.getMessage());
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

        logger.info("AuthRestController | newClient | inputValues: " + questionaryModel );

        try{
            QuestionaryModel postQustionaryModel = clientService.newClient(questionaryModel);
            ClientsModel clientsModel = clientService.mapQuestionaryToClientsModel(postQustionaryModel);

            ClientsModel client = clientService.getClientByPhoneNumberAndCodeCard(clientsModel);

            final JwtResponse token = jwtAuthService.login(client.getPhoneNumber(), client.getCodeCard());

            postQustionaryModel.setClientId(client.getId());

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("accessToken", token.getAccessToken());
            responseHeaders.set("refreshToken", token.getRefreshToken());
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(postQustionaryModel);
        }catch(Exception e){
            logger.error("AuthRestController | newClient: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private static String phoneDefaultIfNull(String phone) {
        phone = phone != null ? phone : "0";
        return phone;
    }

    @ExceptionHandler({NotFoundException.class, JwtAuthException.class,
        RuntimeException.class, AccessDeniedException.class})
    private ResponseEntity<CstErrorResponse> handeException(Exception e){

        CstErrorResponse cstErrorResponse = new CstErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        HttpStatus status = switch (e.getClass().getSimpleName()) {
            case "NotFoundException" -> HttpStatus.NOT_FOUND;
            case "RewardAlreadyUsedException", "DataIntegrityViolationException" -> HttpStatus.CONFLICT;
            case "AccessDeniedException", "JwtAuthException" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.BAD_REQUEST;
        };
        logger.error(cstErrorResponse.getMessage());
        return new ResponseEntity<>(cstErrorResponse, status);
    }
}
