package com.sid.gl.elections;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Date;

public record ElectionResponseDto(
        Long id,
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
        Date dateStart,
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "UTC")
        LocalDateTime dateEnd,
        boolean isActive
) {
}
