package com.sid.gl.elections;

import com.sid.gl.commons.DataResponse;
import com.sid.gl.elections.bulletins.BulletinRepository;
import com.sid.gl.exceptions.ElectionAlreadyExistException;
import com.sid.gl.exceptions.ElectionNotFoundException;
import com.sid.gl.exceptions.UserNotFoundException;
import com.sid.gl.users.User;
import com.sid.gl.users.UserRepository;
import com.sid.gl.utils.ElectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository electionRepository;
    private final UserRepository userRepository;

    //role admin & supervisor
    //Edit name or description of an election
    @Override
    public ElectionResponseDto editElection(Long id, ElectionRequestDto election) throws ElectionNotFoundException {
        Optional< Election> electionOption = electionRepository.findById(id);
        if(electionOption.isEmpty()){
            throw new ElectionNotFoundException("Election with id " + id + " not found");
        }
        Election electionToEdit = electionOption.get();
        electionToEdit.setName(election.name());
        electionToEdit.setDescription(election.description());
        return ElectionMapper.toElectionResponseDto(electionRepository.save(electionToEdit));

    }

    //role admin
    @Override
    public ElectionResponseDto createElection(ElectionRequestDto request) throws ElectionAlreadyExistException {
        Optional<Election> electionVerify = electionRepository.findByName(request.name());
        if(electionVerify.isPresent()){
            log.error("Election with name {} already exist", request.name());
            throw new ElectionAlreadyExistException("Election with name " + request.name() + " already exist");
        }
        Election election = ElectionMapper.toElection(request);
        log.info("Election mapped {} ", election);
        //save election
        Election electionSaved = electionRepository.save(election);
        log.info("Election saved {} ", electionSaved);
        return ElectionMapper.toElectionResponseDto(electionSaved);
    }

    //role admin

    @Override
    public ElectionResponseDto getElection(Long id) throws ElectionNotFoundException {
        return electionRepository.findById(id).map(ElectionMapper::toElectionResponseDto).
                orElseThrow(() -> new ElectionNotFoundException("Election with id " + id + " not found"));
    }

    //role admin
    @Transactional(readOnly = true)
    @Override
    public DataResponse getAllElections(int page, int size) {
        Page <Election> pageElections = electionRepository.findAll(PageRequest.of(page, size));
        List<Election> elections = pageElections.getContent();
        List<ElectionResponseDto> electionResponseDtos = ElectionMapper.toListElectionResponseDto(elections);

        return ElectionUtils.buildDataResponse(electionResponseDtos, pageElections);
    }

    @Override
    public List<ElectionResponseDto> getElectionIsActive() {
        List<ElectionInfoProjection> electionInfoProjections = electionRepository.getElectionIsActive();
        return electionInfoProjections
                .stream()
                .filter(Objects::nonNull)
                .map(ElectionMapper::fromElectionProjection)
                .toList();

    }

    @Override
    public List<ElectionResponseDto> getElectionActiveByUser(String username) throws UserNotFoundException {
       log.info("ElectionService::getElectionActiveByUser {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        List<ElectionInfoProjection> electionsActived =
                electionRepository.getElectionIsActive();
        List<String> electionsParticipated = user.getElections_voted();

        Predicate<ElectionInfoProjection> electionInfoProjectionPredicate =
                election -> !electionsParticipated.contains(election.getName());
        return electionsActived
                .stream()
                .filter(electionInfoProjectionPredicate)
                .map(ElectionMapper::fromElectionProjection)
                .toList();

    }


}
