package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.product.ProductForSiteDTO;
import md.akdev.loyality_cms.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/get-by-articles")
    public ResponseEntity<?> getByArticles(@RequestBody List<String> article){

        List<ProductForSiteDTO> product = productRepository.findAllByArticleIn(article).stream().map((element) ->
                        modelMapper.map(element, ProductForSiteDTO.class)).toList();

     return ResponseEntity.ok().body(product);
    }

    @GetMapping("/get-by-article")
    public ResponseEntity<?> getByArticle(String article){
        return ResponseEntity.ok().body(
                productRepository.findByArticle(article).map((element) -> modelMapper.map(element, ProductForSiteDTO.class))
        );
    }
}
