package md.akdev.loyality_cms.exception;

public class JwtAuthException extends  RuntimeException{
    public JwtAuthException(String message) {
        super(message);
    }
}
