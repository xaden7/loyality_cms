package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.JwtAuthentication;
import md.akdev.loyality_cms.model.Reward;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.RewardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Service
public class RewardService {

    private final RewardRepository rewardRepository;
    private final JwtAuthService jwtAuthService;

    private final ClientsRepository clientsRepository;
    @Autowired
    public RewardService(RewardRepository rewardRepository, JwtAuthService jwtAuthService, ClientsRepository clientsRepository) {
        this.rewardRepository = rewardRepository;
        this.jwtAuthService = jwtAuthService;
        this.clientsRepository = clientsRepository;
    }

    public Optional<Reward> findById(Integer id){
        return rewardRepository.findById(id);
    }

    public List<Reward> findAll(){

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return rewardRepository.findAllActiveRewardsNotAuth(LocalDate.now());
        }

        final JwtAuthentication authentication = jwtAuthService.getAuthInfo();
        ClientsModel clientsModel = clientsRepository.getClientByUuid1c(authentication.getUuid());

        if (clientsModel.getId() != null) {
        return    rewardRepository.findAllActiveRewards(LocalDate.now(), clientsModel.getId());
        }

        return new ArrayList<>();
    }

}
