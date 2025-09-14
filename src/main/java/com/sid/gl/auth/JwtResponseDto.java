package com.sid.gl.auth;

import com.sid.gl.users.UserResponseDto;
import lombok.Builder;

@Builder
public class JwtResponseDto {
    private String token;
    private UserResponseDto user;
}
