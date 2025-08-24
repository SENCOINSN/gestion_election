package com.sid.gl.users;

public interface IUser {
    public void subscribe();

    static void publish(String message) {
        System.out.println(message);
    }
}
