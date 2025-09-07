package com.sid.gl.users;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
