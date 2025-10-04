package com.sid.gl.users;

import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDto {
    private Long id;
    private String lastName;
    private String email;
    private String firstName;
    private String username;
    private Set<Role> roles;
    private String fileUri;

}
