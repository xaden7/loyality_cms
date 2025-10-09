package md.akdev.loyality_cms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAlreadyUsedException extends RuntimeException{
    private String message = "Survey already used";
}
