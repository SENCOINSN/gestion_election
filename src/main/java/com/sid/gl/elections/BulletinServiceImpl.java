package com.sid.gl.elections;


import com.sid.gl.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BulletinServiceImpl implements BulletinService {

    private final BulletinRepository bulletinRepository;
    private final UserRepository userRepository;
    private final static String ROLE_NAME = "CANDIDATE";



    @Override
    public BulletinRequestDto createBulletin (String bulletinRequestDto) {
      return null;
    }


    @Override
    public BulletinRequestDto getBulletin(Long id) {
        return null;
    }

    @Override
    public List<BulletinRequestDto> getAllBulletin() {
        return List.of();
    }
}
