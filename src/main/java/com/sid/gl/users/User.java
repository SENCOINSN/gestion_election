package com.sid.gl.users;

import com.sid.gl.commons.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "tb_users")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@ToString
public class User extends BaseEntity {
    private String lastName;
    private String email;
    private String password;
    private String firstName;
    private String username;


}
