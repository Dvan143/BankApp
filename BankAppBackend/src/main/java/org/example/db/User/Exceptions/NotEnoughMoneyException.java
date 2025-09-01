package org.example.db.User.Exceptions;

public class NotEnoughMoneyException extends Exception{
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
