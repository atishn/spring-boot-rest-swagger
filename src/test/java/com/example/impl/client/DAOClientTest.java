package com.example.impl.client;

import com.example.MemoBootApplication;
import com.example.api.client.DAOClient;
import com.example.exception.DataNotFoundException;
import com.example.exception.InvalidDataException;
import com.example.model.Memo;
import com.example.model.MemoPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for DAOClientImpl.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemoBootApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DAOClientTest {

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

        Memo memo = new Memo();
        memo.setTitle("Some title");
        memo.setAuthor("Some author");
        memo.setText("Some text");
        Memo createdMemo = daoClient.createMemo(memo);
        assertTrue(daoClient.getCount() == 1);

        daoClient.deleteMemo(createdMemo.getId());

        assertTrue("Memo didnt get deleted", daoClient.getCount() == 0);
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
        Memo memo = new Memo();
        memo.setTitle("Some title");
        memo.setAuthor("Some author");
        memo.setText("Some text");
        Memo createdMemo = daoClient.createMemo(memo);

        memo = daoClient.getMemo(createdMemo.getId());
        assertNotNull("No memo found for given Id", memo);
        assertTrue("Id not matching", memo.getId() == 1L);
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
        Memo memo = new Memo();
        memo.setTitle("Some title");
        memo.setAuthor("Some author");
        memo.setText("Some text");
        Memo createdMemo = daoClient.createMemo(memo);


        assertTrue(daoClient.existsMemo(createdMemo.getId()));
    }

    /**
     * Exists memo test Invalid Id.
     */
    @Test
    public void existsMemoTestForInvalid() {
        assertFalse(daoClient.existsMemo(3L));
    }

    /**
     * Gets all memos test valid.
     */
    @Test
    public void getAllMemosTestValid() {

        for (int i = 0; i < 20; i++) {
            Memo memo = new Memo();
            memo.setTitle("Some title " + i);
            memo.setAuthor("Some author " + i);
            memo.setText("Some text " + i);
            daoClient.createMemo(memo);
        }

        List<Memo> list = daoClient.getAllMemos(0, 10);
        assertNotNull(list);
        assertEquals(10, list.size());
    }

    /**
     * Gets all memos test not valid.
     */
    @Test
    public void getMemosForPageValid() {

        for (int i = 0; i < 20; i++) {
            Memo memo = new Memo();
            memo.setTitle("Some title " + i);
            memo.setAuthor("Some author " + i);
            memo.setText("Some text " + i);
            daoClient.createMemo(memo);
        }

        MemoPage page = daoClient.getMemosForPage(0, 10);
        assertNotNull(page);
        assertTrue(1 == page.getNextPageNumber());
        assertNull(page.getPrevPageNumber());
        assertTrue(2 == page.getTotalPages());
        assertTrue(20 == page.getTotalMemosSize());
    }


    /**
     * Gets memo test invalid.
     */
    @Test
    public void getMemosForPageInValid() {
        exception.expect(DataNotFoundException.class);
        daoClient.getMemosForPage(10, 10);
    }
}
