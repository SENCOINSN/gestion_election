package com.sid.gl.elections;

import com.sid.gl.exceptions.ElectionAlreadyExist;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository electionRepository;
    private final BulletinRepository bulletinRepository;

    private final ElectionMapper electionMapper;

    @Override
    public ElectionResponseDto createElection(Election election) throws ElectionAlreadyExist {
        return null;
    }

    @Override
    public ElectionResponseDto getElection(Long id) {
        return null;
    }

    @Override
    public List<ElectionResponseDto> getAllElections() {
        return List.of();
    }
}
