package ai.group2.project_management_system.service.Impl;


import ai.group2.project_management_system.model.entity.EmailDetail;
import ai.group2.project_management_system.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private  JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String simpleMail(EmailDetail emailDetails) {
        try{
            //Built-in
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipients().toArray(new String[0]));
            mailMessage.setText(emailDetails.getMsgBody());
            mailMessage.setSubject(emailDetails.getSubject());
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

//    @Override
//    public String sendMailWithAttachment(EmailDetail emailDetails) {
//        MimeMessage mimeMessage
//                = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper;
//        try {
//            // Setting multipart as true for attachments to
//            // be send
//            mimeMessageHelper
//                    = new MimeMessageHelper(mimeMessage, true);
//            mimeMessageHelper.setFrom(sender);
//            mimeMessageHelper.setTo(emailDetails.getRecipient());
//            mimeMessageHelper.setText(emailDetails.getMsgBody());
//            mimeMessageHelper.setSubject(
//                    emailDetails.getSubject());
//
//            // Adding the attachment
//            FileSystemResource file
//                    = new FileSystemResource(
//                    new File(emailDetails.getAttachment()));
//
//            mimeMessageHelper.addAttachment(
//                    file.getFilename(), file);
//
//            // Sending the mail
//            javaMailSender.send(mimeMessage);
//            return "Mail sent Successfully";
//        }
//
//        // Catch block to handle MessagingException
//        catch (MessagingException e) {
//
//            // Display message when exception occurred
//            return "Error while sending mail!!!";
//        }
//
//    }
    @Override
    public String generateOTP() {
        // Generate a 6-digit random OTP
        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        return String.valueOf(otpValue);
    }

    @Override
    public ResponseEntity<String> sendEmailWithAttachment(String recipient, String subject, String message, MultipartFile attachment) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);

            if (attachment != null) {
                mimeMessageHelper.addAttachment(Objects.requireNonNull(attachment.getOriginalFilename()), attachment);
            }

            javaMailSender.send(mimeMessage);
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error sending email");
        }
    }

    @Override
    public String sendMultipleEmail(EmailDetail emailDetails){
        try{
            //Built-in
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipients().toArray(new String[0]));
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMsgBody());

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

}
