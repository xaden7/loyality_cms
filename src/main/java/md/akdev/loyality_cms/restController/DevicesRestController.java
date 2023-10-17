package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.model.DevicesModel;
import md.akdev.loyality_cms.repository.DevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DevicesRestController {

    @Autowired
    DevicesRepository devicesRepository;

   public ResponseEntity<String> addDevice(DevicesModel inputDevice) {

        DevicesModel getDevice = devicesRepository.getDeviceByDeviceId(inputDevice.getDeviceId());
        if (getDevice == null){
        devicesRepository.save(inputDevice);
        return ResponseEntity.ok("Insert new device");}
        return ResponseEntity.ok("This device already in the database");
    }
}
