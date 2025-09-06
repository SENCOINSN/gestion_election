package com.sid.gl.elections;

import java.util.List;

public interface BulletinService {
    BulletinRequestDto createBulletin(String bulletinRequestDto) ;
    BulletinRequestDto getBulletin(Long id);
    List<BulletinRequestDto> getAllBulletin() ;

}
