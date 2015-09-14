package com.example.exception;


/**
 * Exception thrown when data is not found.
 */
public class DataNotFoundException extends MemoRestException {

    /**
     * Constructs new DataNotFoundException with the specified message.
     *
     * @param msg message
     */
    public DataNotFoundException(final String msg) {
        super(msg);
    }

    /**
     * Constructs a new RegistryLoginException exception with the specified
     * detail message and cause.
     *
     * @param msg   message.
     * @param cause cause.
     */
    public DataNotFoundException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
