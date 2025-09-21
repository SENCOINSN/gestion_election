package com.sid.gl.elections;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

public record ElectionResponseDto(
        Long id,
        @NotNull( message = "name must not be null")
        @NotBlank(message = "name is required")
        String name,

        @NotNull(message = "desc must not be null")
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "UTC")
        LocalDateTime dateStart,
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "UTC")
        LocalDateTime dateEnd
) {
}
