package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.model.entity.Notification;
import ai.group2.project_management_system.repository.NotificationRepository;
import ai.group2.project_management_system.service.NotificationService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
