package md.akdev.loyality_cms.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.NotificationUser;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.NotificationUserRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationUserService {
    private final NotificationUserRepository notificationUserRepository;
    private final ClientsRepository clientsRepository;

    @Transactional
    @Modifying
    public void setIsRead(Integer notificationUserId){

        notificationUserRepository.findById(notificationUserId).ifPresentOrElse(
                notificationUser -> notificationUser.setIsRead(true),
                () -> {
                    throw new NotFoundException("Notification  with id " + notificationUserId + " not found");
                }
        );
    }

    public List<NotificationUser> getAllNotificationUsers() {

        Optional<ClientsModel> client = getLoggedInClientId();

        if (client.isPresent()) {
            return notificationUserRepository.findAllByUserId(client.get().getId());
        }
        throw new NotFoundException("Client not found");

    }

    public Optional<NotificationUser> getNotificationUserById(Integer notificationUserId) {
        return notificationUserRepository.findById(notificationUserId);
    }

    @Transactional
    public void deleteNotificationById(Integer notificationUserId) {
        notificationUserRepository.findById(notificationUserId).ifPresentOrElse(
                notificationUserRepository::delete,
                () -> {
                    throw new NotFoundException("Notification  with id " + notificationUserId + " not found");
                }
        );
    }

    @Transactional
    @Modifying
    public void setAllAsRead() {
        Optional<ClientsModel> client = getLoggedInClientId();

        client.ifPresent(clientsModel -> notificationUserRepository.findAllByUserId(clientsModel.getId())

                .forEach(notificationUser -> notificationUser.setIsRead(true)));

    }

    private Optional<ClientsModel> getLoggedInClientId() {
       return clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
