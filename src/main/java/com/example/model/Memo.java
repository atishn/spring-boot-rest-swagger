package com.example.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

//import javax.persistence.Column;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

/**
 * Memo Data Model.
 */
@Data
@Entity
@Table(name = "MEMOS")
public class Memo implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = -7846773639244185780L;

    /**
     * The Unique Identifier.
     */
    @Id
    @GeneratedValue()
    private Long id;

    /**
     * The Title.
     */
    @NotNull(message = "Memo Title cannot be null or empty.")
    @NotEmpty(message = "Memo Title cannot be null or empty.")
    @Column(nullable = false)
    private String title;

    /**
     * The Author.
     */
    @NotNull(message = "Memo Author cannot be null or empty.")
    @NotEmpty(message = "Memo Author cannot be null or empty.")
    @Column(nullable = false)
    private String author;

    /**
     * The Text.
     */
    @NotNull(message = "Memo Text cannot be null or empty.")
    @NotEmpty(message = "Memo Text cannot be null or empty.")
    @Column(nullable = false)
    private String text;

    /**
     * Memo Created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date created;

    /**
     * The last time Memo Updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false)
    private Date updated;

    /**
     * On create.
     */
    @PrePersist
    protected void onCreate() {
        updated = created = new Date();
    }

    /**
     * On update.
     */
    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }


}
