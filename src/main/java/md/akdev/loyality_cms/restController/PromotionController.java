package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.PromotionDTO;
import md.akdev.loyality_cms.dto.PromotionDetailDTO;
import md.akdev.loyality_cms.model.Promotion;
import md.akdev.loyality_cms.model.PromotionDetail;
import md.akdev.loyality_cms.repository.PromotionDetailsRepository;
import md.akdev.loyality_cms.repository.PromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionRepository promotionRepository;
    private final ModelMapper modelMapper;

    private final PromotionDetailsRepository promotionDetailsRepository;
    @Autowired
    public PromotionController(PromotionRepository promotionRepository, ModelMapper modelMapper, PromotionDetailsRepository promotionDetailsRepository) {
        this.promotionRepository = promotionRepository;
        this.modelMapper = modelMapper;
        this.promotionDetailsRepository = promotionDetailsRepository;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllPromotions(){
        return ResponseEntity.ok().body(
                promotionRepository.findAllActive(LocalDate.now()).stream().map((element) ->
                        modelMapper.map(element, PromotionDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/get-details-by-promotion-id")
    public ResponseEntity<?> getDetailsByPromotionId(Integer id){
        return ResponseEntity.ok().body(
                promotionDetailsRepository.findAllByPromotionId(id).stream().map((element) ->
                        modelMapper.map(element, PromotionDetailDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(Integer id){
        return ResponseEntity.ok().body(
                modelMapper.map(promotionRepository.findById(id), PromotionDTO.class)
        );
    }

    @GetMapping("/header/get-image")
    public ResponseEntity<?> getImage(Integer id){

        Optional<Promotion> promotion = promotionRepository.findById(id);

        String data = promotion.isPresent() ? promotion.get().getImage() : "";

        if (data.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpeg")).body(new byte[0]);
        }

        String base64Image = data.split(",")[1];
        byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(promotion.get().getImageType()))
                .body(imageBytes);

    }

    @GetMapping("/details/get-image")
    public ResponseEntity<?> getImageDetails(Integer id){

        Optional<PromotionDetail> promotionDetails = promotionDetailsRepository.findById(id);

        String data = promotionDetails.isPresent() ? promotionDetails.get().getImage() : "";

        if (data.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpeg")).body(new byte[0]);
        }
        String base64Image = data.split(",")[1];
        byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(promotionDetails.get().getImageType()))
                .body(imageBytes);
    }

}
