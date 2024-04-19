package md.akdev.loyality_cms.restController;

import lombok.AllArgsConstructor;
import md.akdev.loyality_cms.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/get-by-article")
    public ResponseEntity<?> getImageByArticle(String article, boolean isMin) throws IOException {

       return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageService.getImageByArticle(article, isMin));

    }
}
