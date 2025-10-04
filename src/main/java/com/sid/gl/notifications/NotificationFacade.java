package com.sid.gl.notifications;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface NotificationFacade {
    void sendEmail(String to, String subject, String body);
    void sendEmailWithAttachment(String to, String subject, String body, byte[] attachmentPath) throws MessagingException;

    void sendEmailWithTemplate(String to, String name, Map<String, Object> variables);

    default String sendSms(String to, String body){
        return "send sms";
    }
}
