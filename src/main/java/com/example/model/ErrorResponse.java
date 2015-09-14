package com.example.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Data Model for Error Response.
 */
@Data
public class ErrorResponse implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = -7846773639244185780L;

    /**
     * The Code.
     */
    private String code;

    /**
     * The Title.
     */
    private String title;

    /**
     * The Id.
     */
    private String id;

    /**
     * The Links.
     */
    private String link;

    /**
     * The Detail.
     */
    private String detail;
}
