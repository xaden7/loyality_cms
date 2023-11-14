package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.service.BonusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/token")
public class BonusRestController {

    private final BonusService bonusService;

    public BonusRestController(BonusService bonusService) {
        this.bonusService = bonusService;
    }

    @GetMapping("getRefreshBonus")
    public ResponseEntity<?> getRefreshBonus(){
        try {
            ClientsModel clientsModel = bonusService.getRefreshBonus();
            return new ResponseEntity(clientsModel, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

