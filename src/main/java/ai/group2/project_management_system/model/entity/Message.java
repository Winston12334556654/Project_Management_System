package ai.group2.project_management_system.model.entity;

public class Message {

    private String sender;
    private String content;


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
