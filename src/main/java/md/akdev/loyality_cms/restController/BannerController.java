package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.BannerFullDTO;
import md.akdev.loyality_cms.model.Banner;
import md.akdev.loyality_cms.model.partners.PartnerImage;
import md.akdev.loyality_cms.service.BannerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping("/all")
    public List<Banner> getAllBanners() {
        return bannerService.getAllBanners();
    }

    @GetMapping("/{id}")
    public Banner getBannerById(UUID id) {
        return bannerService.getBannerById(id);
    }

    @GetMapping("/full")
    public BannerFullDTO getAllBannersFull() {
        return bannerService.getBannerFullDTO();
    }

    @GetMapping("/get-image")
    public ResponseEntity<?> getImage() {

        Banner banner = getAllBanners().get(0);

        String data = banner.getImage().isEmpty() ? "" : banner.getImage();

        if (data.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/jpeg")).body(new byte[0]);
        }

        byte[] imageBytes = java.util.Base64.getDecoder().decode(data);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);

    }
}
