package md.akdev.loyality_cms.restController;

import jakarta.servlet.http.HttpServletRequest;
import md.akdev.loyality_cms.dto.QuestionaryDTO;
import md.akdev.loyality_cms.model.QuestionaryModel;
import md.akdev.loyality_cms.service.QuestionaryService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class QuestionaryRestController {
    private final QuestionaryService questionaryService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(QuestionaryRestController.class);

    public QuestionaryRestController(QuestionaryService questionaryService) {
        this.questionaryService = questionaryService;
    }

    @GetMapping("getQuestionary")
    public ResponseEntity<?> getQuestionary(HttpServletRequest request){
        try {
            QuestionaryDTO questionaryModel = questionaryService.getQuestionary();
            logger.info("QuestionaryRestController | getQuestionary:  Phone - \u001B[32m"+ request.getUserPrincipal().getName() + "\u001B[0m; " + request.getHeader("Authorization") + " - " + questionaryModel);

            return new ResponseEntity<>(questionaryModel, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("QuestionaryRestController | getQuestionary: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping ("updateQuestionary")
    public ResponseEntity<?> updateQuestionary(@RequestBody QuestionaryModel questionaryModel, HttpServletRequest request){
        try {
            logger.info("QuestionaryRestController | updateQuestionary:  Phone - \u001B[32m"+ request.getUserPrincipal().getName() + "\u001B[0m; " + request.getHeader("Authorization") + " - " + questionaryModel);

            QuestionaryModel postQuestionaryModel = questionaryService.updateQuestionary(questionaryModel);
            return new ResponseEntity<>(postQuestionaryModel, HttpStatus.OK);
        }catch (Exception e) {
            logger.error("QuestionaryRestController | updateQuestionary: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



}
