package com.sid.gl.events;

import com.sid.gl.elections.Election;
import com.sid.gl.elections.ElectionRepository;
import com.sid.gl.elections.bulletins.Bulletin;
import com.sid.gl.elections.bulletins.BulletinRepository;
import com.sid.gl.elections.scrutins.ScrutinPVResultats;
import com.sid.gl.elections.scrutins.ScrutinRepository;
import com.sid.gl.elections.scrutins.ScrutinState;
import com.sid.gl.notifications.NotificationService;
import com.sid.gl.users.User;
import com.sid.gl.users.UserMapper;
import com.sid.gl.users.UserRepository;
import com.sid.gl.users.UserResponseDto;
import com.sid.gl.utils.PdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ElectionEventListener {

    private final ElectionRepository electionRepository;
    private final ScrutinRepository scrutinRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    public final BulletinRepository bulletinRepository;

    public ElectionEventListener(ElectionRepository electionRepository, ScrutinRepository scrutinRepository, UserRepository userRepository, NotificationService notificationService, BulletinRepository bulletinRepository) {
        this.electionRepository = electionRepository;
        this.scrutinRepository = scrutinRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.bulletinRepository = bulletinRepository;
    }

    @EventListener
    public void handleElectionEvent(ElectionEvent event) throws IOException {
        // todo process logic metier pour gerer l'election sur la duree, comptage voix generation pdf,
        //todo si ce n'est pas la fin de l'election , save le scrutin  et update le user sur les elections participées
        //todo si c'est la fin de l'election, (save le scrutin), update user et  fermer l'election
        //done  et comptage des voix => generation pdf
        // et envoyer au supervisor

        log.info("Election event: {}",event);

        //todo verifier si l'election est terminée

        Election election = event.getElection();
        LocalDateTime endDate = election.getEndDate();
        LocalDateTime now = LocalDateTime.now();

        if(endDate.isBefore(now)){ // expiré
            election.setActive(false);
            electionRepository.save(election);
            List<ScrutinPVResultats> pvResultats = new ArrayList<>();
            List<Bulletin> bulletins = retrieveBulletins(election.getId());
            bulletins.forEach(bulletin -> {
                Long candidateId = bulletin.getCandidateId();
                UserResponseDto candidat = userRepository.findById(candidateId)
                        .map(UserMapper::toUserResponse)
                        .orElse(null);

                String fullName = candidat.getFirstName() + " " + candidat.getLastName();
                String email = candidat.getEmail();
                Long voices = countVoices(bulletin.getId());

                ScrutinPVResultats pvResultat = ScrutinPVResultats.builder()
                        .voices(voices)
                        .fullName(fullName)
                        .email(email)
                        .build();
                pvResultats.add(pvResultat);

            });
            // generation du pdf
            byte[] pdfPvResultats = PdfGenerator.generatePvResultats(pvResultats, election.getName());
            // envoyer le pv au superviseur
            List<User> superviseurs = userRepository.findByRoles_RoleName("SUPERVISOR");
            User supervisor = superviseurs.get(0);
            String body= """
                        Veuillez trouver ci-joint le rapport de scrutin de l'élection effectué aujourd'hui sur la plateforme 
                    """;
            notificationService.sendEmailWithAttachment(supervisor.getEmail(), "PV de résultat du scrutin Election "+election.getName(),
                    body,
                    pdfPvResultats);
        }else{
            log.info("Election is not expired");
        }

    }

    private List<Bulletin> retrieveBulletins(Long electionId){
        return bulletinRepository.findByElectionId(electionId);
    }

    private Long countVoices(Long bulletinId){
        return scrutinRepository.countByBulletinIdAndStateAllIgnoreCase
                (bulletinId, ScrutinState.COMPLETED);
    }
}
