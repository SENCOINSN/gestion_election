package com.sid.gl.elections.scrutins;

public record ScrutinVoiceRequest(
        Long bulletinId,
        Long electionId
) {
}
