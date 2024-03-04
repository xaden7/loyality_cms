package md.akdev.loyality_cms.exception;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NotFoundException  extends RuntimeException{
    private String message = "no data found";
}
