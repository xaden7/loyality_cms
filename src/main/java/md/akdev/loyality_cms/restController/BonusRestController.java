package md.akdev.loyality_cms.restController;

import jakarta.servlet.http.HttpServletRequest;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.service.BonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("api/bonus")
public class BonusRestController {

    private final BonusService bonusService;

    private final Logger logger = LoggerFactory.getLogger(BonusRestController.class);

    public BonusRestController(BonusService bonusService) {
        this.bonusService = bonusService;
    }

    @GetMapping("/getRefreshBonus")
    public ResponseEntity<?> getRefreshBonus(HttpServletRequest request) {
        try {
            AtomicReference<ClientsModel> clientsModel = new AtomicReference<>(new ClientsModel());

            bonusService.getRefreshBonus().ifPresentOrElse(
                    cl -> {
                        logger.info("BonusRestController | getRefreshBonus: Phone - \u001B[32m" + request.getUserPrincipal().getName() + "\u001B[0m; " + request.getHeader("Authorization") + " - " + cl.getBonus() + " bonus");
                        clientsModel.set(cl);
                    },
                    () -> {
                        logger.error("BonusRestController | getRefreshBonus: Client " + request.getUserPrincipal().getName() + " not found in database ");
                        throw new RuntimeException("Client " + request.getUserPrincipal().getName() + " not found in database ");
                    }
            );
            return new ResponseEntity<>(clientsModel, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("BonusRestController | getRefreshBonus: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

