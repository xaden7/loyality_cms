package md.akdev.loyality_cms.exception;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RewardAlreadyUsedException extends RuntimeException{
    private String message = "Reward already used";
}
