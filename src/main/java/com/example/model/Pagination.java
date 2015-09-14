package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data Model for Pagination Response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = -7846773639244185780L;

    /**
     * The Prev.
     */
    private int totalRecords;

    /**
     * The Prev.
     */
    private String prev;

    /**
     * The Next.
     */
    private String next;

}
