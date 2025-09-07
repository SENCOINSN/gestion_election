package com.sid.gl.users;

import jakarta.validation.constraints.*;

public record UserRequestDto(
        @NotBlank(message = "lastName is required")
        @NotNull(message = "lastName must not be null")
        @Size(min=4,max = 50,message = "lastName must be between 4 and 50 characters")
        String lastName,

        @NotNull(message = "email must not be null")
        @NotBlank(message = "email is required")
        @Email(message = "email is not valid")
        String email,

        @NotNull(message = "password must not be null")
        @NotBlank(message = "password is required")
        @Size(min=8,max=20,message = "password must be between 8 and 20 characters")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",message = "password must be strong")
        String password,

        @NotNull(message = "firstName must not be null")
        @NotBlank(message = "firstName is required")
        @Size(min=4,max = 50,message = "firstName must be between 12 and 50 characters")
        String firstName,
        String username

) {

}
