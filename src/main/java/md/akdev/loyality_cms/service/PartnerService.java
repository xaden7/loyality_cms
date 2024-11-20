package md.akdev.loyality_cms.service;

import lombok.RequiredArgsConstructor;

import md.akdev.loyality_cms.model.partners.Partner;
import md.akdev.loyality_cms.repository.PartnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;

    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }



}



