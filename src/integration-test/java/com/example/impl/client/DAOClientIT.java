package com.example.impl.client;

import com.example.MemoBootApplication;
import com.example.api.client.DAOClient;
import com.example.exception.DataNotFoundException;
import com.example.exception.InvalidDataException;
import com.example.model.Memo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for DAOClientImpl.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemoBootApplication.class)
@WebAppConfiguration
@TestPropertySource(locations = {"classpath:config/integrationTest.properties"})
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts =
                "classpath:/scripts/setup-teardown.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                "classpath:/scripts/setup-teardown.sql")})
public class DAOClientIT {

    /**
     * Expected exception.
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();
    /**
     * The Jdbc template.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * The Dao client.
     */
    @Autowired
    private DAOClient daoClient;

    /**
     * Create new memo test.
     */
    @Test
    public void createNewMemoTest() {
        Memo memo = new Memo();
        memo.setTitle("Some title");
        memo.setAuthor("Some author");
        memo.setText("Some text");

        Memo createdMemo = daoClient.createMemo(memo);
        assertNotNull("Memo did not get created", createdMemo);
        assertTrue("Created Memo Title not matching", StringUtils.equals(memo.getTitle(), createdMemo.getTitle()));
        assertTrue("Created Memo Author not matching", StringUtils.equals(memo.getAuthor(), createdMemo.getAuthor()));
        assertTrue("Created Memo Text not matching", StringUtils.equals(memo.getText(), createdMemo.getText()));
        assertTrue("Id did not get created", createdMemo.getId() > 0);
        assertTrue("Dates are not matching", DateUtils.isSameInstant(createdMemo.getCreated(), createdMemo.getUpdated()));
        assertTrue("InValid date got created", DateUtils.isSameDay(createdMemo.getCreated(), new Date()));
    }

    /**
     * Create new memo test with bad inputs.
     */
    @Test
    public void createNewMemoTestWithBadInputs() {
        Memo memo = new Memo();
        memo.setTitle("Some title");

        exception.expect(InvalidDataException.class);
        Memo createdMemo = daoClient.createMemo(memo);
        assertNotNull("Memo did not get created", createdMemo);
    }


    /**
     * Delete memo test valid.
     */
    @Test
    public void deleteMemoTestValid() {
        jdbcTemplate.update("INSERT INTO MEMOS (ID, TITLE, AUTHOR, TEXT) VALUES (10," +
                "'title test','author test','random test text')");

        daoClient.deleteMemo(10L);

        Long count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM MEMOS", Long
                .class);
        assertEquals(0L, count.longValue());
    }

    /**
     * Delete memo test with invalid.
     */
    @Test
    public void deleteMemoTestWithInvalid() {
        exception.expect(DataNotFoundException.class);
        daoClient.deleteMemo(1L);
    }

    /**
     * Gets memo test valid.
     */
    @Test
    public void getMemoTestValid() {
        jdbcTemplate.update("INSERT INTO MEMOS (ID, TITLE, AUTHOR, TEXT) VALUES (10," +
                "'title test','author test','random test text')");
        Memo memo = daoClient.getMemo(10L);
        assertNotNull("No memo found for given Id", memo);
        assertTrue("Id not matching", memo.getId() == 10L);
    }

    /**
     * Gets memo test invalid.
     */
    @Test
    public void getMemoTestInvalid() {
        exception.expect(DataNotFoundException.class);
        Memo memo = daoClient.getMemo(10L);
    }

    /**
     * Exists memo test valid.
     */
    @Test
    public void existsMemoTestValid() {
        jdbcTemplate.update("INSERT INTO MEMOS (ID, TITLE, AUTHOR, TEXT) VALUES (10," +
                "'title test','author test','random test text')");
        assertTrue(daoClient.existsMemo(10L));
    }

    /**
     * Exists memo test 2.
     */
    @Test
    public void existsMemoTest2() {
        jdbcTemplate.update("INSERT INTO MEMOS (ID, TITLE, AUTHOR, TEXT) VALUES (10," +
                "'title test','author test','random test text')");
        assertFalse(daoClient.existsMemo(3L));
    }

    /**
     * Gets all memos test valid.
     */
    @Test
    public void getAllMemosTestValid() {
        jdbcTemplate.update("INSERT INTO MEMOS (ID, TITLE, AUTHOR, TEXT) VALUES (1," +
                "'title test','author test','random test text')");
        jdbcTemplate.update("INSERT INTO MEMOS (ID, TITLE, AUTHOR, TEXT) VALUES (2," +
                "'title test','author test','random test text')");

        List<Memo> list = daoClient.getAllMemos(1, 2);
        assertNotNull(list);
        assertEquals(2, list.size());
        Iterator<Memo> ite = list.iterator();

        long count = 0L;
        while (ite.hasNext()) {
            Memo memo = ite.next();
            count += memo.getId();
        }
        assertEquals(3, count);
    }

    /**
     * Gets all memos test not valid.
     */
    @Test
    public void getAllMemosTestNotValid() {
        List<Memo> list = daoClient.getAllMemos(1, 2);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}
