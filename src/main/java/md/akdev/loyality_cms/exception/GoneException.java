package md.akdev.loyality_cms.exception;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GoneException  extends RuntimeException{
    private String message = "Client Error: 410 Gone";
}
