package md.akdev.loyality_cms.service;

import lombok.RequiredArgsConstructor;

import md.akdev.loyality_cms.model.partners.Partner;
import md.akdev.loyality_cms.repository.PartnerImageRepository;
import md.akdev.loyality_cms.repository.PartnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerImageRepository partnerImageRepository;

    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    @Transactional
    public Partner savePartner(Partner partner) {

//        if (partner.getId() != null) {
//            Partner updatetPartner = partnerRepository.findById(partner.getId()).orElseThrow(() -> new RuntimeException("Partner not found"));
//
//            partnerRepository.save(updatetPartner);
//        }

//        Partner newPartner = partnerRepository.save(partner);

        //verify if images already exist in the database
//        if (partner.getImages() != null && !partner.getImages().isEmpty()) {
//            partner.getImages().forEach(image -> {
//                if (image.getId() != null) {
//                    if (partnerImageRepository.existsById(image.getId())) {
//                        image.setPartner(newPartner);
////                        throw new DuplicateKeyException("Image already exists");
//                    }
//                }
//            });
//        }
//
//        return newPartner;
        Partner newPartner = new Partner();

        newPartner.setName(partner.getName());
        newPartner.setTitleRo(partner.getTitleRo());
        newPartner.setTitleRu(partner.getTitleRu());
        newPartner.setTextRo(partner.getTextRo());
        newPartner.setTextRu(partner.getTextRu());
        newPartner.setBannerDescriptionRo(partner.getBannerDescriptionRo());
        newPartner.setBannerDescriptionRu(partner.getBannerDescriptionRu());
        newPartner.setShortDescriptionRo(partner.getShortDescriptionRo());
        newPartner.setShortDescriptionRu(partner.getShortDescriptionRu());
        newPartner.setVeryShortDescriptionRo(partner.getVeryShortDescriptionRo());
        newPartner.setVeryShortDescriptionRu(partner.getVeryShortDescriptionRu());
        newPartner.setDiscount(partner.getDiscount());
        newPartner.setActive(partner.isActive());

        Partner savedPartner = partnerRepository.save(newPartner);

        if (partner.getImages() != null && !partner.getImages().isEmpty()) {
            partner.getImages().forEach(image -> {
                image.setPartner(savedPartner);
                savedPartner.getImages().add(image);
            });
        }

        if (partner.getDetails() != null && !partner.getDetails().isEmpty()) {
            partner.getDetails().forEach(detail -> {
                detail.setPartner(savedPartner);
                savedPartner.getDetails().add(detail);
            });
        }

        if (partner.getContacts() != null && !partner.getContacts().isEmpty()) {
            partner.getContacts().forEach(contact -> {
                contact.setPartner(savedPartner);
                savedPartner.getContacts().add(contact);
            });
        }

        return partnerRepository.save(savedPartner);
    }

    @Transactional
    public Partner updatePartner(Partner source) {
        if (source.getId() == null) {
            throw new RuntimeException("Partner ID cannot be null for update");
        }

        Partner target = partnerRepository.findById(source.getId())
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        target.setName(source.getName());
        target.setTitleRo(source.getTitleRo());
        target.setTitleRu(source.getTitleRu());
        target.setTextRo(source.getTextRo());
        target.setTextRu(source.getTextRu());
        target.setBannerDescriptionRo(source.getBannerDescriptionRo());
        target.setBannerDescriptionRu(source.getBannerDescriptionRu());
        target.setShortDescriptionRo(source.getShortDescriptionRo());
        target.setShortDescriptionRu(source.getShortDescriptionRu());
        target.setVeryShortDescriptionRo(source.getVeryShortDescriptionRo());
        target.setVeryShortDescriptionRu(source.getVeryShortDescriptionRu());
        target.setDiscount(source.getDiscount());
        target.setActive(source.isActive());

        if (source.getImages() != null) {
            target.getImages().clear();
            source.getImages().forEach(image -> {
                image.setPartner(target);
                target.getImages().add(image);
            });
        }

        if (source.getDetails() != null) {
            target.getDetails().clear();
            source.getDetails().forEach(detail -> {
                detail.setPartner(target);
                target.getDetails().add(detail);
            });
        }

        if (source.getContacts() != null) {
            target.getContacts().clear();
            source.getContacts().forEach(contact -> {
                contact.setPartner(target);
                target.getContacts().add(contact);
            });
        }

        return partnerRepository.save(target);
    }

}



