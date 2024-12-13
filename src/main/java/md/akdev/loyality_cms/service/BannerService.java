package md.akdev.loyality_cms.service;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.BannerFullDTO;
import md.akdev.loyality_cms.dto.promotion.PromotionDTO;
import md.akdev.loyality_cms.dto.promotion.PromotionDetailDTO;
import md.akdev.loyality_cms.model.Banner;
import md.akdev.loyality_cms.repository.BannerRepository;
import md.akdev.loyality_cms.repository.promotion.PromotionDetailsRepository;
import md.akdev.loyality_cms.repository.promotion.PromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;
    private final PromotionRepository promotionRepository;
    private final ModelMapper modelMapper;
    private final PromotionDetailsRepository promotionDetailsRepository;


    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    public Banner getBannerById(UUID id) {
        return bannerRepository.findById(id).orElse(null);
    }

    public BannerFullDTO getBannerFullDTO() {
        Banner banner = getAllBanners().get(0);
        BannerFullDTO bannerFullDTO = new BannerFullDTO();
        bannerFullDTO.setTitle(banner.getTitleRu());
        bannerFullDTO.setTitle(banner.getTitleRo());
        bannerFullDTO.setImage(banner.getImage());

        List<PromotionDTO> promotions = promotionRepository.findAllActive(LocalDate.now()).stream().map((element) ->
                        modelMapper.map(element, PromotionDTO.class)).toList()
                .stream().peek((element) -> {
                            element.setPromotionDetails(promotionDetailsRepository.findAllByPromotionId(element.getId()).stream().map((detail) -> modelMapper.map(detail, PromotionDetailDTO.class)).toList());
                        }
                ).toList();
        List<PromotionDetailDTO> promotionDetailDTOs = new ArrayList<>();

        promotions.forEach(promotion -> {
            if (promotion.getPromotionDetails() != null) {
                promotionDetailDTOs.addAll(promotion.getPromotionDetails());
            }
        });

        bannerFullDTO.setPromotionDetailDTOList(promotionDetailDTOs);

        return bannerFullDTO;
    }



}
