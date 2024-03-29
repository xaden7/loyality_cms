package md.akdev.loyality_cms.restController;

import jakarta.servlet.http.HttpServletRequest;
import md.akdev.loyality_cms.model.jwt.JwtAuthentication;
import md.akdev.loyality_cms.model.TemporaryCodeModel;
import md.akdev.loyality_cms.service.ClientService;
import md.akdev.loyality_cms.service.JwtAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TempCodeRestController {
    private final ClientService clientService;
    private final JwtAuthService jwtAuthService;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TempCodeRestController.class);

    public TempCodeRestController(ClientService clientService, JwtAuthService jwtAuthService) {
        this.clientService = clientService;
        this.jwtAuthService = jwtAuthService;
    }
    @GetMapping("/temporaryCode")
    public ResponseEntity<?> temporaryCodeModel(HttpServletRequest request){
        try{
            final JwtAuthentication authentication = jwtAuthService.getAuthInfo();
            TemporaryCodeModel newTemporaryCodeModel = clientService.temporaryCode(authentication.getUuid());
            logger.info("TempCodeRestController | temporaryCodeModel: Phone - \u001B[32m"+ request.getUserPrincipal().getName() + "\u001B[0m; " +  request.getHeader("Authorization") + ", Phone - (" + authentication.getPhoneNumber() +") - " + newTemporaryCodeModel);
            return new ResponseEntity<>(newTemporaryCodeModel, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
