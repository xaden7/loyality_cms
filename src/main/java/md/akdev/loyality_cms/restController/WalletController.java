package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.akdev.loyality_cms.service.AppleWalletService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final AppleWalletService walletService;
    @GetMapping("/generate")
    public ResponseEntity<byte[]> generatePass() throws Exception {
        byte[] pass = walletService.generatePass();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/vnd.apple.pkpass"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=card_frumos.pkpass")
                .body(pass);
    }
}
