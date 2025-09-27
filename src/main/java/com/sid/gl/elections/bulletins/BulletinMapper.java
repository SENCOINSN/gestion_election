package com.sid.gl.elections.bulletins;

import com.sid.gl.elections.Election;
import com.sid.gl.users.User;
import org.springframework.beans.BeanUtils;


public class BulletinMapper {
//dans le cas où on fait le mapping à la main

    private BulletinMapper () {
    }

    //Conversion BulletinRequestDto => bulletin
    public static Bulletin toBulletin(BulletinRequestDto bulletinRequestDto) {
        Bulletin bulletin = new Bulletin();
        BeanUtils.copyProperties(bulletinRequestDto, bulletin);
        return bulletin;
    }
    //Conversion  Bulletin => BulletinRequestDto
    public static BulletinRequestDto toBulletinRequestDto( Bulletin bulletin) {
        return null;
    }
    // Conversion BulletinResponseDto => Bulletin
    public static Bulletin bulletinResponseOtherWay( BulletinResponseDto bulletinResponseDto)  {
        Bulletin bulletin = new Bulletin();
        BeanUtils.copyProperties(bulletinResponseDto, bulletin);
        return bulletin;
    }

    //Conversion
}

