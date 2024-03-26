package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.promotion.PromotionDTO;
import md.akdev.loyality_cms.dto.promotion.PromotionDetailDTO;
import md.akdev.loyality_cms.dto.TagDTO;
import md.akdev.loyality_cms.model.promotion.Promotion;
import md.akdev.loyality_cms.model.promotion.PromotionDetail;
import md.akdev.loyality_cms.repository.promotion.PromotionDetailsRepository;
import md.akdev.loyality_cms.repository.promotion.PromotionRepository;
import md.akdev.loyality_cms.repository.TagRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionRepository promotionRepository;
    private final ModelMapper modelMapper;
    private final PromotionDetailsRepository promotionDetailsRepository;

    private final TagRepository tagRepository;
    @Autowired
    public PromotionController(PromotionRepository promotionRepository, ModelMapper modelMapper, PromotionDetailsRepository promotionDetailsRepository, TagRepository tagRepository) {
        this.promotionRepository = promotionRepository;
        this.modelMapper = modelMapper;
        this.promotionDetailsRepository = promotionDetailsRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllPromotions(){

     List<PromotionDTO> promotion = promotionRepository.findAllActive(LocalDate.now()).stream().map((element) ->
                        modelMapper.map(element, PromotionDTO.class)).toList()
             .stream().peek((element) -> {
                         element.setTags(tagRepository.findAllByPromotionId(element.getId()).stream().map((tag) -> modelMapper.map(tag, TagDTO.class)).toList());
                         element.setPromotionDetails(promotionDetailsRepository.findAllByPromotionId(element.getId()).stream().map((detail) -> modelMapper.map(detail, PromotionDetailDTO.class)).toList());
             }
             ).toList();

        return ResponseEntity.ok().body(promotion);
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
