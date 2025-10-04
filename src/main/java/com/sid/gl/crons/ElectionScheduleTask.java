package com.sid.gl.crons;

import com.sid.gl.elections.Election;
import com.sid.gl.elections.ElectionRepository;
import com.sid.gl.notifications.NotificationService;
import com.sid.gl.users.Role;
import com.sid.gl.users.User;
import com.sid.gl.users.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "shedule.openElection.enabled", havingValue = "true")
public class ElectionScheduleTask {
    private final ElectionRepository electionRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private static final String ROLE_NAME="ELECTOR";


    @Scheduled(cron = "${shedule.openElection.cron}")
    public void processOpenElectionAndNotify(){
        log.info("Task started !!!");
        try{
            List<Election> elections = getElectionsOnDateNow();
            elections.forEach(this::activateElection);
            List<User> electeurs = userRepository.findByRoles_RoleName(ROLE_NAME);
            electeurs.forEach(user -> {
                elections.forEach(election -> {
                    notifyElecteur(election,user);
                });
            });
            log.info("Task completed !!!");
        }catch(Exception e){
            log.error("Task failed !!!",e);
            throw new RuntimeException(e);
        }
    }

    private List<Election> getElectionsOnDateNow(){
        return electionRepository.findByStartDate();
    }

    private void activateElection(Election election){
        election.setActive(true);
        electionRepository.save(election);
    }

    public List<User> getElecteurs(){
        List<User> users = userRepository.findAll();
        Role role = new Role();
        role.setRoleName("ADMIN");
        return users.stream().noneMatch(u -> u.getRoles().contains(role))?users: Collections.emptyList();
    }


    private void notifyElecteur(Election election,User electeur){
        String subject = "Notification d'ouverture d'une election";
        String body = format("Bonjour %s %s , l'élection %s a été ouverte\n . Veuillez entrer dans la plateforme et effectuer votre droit le plus absolu !!",
                electeur.getFirstName(),electeur.getLastName(),election.getName());
        notificationService.sendEmail(electeur.getEmail(),subject,body);
    }

}
