package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.entity.User;
import dev.pjatk.recipeapp.util.Loggable;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class EmailService implements Loggable {
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;
    private final String url;
    private final String from;

    public EmailService(JavaMailSender mailSender,
                        MessageSource messageSource,
                        SpringTemplateEngine templateEngine,
                        @Value("${recipe.base-url}") String url,
                        @Value("${spring.mail.username}") String from) {
        this.mailSender = mailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.url = url;
        this.from = from;
    }

    public void senActivationEmail(User user) {
        log().info("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplateTo(user, "mail/activation");
    }

    private void sendEmailFromTemplateTo(User user, String template) {
        if (user.getEmail() == null) {
            log().warn("Email doesn't exist for user '{}'", user.getEmail());
            return;
        }
        Locale locale = Locale.forLanguageTag("pl"); // we support only polish for now
        Context context = new Context(locale);
        context.setVariable("url", (url + "/activate?token=%s").formatted(user.getActivationToken()));
        String content = templateEngine.process(template, context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmailSync(user.getEmail(), subject, content, false, true);
    }

    private void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log().debug(
                "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart,
                isHtml,
                to,
                subject,
                content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            mailSender.send(mimeMessage);
            log().debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log().warn("Email could not be sent to user '{}'", to, e);
        }
    }
}
