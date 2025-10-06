package com.sid.gl.elections.scrutins;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScrutinPVResultats {
    @Builder.Default
    long voices=0;
    String fullName;
    String email;
}
