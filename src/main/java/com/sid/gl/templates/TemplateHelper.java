package com.sid.gl.templates;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TemplateHelper {
    CONFIRMATION_CANDIDATURE("confirmation_candidature.html", "Confirmation de candidature", "confirmation");

    private final String templateName;
    private final String subject;
    private final String name;

    TemplateHelper(String templateName, String subject, String name) {
        this.templateName = templateName;
        this.subject = subject;
        this.name = name;
    }


    public static TemplateHelper fromName(String name) {
        return Arrays.stream(TemplateHelper.values())
                .filter(templateHelper -> templateHelper.name.equals(name))
                .findFirst()
                .orElse(null);
    }

}
