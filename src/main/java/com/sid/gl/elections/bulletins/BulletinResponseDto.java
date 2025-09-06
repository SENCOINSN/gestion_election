package com.sid.gl.elections.bulletins;

import java.time.LocalDateTime;

public record BulletinResponseDto (
        UserResponseDto candidat,
        ElectionResponseDto election
        )  {
}
