package com.sid.gl.users;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Date;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyName {
    private String name;
    private String address;
    private Date creationDate;
}
