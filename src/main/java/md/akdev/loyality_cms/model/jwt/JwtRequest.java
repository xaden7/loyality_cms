package md.akdev.loyality_cms.model.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequest {

    private String phoneNumber;
    private String codeCard;
    private String uuid;

}