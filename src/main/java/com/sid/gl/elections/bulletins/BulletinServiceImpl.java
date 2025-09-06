package com.sid.gl.elections.bulletins;


import com.sid.gl.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BulletinServiceImpl implements BulletinService {

    private final BulletinRepository bulletinRepository;
    private final UserRepository userRepository;
    private final static String ROLE_NAME = "CANDIDAT";



    @Override
    public BulletinResponseDto createBulletin (BulletinRequestDto bulletinRequestDto) {
      return null;
    }


    @Override
    public BulletinResponseDto getBulletin(Long id) {
        return null;
    }

    @Override
    public List<BulletinResponseDto> getAllBulletin() {
        return List.of();
    }
}
