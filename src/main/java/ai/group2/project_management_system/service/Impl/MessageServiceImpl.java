package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.model.entity.Message;
import ai.group2.project_management_system.repository.MessageRepository;
import ai.group2.project_management_system.service.MessageService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByChannelId(Long issueId) {
        return messageRepository.getMessagesByIssueId(issueId);
    }


}
