package com.sid.gl.elections;

import java.time.LocalDateTime;

public record ElectionResponseDto(
        String name,
        String description,
        LocalDateTime dateStart
) {
}
