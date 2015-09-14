package com.example.api.client;

import com.example.model.Memo;
import com.example.model.MemoPage;

import java.util.List;
import java.util.Map;

/**
 * Persistent client for memos storage.
 */
public interface DAOClient {

    /**
     * Create a new memo and persist it.
     *
     * @param request the request
     *
     * @return the memo
     */
    Memo createMemo(Memo request);

    /**
     * Delete a memo given its identifier.
     *
     * @param id Memo identifier
     *
     * @return the boolean
     */
    boolean deleteMemo(Long id);

    /**
     * Retrieve a memo given its identifier.
     *
     * @param id Memo identifier
     *
     * @return memo requested
     */
    Memo getMemo(Long id);

    /**
     * Check if there is a memo with the given identifier.
     *
     * @param id Memo identifier
     *
     * @return true if the memo exists or false otherwise
     */
    boolean existsMemo(Long id);

    /**
     * Retrieve all memos from the persistent storage unit.
     *
     * @param pageNo the page no
     * @param limit  the limit
     *
     * @return List with all memos
     */
    List<Memo> getAllMemos(int pageNo, int limit);

    /**
     * Gets memos with pagination.
     *
     * @param pageNo the page no
     * @param limit  the limit
     *
     * @return the memos for page
     */
    MemoPage getMemosForPage(int pageNo, int limit);

    /**
     * Update all fields of a memo given a Memo object. memo.id will be used to look up
     * for it on the persistent storage unit.
     *
     * @param memo Model object with all fields to be updated
     *
     * @return the boolean
     */
    boolean updateMemo(final Memo memo);

    /**
     * Update given fields from memo given its identifier and fields to update.
     *
     * @param id     Memo identifier
     * @param fields Map with fields and new values
     */
    void patchMemo(long id, Map<String, Object> fields);

}
