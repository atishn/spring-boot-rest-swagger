package com.example.controller.rest;

import com.example.MemoBootApplication;
import com.example.model.ErrorResponse;
import com.example.model.Memo;
import com.example.model.Pagination;
import com.example.model.ServiceResponse;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller TEST for Integration with Live Test Systems.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemoBootApplication.class)
@WebAppConfiguration
@ActiveProfiles("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemoControllerIT {

    /**
     * Web application Context for Unit Test
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Mock MVC for Unit Testing
     */
    private MockMvc mvc;


    /**
     * Location pattern.
     *
     * @param expectedUrlPattern the expected url pattern
     *
     * @return the result matcher
     */
    private static ResultMatcher locationPattern(final String expectedUrlPattern) {
        return new ResultMatcher() {
            public void match(MvcResult result) {
                Pattern pattern = Pattern.compile("\\A" + expectedUrlPattern + "\\z");
                Assert.assertTrue(pattern.matcher(result.getResponse().getRedirectedUrl())
                        .find());
            }
        };
    }

    /**
     * Set up the tests.
     */
    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }


    /**
     * Test create memos happy.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCreateMemosHappy() throws Exception {

        // Test for JSON Happy Path
        MvcResult mvcResult = mvc.perform(post(MemoController.V1_PREFIX + MemoController.MEMO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"title\":\"some title\"," +
                        "\"text\":\"some text\"," +
                        "\"author\":\"an author\"}"))

                .andExpect(status().isCreated())
                .andExpect(locationPattern("http://.*/1/memo/.*"))
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"title\":\"some title\",\"author\":\"an author\",\"text\":\"some text\""))).andReturn();
    }

    /**
     * Test for xML support out put.
     *
     * @throws Exception the exception
     */
    @Test
    public void testForXMLSupportOutPut() throws Exception {

        // Test for JSON Happy Path
        MvcResult mvcResult = mvc.perform(post(MemoController.V1_PREFIX + MemoController.MEMO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_XML)
                .characterEncoding("UTF-8")
                .content("{\"title\":\"some title\"," +
                        "\"text\":\"some text\"," +
                        "\"author\":\"an author\"}"))

                .andExpect(status().isCreated())
                .andExpect(locationPattern("http://.*/1/memo/.*"))
                .andExpect(content().string(containsString("<title>some title</title><author>an author</author><text>some text</text>"))).andReturn();
    }


    /**
     * Test create memo with bad data.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCreateMemoWithBadData() throws Exception {

        MvcResult mvcResult = mvc.perform(post(MemoController.V1_PREFIX + MemoController.MEMO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"title\":\"some title\"," +
                        "\"author\":\"an author\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Memo Text cannot be null or empty"))).andReturn();
    }


    /**
     * Test Get memos happy.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetMemoByValidIdHappy() throws Exception {
        // Test for JSON Happy Path
        createNewTestMemo();
        MvcResult mvcResult = mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"title\":\"some title\",\"author\":\"an author\",\"text\":\"some text\""))).andReturn();

    }


    /**
     * Test get memo by invalid id bad.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetMemoByInvalidId() throws Exception {
        MvcResult mvcResult = mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "/123345")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("\"errors\":{")))
                .andExpect(content().string(containsString("\"title\":\"No entry exists for that given request"))).andReturn();

    }


    /**
     * Test update memos happy.
     *
     * @throws Exception the exception
     */
    @Test
    public void testUpdateMemosHappyPath() throws Exception {
        // Test for JSON Happy Path
        createNewTestMemo();

        // Update the Resource
        mvc.perform(put(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

                .characterEncoding("UTF-8")
                .content("{\"title\":\"updated title\"," +
                        "\"text\":\"updated text\"," +
                        "\"author\":\"updated author\"}"))
                .andExpect(status().isNoContent());

        // Now Test either its been updated or not.
        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"title\":\"updated title\",\"author\":\"updated author\",\"text\":\"updated text\""))).andReturn();

    }

    /**
     * Test update memos bad path.
     *
     * @throws Exception the exception
     */
    @Test
    public void testUpdateMemosBadPath() throws Exception {

        // Update the Resource with invalid memo id.
        mvc.perform(put(MemoController.V1_PREFIX + MemoController.MEMO + "/122334")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

                .characterEncoding("UTF-8")
                .content("{\"title\":\"updated title\"," +
                        "\"text\":\"updated text\"," +
                        "\"author\":\"updated author\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No entry exists for that given request"))).andReturn();

    }


    /**
     * Test update memos bad data.
     *
     * @throws Exception the exception
     */
    @Test
    public void testUpdateMemosBadData() throws Exception {

        // Test for JSON Happy Path
        createNewTestMemo();

        // Update the Resource with invalid memo id.
        mvc.perform(put(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

                .characterEncoding("UTF-8")
                .content("{\"title\":\"updated title\"," +
                        "\"id\":2222," +
                        "\"text\":\"updated text\"," +
                        "\"author\":\"updated author\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Requested Memo ID from Url not matching with Body"))).andReturn();

    }

    /**
     * Test Get memos as a list without Pagination params.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetAllMemoHappy() throws Exception {

        // Create 20 Memos into the system.
        for (int i = 0; i < 20; i++) {
            createNewTestMemo();
        }

        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"totalRecords\":20")))
                .andExpect(content().string(containsString("\"next\":\"http://localhost/data/1/memo?page=1&limit=10\"")))
                .andReturn();
    }


    /**
     * Test Get memos as a list Pagination params.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetAllMemoHappyWithPagination() throws Exception {

        // Create 20 Memos into the system.
        for (int i = 0; i < 20; i++) {
            createNewTestMemo();
        }

        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "?page=0&limit=10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"totalRecords\":20")))
                .andExpect(content().string(containsString("\"prev\":null")))
                .andExpect(content().string(containsString("\"next\":\"http://localhost/data/1/memo?page=1&limit=10\"")))
                .andReturn();

        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "?page=1&limit=10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":11")))
                .andExpect(content().string(containsString("\"totalRecords\":20")))
                .andExpect(content().string(containsString("\"next\":null")))
                .andExpect(content().string(containsString("\"prev\":\"http://localhost/data/1/memo?page=0&limit=10\"")))
                .andReturn();
    }


    /**
     * Test Get memos as a list Pagination params.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetAllMemoBadWithPagination() throws Exception {


        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No memos found for requested page parameters")));


        // Create 20 Memos into the system.
        for (int i = 0; i < 20; i++) {
            createNewTestMemo();
        }

        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "?page=-1&limit=10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Page number should be equals or greater than 0")));

        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "?page=1&limit=-10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Limit number should be equals or greater than 1")));

        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "?page=5&limit=10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No memos found for requested page parameters")));

    }


    /**
     * Test Delete memo.
     *
     * @throws Exception the exception
     */
    @Test
    public void testDeleteMemoHappyPath() throws Exception {

        // Create 20 Memos into the system.
        createNewTestMemo();

        mvc.perform(delete(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Test either its been deleted or not.
        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Test Delete memo bad path.
     *
     * @throws Exception the exception
     */
    @Test
    public void testDeleteMemoBadPath() throws Exception {

        // Try to delete invalid resource
        mvc.perform(delete(MemoController.V1_PREFIX + MemoController.MEMO + "/1233345")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No entry exists for that given request.")));
    }

    /**
     * Helper Method to create New Memo.
     *
     * @throws Exception the exception
     */
    private void createNewTestMemo() throws Exception {

        MvcResult mvcResult = mvc.perform(post(MemoController.V1_PREFIX + MemoController.MEMO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"title\":\"some title\"," +
                        "\"text\":\"some text\"," +
                        "\"author\":\"an author\"}"))

                .andExpect(status().isCreated())
                .andExpect(locationPattern("http://.*/1/memo/.*")).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Gson gson = new Gson();

        ServiceResponse<Memo, Pagination, ErrorResponse> createdMemo = gson.fromJson(response, ServiceResponse.class);
        assertNotNull(createdMemo);
        assertNotNull(createdMemo.getResult());

    }

}
