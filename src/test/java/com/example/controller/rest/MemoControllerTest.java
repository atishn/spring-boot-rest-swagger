package com.example.controller.rest;

import com.example.MemoBootApplication;
import com.example.api.client.DAOClient;
import com.example.model.Memo;
import com.example.model.MemoPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Memo controller test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemoBootApplication.class)
@WebAppConfiguration
@TestPropertySource(locations = {"classpath:config/test.properties"})
public class MemoControllerTest {

    /**
     * Web application Context for Unit Test
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Mock MVC for Unit Testing
     */
    private MockMvc mvc;


    @InjectMocks
    private MemoController memoController;


    /**
     * The Dao client.
     */
    @Mock
    private DAOClient daoClient;

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
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(memoController).build();

//
//        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
//        daoClient = Mockito.mock(DAOClient.class);
    }


    /**
     * Create memo test 1.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCreateMemoTestHappy() throws Exception {

        Memo memo = new Memo();
        memo.setId(1L);
        memo.setText("text");
        memo.setAuthor("author");
        memo.setTitle("title");
        memo.setCreated(new Date(1442033000000L));
        memo.setUpdated(new Date(1442033000099L));

        Mockito.when(daoClient.createMemo(Matchers.any(Memo.class))).thenReturn(memo);

        mvc.perform(post(MemoController.V1_PREFIX + MemoController.MEMO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"title\":\"some title\"," +
                        "\"text\":\"some text\"," +
                        "\"author\":\"an author\"}"))

                .andExpect(status().isCreated())
                .andExpect(locationPattern("http://.*/1/memo/.*"))
                .andExpect(content().json("{\"result\":{\"title\":\"title\"," +
                        "\"author\":\"author\",\"text\":\"text\",\"id\":1," +
                        "\"created\":1442033000000,\"updated\":1442033000099}," +
                        "\"errors\":null}"));


    }

    /**
     * Test for xML support out put.
     *
     * @throws Exception the exception
     */
    @Test
    public void testForXMLSupportOutPut() throws Exception {

        Memo memo = new Memo();
        memo.setId(1L);
        memo.setText("some text");
        memo.setAuthor("an author");
        memo.setTitle("some title");
        memo.setCreated(new Date(1442033000000L));
        memo.setUpdated(new Date(1442033000099L));

        Mockito.when(daoClient.createMemo(Matchers.any(Memo.class))).thenReturn(memo);
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
        Mockito.when(daoClient.createMemo(Matchers.any(Memo.class))).thenReturn(null);

        mvc.perform(post(MemoController.V1_PREFIX + MemoController.MEMO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"title\":\"some title\"," +
                        "\"author\":\"an author\"}"))
                .andExpect(status().isBadRequest());
    }


    /**
     * Gets memo test.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetMemoByValidIdHappy() throws Exception {
        Memo memo = new Memo();
        memo.setId(1L);
        memo.setText("text");
        memo.setAuthor("author");
        memo.setTitle("title");
        memo.setCreated(new Date(1442033000000L));
        memo.setUpdated(new Date(1442033000099L));

        Mockito.when(daoClient.getMemo(Matchers.anyLong())).thenReturn(memo);

        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":{\"title\":\"title\"," +
                        "\"author\":\"author\",\"text\":\"text\",\"id\":1," +
                        "\"created\":1442033000000,\"updated\":1442033000099}," +
                        "\"errors\":null}"));
    }

    /**
     * Gets memos test 1.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetAllMemoHappy() throws Exception {
        Memo memo1 = new Memo();
        memo1.setId(1L);
        memo1.setText("text1");
        memo1.setAuthor("author1");
        memo1.setTitle("title1");
        memo1.setCreated(new Date(1442033000000L));
        memo1.setUpdated(new Date(1442033000099L));

        Memo memo2 = new Memo();
        memo2.setId(2L);
        memo2.setText("text2");
        memo2.setAuthor("author2");
        memo2.setTitle("title2");
        memo2.setCreated(new Date(1442033001000L));
        memo2.setUpdated(new Date(1442033001099L));


        Memo memo3 = new Memo();
        memo3.setId(3L);
        memo3.setText("text3");
        memo3.setAuthor("author3");
        memo3.setTitle("title3");
        memo3.setCreated(new Date(1442033001000L));
        memo3.setUpdated(new Date(1442033001099L));


        Memo memo4 = new Memo();
        memo4.setId(4L);
        memo4.setText("text4");
        memo4.setAuthor("author4");
        memo4.setTitle("title4");
        memo4.setCreated(new Date(1442033001000L));
        memo4.setUpdated(new Date(1442033001099L));


        Memo memo5 = new Memo();
        memo5.setId(5L);
        memo5.setText("text5");
        memo5.setAuthor("author5");
        memo5.setTitle("title5");
        memo5.setCreated(new Date(1442033001000L));
        memo5.setUpdated(new Date(1442033001099L));


        List<Memo> list = newArrayList();
        list.add(memo1);
        list.add(memo2);
        list.add(memo3);
        list.add(memo4);
        list.add(memo5);

        MemoPage page = new MemoPage();
        page.setMemoList(list);
        page.setTotalMemosSize(5);


        Mockito.when(daoClient.getMemosForPage(Matchers.anyInt(), Matchers.anyInt()))
                .thenReturn(page);

        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"title\":\"title1\",\"author\":\"author1\",\"text\":\"text1\"")))
                .andExpect(content().string(containsString("\"title\":\"title2\",\"author\":\"author2\",\"text\":\"text2\"")))
                .andExpect(content().string(containsString("\"prev\":null")))
                .andExpect(content().string(containsString("\"next\":null")));

        page.setPrevPageNumber(1);
        page.setNextPageNumber(3);
        Mockito.when(daoClient.getMemosForPage(Matchers.anyInt(), Matchers.anyInt()))
                .thenReturn(page);

        mvc.perform(get(MemoController.V1_PREFIX + MemoController.MEMO + "?page=2&limit=2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"prev\":\"http://localhost/data/1/memo?page=1&limit=2\"")))
                .andExpect(content().string(containsString("\"next\":\"http://localhost/data/1/memo?page=3&limit=2\"")))

        ;
    }

    /**
     * Updates memo test.
     *
     * @throws Exception the exception
     */
    @Test
    public void testUpdateMemoByValidIdHappy() throws Exception {

        Mockito.when(daoClient.updateMemo(Matchers.any(Memo.class))).thenReturn(true);

        mvc.perform(put(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"title\":\"some title\"," +
                        "\"text\":\"some text\"," +
                        "\"author\":\"an author\"}"))
                .andExpect(status().isNoContent());
    }

    /**
     * Updates memo test with bad inputs.
     *
     * @throws Exception the exception
     */
    @Test
    public void testUpdateMemoByInvalidData() throws Exception {

        Mockito.when(daoClient.updateMemo(Matchers.any(Memo.class))).thenReturn(true);

        mvc.perform(put(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"title\":\"some title\"," +
                        "\"author\":\"an author\"}"))
                .andExpect(status().isBadRequest());
    }


    /**
     * Deletes memo test.
     *
     * @throws Exception the exception
     */
    @Test
    public void testDeleteMemoByValidIdHappy() throws Exception {

        Mockito.when(daoClient.updateMemo(Matchers.any(Memo.class))).thenReturn(true);

        mvc.perform(delete(MemoController.V1_PREFIX + MemoController.MEMO + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}
