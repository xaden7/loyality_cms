package md.akdev.loyality_cms.exception;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CstErrorResponse {
    private String message;
    private long timestamp;
}
