package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.BannerFullDTO;
import md.akdev.loyality_cms.model.Banner;
import md.akdev.loyality_cms.service.BannerService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping("/all")
    public Iterable<Banner> getAllBanners() {
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
}
