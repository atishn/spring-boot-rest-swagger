package com.example.impl.client;

import com.example.api.client.DAOClient;
import com.example.exception.DataNotFoundException;
import com.example.exception.InvalidDataException;
import com.example.exception.MemoRestException;
import com.example.model.Memo;
import com.example.model.MemoPage;
import com.example.repo.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * DAO client implementation.
 */
@Service
public class DAOClientImpl implements DAOClient {

    /**
     * The Memo repository.
     */
    @Autowired
    private MemoRepository memoRepository;


    /**
     * Create memo.
     *
     * @param request the request
     *
     * @return the memo
     */
    @Override
    public Memo createMemo(final Memo request) {

        try {
            Memo resp = memoRepository.save(request);
            if (resp != null) {
                return resp;
            }
            throw new MemoRestException("Memo did not get created.");
        } catch (DataIntegrityViolationException | TransactionSystemException
                | ConstraintViolationException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }


    /**
     * Delete memo.
     *
     * @param id the id
     *
     * @return the boolean
     */
    @Override
    public boolean deleteMemo(final Long id) {
        try {
            memoRepository.delete(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new DataNotFoundException(ex.getMessage());
        }
        return true;
    }

    /**
     * Gets memo.
     *
     * @param id the id
     *
     * @return the memo
     */
    @Override
    public Memo getMemo(final Long id) {
        Memo memo = memoRepository.findOne(id);
        if (memo == null) {
            throw new DataNotFoundException("Memo with Id : " + id + " does not exists.");
        }
        return memo;
    }

    /**
     * Exists memo.
     *
     * @param id the id
     *
     * @return the boolean
     */
    @SuppressWarnings("deprecation")
    @Override
    public boolean existsMemo(final Long id) {
        return memoRepository.exists(id);
    }

    /**
     * Gets all memos.
     *
     * @param pageNo the page no
     * @param limit  the limit
     *
     * @return the all memos
     */
    @Override
    public List<Memo> getAllMemos(final int pageNo, final int limit) {

        Page pageOfMemos = memoRepository.findAll(new PageRequest(pageNo, limit));

        List<Memo> memos = pageOfMemos.getContent();

        if (isEmpty(memos)) {
            throw new DataNotFoundException(
                    "No memos found for requested page parameters.");
        }
        return memos;
    }

    /**
     * Gets all memos by pagination.
     *
     * @param pageNo the page no
     * @param limit  the limit
     *
     * @return the all memos
     */
    public MemoPage getMemosForPage(final int pageNo, final int limit) {
        MemoPage response = new MemoPage();

        Page pageOfMemos = memoRepository.findAll(new PageRequest(pageNo, limit));

        List<Memo> memos = pageOfMemos.getContent();

        if (isEmpty(memos)) {
            throw new DataNotFoundException(
                    "No memos found for requested page parameters.");
        }
        response.setMemoList(memos);

        Long totalMemos = pageOfMemos.getTotalElements();
        response.setTotalMemosSize(totalMemos);
        response.setTotalPages(pageOfMemos.getTotalPages());

        if (pageOfMemos.hasPrevious()) {
            response.setPrevPageNumber(pageOfMemos.getNumber() - 1);
        }
        if (pageOfMemos.hasNext()) {
            response.setNextPageNumber(pageOfMemos.getNumber() + 1);
        }
        return response;
    }

    /**
     * Gets total memos count.
     *
     * @return the total memos count
     */
    @Override
    public Integer getCount() {
        return (int) memoRepository.count();
    }

    /**
     * Update memo.
     *
     * @param memo the memo
     *
     * @return the boolean
     */
    @Override
    public boolean updateMemo(final Memo memo) {

        if (!existsMemo(memo.getId())) {
            throw new DataNotFoundException(
                    "Memo " + memo.getTitle() + " does not exists for update.");
        }
        Memo updated = memoRepository.save(memo);

        if (updated == null) {
            throw new DataNotFoundException(
                    "Updated Memo " + memo.getTitle() + " does not exists.");
        }
        return true;
    }


    /**
     * Patch memo.
     *
     * @param id     the id
     * @param fields the fields
     */
    @Override
    public void patchMemo(final long id, final Map<String, Object> fields) {

    }
}
