package md.akdev.loyality_cms.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenException extends AuthenticationException {
    public JwtTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
