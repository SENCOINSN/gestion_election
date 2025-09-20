package com.sid.gl.elections;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


public class ElectionMapper {

    private ElectionMapper(){}
            // Conversion Election to ElectionResponseDto on fait le mapping depuis le constructeur
            // vu que un RECORD n'as pas getter ni de setter
    public static ElectionResponseDto toElectionResponseDto( Election election){
        return new ElectionResponseDto(
                election.getId(),
                election.getName(),
                election.getDescription(),
                election.getStartDate()
        );
    }

    public static List<ElectionResponseDto> toListElectionResponseDto(List<Election> elections){
        if(!elections.isEmpty()){
            return elections.stream().map(ElectionMapper::toElectionResponseDto).toList();
        }
        return Collections.emptyList();
    }

    //todo conversion electionRequestDto => election

    public static Election toElection(ElectionRequestDto electionRequestDto) {
        Election election = new Election();
        election.setName(electionRequestDto.name());
        election.setDescription(electionRequestDto.description());
        election.setStartDate(electionRequestDto.startDate());
        LocalDateTime endDate = electionRequestDto.startDate().plusHours(electionRequestDto.duration());
        election.setEndDate(endDate);
        return election;
    }

    //todo converion electionInfoProjection => electionResponseDto
    public static ElectionResponseDto fromElectionProjection(ElectionInfoProjection electionInfoProjection) {
        return new ElectionResponseDto(
                electionInfoProjection.getId(),
                electionInfoProjection.getName(),
                electionInfoProjection.getDescription(),
                electionInfoProjection.getStartDate()
        );
    }
}
