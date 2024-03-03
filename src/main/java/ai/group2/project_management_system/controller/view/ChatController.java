package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.model.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ChatController {
    @GetMapping("/chat-list")
    public String chatList() {
        return "apps-chat1";
    }

    @GetMapping("/aaa/testChat")
    public String index() {
        return "chat-box";
    }
    
}
