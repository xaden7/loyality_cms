package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.model.DevicesModel;
import md.akdev.loyality_cms.repository.DevicesRepository;
import md.akdev.loyality_cms.utils.MappingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    private final MappingUtils mappingUtils;
    public final DevicesRepository devicesRepository;

    public DeviceService(MappingUtils mappingUtils, DevicesRepository devicesRepository) {
        this.mappingUtils = mappingUtils;
        this.devicesRepository = devicesRepository;
    }

    public DevicesModel mapToDeviceModel(ClientDeviceDto dto) {
        return mappingUtils.mapToDeviceModel(dto);
    }

    public ResponseEntity<?> addDevice(DevicesModel inputDevice) {
        DevicesModel getDevice = devicesRepository.getDeviceByDeviceId(inputDevice.getDeviceId());
        if (getDevice == null) {
            devicesRepository.save(inputDevice);
        } devicesRepository.save(getDevice);
        return ResponseEntity.ok("successful");
    }

    public void lastConnectDate (String deviceId){
        DevicesModel getDevice = devicesRepository.getDeviceByDeviceId(deviceId);
        if(getDevice != null){
            java.time.LocalDateTime currentDateTime = java.time.LocalDateTime.now();
            getDevice.setLastConnect(currentDateTime);
            devicesRepository.save(getDevice);
        }
    }
}