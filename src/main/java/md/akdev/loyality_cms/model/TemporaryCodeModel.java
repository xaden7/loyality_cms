package md.akdev.loyality_cms.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TemporaryCodeModel implements Serializable {
    private Integer code;
    private Integer lifeTimeInMinutes;
}
