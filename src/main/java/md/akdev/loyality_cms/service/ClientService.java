package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.restController.ClientsRestController;
import md.akdev.loyality_cms.utils.MappingUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientsRestController clientsRestController;
    private final MappingUtils mappingUtils;

    public ClientService(ClientsRestController clientsRestController, MappingUtils mappingUtils) {
        this.clientsRestController = clientsRestController;
        this.mappingUtils = mappingUtils;
    }

    public ClientsModel mapToClientsModel(ClientDeviceDto dto) {
        return mappingUtils.mapToClientsModel(dto);
    }

    public ClientsModel getClientByPhoneNumberAndCodeCard(ClientsModel client) throws Exception {
        return clientsRestController.getClientByPhoneNumberAndCodeCard(client);
    }

    public Optional<ClientsModel> getClientByPhoneNumber(String phoneNumber){
        return clientsRestController.getClientByPhoneNumber(phoneNumber);
    }

}
