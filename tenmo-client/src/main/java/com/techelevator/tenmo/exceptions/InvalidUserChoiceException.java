package com.techelevator.tenmo.exceptions;

public class InvalidUserChoiceException extends Exception{
    public InvalidUserChoiceException() {
        super("Cannot send money to yourself. Please choose a different user.");
    }
}
