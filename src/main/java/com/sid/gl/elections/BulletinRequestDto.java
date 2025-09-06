package com.sid.gl.elections;

import com.sid.gl.users.User;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class BulletinRequestDto {
    private String lastName;
    private String firstName;
    private String partyName;

}
