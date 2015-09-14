/**
 * Generic service response wrapper for all REST services.
 */

package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Generic Wrapper class for REST Responses.
 * User: anarlawar
 *
 * @param <R>  the type parameter
 * @param <P>  the type parameter
 * @param <E>  the type parameter
 */

@Data
@AllArgsConstructor
public class ServiceResponse<R, P, E> implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 8287081155895075953L;

    /**
     * The Data.
     */
    private R result;


    /**
     * The Pagination.
     */
    private P pagination;

    /**
     * The Errors.
     */
    private E errors;


}
