package com.sid.gl.elections;

public class ElectionMapper {

    private ElectionMapper(){}

    public static Election ToElection( ElectionResponseDto electionResponseDto){
        Election election = new Election();
        election.setName(electionResponseDto.name());
        election.setDescription(electionResponseDto.description());
        election.setEndDate(electionResponseDto.dateStart());
        return election;
    }
}
