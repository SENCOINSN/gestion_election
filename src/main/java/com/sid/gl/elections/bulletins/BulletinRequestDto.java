package com.sid.gl.elections.bulletins;

import com.sid.gl.users.PartyName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BulletinRequestDto(
        @NotNull(message = "email not null")
                @NotBlank(message = "email not null")
                @Email(message = "format email not valide")
        String email,
                                 @NotBlank(message = "le nom est obligatoire")
                                 String nameElection,
                                 @NotNull(message = "les infos du partie ne doivent pas être nuls")
                                 PartyName partyName) {
}
