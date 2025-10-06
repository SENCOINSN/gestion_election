package com.sid.gl.elections.scrutins;


import com.sid.gl.commons.ApiConstants;
import com.sid.gl.elections.Election;
import com.sid.gl.elections.ElectionRepository;
import com.sid.gl.elections.bulletins.Bulletin;
import com.sid.gl.elections.bulletins.BulletinRepository;
import com.sid.gl.events.ElectionEvent;
import com.sid.gl.exceptions.BadValidateException;
import com.sid.gl.exceptions.ElectionNotFoundException;
import com.sid.gl.notifications.NotificationService;
import com.sid.gl.users.User;
import com.sid.gl.users.UserRepository;
import com.sid.gl.utils.OtpHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrutinServiceImpl implements ScrutinService {
    private final ScrutinRepository scrutinRepository;
    private final UserRepository userRepository;
    private final ElectionRepository electionRepository;
    private final ApplicationEventPublisher electionEventPublisher;
    private final NotificationService notificationService;
    private final OtpHelper otpHelper;
    private final BulletinRepository bulletinRepository;


    @Transactional
    @Override
    public Long vote(ScrutinVoiceRequest request,String username) throws ElectionNotFoundException, NoSuchAlgorithmException {
        log.info("Vote request: {}",request);
        validate(request);
        Scrutin scrutin = new Scrutin();
        scrutin.setBulletinId(request.bulletinId());
        User electeur = getElecteur(username);
        scrutin.setElecteurId(electeur.getId());
        scrutin.setState(ScrutinState.PENDING); // en attente de validation otp
        //todo generer otp
        String otp = generateOtp(username);
        Scrutin scrutinSaved = scrutinRepository.save(scrutin);
        //todo send email to electeur to validate otp
        notificationService.sendSimpleEmail(electeur.getEmail(),
                "OTP Validation", "Pour l'approbation de votre scrutin," +
                        " veuillez utiliser le code suivant : " + otp);
       return scrutinSaved.getId();

    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public String validateOtp(String otp, String username,Long bulletinId) throws NoSuchAlgorithmException, BadValidateException {
        log.info("Validate otp request: {}",otp);
        User elector = getElecteur(username);
        Optional<Scrutin> optScrutin = scrutinRepository.findByBulletinIdAndElecteurId(bulletinId,elector.getId()); // not good il faut ajouter electeurId (eviter la duplication)
        if(optScrutin.isEmpty()){
            log.error("scrutin introuvable !!");
            throw new BadValidateException("scrutin introuvable pour validation otp !!");
        }
        Scrutin scrutin = optScrutin.get();

        if(BooleanUtils.isFalse(otpHelper.verifyOtpWithHash(otp,username))){
            log.error("OTP is invalid or expired");
            // todo on incrementer le nombre d'echecs de la validation du scrutin otp
            verifyAttemps(scrutin);
        }

        Bulletin bulletin = bulletinRepository.findById(bulletinId).orElse(null);
        assert bulletin != null;
        Election election = electionRepository.findById(bulletin.getElectionId()).orElse(null);

        scrutin.setState(ScrutinState.COMPLETED);
        scrutinRepository.save(scrutin);

        //deja participé à cette élection (on le met dans la liste des elections votées)
        elector.getElections_voted().add(election.getName()); // update user with election voted
        userRepository.save(elector);

        //todo send email to electeur to confirm vote
        notificationService.sendSimpleEmail(elector.getEmail(),
                "Confirmation de vote", "Votre vote a été enregistré avec succès");

        // publication event
        ElectionEvent event = new ElectionEvent(this,election);
        electionEventPublisher.publishEvent(event);

        return "Scrutin validé avec succés";

    }

    //todo implementer renvoyer otp 

    private String generateOtp(String identifier) throws NoSuchAlgorithmException {
        return otpHelper.generateOtpWithHash(identifier);
    }

    private User getElecteur(String username) {
        User electeur = userRepository.findByUsername(username).orElse(null);
        assert electeur != null;
        return electeur;
    }

    private void validate(ScrutinVoiceRequest request) throws ElectionNotFoundException {
       Optional<Election> optElection = electionRepository.
                findById(request.electionId());

       Optional<Bulletin> optBulletin = bulletinRepository
               .findById(request.bulletinId());

       if(optBulletin.isEmpty()){
           log.error("Bulletin id {} not found", request.bulletinId());
           throw new ElectionNotFoundException("Bulletin id " + request.bulletinId() + " not found");
       }
       if(optElection.isEmpty()){
           log.error("Election id {} not found", request.electionId());
           throw new ElectionNotFoundException("Election id " + request.electionId() + " not found");
       }

       Optional<User> optUser = userRepository.findById(optBulletin.get().getCandidateId());
       if(optUser.isEmpty()){
           log.error("User id {} not found", optBulletin.get().getCandidateId());
           throw new ElectionNotFoundException("le candidat avec le bulletin id " + optBulletin.get().getId() + " est introuvable");
       }
    }

    private void verifyAttemps(Scrutin scrutin) throws BadValidateException {
        if(scrutin.getFailed_attemps() <= ApiConstants.MAX_SCRUTIN_FAILED_ATTEMPT){
            int failedAttempts = scrutin.getFailed_attemps();
            failedAttempts++;
            scrutin.setFailed_attemps(failedAttempts);
            scrutinRepository.save(scrutin);
            int remainingAttempts = ApiConstants.MAX_SCRUTIN_FAILED_ATTEMPT - failedAttempts;
            throw new BadValidateException("l'OTP est invalide ou expiré, il vous reste "+
                    remainingAttempts+" tentatives");
        }else{
            //todo on revoke le vote de l'electeur
            scrutin.setState(ScrutinState.REVOKED);
            scrutinRepository.save(scrutin);
            throw new BadValidateException("l'OTP est toujours invalid , votre vote a été rejeté pour motif de non validation ");
        }
    }

}
