package com.sid.gl.elections.bulletins;

import com.sid.gl.elections.Election;
import com.sid.gl.users.User;
import org.springframework.beans.BeanUtils;


public class BulletinMapper {


    private BulletinMapper () {
    }

    //Conversion BulletinRequestDto => bulletin
    public static Bulletin toBulletin(BulletinRequestDto bulletinRequestDto) {
        Bulletin bulletin = new Bulletin();
        //BeanUtils.copyProperties(candidatRequestDto, candidat);
        bulletin.setUser(new User());
        bulletin.getUser().setFirstName(bulletinRequestDto.getFirstName());
        bulletin.getUser().setLastName(bulletinRequestDto.getLastName());
        bulletin.setPartyName(bulletinRequestDto.getPartyName());
        return bulletin;
    }
    //Conversion  Bulletin => BulletinRequestDto
    public static BulletinRequestDto toBulletinRequestDto( Bulletin bulletin) {
        BulletinRequestDto bulletinRequestDto = new BulletinRequestDto();
        BeanUtils.copyProperties(bulletin, bulletinRequestDto);
        return bulletinRequestDto;
    }
    // Conversion BulletinResponseDto => Bulletin
    public static Bulletin bulletinResponseOtherWay( BulletinResponseDto bulletinResponseDto)  {
        Bulletin bulletin = new Bulletin();
        //BeanUtils.copyProperties(bulletinResponseDto, bulletin);
        bulletin.setUser(new User());
        bulletin.getUser().setFirstName(bulletinResponseDto.firstName());
        bulletin.getUser().setLastName(bulletinResponseDto.lastName());
        bulletin.setPartyName(bulletinResponseDto.partyName());
        bulletin.setParcours(bulletinResponseDto.parcours());
        bulletin.setElection(new Election());
        bulletin.getElection().setName(bulletinResponseDto.electionName());
        bulletin.getElection().setStartDate(bulletinResponseDto.electionStartDate());
        return bulletin;

    }

    //Conversion
}

