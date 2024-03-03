package ai.group2.project_management_system.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    private long senderId;
    private String content;
    private Long issueId;
    private LocalDateTime timeStamp;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message(Long senderId, String content, Long issueId, LocalDateTime timeStamp) {
        this.senderId = senderId;
        this.content = content;
        this.issueId = issueId;
        this.timeStamp = timeStamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
// getters and setters

    public Message() {
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message(Long senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }
}
