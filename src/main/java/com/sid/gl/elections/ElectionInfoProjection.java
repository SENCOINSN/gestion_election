package com.sid.gl.elections;

import java.time.LocalDateTime;

/**
 * Projection for {@link Election}
 */
public interface ElectionInfoProjection {
    Long getId();

    String getName();

    LocalDateTime getStartDate();

    boolean isActive();
    String getDescription();
}