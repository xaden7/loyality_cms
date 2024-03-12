package md.akdev.loyality_cms.service;



import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.RewardUsedDTO;
import md.akdev.loyality_cms.model.*;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.RewardDetailsRepository;
import md.akdev.loyality_cms.repository.RewardUsedRepository;
import md.akdev.loyality_cms.repository.RewardUsedLogRepository;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.exception.RewardAlreadyUsedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;
import java.util.UUID;

import static java.time.LocalDate.now;

@Service
@RequiredArgsConstructor
public class RewardUsedService {
    private final RewardUsedRepository rewardUsedRepository;
    private final RewardService rewardService;
    private final ClientsRepository clientsRepository;
    private final RewardUsedLogRepository rewardUsedLogRepository;
    private final RewardDetailsRepository rewardsDetailsRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveRewardUsed(RewardUsedDTO rewardUsed){
        RewardsType rewardType =
                rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found")).getRewardType();

        if (rewardUsedRepository.findByRewardAndClient(rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found")), clientsRepository.findById(rewardUsed.getClientId()).orElseThrow(() -> new NotFoundException("Client with id " + rewardUsed.getClientId() + " not found"))).isPresent())
            throw new RewardAlreadyUsedException("Reward with id " + rewardUsed.getRewardId() + " is already used by client with id: " + rewardUsed.getClientId());

        if (rewardType.getRewardMethod() == 1)
            saveQrRewardUsed(rewardUsed);
        else if (rewardType.getRewardMethod() == 2)
            saveGiftRewardUser(rewardUsed);
        else if (rewardType.getRewardMethod() == 3 ){
                saveFortuneRewardUser(rewardUsed);
        } else
            throw new NotFoundException("Reward type with id " + rewardType.getId() + " not found");

    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    protected void saveFortuneRewardUser(RewardUsedDTO rewardUsed) {

        verifyRewardUsed(rewardUsed, "FORTUNE REWARD");
        Reward reward = getReward(rewardUsed);

        RewardDetail rewardDetail = rewardsDetailsRepository.findByRewardAndId(reward, rewardUsed.getRewardDetailId()).orElseThrow(() -> new NotFoundException("Reward detail with id " + rewardUsed.getRewardDetailId() + " not found"));

        preSave(rewardUsed, rewardDetail);

    }

    //method 2 in table reward_type;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveQrRewardUsed(RewardUsedDTO rewardUsed){

        verifyRewardUsed(rewardUsed, "QR REWARD");  // todo: modify this to Dynamic value

        Reward reward = getReward(rewardUsed);

        preSave(rewardUsed, reward);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    protected Reward getReward(RewardUsedDTO rewardUsed) {
        Reward reward = rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found"));

        if (now().isAfter(reward.getDateTo()))
            throw new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " is expired");

        if(now().isBefore(reward.getDateFrom()))
            throw new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " is not active or hasn't started yet");
        return reward;
    }

    //method 2 in table reward_type;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveGiftRewardUser(RewardUsedDTO rewardUsed){
       verifyRewardUsed(rewardUsed, "GIFT REWARD");// todo: modify this to Dynamic value

        Reward reward = rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found"));
        preSave(rewardUsed, reward);

    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    protected void preSave(RewardUsedDTO rewardUsed, Reward reward) {
        ClientsModel client = clientsRepository.findById(rewardUsed.getClientId()).orElseThrow(() -> new NotFoundException("Client with id " + rewardUsed.getClientId() + " not found"));

        if (rewardUsedRepository.findByRewardAndClient(reward, client).isEmpty()){
                RewardUsed rewardUsedToSave = new RewardUsed();
                rewardUsedToSave.setClient(client);
                rewardUsedToSave.setMovedToLoyality(0);
                rewardUsedToSave.setReward(reward);

                rewardUsedRepository.save(rewardUsedToSave);
        }else{
            throw new RewardAlreadyUsedException("Reward with id " + rewardUsed.getRewardId() + " is already used by client with id " + rewardUsed.getClientId());
        }



    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    protected void preSave(RewardUsedDTO rewardUsed, RewardDetail rewardDetail){
        ClientsModel client = clientsRepository.findById(rewardUsed.getClientId()).orElseThrow(() -> new NotFoundException("Client with id " + rewardUsed.getClientId() + " not found"));

        if (rewardUsedRepository.findByRewardAndClient(rewardDetail.getReward(), client).isEmpty()){
                RewardUsed rewardUsedToSave = new RewardUsed();
                rewardUsedToSave.setClient(client);
                rewardUsedToSave.setMovedToLoyality(0);
                rewardUsedToSave.setReward(rewardDetail.getReward());
                rewardUsedToSave.setRewardDetail(rewardDetail);

            rewardUsedRepository.save(rewardUsedToSave);
        }else{
            throw new RewardAlreadyUsedException("Reward with id " + rewardUsed.getRewardId() + " is already used by client with id " + rewardUsed.getClientId());
        }


    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    protected void verifyRewardUsed(RewardUsedDTO rewardUsed, String operation){

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
