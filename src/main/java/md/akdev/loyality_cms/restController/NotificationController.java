package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.exception.CstErrorResponse;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.service.NotificationUserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationUserService notificationUserService;

    @DeleteMapping
    public ResponseEntity<?> deleteNotificationById(@RequestParam String id){
        notificationUserService.deleteNotificationById(Integer.valueOf(id));
        return  ResponseEntity.ok(
                Map.of("message" ,"notification has been deleted"));

    }

    @PutMapping("/set-all-as-read")
    public ResponseEntity<?> setAllAsRead(){
        notificationUserService.setAllAsRead();
        return  ResponseEntity.ok(Map.of("message", "all notifications have been set as read"));
    }

    @PutMapping("/set-as-read")
    public ResponseEntity<?> setAsRead(@RequestParam String id){
        notificationUserService.setIsRead(Integer.valueOf(id));
        return  ResponseEntity.ok(Map.of("message", "notification has been set as read"));
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllNotifications(){
        return new ResponseEntity<>(notificationUserService.getAllNotificationUsers(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllNotificationsByUserId(@RequestParam String id){
        return new ResponseEntity<>(notificationUserService.getNotificationUserById(Integer.valueOf(id)), HttpStatus.OK);
    }


    @ExceptionHandler({NotFoundException.class, MethodArgumentNotValidException.class,
            DataIntegrityViolationException.class, RuntimeException.class, AccessDeniedException.class})
    private ResponseEntity<CstErrorResponse> handeException(Exception e){

        CstErrorResponse cstErrorResponse = new CstErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        HttpStatus status = switch (e.getClass().getSimpleName()) {
            case "NotFoundException" -> HttpStatus.NOT_FOUND;
            case "DataIntegrityViolationException" -> HttpStatus.CONFLICT;
            case "AccessDeniedException" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.BAD_REQUEST;
        };
        return new ResponseEntity<>(cstErrorResponse, status);
    }

}
