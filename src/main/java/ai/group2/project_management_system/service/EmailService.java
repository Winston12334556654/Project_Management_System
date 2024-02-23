package ai.group2.project_management_system.service;



import ai.group2.project_management_system.model.entity.EmailDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    String simpleMail(EmailDetail emailDetails);
    String generateOTP();
//    String sendMailWithAttachment(EmailDetail emailDetails);
    ResponseEntity<String> sendEmailWithAttachment(String recipient, String subject, String message, MultipartFile attachment);

    String sendMultipleEmail( EmailDetail emailDetails);
}

