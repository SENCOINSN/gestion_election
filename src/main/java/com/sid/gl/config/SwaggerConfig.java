package com.sid.gl.config;

import com.sid.gl.commons.ApiResponseModelConverter;
import io.swagger.v3.core.converter.ModelConverters;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public SwaggerConfig(ApiResponseModelConverter apiResponseModelConverter) {
        ModelConverters.getInstance().addConverter(apiResponseModelConverter);
    }
}
