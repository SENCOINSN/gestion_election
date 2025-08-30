package com.sid.gl.users;

import com.sid.gl.exceptions.UserAlreadyExistException;
import com.sid.gl.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    UserResponseDto register(UserRequestDto user) throws UserAlreadyExistException;
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUser(Long id) throws UserNotFoundException;

}
