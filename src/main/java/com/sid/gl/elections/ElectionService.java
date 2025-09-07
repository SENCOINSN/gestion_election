package com.sid.gl.elections;

import com.sid.gl.exceptions.ElectionAlreadyExistException;
import com.sid.gl.exceptions.ElectionNotFoundException;

import java.util.List;

public interface ElectionService {
    ElectionResponseDto createElection (ElectionRequestDto election) throws ElectionAlreadyExistException;
    ElectionResponseDto getElection(Long id) throws ElectionNotFoundException;
    List<ElectionResponseDto> getAllElections();
    List<ElectionResponseDto> getElectionIsActive();
}
