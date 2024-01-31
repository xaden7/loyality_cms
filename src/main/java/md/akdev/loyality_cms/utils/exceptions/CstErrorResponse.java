package md.akdev.loyality_cms.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CstErrorResponse {
    private String message;
    private long timestamp;
}
