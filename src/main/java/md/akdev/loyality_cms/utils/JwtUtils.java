package md.akdev.loyality_cms.utils;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import md.akdev.loyality_cms.model.jwt.JwtAuthentication;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
    //    jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setUuid(claims.get("uuid", String.class));
        jwtInfoToken.setPhoneNumber(claims.getSubject());
        return jwtInfoToken;
    }

}