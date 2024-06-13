package md.akdev.loyality_cms.dto.promotion;


import md.akdev.loyality_cms.dto.TagDTO;

import java.time.LocalDate;
import java.util.List;

public interface PromotionImp {
    Integer getId();
    Integer getPriority();
    String getName();
    String getNameRu();
    String getDescription();
    String getDescriptionRu();
    LocalDate getStartDate();
    LocalDate getEndDate();
    Double getUpToDiscount();
    Double getUpToBonus();
    String getImageName();
    String getImageType();
    String getStatus();
    List<TagDTO> getTags();
    List<PromotionDetailDTO> getPromotionDetails();

}
