package md.akdev.loyality_cms.restController;


import jakarta.servlet.http.HttpServletRequest;
import md.akdev.loyality_cms.dto.RewardDTO;
import md.akdev.loyality_cms.dto.RewardUsedDTO;
import md.akdev.loyality_cms.dto.RewardsTypeDTO;
import md.akdev.loyality_cms.model.Reward;
import md.akdev.loyality_cms.model.RewardsType;
import md.akdev.loyality_cms.repository.RewardTypeRepository;
import md.akdev.loyality_cms.service.RewardService;
import md.akdev.loyality_cms.service.RewardUsedService;
import md.akdev.loyality_cms.exception.CstErrorResponse;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.exception.RewardAlreadyUsedException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {
    private final RewardTypeRepository rewardsTypeRepository;
    private final RewardService rewardService;
    private final ModelMapper modelMapper;
    private final RewardUsedService rewardUsedService;

    Logger logger = LoggerFactory.getLogger(RewardController.class);


    @Autowired
    public RewardController(RewardTypeRepository rewardsTypeRepository, RewardService rewardService, ModelMapper modelMapper, RewardUsedService rewardUsedService) {
        this.rewardsTypeRepository = rewardsTypeRepository;
        this.rewardService = rewardService;
        this.modelMapper = modelMapper;
        this.rewardUsedService = rewardUsedService;
    }


    @GetMapping("/get-all-rewards-type")
    public ResponseEntity<?> getAllRewards(HttpServletRequest request){
        logger.info("RewardController | getAllRewards:  Phone - \u001B[32m"+ request.getUserPrincipal().getName() + "\u001B[0m; " +  request.getHeader("Authorization")  +    " - " + rewardsTypeRepository.findAll().size() + " records");
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

    @GetMapping("/header/get-image")
    public ResponseEntity<?> getImage(Integer id){

        Optional<Reward> reward = rewardService.findById(id);

        String data = reward.isPresent() ? reward.get().getImage() : "";

        if (data.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpeg")).body(new byte[0]);
        }

        String base64Image = data.split(",")[1];
        byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(reward.get().getImageType()))
                .body(imageBytes);
    }

    @PostMapping("/new-reward-to-use")
    public ResponseEntity<?> newUsedReward(@RequestBody RewardUsedDTO rewardUsed, HttpServletRequest request){
        logger.info("RewardController | newUsedReward: Phone - \u001B[32m"+ request.getUserPrincipal().getName() + "\u001B[0m; " + request.getHeader("Authorization") + " " + rewardUsed);
        rewardUsedService.saveRewardUsed(rewardUsed);
        return ResponseEntity.ok("Reward used successfully");
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
            case "RewardAlreadyUsedException", "DataIntegrityViolationException" -> HttpStatus.CONFLICT;
            case "AccessDeniedException" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.BAD_REQUEST;
        };
        logger.warn(cstErrorResponse.getMessage());
        return new ResponseEntity<>(cstErrorResponse, status);
    }
}
