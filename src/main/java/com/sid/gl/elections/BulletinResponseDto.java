package com.sid.gl.elections;

import java.time.LocalDateTime;

public record BulletinResponseDto (
        String lastName,
        String firstName,
        String partyName,
        String parcours,
        String electionName,
        LocalDateTime electionStartDate)  {
}
