package md.akdev.loyality_cms.service;



import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.reward.RewardUsedDTO;
import md.akdev.loyality_cms.model.*;
import md.akdev.loyality_cms.model.reward.*;
import md.akdev.loyality_cms.repository.*;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.exception.RewardAlreadyUsedException;
import md.akdev.loyality_cms.repository.reward.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.LocalDate.now;

@Service
@RequiredArgsConstructor
public class RewardUsedService {
    private final RewardUsedRepository rewardUsedRepository;
    private final RewardService rewardService;
    private final ClientsRepository clientsRepository;
    private final RewardUsedLogRepository rewardUsedLogRepository;
    private final RewardDetailsRepository rewardsDetailsRepository;
    private final RewardRepository rewardRepository;
    private final RewardUsedDetailsRepository rewardUsedDetailsRepository;
    private final RewardDetailMultimediaRowRepository rewardDetailMultimediaRowRepository;
    private final ReturnsOfLoyalityCardRepository returnsOfLoyalityCardRepository;


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveRewardUsed(RewardUsedDTO rewardUsed){
        RewardsType rewardType =
                rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found")).getRewardType();

        if (rewardType.getRewardMethod() == 1 || rewardType.getRewardMethod() == 6 || rewardType.getRewardMethod() == 7){
            saveQrRewardUsed(rewardUsed);
        }
        else if (rewardType.getRewardMethod() == 2) {
            saveGiftRewardUsed(rewardUsed);
        }
        else if (rewardType.getRewardMethod() == 3 || rewardType.getRewardMethod() == 5 ){
                saveFortuneRewardUsed(rewardUsed);
        }
        else if (rewardType.getRewardMethod() == 4) {
            saveMultimediaRewardUsed(rewardUsed);
        }
        else if (rewardType.getRewardMethod() == 8){
            saveQrMultiReward(rewardUsed);
        }

        else
            throw new NotFoundException("Reward type with id " + rewardType.getId() + " not found");

    }

    private void saveQrMultiReward(RewardUsedDTO rewardUsed) {
        verifyRewardUsed(rewardUsed, "PROMO CODE REWARD(MULTI)");

        Reward reward = getReward(rewardUsed);

        if (rewardUsed.getText() == null || rewardUsed.getText().isEmpty()) {
            throw new NotFoundException("Code(text) is required");
        }

        if(rewardUsed.getClientId() == null ){
            throw new NotFoundException("Client id is required");
        }


        rewardsDetailsRepository.findRewardDetailByRewardAndQrCodeIgnoreCase(reward, rewardUsed.getText()).ifPresentOrElse(
                rewardDetail -> {
                    if(rewardUsedRepository.findByRewardDetail(rewardDetail).isPresent()){
                        throw new RewardAlreadyUsedException("This text(code) already used");
                    }
                    RewardUsed rewardUsedToSave = new RewardUsed();

                    ClientsModel client = clientsRepository.findById(rewardUsed.getClientId()).orElseThrow(() -> new NotFoundException("Client with id " + rewardUsed.getClientId() + " not found"));

                    rewardUsedToSave.setClient(client);
                    rewardUsedToSave.setMovedToLoyality(0);
                    rewardUsedToSave.setReward(reward);
                    rewardUsedToSave.setRewardDetail(rewardDetail);

                    rewardUsedRepository.save(rewardUsedToSave);
                },
                () -> {
                    throw new NotFoundException("That code  " + rewardUsed.getText() + " not found");
                }
        );
    }


    //method 4 in table reward_type;
    private void saveMultimediaRewardUsed(RewardUsedDTO rewardUsed) {
        verifyRewardUsed(rewardUsed, "MULTIMEDIA REWARD");

        Reward reward = getReward(rewardUsed);

        RewardDetail rewardDetail = rewardsDetailsRepository.findByRewardAndId(reward, rewardUsed.getRewardDetailId()).orElseThrow(() -> new NotFoundException("Reward detail with id " + rewardUsed.getRewardDetailId() + " not found"));

        preSave(rewardUsed, rewardDetail);
    }

