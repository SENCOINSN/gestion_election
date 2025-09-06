package com.sid.gl.elections.bulletins;

import java.time.LocalDateTime;

public record BulletinResponseDto (
        String lastName,
        String firstName,
        String partyName,
        String parcours,
        String electionName,
        LocalDateTime electionStartDate)  {
}
