package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.model.partners.Partner;
import md.akdev.loyality_cms.model.partners.PartnerImage;
import md.akdev.loyality_cms.service.PartnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/get-image")
    public ResponseEntity<?> getImage(UUID id) {

        Optional<PartnerImage> partnerImage = partnerService.findImageById(id);

        String data = partnerImage.isPresent() ? partnerImage.get().getBody() : "";

        if (data.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpeg")).body(new byte[0]);
        }

        byte[] imageBytes = java.util.Base64.getDecoder().decode(data);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);

    }

}


