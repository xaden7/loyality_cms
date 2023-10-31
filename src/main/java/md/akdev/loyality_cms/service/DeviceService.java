package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.model.DevicesModel;
import md.akdev.loyality_cms.restController.DevicesRestController;
import md.akdev.loyality_cms.utils.MappingUtils;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    private final MappingUtils mappingUtils;
    private final DevicesRestController devicesRestController;

    public DeviceService(MappingUtils mappingUtils, DevicesRestController devicesRestController) {
        this.mappingUtils = mappingUtils;
        this.devicesRestController = devicesRestController;
    }

    public DevicesModel mapToDeviceModel(ClientDeviceDto dto){
        return mappingUtils.mapToDeviceModel(dto);
    }

    public void addDevice(DevicesModel device){
        devicesRestController.addDevice(device);
    }

}
