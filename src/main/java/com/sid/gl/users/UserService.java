package com.sid.gl.users;

import com.sid.gl.commons.DataResponse;
import com.sid.gl.exceptions.UserAlreadyExistException;
import com.sid.gl.exceptions.UserNotFoundException;

public interface UserService {
    UserResponseDto register(UserRequestDto user) throws UserAlreadyExistException;
    DataResponse getAllUsers(int page,int size);
    UserResponseDto getUser(Long id) throws UserNotFoundException;
    UserResponseDto addRole(Long id,RoleRequestDto role) throws UserNotFoundException;
    UserResponseDto deleteRoleUser(Long id, RoleRequestDto role) throws UserNotFoundException;
    DataResponse getAllElectors(int page,int size);
    DataResponse getAllSupervisors(int page,int size);
    DataResponse getAllCandidates(int page, int size);

    // Edit user
    UserResponseDto editUser(Long id, UserRequestDto user) throws UserNotFoundException;
}
