package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.DevicesModel;
import md.akdev.loyality_cms.repository.DevicesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    public final DevicesRepository devicesRepository;

    public DeviceService(DevicesRepository devicesRepository) {
        this.devicesRepository = devicesRepository;
    }

    public ResponseEntity<?> addDevice(DevicesModel inputDevice) {
            devicesRepository.save(inputDevice);
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
