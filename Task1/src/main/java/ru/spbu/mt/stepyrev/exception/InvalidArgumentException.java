package ru.spbu.mt.stepyrev.exception;

import java.security.InvalidParameterException;

/** A class that realizes invalid argument exception. */
public class InvalidArgumentException extends InvalidParameterException {

    public InvalidArgumentException(String msg) {
        super(msg);
    }
}
