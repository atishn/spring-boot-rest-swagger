package com.example.exception;


/**
 * Exception thrown when incoming argument is not valid.
 */
public class InvalidArgumentException extends MemoRestException {

    /**
     * Constructs new InvalidDataException with the specified message.
     *
     * @param msg message
     */
    public InvalidArgumentException(final String msg) {
        super(msg);
    }

    /**
     * Constructs a new InvalidDataException exception with the
     * specified detail message and cause.
     *
     * @param msg   message.
     * @param cause cause.
     */
    public InvalidArgumentException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
