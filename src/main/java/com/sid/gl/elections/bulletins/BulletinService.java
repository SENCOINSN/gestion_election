package com.sid.gl.elections.bulletins;

import com.sid.gl.exceptions.ElectionNotFoundException;
import com.sid.gl.exceptions.GestionElectionNotFoundException;
import com.sid.gl.exceptions.UserNotFoundException;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface BulletinService {
    BulletinResponseDto createBulletinCandidat(BulletinRequestDto bulletinRequestDto) throws UserNotFoundException, ElectionNotFoundException, RoleNotFoundException;
    BulletinResponseDto getBulletin(Long id) throws GestionElectionNotFoundException;
    List<BulletinResponseDto> getAllBulletin() ;
    List<BulletinResponseDto> getBulletinByElectionId(Long electionId);

}
