package md.akdev.loyality_cms.restController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.model.DevicesModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.service.DeviceService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/device")
@RequiredArgsConstructor
public class DeviceRestController {
    private final DeviceService deviceService;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceRestController.class);
    private final ClientsRepository clientsRepository;

    @PostMapping("/addDevice")
    public ResponseEntity<?> addDevice(@RequestBody DevicesModel devicesModel, HttpServletRequest request){
        try{
           logger.info("DeviceRestController | addDevice: Phone - \u001B[32m"+ request.getUserPrincipal().getName() + "\u001B[0m; " +  request.getHeader("Authorization") + " - " + devicesModel);

           if (devicesModel.getClientId() == null) {
               clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).ifPresent(cl -> devicesModel.setClientId(cl.getId()));
           }

            return deviceService.addDevice(devicesModel);
        }catch (Exception e) {
            logger.error("DeviceRestController | addDevice: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
