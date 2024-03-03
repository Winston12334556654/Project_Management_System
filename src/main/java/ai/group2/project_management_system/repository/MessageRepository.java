package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long>{

    List<Message> getMessagesByIssueId(Long issueId);

}
