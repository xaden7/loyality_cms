package md.akdev.loyality_cms.service;

import lombok.AllArgsConstructor;
import md.akdev.loyality_cms.model.product.ProductForSite;
import md.akdev.loyality_cms.repository.product.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import org.springframework.stereotype.Service;


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
        String minMaxPath = isMin ? "images/min/" : "images/max/";
        path = Path.of(minMaxPath + imageName);
        return     Files.readAllBytes(path).length == 0 ? Files.readAllBytes(Path.of(minMaxPath + "/No_Image_Available.jpg")) : Files.readAllBytes(path);

    }


    public byte[] getImageByArticle(String article, boolean isMIn) throws IOException {

        logger.info("ImageService | getImageByArticle for article: {} isMin: {}", article, isMIn);
        byte[] image;
        String productImage = productRepository.findByArticle(article).map(ProductForSite::getImages).orElse("No_Image_Available.jpg");

        if (productImage.isEmpty()) {
            productImage = "No_Image_Available.jpg";
        }

        image = getImage(productImage, isMIn);
        return image;
    }
}
