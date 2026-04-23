package com.mycompany.csaprj.exception;

// Custom exception for Part 5.1
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String message) {
        super(message);
    }
}