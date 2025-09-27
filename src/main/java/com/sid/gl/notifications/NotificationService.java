package com.sid.gl.notifications;

import com.sid.gl.exceptions.GestionElectionTechnicalException;
import com.sid.gl.templates.TemplateHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationService implements NotificationFacade {
    private final JavaMailSender javaMailSender;
    private final String FROM = "seyeadam28@gmail.com";
    private final TaskExecutor taskExecutor;
    private final SpringTemplateEngine springTemplateEngine;


    //Thread principal (spring execute)
    //Thread secondaire (thread pool)

    //mode async (non bloquant) ça ne bloque pas le thread principal (mode background)

   //@Async (non dans les méthodes privées)
    @Override
    public void sendEmail(String to, String subject, String body) {
       taskExecutor.execute(() -> sendSimpleEmail(to, subject, body));
    }


    @Override
    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachmentPath) {
       taskExecutor.execute(() -> {
           try {
               sendEmailAttachment(to, subject, body, attachmentPath);
           } catch (Exception e) {
               log.warn("Technical error send email with attachment");
               throw new GestionElectionTechnicalException("failed to send email with attachment",e);
           }
       });
    }


    //template customisé
    @Override
    public void sendEmailWithTemplate(String to, String templateName, Map<String, Object> variables) {
       taskExecutor.execute(() -> {
           try {
               sendEmailWithCustomTemplate(to, templateName, variables);
           } catch (Exception e) {
               throw new GestionElectionTechnicalException("Failed to send email template");
           }
       });
    }

    private void sendEmailWithCustomTemplate(String to, String templateName, Map<String, Object> variables) throws MessagingException {
       log.info("Send email with template {} to user {}",templateName,to);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, UTF_8.name());
        TemplateHelper templateHelper = TemplateHelper.fromName(templateName);
        String nameTemplate = templateHelper.getTemplateName();
        processSendNotification(to,mimeMessage,messageHelper,nameTemplate,variables);

    }


    private void processSendNotification(String email,MimeMessage  mimeMessage,MimeMessageHelper messageHelper,
                                         String templateName,Map<String, Object> variables)  {
        try{
            Context context = new Context();
            context.setVariables(variables);
            String htmlTemplate = springTemplateEngine.process(templateName,context);
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setFrom(FROM);
            messageHelper.setTo(email);
            javaMailSender.send(mimeMessage);
            log.info("send email successfully with email {}",email);
        }catch (Exception e) {
            log.warn("Technical error send email with template");
            throw new GestionElectionTechnicalException("failed to send email with template",e);
        }
    }


    private void sendSimpleEmail(String email,String subject,String body){
        log.info("Send email to user ---{}",email);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(FROM);
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);
        log.info("SuccessFully sended mail");
    }

    private void sendEmailAttachment(String to,String subject,String body,byte[] attachmentPath) throws MessagingException {
        log.info("Sending email rapport pdf to {}",to);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.addAttachment("rapport.pdf",new ByteArrayResource(attachmentPath));
        javaMailSender.send(mimeMessage);
        log.info("Email rapport pdf sent to {}",to);
    }
}
