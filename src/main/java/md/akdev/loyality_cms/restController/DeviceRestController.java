package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.model.DevicesModel;
import md.akdev.loyality_cms.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/device")
public class DeviceRestController {
    private final DeviceService deviceService;

    public DeviceRestController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    @PostMapping("/addDevice")
    public ResponseEntity<?> addDevice(@RequestBody DevicesModel devicesModel){
        try{
           ResponseEntity<?> req = deviceService.addDevice(devicesModel);
           return req;
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
