package com.sid.gl.elections;

import java.time.LocalDateTime;


public class ElectionMapper {

    private ElectionMapper(){}

    public static ElectionResponseDto toElectionResponseDto( Election election){
        return new ElectionResponseDto(
                election.getName(),
                election.getDescription(),
                election.getStartDate()
        );
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
                electionInfoProjection.getName(),
                electionInfoProjection.getDescription(),
                electionInfoProjection.getStartDate()
        );
    }
}
