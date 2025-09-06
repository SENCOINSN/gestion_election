package com.sid.gl.elections;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ElectionRequestDto(
        @NotBlank(message = "name is required")
                @NotNull(message = "name must not be null")
        String name,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
        LocalDateTime startDate,
        Long duration
) {
}
