package md.akdev.loyality_cms.restController;


import md.akdev.loyality_cms.dto.RewardDTO;
import md.akdev.loyality_cms.dto.RewardsTypeDTO;
import md.akdev.loyality_cms.model.Reward;
import md.akdev.loyality_cms.model.RewardsType;
import md.akdev.loyality_cms.repository.RewardTypeRepository;
import md.akdev.loyality_cms.service.RewardService;
import md.akdev.loyality_cms.utils.exceptions.CstErrorResponse;
import md.akdev.loyality_cms.utils.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {
    private final RewardTypeRepository rewardsTypeRepository;
    private final RewardService rewardService;
    private final ModelMapper modelMapper;

    @Autowired
    public RewardController(RewardTypeRepository rewardsTypeRepository, RewardService rewardService, ModelMapper modelMapper) {
        this.rewardsTypeRepository = rewardsTypeRepository;
        this.rewardService = rewardService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/get-all-rewards-type")
    public ResponseEntity<?> getAllRewards(){

        return ResponseEntity.ok().body(rewardsTypeRepository.findAll().stream().map((element) ->
                modelMapper.map(element, RewardsTypeDTO.class)).collect(java.util.stream.Collectors.toList()));
    }

    @GetMapping("/get-reward-type/{id}")
    public ResponseEntity<?> getRewardById(@PathVariable Integer id){

        RewardsType rewardsType = rewardsTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Reward type with id " + id + " not found"));
        return ResponseEntity.ok().body(modelMapper.map(rewardsType, RewardsTypeDTO.class));
    }

    @GetMapping("/get-reward/{id}")
    public ResponseEntity<?> getRewardTypeById(@PathVariable Integer id){

        Reward reward = rewardService.findById(id).orElseThrow(() -> new NotFoundException("Reward with id " + id + " not found"));
        return ResponseEntity.ok().body(modelMapper.map(reward, RewardDTO.class));
    }

    @GetMapping("/get-all-rewards")
    public ResponseEntity<?> getAllRewardsType(){

        return ResponseEntity.ok().body(rewardService.findAll().stream().map((element) ->
                modelMapper.map(element, RewardDTO.class)).collect(java.util.stream.Collectors.toList()));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NotFoundException.class, MethodArgumentNotValidException.class,
            DataIntegrityViolationException.class, RuntimeException.class, AccessDeniedException.class})
    private ResponseEntity<CstErrorResponse> handeException(Exception e){

        CstErrorResponse cstErrorResponse = new CstErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(cstErrorResponse, HttpStatus.BAD_REQUEST );
    }
}
