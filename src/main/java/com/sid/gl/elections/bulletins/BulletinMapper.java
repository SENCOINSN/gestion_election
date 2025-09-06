package com.sid.gl.elections.bulletins;

import com.sid.gl.elections.Election;
import com.sid.gl.users.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BulletinMapper {
    private final UserRepository userRepository;

    public BulletinMapper(BulletinRepository repository){
        this.bulletinRepository= repository;
    }



    
    
}

