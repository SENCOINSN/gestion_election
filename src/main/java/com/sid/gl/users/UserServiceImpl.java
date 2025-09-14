package com.sid.gl.users;

import com.sid.gl.commons.DataResponse;
import com.sid.gl.exceptions.UserAlreadyExistException;
import com.sid.gl.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    //private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final static String ROLE_NAME = "ELECTOR";

    @Override
    public UserResponseDto register(UserRequestDto userRequest) throws UserAlreadyExistException {
        log.info("Registering User {}", userRequest);
        // done: verifier verification email s'il existe deja au niveau base de donnée
        this.verifyEmailAlreadyExist(userRequest.email());

        // todo map userDto => user
        User user = UserMapper.toUser(userRequest);
        // hash du mot de passe
        String passwordHash = bCryptPasswordEncoder.encode(userRequest.password());
        user.setPassword(passwordHash);
        //todo attribuer role user
        attributeDefaultRole(user,ROLE_NAME);

        // todo save user
        User savedUser = userRepository.save(user);

        // todo send email for activation account

        // todo map user => userResponse
        return UserMapper.toUserResponse(savedUser);
    }

    //role admin
    @Override
    public DataResponse getAllUsers(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        return convertUserToDataResponse(users);
    }

    private DataResponse convertUserToDataResponse(Page<User> users){
        List<User> userContents = users.getContent();
        List<UserResponseDto> responses = userContents
                .stream()
                .filter(Objects::nonNull)
                .map(UserMapper::toUserResponse)
                .toList();

        DataResponse dataResponse = new DataResponse();
        dataResponse.setContent(responses);
        dataResponse.setPageNo(users.getNumber());
        dataResponse.setPageSize(users.getSize());
        dataResponse.setTotalElements(users.getTotalElements());
        dataResponse.setTotalPages(users.getTotalPages());
        dataResponse.setLast(users.isLast());
        return dataResponse;
    }

    //role admin

    @Override
    public UserResponseDto getUser(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .map(UserMapper::toUserResponse)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    private void verifyEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    try {
                        throw new UserAlreadyExistException("User with email " + email + " already exist");
                    } catch (UserAlreadyExistException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void verifyEmailAlreadyExist(String email) throws UserAlreadyExistException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.warn("User with email {} already exist", email);
            throw new UserAlreadyExistException("User with email " + email + " already exist");
        }
    }

    private void attributeDefaultRole(User user,String roleName) {
        Role role;
        Optional<Role> roleOption = roleRepository.findByRoleName(roleName);
        if(roleOption.isEmpty()){
            log.info("creation role niveau base de données");
            Role roleToSave = new Role();
            roleToSave.setRoleName(roleName);
            role = roleRepository.save(roleToSave);
            user.getRoles().add(role);
        }else{
            role = roleOption.get();
            user.getRoles().add(role);
        }
        log.info("role attribué à l'utilisateur");

    }
}
