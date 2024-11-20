package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.model.partners.Partner;
import md.akdev.loyality_cms.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;



    @GetMapping("/all")
    public ResponseEntity<Iterable<Partner>> getAllPartners() {
        Iterable<Partner> partners = partnerService.getAllPartners();
        return ResponseEntity.ok(partners);
    }

}


