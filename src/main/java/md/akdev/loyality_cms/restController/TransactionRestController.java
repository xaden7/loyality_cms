package md.akdev.loyality_cms.restController;

import jakarta.servlet.http.HttpServletRequest;
import md.akdev.loyality_cms.model.transaction.TransactionModel;
import md.akdev.loyality_cms.service.TransactionService;
import org.slf4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class TransactionRestController {
    private final TransactionService transactionService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(TransactionRestController.class);

    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @GetMapping("getTransaction")
    public ResponseEntity<?> getTransaction(HttpServletRequest request){
        try{
            List<TransactionModel> transaction = transactionService.getTransaction();

            logger.info("TransactionRestController | getTransaction: Phone - \u001B[32m"+ request.getUserPrincipal().getName() + "\u001B[0m; " + request.getHeader("Authorization") + " - " + transaction.size() + " records");

            return new ResponseEntity<>(transaction, HttpStatus.OK);

        }catch (Exception e)
        {
            logger.error("TransactionRestController | getTransaction: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
