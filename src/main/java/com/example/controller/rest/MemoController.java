package com.example.controller.rest;

import com.example.api.client.DAOClient;
import com.example.exception.DataNotFoundException;
import com.example.exception.InvalidArgumentException;
import com.example.exception.InvalidDataException;
import com.example.exception.MemoRestException;
import com.example.model.ErrorResponse;
import com.example.model.Memo;
import com.example.model.MemoPage;
import com.example.model.Pagination;
import com.example.model.ServiceResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * REST Controller for managing Memo Endpoints.
 */
@RestController
@Api(value = "Memo End Point", description = "Manage Memo operations")
@RequestMapping(MemoController.V1_PREFIX + MemoController.MEMO)
public class MemoController {

    /**
     * The constant for Version 1.
     */
    public static final String V1_PREFIX = "/data/1";

    /**
     * Hello World URL.
     */
    public static final String MEMO = "/memo";


    /**
     * URI Prefix for ID.
     */
    public static final String URI_BY_ID = "/{id}";

    /**
     * The constant JSON.
     */
    public static final String JSON = "application/json";


    /**
     * The constant JSON.
     */
    public static final String XML = "application/xml";

    /**
     * Dao client.
     */
    @Autowired
    private DAOClient daoClient;

    /**
     * Create a new Memo.
     *
     * @param memo the request
     * @param req     the req
     * @param resp    the resp
     *
     * @return Response Message
     */
    @RequestMapping(method = RequestMethod.POST, consumes = {JSON, XML},
            produces = {JSON, XML})
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a Memo", notes = "Creates new Memo")
    final ServiceResponse<Memo, Pagination, ErrorResponse> createMemo(
            @ApiParam(value = "Memo request body.") @RequestBody @Valid final Memo memo,
            final HttpServletRequest req, final HttpServletResponse resp) {

        Memo createdMemo = daoClient.createMemo(memo);
        if (createdMemo != null) {
            resp.setHeader("Location", req.getRequestURL() + "/" + createdMemo.getId());
            return new ServiceResponse<>(createdMemo, null, null);
        }

        throw new MemoRestException("Failed to create new Memo");

    }

    /**
     * Get a Memo by Id.
     *
     * @param id the id
     *
     * @return Response Message
     */
    @RequestMapping(value = URI_BY_ID, method = RequestMethod.GET, produces = {JSON, XML})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a Memo by Id", notes = "Get a Memo by Id")
    final ServiceResponse<Memo, String, String> getMemo(
            @ApiParam(value = "The ID of the existing Memo resource.")
            @PathVariable final Long id) {
        Memo memo = daoClient.getMemo(id);
        return new ServiceResponse<>(memo, null, null);
    }


    /**
     * Get list of all Memos.
     *
     * @param page  the page
     * @param limit the limit
     * @param req   the req
     *
     * @return Response Message
     */
    @RequestMapping(method = RequestMethod.GET, produces = {JSON, XML})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get List of Memos", notes = "Get Memos")
    final ServiceResponse<Memo[], Pagination, String> getMemos(
            @ApiParam(value = "The page number.", required = false, defaultValue = "1")
            @RequestParam(defaultValue = "1", required = false) final int page,
            @ApiParam(value = "Results per page.", required = false, defaultValue = "10")
            @RequestParam(defaultValue = "10", required = false) final int limit,
            final HttpServletRequest req) {

        // Validates incoming params.
        if (page < 1) {
            throw new InvalidArgumentException(
                    "Page number should be equals or greater than 1");
        }

        if (limit < 1) {
            throw new InvalidArgumentException(
                    "Limit number should be equals or greater than 1");
        }

        MemoPage pageResponse = daoClient.getMemosForPage(page, limit);

        Memo[] memoArray = null;

        Pagination pagination = null;

        if (pageResponse != null && isNotEmpty(pageResponse.getMemoList())) {
            List<Memo> list = pageResponse.getMemoList();
            memoArray = list.toArray(new Memo[list.size()]);

            String prev = null;
            String next = null;
            if (pageResponse.getPrevPageNumber() != null) {
                prev = req.getRequestURL()
                        +"?page=" + pageResponse.getPrevPageNumber() + "&limit=" + limit;
            }

            if (pageResponse.getNextPageNumber() != null) {
                next = req.getRequestURL()
                        + "?page=" + pageResponse.getNextPageNumber() + "&limit=" + limit;
            }
            pagination = new Pagination(pageResponse.getTotalMemosSize(), prev, next);
        } else {
            throw new DataNotFoundException(
                    "No memos found for requested page parameters.");
        }

        return new ServiceResponse<>(memoArray, pagination, null);
    }


    /**
     * Update the Memo.
     *
     * @param id      the id
     * @param request the request
     *
     * @return Response Message
     */
    @RequestMapping(value = URI_BY_ID, method = RequestMethod.PUT, consumes = {JSON, XML},
            produces = {JSON, XML})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update Memo", notes = "Update a Memo")
    final void updateMemo(@ApiParam(value = "The ID of the existing memo resource.")
                          @PathVariable final Long id,
                          @ApiParam(value = "The memo request body.")
                          @RequestBody @Valid final Memo request) {
        if (request.getId() != null && id != request.getId()) {
            throw new InvalidDataException("Requested Memo ID not matching with Body.");
        } else {
            request.setId(id);
        }
        daoClient.updateMemo(request);
    }

    /**
     * Delete the Memo.
     *
     * @param id the id
     *
     * @return Response Message
     */
    @RequestMapping(value = URI_BY_ID, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete the Memo", notes = "Delete a Memo")
    final void deleteMemo(@ApiParam(value = "The ID of the existing memo resource.")
                          @PathVariable final Long id) {

        daoClient.deleteMemo(id);
    }
}
