package com.chaoui.rooms.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String input) {
        super("User with username/email (" + input + ") already exists!");
    }
}
