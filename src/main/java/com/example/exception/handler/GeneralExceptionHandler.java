package com.example.exception.handler;

import com.example.exception.DataNotFoundException;
import com.example.exception.InvalidArgumentException;
import com.example.exception.InvalidDataException;
import com.example.exception.MemoRestException;
import com.example.model.ErrorResponse;
import com.example.model.ServiceResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.String.valueOf;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Generic Exception Handler for Memo Service.
 */
@ControllerAdvice
public class GeneralExceptionHandler {
    /**
     * The constant LOG.
     */
    private static final Logger LOG = getLogger(GeneralExceptionHandler.class);

    /**
     * Method to handle MemoREST exceptions.
     *
     * @param ex exception thrown
     * @param req the req
     * @param resp response
     * @return ResponseEntity service response
     */
    @ExceptionHandler(MemoRestException.class)
    @ResponseBody
    ServiceResponse<String, String, ErrorResponse> handleMemoException(
            final MemoRestException ex,
            final HttpServletRequest req,
            final HttpServletResponse resp) {

        resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ErrorResponse error = new ErrorResponse();
        error.setCode(valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        error.setTitle("Unexpected Exception.");
        error.setLink(req.getRequestURL().toString());
        error.setDetail(ex.getMessage());

        return new ServiceResponse(null, null, error);
    }


    /**
     * Method to handle DataNotFound exception.
     *
     * @param ex exception thrown
     * @param req the req
     * @param resp response
     * @return ResponseEntity service response
     */
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseBody
    ServiceResponse<String, String, ErrorResponse> handleDataNotFoundException(
            final DataNotFoundException ex, final HttpServletRequest req,
            final HttpServletResponse resp) {
        resp.setStatus(HttpStatus.NOT_FOUND.value());
        ErrorResponse error = new ErrorResponse();
        error.setCode(valueOf(HttpStatus.NOT_FOUND.value()));
        error.setTitle("No entry exists for that given request.");
        error.setLink(req.getRequestURL().toString());
        error.setDetail(ex.getMessage());

        return new ServiceResponse(null, null, error);
    }


    /**
     * Method to handle InvalidDataException.
     *
     * @param ex exception thrown
     * @param req the req
     * @param resp response
     * @return ResponseEntity service response
     */
    @ExceptionHandler(InvalidDataException.class)
    @ResponseBody
    ServiceResponse<String, String, ErrorResponse> handleMemoException(
            final InvalidDataException ex, final HttpServletRequest req,
            final HttpServletResponse resp) {
        resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ErrorResponse error = new ErrorResponse();
        error.setCode(valueOf(HttpStatus.NOT_FOUND.value()));
        error.setTitle("Invalid Data.");
        error.setDetail(ex.getMessage());
        error.setLink(req.getRequestURL().toString());

        return new ServiceResponse(null, null, error);
    }


    /**
     * Method to handle General Unknown exceptions.
     *
     * @param ex exception thrown
     * @param req the req
     * @param resp response
     * @return ResponseEntity service response
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ServiceResponse<String, String, ErrorResponse> handleGeneralException(
            final Exception ex, final HttpServletRequest req,
            final HttpServletResponse resp) {

        resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ErrorResponse error = new ErrorResponse();
        error.setCode(valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        error.setTitle("Unknown Exception. Please try again later.");
        error.setLink(req.getRequestURL().toString());
        error.setDetail(ex.getMessage());

        return new ServiceResponse(null, null, error);
    }

    /**
     * Method to invalid arguments exceptions.
     *
     * @param ex exception thrown
     * @param req the req
     * @param resp response
     * @return ResponseEntity service response
     */
    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseBody
    ServiceResponse<String, String, ErrorResponse> handleInvalidArgumentException(
            final InvalidArgumentException ex, final HttpServletRequest req,
            final HttpServletResponse resp) {

        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse error = new ErrorResponse();
        error.setCode(valueOf(HttpStatus.BAD_REQUEST.value()));
        error.setTitle("Invalid Request Data. Please verify.");
        error.setLink(req.getRequestURL().toString());
        error.setDetail(ex.getMessage());
        return new ServiceResponse(null, null, error);
    }

    /**
     * Method to Spring Binding exceptions.
     *
     * @param ex exception thrown
     * @param req the req
     * @param resp response
     * @return ResponseEntity service response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ServiceResponse<String, String, ErrorResponse> handleInvalidArgumentException(
            final MethodArgumentNotValidException ex, final HttpServletRequest req,
            final HttpServletResponse resp) {

        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResponse error = new ErrorResponse();
        error.setCode(valueOf(HttpStatus.BAD_REQUEST.value()));
        error.setTitle("Invalid Request Data. Please verify.");
        error.setLink(req.getRequestURL().toString());

        String detailedMessage = null;
        if (ex.getBindingResult() != null) {
            if (isNotEmpty(ex.getBindingResult().getAllErrors())) {

                for (ObjectError errorMessage : ex.getBindingResult().getAllErrors()) {
                    detailedMessage = errorMessage.getDefaultMessage() + "\n";
                }

            }
        }
        error.setDetail(detailedMessage);

        return new ServiceResponse(null, null, error);
    }


}
