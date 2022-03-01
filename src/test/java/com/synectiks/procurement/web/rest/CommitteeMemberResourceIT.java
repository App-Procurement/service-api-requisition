package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.CommitteeMember;
import com.synectiks.procurement.repository.CommitteeMemberRepository;

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
 * Integration tests for the {@link CommitteeMemberResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CommitteeMemberResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private CommitteeMemberRepository committeeMemberRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommitteeMemberMockMvc;

    private CommitteeMember committeeMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeMember createEntity(EntityManager em) {
        CommitteeMember committeeMember = new CommitteeMember();
        return committeeMember;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeMember createUpdatedEntity(EntityManager em) {
        CommitteeMember committeeMember = new CommitteeMember();
        return committeeMember;
    }

    @BeforeEach
    public void initTest() {
        committeeMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommitteeMember() throws Exception {
        int databaseSizeBeforeCreate = committeeMemberRepository.findAll().size();
        // Create the CommitteeMember
        restCommitteeMemberMockMvc.perform(post("/api/committee-members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeMember)))
            .andExpect(status().isCreated());

        // Validate the CommitteeMember in the database
        List<CommitteeMember> committeeMemberList = committeeMemberRepository.findAll();
        assertThat(committeeMemberList).hasSize(databaseSizeBeforeCreate + 1);
        CommitteeMember testCommitteeMember = committeeMemberList.get(committeeMemberList.size() - 1);
        assertThat(testCommitteeMember.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommitteeMember.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCommitteeMember.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testCommitteeMember.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCommitteeMember.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCommitteeMember.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testCommitteeMember.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testCommitteeMember.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCommitteeMember.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testCommitteeMember.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createCommitteeMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = committeeMemberRepository.findAll().size();

        // Create the CommitteeMember with an existing ID
        committeeMember.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitteeMemberMockMvc.perform(post("/api/committee-members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeMember)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeMember in the database
        List<CommitteeMember> committeeMemberList = committeeMemberRepository.findAll();
        assertThat(committeeMemberList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCommitteeMembers() throws Exception {
        // Initialize the database
        committeeMemberRepository.saveAndFlush(committeeMember);

        // Get all the committeeMemberList
        restCommitteeMemberMockMvc.perform(get("/api/committee-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committeeMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }
    
    @Test
    @Transactional
    public void getCommitteeMember() throws Exception {
        // Initialize the database
        committeeMemberRepository.saveAndFlush(committeeMember);

        // Get the committeeMember
        restCommitteeMemberMockMvc.perform(get("/api/committee-members/{id}", committeeMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(committeeMember.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingCommitteeMember() throws Exception {
        // Get the committeeMember
        restCommitteeMemberMockMvc.perform(get("/api/committee-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommitteeMember() throws Exception {
        // Initialize the database
        committeeMemberRepository.saveAndFlush(committeeMember);

        int databaseSizeBeforeUpdate = committeeMemberRepository.findAll().size();

        // Update the committeeMember
        CommitteeMember updatedCommitteeMember = committeeMemberRepository.findById(committeeMember.getId()).get();
        // Disconnect from session so that the updates on updatedCommitteeMember are not directly saved in db
        em.detach(updatedCommitteeMember);
       

        restCommitteeMemberMockMvc.perform(put("/api/committee-members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommitteeMember)))
            .andExpect(status().isOk());

        // Validate the CommitteeMember in the database
        List<CommitteeMember> committeeMemberList = committeeMemberRepository.findAll();
        assertThat(committeeMemberList).hasSize(databaseSizeBeforeUpdate);
        CommitteeMember testCommitteeMember = committeeMemberList.get(committeeMemberList.size() - 1);
        assertThat(testCommitteeMember.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommitteeMember.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCommitteeMember.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testCommitteeMember.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCommitteeMember.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCommitteeMember.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testCommitteeMember.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCommitteeMember.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCommitteeMember.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testCommitteeMember.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingCommitteeMember() throws Exception {
        int databaseSizeBeforeUpdate = committeeMemberRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeMemberMockMvc.perform(put("/api/committee-members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeMember)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeMember in the database
        List<CommitteeMember> committeeMemberList = committeeMemberRepository.findAll();
        assertThat(committeeMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommitteeMember() throws Exception {
        // Initialize the database
        committeeMemberRepository.saveAndFlush(committeeMember);

        int databaseSizeBeforeDelete = committeeMemberRepository.findAll().size();

        // Delete the committeeMember
        restCommitteeMemberMockMvc.perform(delete("/api/committee-members/{id}", committeeMember.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommitteeMember> committeeMemberList = committeeMemberRepository.findAll();
        assertThat(committeeMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
