package com.example.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Memo Data Model.
 */
@Data
public class Memo implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = -7846773639244185780L;

    /**
     * The Unique Identifier.
     */
    private Long id;

    /**
     * Memo Created.
     */
    private Date created;

    /**
     * The last time Memo Updated.
     */
    private Date updated;


    /**
     * The Title.
     */
    @NotNull(message = "Memo Title cannot be null or empty.")
    @NotEmpty(message = "Memo Title cannot be null or empty.")
    private String title;

    /**
     * The Author.
     */
    @NotNull(message = "Memo Author cannot be null or empty.")
    @NotEmpty(message = "Memo Author cannot be null or empty.")
    private String author;

    /**
     * The Text.
     */
    @NotNull(message = "Memo Text cannot be null or empty.")
    @NotEmpty(message = "Memo Text cannot be null or empty.")
    private String text;

}
