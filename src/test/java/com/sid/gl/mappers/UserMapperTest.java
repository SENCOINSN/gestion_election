package com.sid.gl.mappers;

import com.sid.gl.users.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    @Test
    void toUser_shouldMapFieldsCorrectly() {
        UserRequestDto dto = new UserRequestDto("Doe", "john.doe@example.com", "passer123", "John","john.doe");
        User user = UserMapper.toUser(dto);

        assertEquals("john.doe", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void toUserResponse_shouldMapFieldsCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setUsername("jane.doe");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.com");
        Role role = new Role();
        role.setRoleName("ADMIN");
        user.setRoles(Set.of(role));

        UserResponseDto dto = UserMapper.toUserResponse(user);

        assertEquals(1L, dto.getId());
        assertEquals("jane.doe", dto.getUsername());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("jane.doe@example.com", dto.getEmail());
        assertEquals(Set.of(role), dto.getRoles());
    }
}
