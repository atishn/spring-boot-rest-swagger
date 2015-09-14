package com.example.impl.client;

import com.example.api.client.DAOClient;
import com.example.exception.DataNotFoundException;
import com.example.exception.InvalidDataException;
import com.example.exception.MemoRestException;
import com.example.model.Memo;
import com.example.model.MemoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * DAO client implementation.
 */
@Repository
public class DAOClientImpl implements DAOClient {

    /**
     * The constant MEMO_ID_LEN.
     */
    private static final int MEMO_ID_LEN = 15;
    /**
     * The constant INSERT_NEW_MEMO.
     */
    private static final String INSERT_NEW_MEMO = "INSERT INTO MEMOS "
            + "(ID, TITLE, AUTHOR, TEXT) VALUES (?,?,?,?)";
    /**
     * The constant UPDATE_MEMO.
     */
    private static final String UPDATE_MEMO = "update MEMOS set TITLE = ?, AUTHOR=?"
            + ", TEXT = ?, UPDATED = current_timestamp where Id = ?";
    /**
     * The constant SELECT_MEMO_BYID.
     */
    private static final String SELECT_MEMO_BYID = "SELECT * FROM MEMOS m WHERE m.Id = ?";
    /**
     * The constant SELECT_MEMOS.
     */
    private static final String SELECT_MEMOS = "SELECT * FROM MEMOS order by 1 LIMIT ? OFFSET ?";
    /**
     * The constant EXIST_MEMO.
     */
    private static final String EXIST_MEMO = "SELECT COUNT(1) FROM MEMOS"
            + " m WHERE m.Id = ?";
    /**
     * The constant MEMOS_COUNT.
     */
    private static final String MEMOS_COUNT = "SELECT COUNT(1) FROM MEMOS";
    /**
     * The constant DELETE_MEMO.
     */
    private static final String DELETE_MEMO = "DELETE FROM MEMOS m WHERE m.id = ?";
    /**
     * The constant GET_SERIAL_NO.
     */
    private static final String GET_SERIAL_NO = "SELECT nextval('serial')";
    /**
     * The Jdbc template.
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Sets data source.
     *
     * @param dataSource the data source
     */
    @Autowired
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Create memo.
     *
     * @param request the request
     *
     * @return the memo
     */
    @Override
    public Memo createMemo(final Memo request) {
        Long nextSeqNo = getNextSeqNo();
        Object[] params = new Object[]{nextSeqNo, request.getTitle(), request.getAuthor()
                , request.getText()};
        int[] types = new int[]
                {Types.NUMERIC, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
        try {
            int response = jdbcTemplate.update(INSERT_NEW_MEMO, params, types);
            if (response > 0) {
                return getMemo(nextSeqNo);
            }
            throw new MemoRestException("Memo did not get created.");
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidDataException(ex.getMessage());
        }
    }

    /**
     * Get next seq no.
     *
     * @return the integer
     */
    private Long getNextSeqNo() {
        Long newId = jdbcTemplate.queryForObject(GET_SERIAL_NO, Long.class);
        return newId;
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
        int rows = jdbcTemplate.update(DELETE_MEMO, id);
        if (rows == 0) {
            throw new DataNotFoundException("Memo with Id : " + id + " does not exists.");
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
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_MEMO_BYID, id);
        if (rows.size() == 1) {
            return adaptMemo(rows.get(0));
        }
        throw new DataNotFoundException("Memo with Id : " + id + " does not exists.");
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
        int total = jdbcTemplate.queryForInt(EXIST_MEMO, id);
        return total > 0;
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
    public List<Memo> getAllMemos(int pageNo, int limit) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_MEMOS, limit, (pageNo - 1) * limit);
        List<Memo> memos = newArrayList();
        for (Map<String, Object> row : rows) {
            memos.add(adaptMemo(row));
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
    public MemoPage getMemosForPage(int pageNo, int limit) {
        MemoPage response = new MemoPage();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_MEMOS, limit, (pageNo - 1) * limit);
        if (isEmpty(rows)) {
            throw new DataNotFoundException("No memos found for requested page parameters.");
        }

        List<Memo> memos = newArrayList();
        for (Map<String, Object> row : rows) {
            memos.add(adaptMemo(row));
        }

        Integer totalMemos = getTotalMemosCount();
        response.setMemoList(memos);
        response.setTotalMemosSize(totalMemos);
        if (pageNo > 1) {
            response.setPrevPageNumber(pageNo - 1);
        }
        if (pageNo * limit < totalMemos) {
            response.setNextPageNumber(pageNo + 1);
        }
        return response;
    }

    /**
     * Gets total memos count.
     *
     * @return the total memos count
     */
    private Integer getTotalMemosCount() {
        Integer total = jdbcTemplate.queryForObject(MEMOS_COUNT, Integer.class);
        return total;
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
        Object[] params = new Object[]{memo.getTitle(), memo.getAuthor(),
                memo.getText(), memo.getId()};
        int[] types = new int[]{Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.NUMERIC};
        int rows = jdbcTemplate.update(UPDATE_MEMO, params, types);
        if (rows == 0) {
            throw new DataNotFoundException(
                    "Memo " + memo.getTitle() + " does not exists.");
        }
        return true;
    }

    /**
     * Translate to memo POJO object.
     *
     * @param row the row
     *
     * @return the memo
     */
    private Memo adaptMemo(final Map<String, Object> row) {
        Memo memo = new Memo();
        memo.setId((Long.valueOf(row.get("ID").toString())));
        memo.setTitle((String) row.get("TITLE"));
        memo.setAuthor((String) row.get("AUTHOR"));
        memo.setText((String) row.get("TEXT"));

        Timestamp created = (Timestamp) row.get("CREATED");
        memo.setCreated(new Date(created.getTime()));

        Timestamp updated = (Timestamp) row.get("UPDATED");
        memo.setUpdated(new Date(updated.getTime()));

        return memo;
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
