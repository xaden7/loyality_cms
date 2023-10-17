package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.DevicesModel;
import md.akdev.loyality_cms.utils.MappingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientDeviceDtoController {

    private final MappingUtils mappingUtils;
    private final ClientsRestController clientsRestController;
    private final DevicesRestController devicesRestController;

    public ClientDeviceDtoController(MappingUtils mappingUtils, ClientsRestController clientsRestController, DevicesRestController devicesRestController) {
        this.mappingUtils = mappingUtils;
        this.clientsRestController = clientsRestController;
        this.devicesRestController = devicesRestController;
    }

    @GetMapping("/api/clients/phone={phone}&barcode={barcode}&deviceId={deviceId}")
    public ClientDeviceDto getClientDeviceDto(@PathVariable String phone, @PathVariable String barcode, @PathVariable String deviceId){

        ClientDeviceDto inputClient = new ClientDeviceDto();
        inputClient.setPhoneNumber(phone);
        inputClient.setCodeCard(barcode);
        inputClient.setDeviceId(deviceId);

        ClientsModel clientsModel = mappingUtils.mapToClientsModel(inputClient);
        ClientsModel getClient = clientsRestController.getClientByPhoneAndBarcode(clientsModel);

        inputClient.setId(getClient.getId());
        inputClient.setBonus(getClient.getBonus());
        inputClient.setClientName(getClient.getClientName());

        DevicesModel devicesModel = mappingUtils.mapToDeviceModel(inputClient);
        devicesRestController.addDevice(devicesModel);

        return inputClient;
    }

}
