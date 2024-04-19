package md.akdev.loyality_cms.service;

import lombok.AllArgsConstructor;
import md.akdev.loyality_cms.model.product.ProductForSite;
import md.akdev.loyality_cms.repository.product.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@AllArgsConstructor
public class ImageService {

    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ProductRepository productRepository;

    public byte[] getImage(String imageName, boolean isMin) throws IOException {
        Path path;
        if (isMin) {
            path = Path.of("images/min/" + imageName);
        } else {
            path = Path.of("images/max/" + imageName);
        }
        File file;
        if (!Files.exists(path)) {
             file =  new ClassPathResource("default_images/No_Image_Available.jpg").getFile();
            return Files.readAllBytes(file.toPath());
        }
    return     Files.readAllBytes(path);

    }


    public byte[] getImageByArticle(String article, boolean isMIn) throws IOException {

        byte[] image;
        String productImage = productRepository.findByArticle(article).map(ProductForSite::getImages).orElse("No_Image_Available.jpg");

        image = getImage(productImage, isMIn);
        return image;
    }
}
