package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/branches")
public class BranchController {
    private final BranchRepository branchRepository;


    @Autowired
    public BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllBranches(){
        return ResponseEntity.ok().body(branchRepository.findAll());
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(Integer id){
        return ResponseEntity.ok().body(branchRepository.findById(id));
    }

    @GetMapping("/get-by-code")
    public ResponseEntity<?> getByCode(String code){
        return ResponseEntity.ok().body(branchRepository.findByCode(code));
    }


}
