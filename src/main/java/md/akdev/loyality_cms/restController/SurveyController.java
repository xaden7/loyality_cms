package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.survey.SurveyUsedRequest;
import md.akdev.loyality_cms.service.SurveyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;


    @GetMapping("/active")
    public ResponseEntity<?> getActiveSurveys() {
        return ResponseEntity.ok(surveyService.getActiveSurveysForClient());
    }

    @PostMapping("/save-used")
    public ResponseEntity<?> saveSurveyUsed(@RequestBody SurveyUsedRequest request) {
        surveyService.saveSurveyUsed(request);
        return ResponseEntity.ok().body(
                surveyService.getActiveSurveysForClient());
    }
}
