package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.CommitteeAndMemberLink;
import com.synectiks.procurement.repository.CommitteeAndMemberLinkRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CommitteeAndMemberLinkResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CommitteeAndMemberLinkResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private CommitteeAndMemberLinkRepository committeeAndMemberLinkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommitteeAndMemberLinkMockMvc;

    private CommitteeAndMemberLink committeeAndMemberLink;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeAndMemberLink createEntity(EntityManager em) {
        CommitteeAndMemberLink committeeAndMemberLink = new CommitteeAndMemberLink();
        committeeAndMemberLink.setStatus(DEFAULT_STATUS);
        return committeeAndMemberLink;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeAndMemberLink createUpdatedEntity(EntityManager em) {
        CommitteeAndMemberLink committeeAndMemberLink = new CommitteeAndMemberLink();
        committeeAndMemberLink.setStatus(UPDATED_STATUS);
        return committeeAndMemberLink;
    }

    @BeforeEach
    public void initTest() {
        committeeAndMemberLink = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommitteeAndMemberLink() throws Exception {
        int databaseSizeBeforeCreate = committeeAndMemberLinkRepository.findAll().size();
        // Create the CommitteeAndMemberLink
        restCommitteeAndMemberLinkMockMvc.perform(post("/api/committee-and-member-links")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeAndMemberLink)))
            .andExpect(status().isCreated());

        // Validate the CommitteeAndMemberLink in the database
        List<CommitteeAndMemberLink> committeeAndMemberLinkList = committeeAndMemberLinkRepository.findAll();
        assertThat(committeeAndMemberLinkList).hasSize(databaseSizeBeforeCreate + 1);
        CommitteeAndMemberLink testCommitteeAndMemberLink = committeeAndMemberLinkList.get(committeeAndMemberLinkList.size() - 1);
        assertThat(testCommitteeAndMemberLink.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCommitteeAndMemberLinkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = committeeAndMemberLinkRepository.findAll().size();

        // Create the CommitteeAndMemberLink with an existing ID
        committeeAndMemberLink.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitteeAndMemberLinkMockMvc.perform(post("/api/committee-and-member-links")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeAndMemberLink)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeAndMemberLink in the database
        List<CommitteeAndMemberLink> committeeAndMemberLinkList = committeeAndMemberLinkRepository.findAll();
        assertThat(committeeAndMemberLinkList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCommitteeAndMemberLinks() throws Exception {
        // Initialize the database
        committeeAndMemberLinkRepository.saveAndFlush(committeeAndMemberLink);

        // Get all the committeeAndMemberLinkList
        restCommitteeAndMemberLinkMockMvc.perform(get("/api/committee-and-member-links?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committeeAndMemberLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getCommitteeAndMemberLink() throws Exception {
        // Initialize the database
        committeeAndMemberLinkRepository.saveAndFlush(committeeAndMemberLink);

        // Get the committeeAndMemberLink
        restCommitteeAndMemberLinkMockMvc.perform(get("/api/committee-and-member-links/{id}", committeeAndMemberLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(committeeAndMemberLink.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }
    @Test
    @Transactional
    public void getNonExistingCommitteeAndMemberLink() throws Exception {
        // Get the committeeAndMemberLink
        restCommitteeAndMemberLinkMockMvc.perform(get("/api/committee-and-member-links/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommitteeAndMemberLink() throws Exception {
        // Initialize the database
        committeeAndMemberLinkRepository.saveAndFlush(committeeAndMemberLink);

        int databaseSizeBeforeUpdate = committeeAndMemberLinkRepository.findAll().size();

        // Update the committeeAndMemberLink
        CommitteeAndMemberLink updatedCommitteeAndMemberLink = committeeAndMemberLinkRepository.findById(committeeAndMemberLink.getId()).get();
        // Disconnect from session so that the updates on updatedCommitteeAndMemberLink are not directly saved in db
        em.detach(updatedCommitteeAndMemberLink);
        updatedCommitteeAndMemberLink
            .setStatus(UPDATED_STATUS);

        restCommitteeAndMemberLinkMockMvc.perform(put("/api/committee-and-member-links")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommitteeAndMemberLink)))
            .andExpect(status().isOk());

        // Validate the CommitteeAndMemberLink in the database
        List<CommitteeAndMemberLink> committeeAndMemberLinkList = committeeAndMemberLinkRepository.findAll();
        assertThat(committeeAndMemberLinkList).hasSize(databaseSizeBeforeUpdate);
        CommitteeAndMemberLink testCommitteeAndMemberLink = committeeAndMemberLinkList.get(committeeAndMemberLinkList.size() - 1);
        assertThat(testCommitteeAndMemberLink.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCommitteeAndMemberLink() throws Exception {
        int databaseSizeBeforeUpdate = committeeAndMemberLinkRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeAndMemberLinkMockMvc.perform(put("/api/committee-and-member-links")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeAndMemberLink)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeAndMemberLink in the database
        List<CommitteeAndMemberLink> committeeAndMemberLinkList = committeeAndMemberLinkRepository.findAll();
        assertThat(committeeAndMemberLinkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommitteeAndMemberLink() throws Exception {
        // Initialize the database
        committeeAndMemberLinkRepository.saveAndFlush(committeeAndMemberLink);

        int databaseSizeBeforeDelete = committeeAndMemberLinkRepository.findAll().size();

        // Delete the committeeAndMemberLink
        restCommitteeAndMemberLinkMockMvc.perform(delete("/api/committee-and-member-links/{id}", committeeAndMemberLink.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommitteeAndMemberLink> committeeAndMemberLinkList = committeeAndMemberLinkRepository.findAll();
        assertThat(committeeAndMemberLinkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
