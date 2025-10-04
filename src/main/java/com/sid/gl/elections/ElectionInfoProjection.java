package com.sid.gl.elections;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Projection for {@link Election}
 */
public interface ElectionInfoProjection {
    Long getId();

    String getName();

    Date getStartDate();

    boolean isActive();
    String getDescription();
    LocalDateTime getEndDate();
}