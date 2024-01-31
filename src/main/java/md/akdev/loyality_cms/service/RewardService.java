package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.Reward;
import md.akdev.loyality_cms.repository.RewardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

import java.util.Optional;

@Service
public class RewardService {

    private final RewardRepository rewardRepository;

    @Autowired
    public RewardService(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }


    public Optional<Reward> findById(Integer id){
        return rewardRepository.findById(id);
    }

    public List<Reward> findAll(){
        return rewardRepository.findAll();
    }

}
