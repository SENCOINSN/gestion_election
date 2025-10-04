package com.sid.gl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// binding properties from application.properties

@Component
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class MediaProperties {
    private String uploadPath;
}
