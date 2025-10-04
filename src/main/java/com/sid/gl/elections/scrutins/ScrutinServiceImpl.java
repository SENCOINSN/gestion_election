package com.sid.gl.elections.scrutins;


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
        scrutin.setOtp(otp);
        Scrutin scrutinSaved = scrutinRepository.save(scrutin);
        //todo send email to electeur to validate otp
        notificationService.sendSimpleEmail(electeur.getEmail(),
                "OTP Validation", "Pour l'approbation de votre scrutin," +
                        " veuillez utiliser le code suivant : " + otp);
       return scrutinSaved.getId();

    }

    @Override
    public void validateOtp(String otp, String username) throws NoSuchAlgorithmException, BadValidateException {
        log.info("Validate otp request: {}",otp);
        if(BooleanUtils.isFalse(otpHelper.verifyOtpWithHash(otp,username))){
            log.error("OTP is invalid or expired");
            throw new BadValidateException("l'OTP est invalide ou expiré");
        }
        User electeur = getElecteur(username);
        Scrutin scrutin = scrutinRepository.findByOtp(otp);
        if (scrutin == null) {
            log.error("Scrutin not found for OTP: {}", otp);
            throw new BadValidateException("Scrutin not found for the provided OTP");
        }
        Bulletin bulletin = bulletinRepository.findById(scrutin.getBulletinId()).orElse(null);
        Election election = electionRepository.findById(bulletin.getElectionId()).orElse(null);

        scrutin.setState(ScrutinState.COMPLETED);
        scrutinRepository.save(scrutin);

        electeur.getElections_voted().add(election.getName()); // update user with election voted
        userRepository.save(electeur);

        //todo send email to electeur to confirm vote
        notificationService.sendSimpleEmail(electeur.getEmail(),
                "Confirmation de vote", "Votre vote a été enregistré avec succès");

        // publication event
        ElectionEvent event = new ElectionEvent(this,election);
        electionEventPublisher.publishEvent(event);

    }

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

}