    private void saveFortuneRewardUsed(RewardUsedDTO rewardUsed) {

        verifyRewardUsed(rewardUsed, "FORTUNE REWARD");
        Reward reward = getReward(rewardUsed);

        RewardDetail rewardDetail = rewardsDetailsRepository.findByRewardAndId(reward, rewardUsed.getRewardDetailId()).orElseThrow(() -> new NotFoundException("Reward detail with id " + rewardUsed.getRewardDetailId() + " not found"));

        preSave(rewardUsed, rewardDetail);

    }

    //method 2 in table reward_type;
    public void saveQrRewardUsed(RewardUsedDTO rewardUsed){
        Reward reward = getReward(rewardUsed);

        String operation = switch (reward.getRewardType().getRewardMethod()) {
            case 1 -> "QR REWARD";
            case 2 -> "CARD BACK REWARD";
            case 6 -> "RETURN OF CARD REWARD";
            case 7 -> "PROMO CODE REWARD";
            default ->
                    throw new NotFoundException("Reward method with id " + reward.getRewardType().getRewardMethod() + " not found");
        };

        verifyRewardUsed(rewardUsed, operation);  // todo: modify this to Dynamic value

        preSave(rewardUsed, reward);
    }

    private Reward getReward(RewardUsedDTO rewardUsed) {
        Reward reward = rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found"));

        if (now().isAfter(reward.getDateTo()))
            throw new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " is expired");

