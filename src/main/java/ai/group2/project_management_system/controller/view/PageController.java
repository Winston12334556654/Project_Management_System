package ai.group2.project_management_system.controller.view;

import ai.group2.project_management_system.model.entity.EmailDetail;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.UserRepository;
import ai.group2.project_management_system.service.EmailService;
import ai.group2.project_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PageController {
    private final UserService userService;
    private final UserRepository userRepository;
    @Autowired
    private EmailService service;

    // Sending a simple Email
    @PostMapping("/forgot-password")
    public String sendMail(@RequestParam String recipient,
                           @RequestParam String subject, HttpSession session,Model model) {
        List<String> emails=userRepository.findAllEmails();
        if(emails.contains(recipient)){
            // Create an EmailDetail object and set the values
            EmailDetail details = new EmailDetail();
            details.setRecipients(Collections.singletonList(recipient));
            details.setSubject(subject);
            String OTP = service.generateOTP();
            details.setMsgBody(OTP);
            session.setAttribute("otp", OTP);
            {
                // Call the service method to send the email
                String status = service.simpleMail(details);
                session.setAttribute("email",recipient);
                return "otp-form";
            }
        }else {
            model.addAttribute("error","Your Email is invalie!");
            return "forgot-password";
        }

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordGet() {
        return "forgot-password";
    }

    @PostMapping("/otp-form")
    public String otpFormSubmit(@RequestParam String OTPInput, HttpSession session, Model model) {

        String OTP = (String) session.getAttribute("otp");

        if (OTPInput.equals(OTP)) {
            // OTPs match, redirect to create-new-password
            return "create-new-password";
        } else {
            // OTPs don't match, redirect back to otp-form
            model.addAttribute("otpError", "Your OTP is shit, please type a correct one. Thanks");
            return "otp-form";
        }

    }

    @GetMapping("/email-attachment-testing")
    public String emailAttachment() {

        return "email-attachment-testing";
    }

    @PostMapping("/sendEmailWithAttachment")
    public String sendEmailWithAttachment(@RequestParam String recipient,
                                          @RequestParam String subject,
                                          @RequestParam String message,
                                          @RequestParam(required = false) MultipartFile attachment) {
         service.sendEmailWithAttachment(recipient, subject, message, attachment);
         return "login";
    }

    @GetMapping("/sendMultipleEmail")
    public String getEmailForm(){

        return "multiple-email-selection";
    }

    @PostMapping("/sendMultipleEmail")
    public String sendMultipleEmail(@RequestBody EmailDetail email){

        System.out.println("Emails :  "+ email.getRecipients());
        System.out.println("Subject :  "+ email.getSubject());
        System.out.println("message :  "+ email.getMsgBody());
        service.sendMultipleEmail(email);

        return "login";
    }

    @PostMapping("/create-new-password")
    public String createNewPassword(@RequestParam("password") String password,HttpSession httpSession){
        System.out.println("Password:"+password);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String email= (String) httpSession.getAttribute("email");
        User user=userRepository.findByEmail(email).orElse(null);
        user.setPassword(hashedPassword);
        User savedUser=userRepository.save(user);
        if (savedUser != null) {
            System.out.println("User saved successfully with ID: " + savedUser.getId());
        } else {
            System.out.println("Failed to save user.");
        }
        httpSession.invalidate();
        return "login";
    }


}
