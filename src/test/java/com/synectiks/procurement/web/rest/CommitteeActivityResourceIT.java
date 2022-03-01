package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.CommitteeActivity;
import com.synectiks.procurement.repository.CommitteeActivityRepository;

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
 * Integration tests for the {@link CommitteeActivityResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CommitteeActivityResourceIT {

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

    private static final Long DEFAULT_COMMITTEE_ID = 1L;
    private static final Long UPDATED_COMMITTEE_ID = 2L;

    @Autowired
    private CommitteeActivityRepository committeeActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommitteeActivityMockMvc;

    private CommitteeActivity committeeActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeActivity createEntity(EntityManager em) {
        CommitteeActivity committeeActivity = new CommitteeActivity()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .notes(DEFAULT_NOTES)
            .committeeId(DEFAULT_COMMITTEE_ID);
        return committeeActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeActivity createUpdatedEntity(EntityManager em) {
        CommitteeActivity committeeActivity = new CommitteeActivity()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .notes(UPDATED_NOTES)
            .committeeId(UPDATED_COMMITTEE_ID);
        return committeeActivity;
    }

    @BeforeEach
    public void initTest() {
        committeeActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommitteeActivity() throws Exception {
        int databaseSizeBeforeCreate = committeeActivityRepository.findAll().size();
        // Create the CommitteeActivity
        restCommitteeActivityMockMvc.perform(post("/api/committee-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeActivity)))
            .andExpect(status().isCreated());

        // Validate the CommitteeActivity in the database
        List<CommitteeActivity> committeeActivityList = committeeActivityRepository.findAll();
        assertThat(committeeActivityList).hasSize(databaseSizeBeforeCreate + 1);
        CommitteeActivity testCommitteeActivity = committeeActivityList.get(committeeActivityList.size() - 1);
        assertThat(testCommitteeActivity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommitteeActivity.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCommitteeActivity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCommitteeActivity.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testCommitteeActivity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCommitteeActivity.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testCommitteeActivity.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testCommitteeActivity.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCommitteeActivity.getCommitteeId()).isEqualTo(DEFAULT_COMMITTEE_ID);
    }

    @Test
    @Transactional
    public void createCommitteeActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = committeeActivityRepository.findAll().size();

        // Create the CommitteeActivity with an existing ID
        committeeActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitteeActivityMockMvc.perform(post("/api/committee-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeActivity)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeActivity in the database
        List<CommitteeActivity> committeeActivityList = committeeActivityRepository.findAll();
        assertThat(committeeActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCommitteeActivities() throws Exception {
        // Initialize the database
        committeeActivityRepository.saveAndFlush(committeeActivity);

        // Get all the committeeActivityList
        restCommitteeActivityMockMvc.perform(get("/api/committee-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committeeActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].committeeId").value(hasItem(DEFAULT_COMMITTEE_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getCommitteeActivity() throws Exception {
        // Initialize the database
        committeeActivityRepository.saveAndFlush(committeeActivity);

        // Get the committeeActivity
        restCommitteeActivityMockMvc.perform(get("/api/committee-activities/{id}", committeeActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(committeeActivity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.committeeId").value(DEFAULT_COMMITTEE_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingCommitteeActivity() throws Exception {
        // Get the committeeActivity
        restCommitteeActivityMockMvc.perform(get("/api/committee-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommitteeActivity() throws Exception {
        // Initialize the database
        committeeActivityRepository.saveAndFlush(committeeActivity);

        int databaseSizeBeforeUpdate = committeeActivityRepository.findAll().size();

        // Update the committeeActivity
        CommitteeActivity updatedCommitteeActivity = committeeActivityRepository.findById(committeeActivity.getId()).get();
        // Disconnect from session so that the updates on updatedCommitteeActivity are not directly saved in db
        em.detach(updatedCommitteeActivity);
        updatedCommitteeActivity
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .notes(UPDATED_NOTES)
            .committeeId(UPDATED_COMMITTEE_ID);

        restCommitteeActivityMockMvc.perform(put("/api/committee-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommitteeActivity)))
            .andExpect(status().isOk());

        // Validate the CommitteeActivity in the database
        List<CommitteeActivity> committeeActivityList = committeeActivityRepository.findAll();
        assertThat(committeeActivityList).hasSize(databaseSizeBeforeUpdate);
        CommitteeActivity testCommitteeActivity = committeeActivityList.get(committeeActivityList.size() - 1);
        assertThat(testCommitteeActivity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommitteeActivity.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCommitteeActivity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCommitteeActivity.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCommitteeActivity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCommitteeActivity.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testCommitteeActivity.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testCommitteeActivity.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCommitteeActivity.getCommitteeId()).isEqualTo(UPDATED_COMMITTEE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingCommitteeActivity() throws Exception {
        int databaseSizeBeforeUpdate = committeeActivityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeActivityMockMvc.perform(put("/api/committee-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeActivity)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeActivity in the database
        List<CommitteeActivity> committeeActivityList = committeeActivityRepository.findAll();
        assertThat(committeeActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommitteeActivity() throws Exception {
        // Initialize the database
        committeeActivityRepository.saveAndFlush(committeeActivity);

        int databaseSizeBeforeDelete = committeeActivityRepository.findAll().size();

        // Delete the committeeActivity
        restCommitteeActivityMockMvc.perform(delete("/api/committee-activities/{id}", committeeActivity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommitteeActivity> committeeActivityList = committeeActivityRepository.findAll();
        assertThat(committeeActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
