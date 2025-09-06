package com.sid.gl.elections;

import com.sid.gl.exceptions.ElectionAlreadyExist;

import java.util.List;

public interface ElectionService {
    ElectionResponseDto createElection (Election election) throws ElectionAlreadyExist;
    ElectionResponseDto getElection(Long id);
    List<ElectionResponseDto> getAllElections();
}
