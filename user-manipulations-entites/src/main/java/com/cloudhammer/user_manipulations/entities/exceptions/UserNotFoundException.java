package com.cloudhammer.user_manipulations.entities.exceptions;

/**
 * Exception to be thrown when the user cannot be found
 */
public class UserNotFoundException extends Exception {
    private static final long serialVersionUID = 4501946478396677291L;

    public UserNotFoundException(String id) {
        super("The user with id " + id + " could not be found");
    }
}
