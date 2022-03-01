package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.RolesGroup;
import com.synectiks.procurement.repository.RolesGroupRepository;

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
 * Integration tests for the {@link RolesGroupResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RolesGroupResourceIT {

    private static final Boolean DEFAULT_GROUP = false;
    private static final Boolean UPDATED_GROUP = true;

    @Autowired
    private RolesGroupRepository rolesGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRolesGroupMockMvc;

    private RolesGroup rolesGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolesGroup createEntity(EntityManager em) {
        RolesGroup rolesGroup = new RolesGroup()
            .group(DEFAULT_GROUP);
        return rolesGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RolesGroup createUpdatedEntity(EntityManager em) {
        RolesGroup rolesGroup = new RolesGroup()
            .group(UPDATED_GROUP);
        return rolesGroup;
    }

    @BeforeEach
    public void initTest() {
        rolesGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createRolesGroup() throws Exception {
        int databaseSizeBeforeCreate = rolesGroupRepository.findAll().size();
        // Create the RolesGroup
        restRolesGroupMockMvc.perform(post("/api/roles-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rolesGroup)))
            .andExpect(status().isCreated());

        // Validate the RolesGroup in the database
        List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
        assertThat(rolesGroupList).hasSize(databaseSizeBeforeCreate + 1);
        RolesGroup testRolesGroup = rolesGroupList.get(rolesGroupList.size() - 1);
        assertThat(testRolesGroup.getGroup()).isEqualTo(DEFAULT_GROUP);
    }

    @Test
    @Transactional
    public void createRolesGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rolesGroupRepository.findAll().size();

        // Create the RolesGroup with an existing ID
        rolesGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRolesGroupMockMvc.perform(post("/api/roles-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rolesGroup)))
            .andExpect(status().isBadRequest());

        // Validate the RolesGroup in the database
        List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
        assertThat(rolesGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRolesGroups() throws Exception {
        // Initialize the database
        rolesGroupRepository.saveAndFlush(rolesGroup);

        // Get all the rolesGroupList
        restRolesGroupMockMvc.perform(get("/api/roles-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rolesGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getRolesGroup() throws Exception {
        // Initialize the database
        rolesGroupRepository.saveAndFlush(rolesGroup);

        // Get the rolesGroup
        restRolesGroupMockMvc.perform(get("/api/roles-groups/{id}", rolesGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rolesGroup.getId().intValue()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingRolesGroup() throws Exception {
        // Get the rolesGroup
        restRolesGroupMockMvc.perform(get("/api/roles-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRolesGroup() throws Exception {
        // Initialize the database
        rolesGroupRepository.saveAndFlush(rolesGroup);

        int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();

        // Update the rolesGroup
        RolesGroup updatedRolesGroup = rolesGroupRepository.findById(rolesGroup.getId()).get();
        // Disconnect from session so that the updates on updatedRolesGroup are not directly saved in db
        em.detach(updatedRolesGroup);
        updatedRolesGroup
            .group(UPDATED_GROUP);

        restRolesGroupMockMvc.perform(put("/api/roles-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRolesGroup)))
            .andExpect(status().isOk());

        // Validate the RolesGroup in the database
        List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
        assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
        RolesGroup testRolesGroup = rolesGroupList.get(rolesGroupList.size() - 1);
        assertThat(testRolesGroup.getGroup()).isEqualTo(UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void updateNonExistingRolesGroup() throws Exception {
        int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolesGroupMockMvc.perform(put("/api/roles-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rolesGroup)))
            .andExpect(status().isBadRequest());

        // Validate the RolesGroup in the database
        List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
        assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRolesGroup() throws Exception {
        // Initialize the database
        rolesGroupRepository.saveAndFlush(rolesGroup);

        int databaseSizeBeforeDelete = rolesGroupRepository.findAll().size();

        // Delete the rolesGroup
        restRolesGroupMockMvc.perform(delete("/api/roles-groups/{id}", rolesGroup.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
        assertThat(rolesGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
