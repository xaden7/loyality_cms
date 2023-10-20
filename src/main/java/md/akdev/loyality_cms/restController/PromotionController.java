package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.PromotionDTO;
import md.akdev.loyality_cms.model.Promotion;
import md.akdev.loyality_cms.repository.PromotionDetailsRepository;
import md.akdev.loyality_cms.repository.PromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/promotions")
public class PromotionController {

   final private PromotionRepository promotionRepository;
   final private PromotionDetailsRepository promotionDetailsRepository;
    private final ModelMapper modelMapper;
   @Autowired
    public PromotionController(PromotionRepository promotionRepository, PromotionDetailsRepository promotionDetailsRepository, ModelMapper modelMapper) {
        this.promotionRepository = promotionRepository;
       this.promotionDetailsRepository = promotionDetailsRepository;
       this.modelMapper = modelMapper;
   }

    @RequestMapping("/get-all-active")
    public ResponseEntity<?> getAllActivePromotions(LocalDateTime someDate){

       return ResponseEntity.ok().body(
               promotionRepository.findAllActive(someDate).stream().map((element) ->
                       modelMapper.map(element, PromotionDTO.class)).collect(Collectors.toList())
       );
    }


    @RequestMapping("/get-all")
    public ResponseEntity<?> getAllPromotions(){
        return ResponseEntity.ok().body(
                promotionRepository.findAll().stream().map((element) ->
                        modelMapper.map(element, PromotionDTO.class)).collect(Collectors.toList())
        );
    }


    @RequestMapping("/get-details-by-promotion-id")
    public ResponseEntity<?> getDetailsByPromotionId(Integer promotionId){
        return ResponseEntity.ok().body(
                promotionDetailsRepository.findByPromotionId(promotionId).stream().map((element) ->
                        modelMapper.map(element, PromotionDTO.class)).collect(Collectors.toList()
                ));
    }

    @PostMapping("/new-promotion")
    public ResponseEntity<?> newPromotion(@RequestBody Promotion promotion){
        return ResponseEntity.ok().body(
                promotionRepository.save(promotion)

        );
    }
}
