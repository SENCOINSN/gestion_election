package com.sid.gl.notifications;

import com.sid.gl.templates.TemplateHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class NotificationServiceTest {


    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private TaskExecutor taskExecutor;
    @Mock
    private SpringTemplateEngine springTemplateEngine;
    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private TemplateHelper templateHelper;

    // Add to @InjectMocks NotificationService notificationService;
    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
       lenient().when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(notificationService, "FROM", "seyeadam28@gmail.com");
    }

    @Test
    void sendEmail_shouldExecuteTaskToSendSimpleEmail() {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(taskExecutor).execute(any(Runnable.class));

        notificationService.sendEmail("to@example.com", "Subject", "Body");

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmailWithAttachment_shouldExecuteTaskToSendEmailWithAttachment() throws Exception {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(taskExecutor).execute(any(Runnable.class));

        notificationService.sendEmailWithAttachment("to@example.com", "Subject", "Body", new byte[]{1, 2, 3});

        verify(javaMailSender).send(any(MimeMessage.class));
    }

    //@Test
    void sendEmailWithAttachment_shouldThrowRuntimeExceptionOnMessagingException() {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            try {
                runnable.run();
            } catch (RuntimeException e) {
                throw e;
            }
            return null;
        }).when(taskExecutor).execute(any(Runnable.class));

        doThrow(new RuntimeException("error")).when(javaMailSender).send(any(MimeMessage.class));

        //notificationService.sendEmailWithAttachment("to@example.com", "Subject", "Body", new byte[]{1, 2, 3});
    }

    @Test
    void sendEmailWithTemplate_shouldExecuteTaskToSendEmailWithTemplate() {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(taskExecutor).execute(any(Runnable.class));

        when(springTemplateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Email</html>");

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "User");

        notificationService.sendEmailWithTemplate("to@example.com", "confirmation", variables);

        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendEmailWithTemplate_shouldThrowRuntimeExceptionOnMessagingException() {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            try {
                runnable.run();
            } catch (RuntimeException e) {
                throw e;
            }
            return null;
        }).when(taskExecutor).execute(any(Runnable.class));

        when(springTemplateEngine.process(anyString(), any(Context.class)))
            .thenThrow(new RuntimeException("template error"));

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "User");

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            notificationService.sendEmailWithTemplate("to@example.com", "confirmation", variables)
        );
        // Optionally check the message
        org.junit.jupiter.api.Assertions.assertTrue(
            ex.getMessage().contains("Failed to send email template")
        );
    }

    @Test
    void sendSimpleEmail_shouldSendEmailWithCorrectParameters() {
        SimpleMailMessage[] sentMessage = new SimpleMailMessage[1];
        doAnswer(invocation -> {
            sentMessage[0] = invocation.getArgument(0);
            return null;
        }).when(javaMailSender).send(any(SimpleMailMessage.class));

        ReflectionTestUtils.invokeMethod(notificationService, "sendSimpleEmail", "to@example.com", "Subject", "Body");

        org.junit.jupiter.api.Assertions.assertEquals("to@example.com", sentMessage[0].getTo()[0]);
        org.junit.jupiter.api.Assertions.assertEquals("Subject", sentMessage[0].getSubject());
        org.junit.jupiter.api.Assertions.assertEquals("Body", sentMessage[0].getText());
    }

}
