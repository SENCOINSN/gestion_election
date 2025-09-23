package com.sid.gl.elections.bulletins;


import com.sid.gl.elections.Election;
import com.sid.gl.elections.ElectionMapper;
import com.sid.gl.elections.ElectionRepository;
import com.sid.gl.elections.ElectionResponseDto;
import com.sid.gl.exceptions.ElectionNotFoundException;
import com.sid.gl.exceptions.GestionElectionNotFoundException;
import com.sid.gl.exceptions.UserNotFoundException;
import com.sid.gl.users.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class BulletinServiceImpl implements BulletinService {

    private final BulletinRepository bulletinRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ElectionRepository electionRepository;

    private final static String ROLE_NAME = "CANDIDAT";



    @Override
    public BulletinResponseDto createBulletinCandidat (BulletinRequestDto bulletinRequestDto) throws UserNotFoundException, ElectionNotFoundException, RoleNotFoundException {
        log.info("creation du bulletin du candidat");
        User user = userRepository.findByEmail(bulletinRequestDto.email())
                .orElseThrow(() -> new UserNotFoundException("l'utilisateur n'est pas trouvé"));

        Election election = electionRepository.findByName(bulletinRequestDto.nameElection())
                .orElseThrow(() -> new ElectionNotFoundException("l'élection pour la souscription du candidat n'est pas trouvée"));

        Bulletin bulletin = new Bulletin();
        bulletin.setCandidateId(user.getId());
        bulletin.setElectionId(election.getId());
        //save bulletin
        bulletinRepository.save(bulletin);

        //add role CANDIDAT to user
        if(roleRepository.findByRoleName(ROLE_NAME).isPresent()){
            user.getRoles().add(roleRepository.findByRoleName(ROLE_NAME).get());
        }else{
             log.warn("role CANDIDAT n'est pas trouvé");
             createRoleCandidatIfNotExists(user, ROLE_NAME);
        }
        // todo notify user bulletin created successfully

        //todo send email to user

        //gerer les infos du parti du candidat
        if(bulletinRequestDto.nameParty() != null && bulletinRequestDto.addressParty() != null){
            PartyName infosParty = new PartyName();
            infosParty.setName(bulletinRequestDto.nameParty());
            infosParty.setAddress(bulletinRequestDto.addressParty());
            infosParty.setCreationDate(bulletinRequestDto.dateCreation()!=null ? bulletinRequestDto.dateCreation() : null);
            user.setPartyName(infosParty);
        }

        //update user
        userRepository.save(user);

        //todo map bulletin => bulletinResponseDto
        UserResponseDto candidat = UserMapper.toUserResponse(user);
        ElectionResponseDto electionResponseDto = ElectionMapper.toElectionResponseDto(election);
        return new BulletinResponseDto(bulletin.getId(), candidat, electionResponseDto);
    }

    private void createRoleCandidatIfNotExists(User user, String roleName){
        Role role = new Role();
        role.setRoleName(roleName);
        roleRepository.save(role);
        user.getRoles().add(role);
    }


    @Override
    public BulletinResponseDto getBulletin(Long id) throws GestionElectionNotFoundException {
        Bulletin bulletin = bulletinRepository.findById(id)
                .orElseThrow(() -> new GestionElectionNotFoundException("le bulletin n'est pas trouvé"));
        return mapToBulletinResponseDto(bulletin);

    }

    @Override
    public List<BulletinResponseDto> getAllBulletin() {
        List<Bulletin> bulletins = bulletinRepository.findAll();
        return bulletins.stream().map(this::mapToBulletinResponseDto).collect(Collectors.toList());
    }

    private BulletinResponseDto mapToBulletinResponseDto(Bulletin bulletin) {
        Optional<User> optUser = userRepository.findById(bulletin.getCandidateId());
        Optional<Election> optElection = electionRepository.findById(bulletin.getElectionId());
        User user ;
        Election election ;
        if(optUser.isPresent() && optElection.isPresent()){
            user = optUser.get();
            election = optElection.get();
            return new BulletinResponseDto(bulletin.getId(),UserMapper.toUserResponse(user),
                    ElectionMapper.toElectionResponseDto(election));
        }

        return null;
    }
}
