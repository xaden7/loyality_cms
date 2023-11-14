package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.model.JwtRefreshRequest;
import md.akdev.loyality_cms.model.JwtResponse;
import md.akdev.loyality_cms.service.JwtAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/login")
public class TokenRestController {
    private final JwtAuthService jwtAuthService;

    public TokenRestController(JwtAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
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

}
