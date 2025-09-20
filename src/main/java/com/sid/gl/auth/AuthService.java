package com.sid.gl.auth;

import com.sid.gl.security.JwtService;
import com.sid.gl.security.UserCustomDetails;
import com.sid.gl.users.User;
import com.sid.gl.users.UserMapper;
import com.sid.gl.users.UserRepository;
import com.sid.gl.users.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    //todo authentification
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;



    public JwtResponseDto authenticate(AuthRequestDto authRequestDto) {
       log.info("Authenticating user: {}", authRequestDto.usernameOrEmail());

       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(authRequestDto.usernameOrEmail(),authRequestDto.password())
       );
       if(authentication.isAuthenticated()) {
           log.info("User is authenticated !!");
           UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();
           String token = jwtService.generateToken(userDetails.getUsername());
           log.info("Token generated: {}", token);
        User user =  userRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                   .stream().findFirst().orElse(null);
        UserResponseDto responseDto = UserMapper.toUserResponse(user);

        return JwtResponseDto.builder()
                .accessToken(token)
                .user(responseDto)
                .build();

       }
       log.error("User is not authenticated !!");
       throw new BadCredentialsException("Mot de passe ou nom d'utilisateur incorrect !!");
    }

}
