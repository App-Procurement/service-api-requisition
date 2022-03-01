package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.repository.CommitteeRepository;

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
 * Integration tests for the {@link CommitteeResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CommitteeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

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

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommitteeMockMvc;

    private Committee committee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Committee createEntity(EntityManager em) {
        Committee committee = new Committee()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .notes(DEFAULT_NOTES);
        return committee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Committee createUpdatedEntity(EntityManager em) {
        Committee committee = new Committee()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .notes(UPDATED_NOTES);
        return committee;
    }

    @BeforeEach
    public void initTest() {
        committee = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommittee() throws Exception {
        int databaseSizeBeforeCreate = committeeRepository.findAll().size();
        // Create the Committee
        restCommitteeMockMvc.perform(post("/api/committees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committee)))
            .andExpect(status().isCreated());

        // Validate the Committee in the database
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeCreate + 1);
        Committee testCommittee = committeeList.get(committeeList.size() - 1);
        assertThat(testCommittee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommittee.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCommittee.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCommittee.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testCommittee.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCommittee.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testCommittee.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testCommittee.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createCommitteeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = committeeRepository.findAll().size();

        // Create the Committee with an existing ID
        committee.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitteeMockMvc.perform(post("/api/committees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committee)))
            .andExpect(status().isBadRequest());

        // Validate the Committee in the database
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCommittees() throws Exception {
        // Initialize the database
        committeeRepository.saveAndFlush(committee);

        // Get all the committeeList
        restCommitteeMockMvc.perform(get("/api/committees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }
    
    @Test
    @Transactional
    public void getCommittee() throws Exception {
        // Initialize the database
        committeeRepository.saveAndFlush(committee);

        // Get the committee
        restCommitteeMockMvc.perform(get("/api/committees/{id}", committee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(committee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }
    @Test
    @Transactional
    public void getNonExistingCommittee() throws Exception {
        // Get the committee
        restCommitteeMockMvc.perform(get("/api/committees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommittee() throws Exception {
        // Initialize the database
        committeeRepository.saveAndFlush(committee);

        int databaseSizeBeforeUpdate = committeeRepository.findAll().size();

        // Update the committee
        Committee updatedCommittee = committeeRepository.findById(committee.getId()).get();
        // Disconnect from session so that the updates on updatedCommittee are not directly saved in db
        em.detach(updatedCommittee);
        updatedCommittee
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .notes(UPDATED_NOTES);

        restCommitteeMockMvc.perform(put("/api/committees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommittee)))
            .andExpect(status().isOk());

        // Validate the Committee in the database
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeUpdate);
        Committee testCommittee = committeeList.get(committeeList.size() - 1);
        assertThat(testCommittee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommittee.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCommittee.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCommittee.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCommittee.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCommittee.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testCommittee.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testCommittee.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingCommittee() throws Exception {
        int databaseSizeBeforeUpdate = committeeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeMockMvc.perform(put("/api/committees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committee)))
            .andExpect(status().isBadRequest());

        // Validate the Committee in the database
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommittee() throws Exception {
        // Initialize the database
        committeeRepository.saveAndFlush(committee);

        int databaseSizeBeforeDelete = committeeRepository.findAll().size();

        // Delete the committee
        restCommitteeMockMvc.perform(delete("/api/committees/{id}", committee.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
