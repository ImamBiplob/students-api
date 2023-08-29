package com.imambiplob.studentsapi.exception;

public class EmailAlreadyTakenException extends Exception {
    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
