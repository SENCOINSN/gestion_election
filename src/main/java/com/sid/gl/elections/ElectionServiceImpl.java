package com.sid.gl.elections;

import com.sid.gl.elections.bulletins.BulletinRepository;
import com.sid.gl.exceptions.ElectionAlreadyExistException;
import com.sid.gl.exceptions.ElectionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository electionRepository;
    private final BulletinRepository bulletinRepository;


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
    @Override
    public List<ElectionResponseDto> getAllElections() {
        List<Election> elections = electionRepository.findAll();
        return elections
                .stream()
                .filter(Objects::nonNull)
                .map(ElectionMapper::toElectionResponseDto)
                .toList();
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


}
