package com.sid.gl.medias;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class FileSizeValidator implements ConstraintValidator<ValidateSize, Optional<MultipartFile>> {
    private long maxFileSize;
    @Override
    public void initialize(ValidateSize constraintAnnotation) {
        this.maxFileSize = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Optional<MultipartFile> value, ConstraintValidatorContext context) {
        return value.isPresent() && value.get().getSize() <= maxFileSize;
    }

}
