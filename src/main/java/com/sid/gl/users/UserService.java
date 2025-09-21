package com.sid.gl.users;

import com.sid.gl.commons.DataResponse;
import com.sid.gl.exceptions.RoleNotFoundException;
import com.sid.gl.exceptions.UserAlreadyExistException;
import com.sid.gl.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    UserResponseDto register(UserRequestDto user) throws UserAlreadyExistException;
    DataResponse getAllUsers(int page,int size);
    UserResponseDto getUser(Long id) throws UserNotFoundException;
    UserResponseDto addRole(Long id,RoleRequestDto role) throws UserNotFoundException;

}
