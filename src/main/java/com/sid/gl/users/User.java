package com.sid.gl.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sid.gl.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Table(name = "tb_users")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@ToString
public class User extends BaseEntity{
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    //@JsonIgnore
    private String password;
    private String firstName;
    @Column(unique = true)
    private String username;
    //private boolean accountNonExpired=false;
    private boolean enabled=false;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private UserState userState=UserState.ACTIVE;
    @Embedded
    private PartyName partyName;

}
