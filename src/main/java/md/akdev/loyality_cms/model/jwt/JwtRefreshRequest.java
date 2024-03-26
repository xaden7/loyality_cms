package md.akdev.loyality_cms.model.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRefreshRequest {
    public String refreshToken;
}
