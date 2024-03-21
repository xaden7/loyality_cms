package md.akdev.loyality_cms.service;

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
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RewardService {

    private final RewardRepository rewardRepository;

    private final ClientsRepository clientsRepository;
    @Autowired
    public RewardService(RewardRepository rewardRepository,  ClientsRepository clientsRepository) {
        this.rewardRepository = rewardRepository;
        this.clientsRepository = clientsRepository;
    }

    public Optional<Reward> findById(Integer id){
        return rewardRepository.findById(id);
    }

    public List<Reward> findAll(){

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return rewardRepository.findAllActiveRewardsNotAuth(LocalDate.now());
        }

        AtomicReference<List<Reward>> rewards = new AtomicReference<>(new ArrayList<>());

        clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).ifPresent( cl -> {
            rewards.set(rewardRepository.findAllActiveRewards(LocalDate.now(), cl.getId()));
        });

        return rewards.get();
    }

}
