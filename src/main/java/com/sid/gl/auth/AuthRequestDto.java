package com.sid.gl.auth;

public record AuthRequestDto(String usernameOrEmail, String password) {
}
