package com.sid.gl.events;

import com.sid.gl.elections.Election;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ElectionEvent extends ApplicationEvent {
    private Election election;

    public ElectionEvent(Object source,Election election) {
        super(source);
        this.election = election;
    }
}
