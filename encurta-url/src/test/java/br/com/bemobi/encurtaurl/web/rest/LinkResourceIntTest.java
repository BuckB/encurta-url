package br.com.bemobi.encurtaurl.web.rest;

import br.com.bemobi.encurtaurl.EncurtaUrlApp;
import br.com.bemobi.encurtaurl.domain.Link;
import br.com.bemobi.encurtaurl.repository.LinkRepository;
import br.com.bemobi.encurtaurl.service.LinkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LinkResource REST controller.
 *
 * @see LinkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EncurtaUrlApp.class)
public class LinkResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBBB";

    private static final String DEFAULT_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_ALIAS = "BBBBBBBBBB";

    private static final Long DEFAULT_TIME_TAKEN = 1L;
    private static final Long UPDATED_TIME_TAKEN = 2L;

    @Inject
    private LinkRepository linkRepository;

    @Inject
    private LinkService linkService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLinkMockMvc;

    private Link link;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LinkResource linkResource = new LinkResource();
        ReflectionTestUtils.setField(linkResource, "linkService", linkService);
        this.restLinkMockMvc = MockMvcBuilders.standaloneSetup(linkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Link createEntity(EntityManager em) {
        Link link = new Link()
                .url(DEFAULT_URL)
                .alias(DEFAULT_ALIAS)
                .time_taken(DEFAULT_TIME_TAKEN);
        return link;
    }

    @Before
    public void initTest() {
        link = createEntity(em);
    }

    @Test
    @Transactional
    public void createLink() throws Exception {
        int databaseSizeBeforeCreate = linkRepository.findAll().size();

        // Create the Link

        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isCreated());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeCreate + 1);
        Link testLink = linkList.get(linkList.size() - 1);
        assertThat(testLink.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testLink.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testLink.getTime_taken()).isEqualTo(DEFAULT_TIME_TAKEN);
    }

    @Test
    @Transactional
    public void createLinkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = linkRepository.findAll().size();

        // Create the Link with an existing ID
        Link existingLink = new Link();
        existingLink.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLink)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = linkRepository.findAll().size();
        // set the field null
        link.setUrl(null);

        // Create the Link, which fails.

        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isBadRequest());

        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = linkRepository.findAll().size();
        // set the field null
        link.setAlias(null);

        // Create the Link, which fails.

        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isBadRequest());

        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLinks() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);

        // Get all the linkList
        restLinkMockMvc.perform(get("/api/links?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(link.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
            .andExpect(jsonPath("$.[*].time_taken").value(hasItem(DEFAULT_TIME_TAKEN.intValue())));
    }

    @Test
    @Transactional
    public void getLink() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);

        // Get the link
        restLinkMockMvc.perform(get("/api/links/{id}", link.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(link.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()))
            .andExpect(jsonPath("$.time_taken").value(DEFAULT_TIME_TAKEN.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLink() throws Exception {
        // Get the link
        restLinkMockMvc.perform(get("/api/links/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLink() throws Exception {
        // Initialize the database
        linkService.save(link);

        int databaseSizeBeforeUpdate = linkRepository.findAll().size();

        // Update the link
        Link updatedLink = linkRepository.findOne(link.getId());
        updatedLink
                .url(UPDATED_URL)
                .alias(UPDATED_ALIAS)
                .time_taken(UPDATED_TIME_TAKEN);

        restLinkMockMvc.perform(put("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLink)))
            .andExpect(status().isOk());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeUpdate);
        Link testLink = linkList.get(linkList.size() - 1);
        assertThat(testLink.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testLink.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testLink.getTime_taken()).isEqualTo(UPDATED_TIME_TAKEN);
    }

    @Test
    @Transactional
    public void updateNonExistingLink() throws Exception {
        int databaseSizeBeforeUpdate = linkRepository.findAll().size();

        // Create the Link

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLinkMockMvc.perform(put("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isCreated());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLink() throws Exception {
        // Initialize the database
        linkService.save(link);

        int databaseSizeBeforeDelete = linkRepository.findAll().size();

        // Get the link
        restLinkMockMvc.perform(delete("/api/links/{id}", link.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
