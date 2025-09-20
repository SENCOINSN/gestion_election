package com.sid.gl.elections;

import com.sid.gl.commons.DataResponse;
import com.sid.gl.exceptions.ElectionAlreadyExistException;
import com.sid.gl.exceptions.ElectionNotFoundException;

import java.util.List;

public interface ElectionService {
    ElectionResponseDto createElection (ElectionRequestDto election) throws ElectionAlreadyExistException;
    ElectionResponseDto getElection(Long id) throws ElectionNotFoundException;
    DataResponse getAllElections(int page, int size);
    List<ElectionResponseDto> getElectionIsActive();
}
