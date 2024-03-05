package md.akdev.loyality_cms.service;


import md.akdev.loyality_cms.dto.RewardUsedDTO;
import md.akdev.loyality_cms.model.*;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.RewardUsedRepository;
import md.akdev.loyality_cms.repository.RewardUsedLogRepository;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.exception.RewardAlreadyUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static java.time.LocalDate.now;

@Service
public class RewardUsedService {
    private final RewardUsedRepository rewardUsedRepository;
    private final RewardService rewardService;
    private final ClientsRepository clientsRepository;
    private final RewardUsedLogRepository rewardUsedLogRepository;


    @Autowired
    public RewardUsedService(RewardUsedRepository rewardUsedRepository, RewardService rewardService, ClientsRepository clientsRepository,  RewardUsedLogRepository rewardUsedLogRepository) {
        this.rewardUsedRepository = rewardUsedRepository;
        this.rewardService = rewardService;
        this.clientsRepository = clientsRepository;
        this.rewardUsedLogRepository = rewardUsedLogRepository;

    }


    public void saveRewardUsed(RewardUsedDTO rewardUsed){
        RewardsType rewardType =
                rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found")).getRewardType();

        if (rewardType.getRewardMethod() == 1)
            saveQrRewardUsed(rewardUsed);
        else if (rewardType.getRewardMethod() == 2)
            saveGiftRewardUser(rewardUsed);
        else
            throw new NotFoundException("Reward type with id " + rewardType.getId() + " not found");

    }

    //method 2 in table reward_type;
    public void saveQrRewardUsed(RewardUsedDTO rewardUsed){

        verifyRewardUsed(rewardUsed, "QR REWARD");  // todo: modify this to Dynamic value

        Reward reward = rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found"));

        if (now().isAfter(reward.getDateTo()))
            throw new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " is expired");

        if(now().isBefore(reward.getDateFrom()))
            throw new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " is not active or hasn't started yet");

         preSave(rewardUsed, reward);
    }

    //method 2 in table reward_type;
    public void saveGiftRewardUser(RewardUsedDTO rewardUsed){
       verifyRewardUsed(rewardUsed, "GIFT REWARD");// todo: modify this to Dynamic value
        Reward reward = rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found"));
        preSave(rewardUsed, reward);

    }

    private void preSave(RewardUsedDTO rewardUsed, Reward reward) {
        ClientsModel client = clientsRepository.findById(rewardUsed.getClientId()).orElseThrow(() -> new NotFoundException("Client with id " + rewardUsed.getClientId() + " not found"));

        if (rewardUsedRepository.findByRewardAndClient(reward, client).isPresent())
            throw new RewardAlreadyUsedException("Reward with id " + rewardUsed.getRewardId() + " is already used by client with id " + rewardUsed.getClientId());

        RewardUsed rewardUsedToSave = new RewardUsed();
        rewardUsedToSave.setClient(client);
        rewardUsedToSave.setMovedToLoyality(0);
        rewardUsedToSave.setReward(reward);

        rewardUsedRepository.save(rewardUsedToSave);
    }


    private void verifyRewardUsed(RewardUsedDTO rewardUsed, String operation){

        UUID clientId;

        if(rewardUsed == null)
            throw new NotFoundException("RewardUsed object is required");

        if (rewardUsed.getRewardId() == null)
            throw new NotFoundException("Reward object (reward id) is required");

        if (rewardUsed.getClientId() == null){

            String clientUuid1c = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (Objects.equals(clientUuid1c, "anonymousUser"))
                throw new NotFoundException("Client object (id key)  is required, please login first");

            clientId = clientsRepository.getClientByUuid1c(clientUuid1c).getId();

            rewardUsed.setClientId(clientId);
        }

        if (rewardUsed.getClientId() == null)
            throw new NotFoundException("Client object (id key)  is required");

        RewardUsedLog rewardUsedLog = new RewardUsedLog();
        rewardUsedLog.setClientId(rewardUsed.getClientId());
        rewardUsedLog.setRewardId(rewardUsed.getRewardId());
        rewardUsedLog.setOperation(operation);
        rewardUsedLogRepository.save(rewardUsedLog);
    }
}
