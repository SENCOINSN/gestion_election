package com.sid.gl.security;

import com.sid.gl.users.Role;
import com.sid.gl.users.User;
import com.sid.gl.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.info("loadUserByUsername: {}" ,usernameOrEmail);
        Optional<User> optUser = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if(optUser.isEmpty()){
            log.error("User not found with username or email: " + usernameOrEmail);
            throw new UsernameNotFoundException("User not found with username or email for authentication: " + usernameOrEmail);
        }
        UserDetails userDetails = new UserCustomDetails(optUser.get());
        return userDetails;
    }
}
