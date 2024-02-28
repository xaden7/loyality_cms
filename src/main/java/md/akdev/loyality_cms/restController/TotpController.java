package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.service.TotpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/totp")
public class TotpController {
    private final TotpService totpService;

    @Autowired
    public TotpController(TotpService totpService) {
        this.totpService = totpService;
    }

    @RequestMapping("/generate")
    public int generateTotp(String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        return totpService.generateTotp(secret);
    }
}
