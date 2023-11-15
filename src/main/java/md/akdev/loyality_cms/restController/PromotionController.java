package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.PromotionDTO;
import md.akdev.loyality_cms.repository.PromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionRepository promotionRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public PromotionController(PromotionRepository promotionRepository, ModelMapper modelMapper) {
        this.promotionRepository = promotionRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllPromotions(){
        return ResponseEntity.ok().body(
                promotionRepository.findAll().stream().map((element) ->
                        modelMapper.map(element, PromotionDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(Integer id){
        return ResponseEntity.ok().body(
                modelMapper.map(promotionRepository.findById(id), PromotionDTO.class)
        );
    }
}
