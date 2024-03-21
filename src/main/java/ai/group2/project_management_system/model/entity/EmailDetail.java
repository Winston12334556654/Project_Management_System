package ai.group2.project_management_system.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

import java.io.Serializable;



// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailDetail implements Serializable {
    // Importing required classes

    private List<String> recipients;

        private String msgBody;
        private String subject;
    private List<MultipartFile> attachments;

    public List<MultipartFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MultipartFile> attachments) {
        this.attachments = attachments;
    }

    public String getMsgBody() {
            return msgBody;
        }

        public void setMsgBody(String msgBody) {
            this.msgBody = msgBody;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public void addRecipient(String mail) {
    }
}
