package md.akdev.loyality_cms.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.model.reward.Reward;
import md.akdev.loyality_cms.model.reward.RewardDetail;
import md.akdev.loyality_cms.model.reward.RewardsType;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.reward.RewardRepository;

import md.akdev.loyality_cms.repository.reward.RewardTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RewardService {

    private final RewardRepository rewardRepository;

    private final ClientsRepository clientsRepository;
    private final RewardTypeRepository rewardTypeRepository;
    @PersistenceContext
    private EntityManager entityManager;
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public RewardService(RewardRepository rewardRepository, ClientsRepository clientsRepository, RewardTypeRepository rewardTypeRepository) {
        this.rewardRepository = rewardRepository;
        this.clientsRepository = clientsRepository;
        this.rewardTypeRepository = rewardTypeRepository;
    }

    public Optional<Reward> findById(Integer id){


        RewardsType rewardType = rewardTypeRepository.findRewardsTypeByRewardIdNativeQuery(id).orElseThrow(
                () -> new NotFoundException("Reward type with id " + id + " not found")
        );

        if(rewardType.getRewardMethod().equals(8)){

            Object[] obj  = (Object[]) entityManager.createNativeQuery("select id, reward_type, to_char(date_from, 'yyyy-MM-dd'), to_char(date_to, 'yyyy-MM-dd'), image, image_name, image_type, description, description_ru FROM rewards r WHERE r.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();

            if (obj == null) {
                throw new NotFoundException("Reward with id " + id + " not found");
            }

            Reward reward  = new Reward();
            reward.setId((Integer) obj[0]);
            reward.setRewardType(rewardType);
            reward.setDateFrom(LocalDate.parse(obj[2].toString(), dtf));
            reward.setDateTo(LocalDate.parse(obj[3].toString(), dtf));
            reward.setImage((String) obj[4]);
            reward.setImageName((String) obj[5]);
            reward.setImageType((String) obj[6]);
            reward.setDescription((String) obj[7]);
            reward.setDescriptionRu((String) obj[8]);

            @SuppressWarnings("unchecked")
            List<Object[]> rewardDetailsObj =(List<Object[]>)  entityManager.createNativeQuery("SELECT id, qr_code, bonus_qty, description, description_ru FROM rewards_details WHERE reward_id = :id limit 10")
                    .setParameter("id", id)
                    .getResultList() ;

            reward.setRewardDetails(getListOfRewardDetails(rewardDetailsObj));

            return Optional.of(reward);
        }

        return rewardRepository.findById(id);
    }

    private static List<RewardDetail> getListOfRewardDetails(List<Object[]> rewardDetailsObj) {
        List<RewardDetail> rewardDetails = new ArrayList<>();

        for (Object[] rdObj : rewardDetailsObj) {
            RewardDetail rewardDetail = new RewardDetail();

            rewardDetail.setId((Integer) rdObj[0]);
            rewardDetail.setQrCode("NOT_AVAILABLE");
            rewardDetail.setBonusQty((BigDecimal) rdObj[2]);
            rewardDetail.setDescription((String) rdObj[3]);
            rewardDetail.setDescriptionRu((String) rdObj[4]);

            rewardDetails.add(rewardDetail);
        }
        return rewardDetails;
    }

    public List<Reward> findAll(){

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return rewardRepository.findAllActiveRewardsNotAuth(LocalDate.now());
        }

        AtomicReference<List<Reward>> rewards = new AtomicReference<>(new ArrayList<>());

        clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).ifPresent( cl -> {

            List<RewardsType> rewardType = rewardTypeRepository.findByRewardMethod(8);

            List<Integer> rewardTypeIds = rewardType.stream().map(RewardsType::getId).toList();
            List<Reward> rewards_cl = new ArrayList<>();

            if (!rewardTypeIds.isEmpty()) {

                @SuppressWarnings("unchecked")
                List<Integer> rewardIds =(List<Integer>)  entityManager.createNativeQuery("SELECT id  FROM rewards WHERE now() between date_from and date_to and reward_type in ( :id)", Integer.class)
                        .setParameter("id", rewardTypeIds)
                        .getResultList() ;


                for (Integer rewardId : rewardIds) {
                    rewards_cl.add(findById(rewardId)
                            .orElseThrow(() -> new NotFoundException("Reward with id " + rewardId + " not found"))
                    );
                }

            }

            rewards_cl.addAll(rewardRepository.findAllActiveRewards(LocalDate.now(), cl.getId()));
            rewards.set(rewards_cl);
        });

        return rewards.get();
    }

}
