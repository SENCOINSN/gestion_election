package com.sid.gl.elections.bulletins;

import com.sid.gl.elections.ElectionResponseDto;
import com.sid.gl.users.UserResponseDto;


public record BulletinResponseDto (
        UserResponseDto candidat,
        ElectionResponseDto election
        )  {
}
