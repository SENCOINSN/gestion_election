package com.sid.gl.elections.bulletins;

import java.util.List;

public interface BulletinService {
    BulletinResponseDto createBulletin(BulletinRequestDto bulletinRequestDto) ;
    BulletinResponseDto getBulletin(Long id);
    List<BulletinResponseDto> getAllBulletin() ;

}
