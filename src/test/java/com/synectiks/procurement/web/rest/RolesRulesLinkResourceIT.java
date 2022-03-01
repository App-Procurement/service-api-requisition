package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.RolesRulesLink;
import com.synectiks.procurement.repository.RolesRulesLinkRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RolesRulesLinkResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RolesRulesLinkResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private RolesRulesLinkRepository rolesRulesLinkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRolesRulesLinkMockMvc;

    private RolesRulesLink rolesRulesLink;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolesRulesLink createEntity(EntityManager em) {
        RolesRulesLink rolesRulesLink = new RolesRulesLink()
            .status(DEFAULT_STATUS)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY);
        return rolesRulesLink;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolesRulesLink createUpdatedEntity(EntityManager em) {
        RolesRulesLink rolesRulesLink = new RolesRulesLink()
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);
        return rolesRulesLink;
    }

    @BeforeEach
    public void initTest() {
        rolesRulesLink = createEntity(em);
    }

    @Test
    @Transactional
    public void createRolesRulesLink() throws Exception {
        int databaseSizeBeforeCreate = rolesRulesLinkRepository.findAll().size();
        // Create the RolesRulesLink
        restRolesRulesLinkMockMvc.perform(post("/api/roles-rules-links")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rolesRulesLink)))
            .andExpect(status().isCreated());

        // Validate the RolesRulesLink in the database
        List<RolesRulesLink> rolesRulesLinkList = rolesRulesLinkRepository.findAll();
        assertThat(rolesRulesLinkList).hasSize(databaseSizeBeforeCreate + 1);
        RolesRulesLink testRolesRulesLink = rolesRulesLinkList.get(rolesRulesLinkList.size() - 1);
        assertThat(testRolesRulesLink.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRolesRulesLink.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRolesRulesLink.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRolesRulesLink.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRolesRulesLink.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createRolesRulesLinkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rolesRulesLinkRepository.findAll().size();

        // Create the RolesRulesLink with an existing ID
        rolesRulesLink.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRolesRulesLinkMockMvc.perform(post("/api/roles-rules-links")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rolesRulesLink)))
            .andExpect(status().isBadRequest());

        // Validate the RolesRulesLink in the database
        List<RolesRulesLink> rolesRulesLinkList = rolesRulesLinkRepository.findAll();
        assertThat(rolesRulesLinkList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRolesRulesLinks() throws Exception {
        // Initialize the database
        rolesRulesLinkRepository.saveAndFlush(rolesRulesLink);

        // Get all the rolesRulesLinkList
        restRolesRulesLinkMockMvc.perform(get("/api/roles-rules-links?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rolesRulesLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }
    
    @Test
    @Transactional
    public void getRolesRulesLink() throws Exception {
        // Initialize the database
        rolesRulesLinkRepository.saveAndFlush(rolesRulesLink);

        // Get the rolesRulesLink
        restRolesRulesLinkMockMvc.perform(get("/api/roles-rules-links/{id}", rolesRulesLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rolesRulesLink.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingRolesRulesLink() throws Exception {
        // Get the rolesRulesLink
        restRolesRulesLinkMockMvc.perform(get("/api/roles-rules-links/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRolesRulesLink() throws Exception {
        // Initialize the database
        rolesRulesLinkRepository.saveAndFlush(rolesRulesLink);

        int databaseSizeBeforeUpdate = rolesRulesLinkRepository.findAll().size();

        // Update the rolesRulesLink
        RolesRulesLink updatedRolesRulesLink = rolesRulesLinkRepository.findById(rolesRulesLink.getId()).get();
        // Disconnect from session so that the updates on updatedRolesRulesLink are not directly saved in db
        em.detach(updatedRolesRulesLink);
        updatedRolesRulesLink
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);

        restRolesRulesLinkMockMvc.perform(put("/api/roles-rules-links")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRolesRulesLink)))
            .andExpect(status().isOk());

        // Validate the RolesRulesLink in the database
        List<RolesRulesLink> rolesRulesLinkList = rolesRulesLinkRepository.findAll();
        assertThat(rolesRulesLinkList).hasSize(databaseSizeBeforeUpdate);
        RolesRulesLink testRolesRulesLink = rolesRulesLinkList.get(rolesRulesLinkList.size() - 1);
        assertThat(testRolesRulesLink.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRolesRulesLink.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRolesRulesLink.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRolesRulesLink.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRolesRulesLink.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingRolesRulesLink() throws Exception {
        int databaseSizeBeforeUpdate = rolesRulesLinkRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolesRulesLinkMockMvc.perform(put("/api/roles-rules-links")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rolesRulesLink)))
            .andExpect(status().isBadRequest());

        // Validate the RolesRulesLink in the database
        List<RolesRulesLink> rolesRulesLinkList = rolesRulesLinkRepository.findAll();
        assertThat(rolesRulesLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRolesRulesLink() throws Exception {
        // Initialize the database
        rolesRulesLinkRepository.saveAndFlush(rolesRulesLink);

        int databaseSizeBeforeDelete = rolesRulesLinkRepository.findAll().size();

        // Delete the rolesRulesLink
        restRolesRulesLinkMockMvc.perform(delete("/api/roles-rules-links/{id}", rolesRulesLink.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RolesRulesLink> rolesRulesLinkList = rolesRulesLinkRepository.findAll();
        assertThat(rolesRulesLinkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
