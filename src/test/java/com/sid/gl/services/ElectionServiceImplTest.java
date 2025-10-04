package com.sid.gl.services;

import com.sid.gl.elections.*;
import com.sid.gl.elections.bulletins.BulletinRepository;
import com.sid.gl.exceptions.ElectionAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ElectionServiceImplTest {
    @Mock
    private ElectionRepository electionRepository;
    @Mock private BulletinRepository bulletinRepository;

    @InjectMocks
    private ElectionServiceImpl electionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createElection_shouldCreateElection_whenNotExists() throws ElectionAlreadyExistException {

        //GIVEN

        ElectionRequestDto request = new ElectionRequestDto("Election 2024", "Description", new Date(), 9);
        Election election = new Election(); // set fields as needed
        Election savedElection = new Election(); // set fields as needed

        //WHEN

        when(electionRepository.findByName(request.name())).thenReturn(Optional.empty());
        // Static mapping methods, so no need to mock ElectionMapper
        when(electionRepository.save(any(Election.class))).thenReturn(savedElection);


        //THEN
        ElectionResponseDto response = electionService.createElection(request);

        assertNotNull(response);
        verify(electionRepository).save(any(Election.class));
    }

    @Test
    void createElection_shouldThrowException_whenElectionExists() {
        ElectionRequestDto request = new ElectionRequestDto("Election 2024", "Description", new Date(), 9);
        Election existingElection = new Election();

        when(electionRepository.findByName(request.name())).thenReturn(Optional.of(existingElection));

        assertThrows(ElectionAlreadyExistException.class, () -> {
            electionService.createElection(request);
        });

        verify(electionRepository, never()).save(any());
    }
}
