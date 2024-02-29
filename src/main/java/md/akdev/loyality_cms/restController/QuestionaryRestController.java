package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.QuestionaryDTO;
import md.akdev.loyality_cms.model.QuestionaryModel;
import md.akdev.loyality_cms.service.QuestionaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class QuestionaryRestController {
    private final QuestionaryService questionaryService;

    public QuestionaryRestController(QuestionaryService questionaryService) {
        this.questionaryService = questionaryService;
    }

    @GetMapping("getQuestionary")
    public ResponseEntity<?> getQuestionary(){
        try {
            QuestionaryDTO questionaryModel = questionaryService.getQuestionary();
            return new ResponseEntity<>(questionaryModel, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping ("updateQuestionary")
    public ResponseEntity<?> updateQuestionary(@RequestBody QuestionaryModel questionaryModel){
        try {
            QuestionaryModel postQuestionaryModel = questionaryService.updateQuestionary(questionaryModel);
            return new ResponseEntity<>(postQuestionaryModel, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



}