        if(now().isBefore(reward.getDateFrom()))
            throw new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " is not active or hasn't started yet");
        return reward;
    }

    //method 2 in table reward_type;
    public void saveGiftRewardUsed(RewardUsedDTO rewardUsed){
       verifyRewardUsed(rewardUsed, "GIFT REWARD");// todo: modify this to Dynamic value
        Reward reward = rewardService.findById(rewardUsed.getRewardId()).orElseThrow(() -> new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found"));
        preSave(rewardUsed, reward);

    }

    private void preSave(RewardUsedDTO rewardUsed, Reward reward) {
        ClientsModel client = clientsRepository.findById(rewardUsed.getClientId()).orElseThrow(() -> new NotFoundException("Client with id " + rewardUsed.getClientId() + " not found"));

        if (rewardUsedRepository.findByRewardAndClient(reward, client).isPresent())
            throw new RewardAlreadyUsedException("Reward with id " + rewardUsed.getRewardId() + " is already used by client with id " + rewardUsed.getClientId());

        if (reward.getRewardType().getRewardMethod() == 6){

            if (rewardUsed.getText().isEmpty()){
                throw new NotFoundException("Once returned card code is required");
            }


            returnsOfLoyalityCardRepository.findByClientIdAndPromoCode(UUID.fromString(client.getUuid1c())
                                , rewardUsed.getText().trim().toUpperCase(Locale.ROOT)).ifPresentOrElse(
                                        i -> {
                                            i.setPromoCodeUsed(true);
                                            returnsOfLoyalityCardRepository.save(i);
                                        },
                                        () -> {
                                            throw new NotFoundException("Return of loyality card with promo code " + rewardUsed.getText() + " not found for client " + client.getPhoneNumber());
                                        }
                        );
        }

        RewardUsed rewardUsedToSave = new RewardUsed();
        rewardUsedToSave.setClient(client);
        rewardUsedToSave.setMovedToLoyality(0);
        rewardUsedToSave.setReward(reward);

        rewardUsedRepository.save(rewardUsedToSave);
    }

    private void preSave(RewardUsedDTO rewardUsed, RewardDetail rewardDetail){
        ClientsModel client = clientsRepository.findById(rewardUsed.getClientId()).orElseThrow(() -> new NotFoundException("Client with id " + rewardUsed.getClientId() + " not found"));

        AtomicInteger allowToSendLoyality = new AtomicInteger();

        rewardRepository.findById(rewardUsed.getRewardId()).ifPresentOrElse(present -> {

            List<Integer> OncePerClient = Arrays.asList(1, 2, 3);   // Used only once reward method per client
            List<Integer> OncePerRewardDetail = List.of(4); // Used only once reward method per reward detail
            List<Integer> OncePerDay = List.of(5); // Used only once reward method per day

            if (OncePerClient.contains(present.getRewardType().getRewardMethod())) {
                if (rewardUsedRepository.findByRewardAndClient(present, client).isPresent()) {
                    throw new RewardAlreadyUsedException("Reward with id " + rewardUsed.getRewardId() + " is already used by client with id " + rewardUsed.getClientId());
                }

                allowToSendLoyality.set(1);
            }
//            add new type of reward method
            if (OncePerDay.contains(present.getRewardType().getRewardMethod())) {

                if (rewardUsedRepository.findByRewardAndClientAndCreatedAt(present.getId(), client.getId(), now()).isPresent()) {
                    throw new RewardAlreadyUsedException("Reward with id " + rewardUsed.getRewardId() + " is already used by client with id " + rewardUsed.getClientId());
                }

                allowToSendLoyality.set(1);
            }

            if (OncePerRewardDetail.contains(present.getRewardType().getRewardMethod())){

                if (rewardsDetailsRepository.findByRewardAndId(present, rewardUsed.getRewardDetailId()).isEmpty()){
                    throw new NotFoundException("Reward detail with id " + rewardUsed.getRewardDetailId() + " not found");
                }

                if (rewardDetailMultimediaRowRepository.findById(rewardUsed.getRewardDetailMultimediaRowId()).isEmpty()){
                    throw new NotFoundException("Reward detail multimedia row with id " + rewardUsed.getRewardDetailMultimediaRowId() + " not found");
                }

                if (rewardUsedRepository.findByRewardDetailAndClient(rewardDetail, client).isPresent()){
                    throw new RewardAlreadyUsedException("Reward with id " + rewardUsed.getRewardId() + " is already used by client with id " + rewardUsed.getClientId());
                }

                if (rewardUsedDetailsRepository.findByRewardDetailIdAndClientId(rewardDetail.getId(), client.getId()).isPresent()){
                    throw new RewardAlreadyUsedException("Reward with id " + rewardUsed.getRewardId() + " is already used by client with id " + rewardUsed.getClientId());
                }

                RewardUsedDetails rewardUsedDetails = getRewardUsedDetails(rewardUsed, client);

                rewardUsedDetailsRepository.save(rewardUsedDetails);

               if (isCorrectAnswer(rewardUsed)){
                   allowToSendLoyality.set(1);
               }
            }

        }, () -> {
            throw new NotFoundException("Reward with id " + rewardUsed.getRewardId() + " not found");
        });

        if (allowToSendLoyality.get() == 1){
                RewardUsed rewardUsedToSave = new RewardUsed();
                rewardUsedToSave.setClient(client);
                rewardUsedToSave.setMovedToLoyality(0);
                rewardUsedToSave.setReward(rewardDetail.getReward());
                rewardUsedToSave.setRewardDetail(rewardDetail);

            rewardUsedRepository.save(rewardUsedToSave);
        }

    }

    private boolean isCorrectAnswer(RewardUsedDTO rewardUsed) {
        return rewardDetailMultimediaRowRepository.findById(rewardUsed.getRewardDetailMultimediaRowId()).map(RewardsDetailsMultimediaRow::getIsCorrect).orElse(false);
    }

    private static RewardUsedDetails getRewardUsedDetails(RewardUsedDTO rewardUsed, ClientsModel client) {
        RewardUsedDetails rewardUsedDetails = new RewardUsedDetails();
        rewardUsedDetails.setClientId(client.getId());

        rewardUsedDetails.setRewardId(rewardUsed.getRewardId());
        rewardUsedDetails.setRewardDetailId(rewardUsed.getRewardDetailId());
        rewardUsedDetails.setRewardDetailMultimediaId(rewardUsed.getRewardDetailMultimediaId());
        rewardUsedDetails.setRewardDetailMultimediaRowId(rewardUsed.getRewardDetailMultimediaRowId());
        return rewardUsedDetails;
    }


    private void verifyRewardUsed(RewardUsedDTO rewardUsed, String operation){

        AtomicReference<UUID> clientId = new AtomicReference<>();

        if(rewardUsed == null)
            throw new NotFoundException("RewardUsed object is required");

        if (rewardUsed.getRewardId() == null)
            throw new NotFoundException("Reward object (reward id) is required");

        if (rewardUsed.getClientId() == null){

            String clientUuid1c = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (Objects.equals(clientUuid1c, "anonymousUser"))
                throw new NotFoundException("Client object (id key)  is required, please login first");


            clientsRepository.getClientByUuid1c(clientUuid1c).ifPresent(clientsModel -> clientId.set(clientsModel.getId()));


            rewardUsed.setClientId(clientId.get());
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
