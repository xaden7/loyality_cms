package md.akdev.loyality_cms.utils;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.DevicesModel;
import md.akdev.loyality_cms.model.QuestionaryModel;
import org.springframework.stereotype.Service;

@Service
public class MappingUtils {
    public ClientsModel mapToClientsModel(ClientDeviceDto dto){
        ClientsModel clientsModel = new ClientsModel();
        clientsModel.setClientName(dto.getClientName());
        clientsModel.setPhoneNumber(dto.getPhoneNumber());
        clientsModel.setCodeCard(dto.getCodeCard());
        return clientsModel;
    }

    public DevicesModel mapToDeviceModel(ClientDeviceDto dto){
        DevicesModel devicesModel = new DevicesModel();
        devicesModel.setDeviceId(dto.getDeviceId());
        devicesModel.setClientId(dto.getId());
        return devicesModel;
    }

    public ClientsModel mapQuestionaryToClientsModel(QuestionaryModel questionaryModel){
        ClientsModel clientsModel = new ClientsModel();
        clientsModel.setPhoneNumber(questionaryModel.getPhoneNumber());
        clientsModel.setCodeCard(questionaryModel.getBarcode());
        return clientsModel;
    }
}
