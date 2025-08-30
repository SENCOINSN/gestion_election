package com.sid.gl.users;

import com.sid.gl.exceptions.UserAlreadyExistException;
import com.sid.gl.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    //private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final static String ROLE_NAME = "ELECTOR";

    @Override
    public UserResponseDto register(UserRequestDto userRequest) throws UserAlreadyExistException {
        log.info("Registering User {}", userRequest);
        // done: verifier verification email s'il existe deja au niveau base de donnée
          this.verifyEmailOtherway(userRequest.email());

        // todo map userDto => user
        User user = UserMapper.toUser(userRequest);
        //todo attribuer role user
         attributeDefaultRole(user);

        // todo save user
        User savedUser = userRepository.save(user);

        // todo send email for activation account

        // todo map user => userResponse
        return UserMapper.toUserResponse(savedUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toUserResponse)
                .toList();

    }

    @Override
    public UserResponseDto getUser(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .map(UserMapper::toUserResponse)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        /*Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        User user1 = user.get();
        UserResponseDto response = UserMapper.toUserResponse(user1);
        return response;*/
    }

    private void verifyEmail(String email)  {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    try {
                        throw new UserAlreadyExistException("User with email " + email + " already exist");
                    } catch (UserAlreadyExistException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void verifyEmailOtherway(String email) throws UserAlreadyExistException {
          Optional<User> user = userRepository.findByEmail(email);
          if(user.isPresent()){
              log.warn("User with email {} already exist", email);
              throw new UserAlreadyExistException("User with email " + email + " already exist");
          }
    }

    private void attributeDefaultRole(User user) {
        /*roleRepository.findByRoleName(ROLE_NAME)
                .map(role -> {
                    user.getRoles().add(role);
                    return role;
                })
                .orElseThrow(() -> new RuntimeException("Role not found"));*/

        Role role = roleRepository.findByRoleName(ROLE_NAME)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
    }
}
