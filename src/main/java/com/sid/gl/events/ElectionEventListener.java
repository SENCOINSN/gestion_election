package com.sid.gl.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ElectionEventListener {

    @EventListener
    public void handleProductCreatedEvent(ElectionEvent event) {
        // todo process logic metier pour gerer l'election sur la duree, comptage voix generation pdf,
        //todo si ce n'est pas la fin de l'election , save le scrutin  et update le user sur les elections participées
        //todo si c'est la fin de l'election, (save le scrutin), update user et  fermer l'election
        //todo et comptage des voix => generation pdf et envoyer au supervisor

        log.info("Election event: {}",event);
    }
}
