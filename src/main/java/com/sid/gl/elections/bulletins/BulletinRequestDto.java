package com.sid.gl.elections.bulletins;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record BulletinRequestDto(
        @NotNull(message = "email not null")
                @NotBlank(message = "email not null")
                @Email(message = "format email not valide")
        String email,
        @NotBlank(message = "le nom est obligatoire")
                @NotNull(message = "le nom est obligatoire")
                                 String nameElection,
        String nameParty,
        String addressParty,
        @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
        Date dateCreation) {
}
