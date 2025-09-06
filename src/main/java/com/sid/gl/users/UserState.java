package com.sid.gl.users;

import lombok.Getter;

@Getter
public enum UserState {
    ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), DELETED("DELETED");

    private final String value;

    UserState(String value) {
        this.value = value;
    }
}
