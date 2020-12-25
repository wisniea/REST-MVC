package pl.wisniea.restmvc_data.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.wisniea.restmvc_data.entities.UserEntity;
import pl.wisniea.restmvc_data.entities.VerificationTokenEntity;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final VerificationTokenService verificationTokenService;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;



    public EmailServiceImpl(VerificationTokenService verificationTokenService, TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.verificationTokenService = verificationTokenService;
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }


    @Override
    public void sendHtmlMail(UserEntity user) throws MessagingException {
        VerificationTokenEntity verificationToken = verificationTokenService.getByUser(user);


        if (verificationToken != null) {
            String token = verificationToken.getToken();
            Context context = new Context();

            // Setting url with DNS and Token as query param
            String link = String.format("http://localhost:8080/activation?token=%s", token);

            context.setVariable("title", "Welcome to E-Libre! Please verify your email address");
            context.setVariable("link", link);

            // passing variables to HTML template
            String body = templateEngine.process("activation", context);

            // Sending msg
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("E-Libre e-mail verification");
            helper.setText(body, true);

            javaMailSender.send(message);
        }
    }
}
