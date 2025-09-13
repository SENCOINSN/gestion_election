package com.sid.gl.elections;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ElectionResponseDto(
        Long id,
        @NotNull( message = "name must not be null")
        @NotBlank(message = "name is required")
        String name,

        @NotNull(message = "desc must not be null")
        String description,
        LocalDateTime dateStart
) {
}
