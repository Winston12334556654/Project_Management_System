package ai.group2.project_management_system.controller.api;

import ai.group2.project_management_system.model.entity.Department;
import ai.group2.project_management_system.model.entity.Issue;
import ai.group2.project_management_system.model.entity.Message;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.service.IssueService;
import ai.group2.project_management_system.service.MessageService;
import ai.group2.project_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatAPI {

    private final MessageService messageService;
    private final IssueService issueService;
    private final UserService userService;

    @MessageMapping("/chat/{issueId}") // Dynamic channel ID
    @SendTo("/topic/messages/{issueId}") // Dynamic topic based on channel ID
    public ResponseEntity<Message> sendMessage(@DestinationVariable("issueId") Long issueId, Message message) {
            message.setIssueId(issueId);
            message.setTimeStamp(LocalDateTime.now());
            message.setUser(userService.getUserById(message.getSenderId()));
            Message newMessage = messageService.save(message);
            System.out.println(newMessage);
            return new ResponseEntity<>(newMessage, HttpStatus.OK);
    }

    @GetMapping("/get-messages/{issueId}")
    public ResponseEntity<List<Message>> getAllMessage(@PathVariable("issueId") Long issueId) {
        List<Message> messageList = messageService.getMessagesByChannelId(issueId);
        return ResponseEntity.ok(messageList);
    }

    @GetMapping("/get-issues/{userId}")
    public ResponseEntity<List<Issue>> getIssues(@PathVariable("userId") Long userId) {
        List<Issue> issues = issueService.getAllIssueByUserId(userId);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/get-members/{issueId}")
    public ResponseEntity<List<User>> getUser(@PathVariable("issueId") Long issueId) {
        List<User> users = userService.getAllUserByIssueId(issueId);
        return ResponseEntity.ok(users);
    }

}
