package com.sid.gl.users;

import jakarta.validation.constraints.NotNull;

public record RoleRequestDto (
        @NotNull(message = "Role name is required")
        String roleName){
}
