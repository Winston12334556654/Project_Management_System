package ai.group2.project_management_system.service;

import ai.group2.project_management_system.model.entity.Message;

import java.util.List;

public interface MessageService {
    Message save(Message message);
    List<Message> getMessagesByChannelId(Long issueId);
}
