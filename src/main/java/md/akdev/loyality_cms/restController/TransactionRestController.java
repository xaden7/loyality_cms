package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.model.TransactionModel;
import md.akdev.loyality_cms.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user/token")
public class TransactionRestController {
    private final TransactionService transactionService;

    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @GetMapping("getTransaction")
    public ResponseEntity<?> getTransaction(){
        try{
            List<TransactionModel> transaction = transactionService.getTransaction();
            return new ResponseEntity(transaction, HttpStatus.OK);
        }catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
