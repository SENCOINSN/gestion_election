package com.sid.gl.users;

import org.springframework.beans.BeanUtils;

public class UserMapper {

    private UserMapper() {
    }
    // conversion userRequestDto => user
    public static User toUser (UserRequestDto userRequestDto) {
        User user = new User();
        user.setLastName(userRequestDto.lastName());
        user.setEmail(userRequestDto.email());
        user.setPassword(userRequestDto.password()); // todo password encoder
        user.setFirstName(userRequestDto.firstName());
        return user;
    }

    // conversion user => UserResponseDto
    public static UserResponseDto toUserResponse(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        //BeanUtils.copyProperties(user, userResponseDto);
        userResponseDto.setId(user.getId());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setUsername(user.getUsername() != null ? user.getUsername() : "Neant");
        userResponseDto.setRoles(user.getRoles());
        return userResponseDto;
    }

    // conversion userRequestDto => user
    public static User toUserOtherWay(UserRequestDto userRequestDto) {
        User user = new User();
        BeanUtils.copyProperties(userRequestDto, user);
        return user;
    }

}
