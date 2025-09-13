package com.sid.gl.elections.bulletins;

import java.util.List;

public interface BulletinService {
    BulletinRequestDto createBulletin(BulletinRequestDto bulletinRequestDto) ;
    BulletinRequestDto getBulletin(Long id);
    List<BulletinRequestDto> getAllBulletin() ;

}
