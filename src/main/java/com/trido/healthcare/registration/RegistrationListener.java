package com.trido.healthcare.registration;

import com.trido.healthcare.entity.User;
import com.trido.healthcare.entity.VerificationToken;
import com.trido.healthcare.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final JavaMailSender javaMailSender;
    private final UserService userService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent onRegistrationCompleteEvent) {
        User user = onRegistrationCompleteEvent.getUser();
        VerificationToken verificationToken = userService.createVerificationToken(user);
        String token = verificationToken.getToken();

        String recipientAddress = user.getUsername();
        String subject = "Registration Confirmation";
        String confirmationUrl =
                onRegistrationCompleteEvent.getAppUrl() + "/registrationConfirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Complete registration by this URL" + "\r\n" + confirmationUrl);
        javaMailSender.send(email);
    }
}
