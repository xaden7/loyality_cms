package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.survey.SurveyUsedRequest;
import md.akdev.loyality_cms.exception.CstErrorResponse;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.exception.RewardAlreadyUsedException;
import md.akdev.loyality_cms.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;
    Logger logger = LoggerFactory.getLogger(SurveyController.class);


    @GetMapping("/active")
    public ResponseEntity<?> getActiveSurveys() {
        return ResponseEntity.ok(surveyService.getActiveSurveysForClient());
    }

    @PostMapping("/save-used")
    public ResponseEntity<?> saveSurveyUsed(@RequestBody @Validated SurveyUsedRequest request) {
        surveyService.saveSurveyUsed(request);
        return ResponseEntity.ok().body(
                surveyService.getActiveSurveysForClient());
    }

    @ExceptionHandler({NotFoundException.class, MethodArgumentNotValidException.class, RewardAlreadyUsedException.class,
            DataIntegrityViolationException.class, RuntimeException.class, AccessDeniedException.class})
    private ResponseEntity<CstErrorResponse> handeException(Exception e){

        CstErrorResponse cstErrorResponse = new CstErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        HttpStatus status = switch (e.getClass().getSimpleName()) {
            case "NotFoundException" -> HttpStatus.NOT_FOUND;
            case  "DataIntegrityViolationException" -> HttpStatus.CONFLICT;
            case "AccessDeniedException" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.BAD_REQUEST;
        };
        logger.warn(cstErrorResponse.getMessage());
        return new ResponseEntity<>(cstErrorResponse, status);
    }
}
