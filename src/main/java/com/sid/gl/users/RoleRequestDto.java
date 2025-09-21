package com.sid.gl.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoleRequestDto (
        @NotNull(message = "Role name is required")
                @NotBlank(message = "Role name is required")
        String roleName){
}
