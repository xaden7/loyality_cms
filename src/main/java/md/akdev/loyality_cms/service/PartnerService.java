package md.akdev.loyality_cms.service;

import lombok.RequiredArgsConstructor;

import md.akdev.loyality_cms.model.partners.Partner;
import md.akdev.loyality_cms.model.partners.PartnerImage;
import md.akdev.loyality_cms.repository.PartnerImageRepository;
import md.akdev.loyality_cms.repository.PartnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerImageRepository partnerImageRepository;

    public List<Partner> getAllPartners() {
        return partnerRepository.findAllByActive(true);
    }


    public Optional<PartnerImage> findImageById(UUID id) {
        return partnerImageRepository.findById(id);
    }
}



