package com.sid.gl.elections;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ElectionResponseDto(
        Long id,
        String name,
        String description,
        LocalDateTime dateStart
) {
}
