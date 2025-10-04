package com.sid.gl.events;

import com.sid.gl.elections.Election;
import org.springframework.context.ApplicationEvent;

public class ElectionEvent extends ApplicationEvent {
    private Election election;

    public ElectionEvent(Object source,Election election) {
        super(source);
        this.election = election;
    }
}
