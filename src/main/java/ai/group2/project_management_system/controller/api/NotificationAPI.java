package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.entity.Notification;
import ai.group2.project_management_system.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
public class NotificationAPI {

    private  Long id=0L;
     private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/sendNotification")
//    @SendTo("/queue/notifications")
    public void sendNotification(@Payload Notification notification)
    {
        String recipient = notification.getSendTo().getEmail();
        Notification filteredNotification = new Notification();
        filteredNotification.setContent(notification.getContent());
        filteredNotification.setSender(notification.getSender());
        filteredNotification.setSendTo(notification.getSendTo());
        filteredNotification.setTitle(notification.getTitle());
        filteredNotification.setRedirectURL(notification.getRedirectURL());
        filteredNotification.setId(id++); /// temp id // to replace with .save() autogenerate from database
        simpMessagingTemplate.convertAndSendToUser(recipient, "/specific", filteredNotification);
    }
}
