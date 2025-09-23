package com.sid.gl.elections;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;


// Request recuperer depuis la base de données pour l'affiche sur le front,
public record ElectionRequestDto(
        @NotBlank(message = "name is required")
                @NotNull(message = "name must not be null")
        String name,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        Date startDate,
        Long duration
) {
}
