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
        return "apps-chat";
    }

    @GetMapping("/aaa/testChat")
    public String index() {
        return "chat-box";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {


        String sender = message.getSender();
        String content = message.getContent();

        System.out.println("sender   : "+ sender);
        System.out.println("message   : "+ content);

        // Process the incoming message (e.g., store in a database) and return it
        return message;
    }
}
