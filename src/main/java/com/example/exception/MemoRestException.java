package com.example.exception;


import org.springframework.core.NestedRuntimeException;

/**
 * Generic Memo exception .
 */
public class MemoRestException extends NestedRuntimeException {
    /**
     * Constructs new MemoException with the specified message.
     *
     * @param msg message
     */
    public MemoRestException(final String msg) {
        super(msg);
    }

    /**
     * Constructs a new MemoException with the specified detail message and
     * cause.
     *
     * @param msg   message.
     * @param cause cause.
     */
    public MemoRestException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
