package com.sid.gl.mappers;

import com.sid.gl.elections.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ElectionMapperTest {

    @Test
    void toElectionResponseDto_shouldMapFieldsCorrectly() {
        Election election = new Election();
        election.setId(1L);
        election.setName("Election 2024");
        election.setDescription("Presidential Election");
        election.setStartDate(new Date());
        election.setEndDate(LocalDateTime.of(2024, 6, 10, 10, 0));
        election.setActive(true);

        ElectionResponseDto dto = ElectionMapper.toElectionResponseDto(election);

        assertEquals(1L, dto.id());
        assertEquals("Election 2024", dto.name());
        assertEquals("Presidential Election", dto.description());
        //assertEquals(LocalDateTime.of(2024, 6, 1, 10, 0), dto.dateStart());
        //assertEquals(LocalDateTime.of(2024, 6, 10, 10, 0), dto.dateEnd());  //todo fix after
        assertTrue(dto.isActive());
    }

    @Test
    void toListElectionResponseDto_shouldReturnListOfDtos() {
        Election election = new Election();
        election.setId(2L);
        election.setName("Election 2025");
        election.setDescription("Local Election");
        election.setStartDate(new Date());
        election.setEndDate(LocalDateTime.of(2025, 5, 5, 9, 0));
        election.setActive(false);

        List<ElectionResponseDto> dtos = ElectionMapper.toListElectionResponseDto(List.of(election));
        assertEquals(1, dtos.size());
        assertEquals("Election 2025", dtos.get(0).name());
    }

    @Test
    void toListElectionResponseDto_shouldReturnEmptyListWhenInputIsEmpty() {
        List<ElectionResponseDto> dtos = ElectionMapper.toListElectionResponseDto(Collections.emptyList());
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toElection_shouldMapFieldsFromRequestDto() {
        Date startDate = new Date();
        ElectionRequestDto dto = mock(ElectionRequestDto.class);
        when(dto.name()).thenReturn("Election 2030");
        when(dto.description()).thenReturn("Future Election");
        when(dto.startDate()).thenReturn(startDate);
        when(dto.duration()).thenReturn(5);

        Election election = ElectionMapper.toElection(dto);

        assertEquals("Election 2030", election.getName());
        assertEquals("Future Election", election.getDescription());
        assertEquals(startDate, election.getStartDate());
        assertNotNull(election.getEndDate());
    }

   /* @Test
    void fromElectionProjection_shouldMapFieldsCorrectly() {
        ElectionInfoProjection projection = mock(ElectionInfoProjection.class);
        when(projection.getId()).thenReturn(3L);
        when(projection.getName()).thenReturn("Election 2040");
        when(projection.getDescription()).thenReturn("Projection Election");
        LocalDateTime start = LocalDateTime.of(2040, 1, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2040, 1, 10, 8, 0);
        when(projection.getStartDate()).thenReturn(start);
        when(projection.getEndDate()).thenReturn(end);
        when(projection.isActive()).thenReturn(true);

        ElectionResponseDto dto = ElectionMapper.fromElectionProjection(projection);

        assertEquals(3L, dto.id());
        assertEquals("Election 2040", dto.name());
        assertEquals("Projection Election", dto.description());
        assertEquals(start, dto.dateStart());
        assertEquals(end, dto.dateEnd());
        assertTrue(dto.isActive());
    }*/
}
