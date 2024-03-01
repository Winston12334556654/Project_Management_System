package ai.group2.project_management_system.model.entity;

import java.time.LocalDateTime;

public class Message {

    private String sender;
    private String content;
    private Long issueId;
    private LocalDateTime timeStamp;

    public Message(String sender, String content, Long issueId, LocalDateTime timeStamp) {
        this.sender = sender;
        this.content = content;
        this.issueId = issueId;
        this.timeStamp = timeStamp;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
}
