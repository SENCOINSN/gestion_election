package com.sid.gl.elections;

import com.sid.gl.exceptions.ElectionAlreadyExist;
import com.sid.gl.exceptions.ElectionNotFoundException;

import java.util.List;

public interface ElectionService {
    ElectionResponseDto createElection (ElectionRequestDto election) throws ElectionAlreadyExist;
    ElectionResponseDto getElection(Long id) throws ElectionNotFoundException;
    List<ElectionResponseDto> getAllElections();
    List<ElectionResponseDto> getElectionIsActive();
}
