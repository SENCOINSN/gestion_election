package com.sid.gl.users;

import java.util.List;

public interface IUser {
    public void subscribe();

    static void publish(String message) {
        System.out.println(message);
    }

}
