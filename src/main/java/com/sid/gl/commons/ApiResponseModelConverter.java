package com.sid.gl.commons;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Iterator;

@Component
public class ApiResponseModelConverter implements ModelConverter {
    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (type.getType() instanceof java.lang.reflect.ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();

            if (rawType == ApiResponse.class) {
                // Extraire le type générique (T) ApiResponse  UserResponseDto
                Type actualType = parameterizedType.getActualTypeArguments()[0];
                AnnotatedType annotatedType = new AnnotatedType(actualType).resolveAsRef(true);

                // Retourner le schéma de T (et ignorer le wrapper)
                return context.resolve(annotatedType);
            }
        }

        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        }
        return null;
    }
}
