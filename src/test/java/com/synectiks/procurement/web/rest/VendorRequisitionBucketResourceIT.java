package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.VendorRequisitionBucket;
import com.synectiks.procurement.repository.VendorRequisitionBucketRepository;

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
 * Integration tests for the {@link VendorRequisitionBucketResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VendorRequisitionBucketResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_STAGES = "AAAAAAAAAA";
    private static final String UPDATED_STAGES = "BBBBBBBBBB";

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
    private VendorRequisitionBucketRepository vendorRequisitionBucketRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendorRequisitionBucketMockMvc;

    private VendorRequisitionBucket vendorRequisitionBucket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VendorRequisitionBucket createEntity(EntityManager em) {
        VendorRequisitionBucket vendorRequisitionBucket = new VendorRequisitionBucket()
            .status(DEFAULT_STATUS)
            .stages(DEFAULT_STAGES)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .notes(DEFAULT_NOTES);
        return vendorRequisitionBucket;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VendorRequisitionBucket createUpdatedEntity(EntityManager em) {
        VendorRequisitionBucket vendorRequisitionBucket = new VendorRequisitionBucket()
            .status(UPDATED_STATUS)
            .stages(UPDATED_STAGES)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .notes(UPDATED_NOTES);
        return vendorRequisitionBucket;
    }

    @BeforeEach
    public void initTest() {
        vendorRequisitionBucket = createEntity(em);
    }

    @Test
    @Transactional
    public void createVendorRequisitionBucket() throws Exception {
        int databaseSizeBeforeCreate = vendorRequisitionBucketRepository.findAll().size();
        // Create the VendorRequisitionBucket
        restVendorRequisitionBucketMockMvc.perform(post("/api/vendor-requisition-buckets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vendorRequisitionBucket)))
            .andExpect(status().isCreated());

        // Validate the VendorRequisitionBucket in the database
        List<VendorRequisitionBucket> vendorRequisitionBucketList = vendorRequisitionBucketRepository.findAll();
        assertThat(vendorRequisitionBucketList).hasSize(databaseSizeBeforeCreate + 1);
        VendorRequisitionBucket testVendorRequisitionBucket = vendorRequisitionBucketList.get(vendorRequisitionBucketList.size() - 1);
        assertThat(testVendorRequisitionBucket.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVendorRequisitionBucket.getStages()).isEqualTo(DEFAULT_STAGES);
        assertThat(testVendorRequisitionBucket.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testVendorRequisitionBucket.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testVendorRequisitionBucket.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testVendorRequisitionBucket.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testVendorRequisitionBucket.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createVendorRequisitionBucketWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vendorRequisitionBucketRepository.findAll().size();

        // Create the VendorRequisitionBucket with an existing ID
        vendorRequisitionBucket.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendorRequisitionBucketMockMvc.perform(post("/api/vendor-requisition-buckets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vendorRequisitionBucket)))
            .andExpect(status().isBadRequest());

        // Validate the VendorRequisitionBucket in the database
        List<VendorRequisitionBucket> vendorRequisitionBucketList = vendorRequisitionBucketRepository.findAll();
        assertThat(vendorRequisitionBucketList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVendorRequisitionBuckets() throws Exception {
        // Initialize the database
        vendorRequisitionBucketRepository.saveAndFlush(vendorRequisitionBucket);

        // Get all the vendorRequisitionBucketList
        restVendorRequisitionBucketMockMvc.perform(get("/api/vendor-requisition-buckets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendorRequisitionBucket.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].stages").value(hasItem(DEFAULT_STAGES)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }
    
    @Test
    @Transactional
    public void getVendorRequisitionBucket() throws Exception {
        // Initialize the database
        vendorRequisitionBucketRepository.saveAndFlush(vendorRequisitionBucket);

        // Get the vendorRequisitionBucket
        restVendorRequisitionBucketMockMvc.perform(get("/api/vendor-requisition-buckets/{id}", vendorRequisitionBucket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendorRequisitionBucket.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.stages").value(DEFAULT_STAGES))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }
    @Test
    @Transactional
    public void getNonExistingVendorRequisitionBucket() throws Exception {
        // Get the vendorRequisitionBucket
        restVendorRequisitionBucketMockMvc.perform(get("/api/vendor-requisition-buckets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVendorRequisitionBucket() throws Exception {
        // Initialize the database
        vendorRequisitionBucketRepository.saveAndFlush(vendorRequisitionBucket);

        int databaseSizeBeforeUpdate = vendorRequisitionBucketRepository.findAll().size();

        // Update the vendorRequisitionBucket
        VendorRequisitionBucket updatedVendorRequisitionBucket = vendorRequisitionBucketRepository.findById(vendorRequisitionBucket.getId()).get();
        // Disconnect from session so that the updates on updatedVendorRequisitionBucket are not directly saved in db
        em.detach(updatedVendorRequisitionBucket);
        updatedVendorRequisitionBucket
            .status(UPDATED_STATUS)
            .stages(UPDATED_STAGES)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .notes(UPDATED_NOTES);

        restVendorRequisitionBucketMockMvc.perform(put("/api/vendor-requisition-buckets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVendorRequisitionBucket)))
            .andExpect(status().isOk());

        // Validate the VendorRequisitionBucket in the database
        List<VendorRequisitionBucket> vendorRequisitionBucketList = vendorRequisitionBucketRepository.findAll();
        assertThat(vendorRequisitionBucketList).hasSize(databaseSizeBeforeUpdate);
        VendorRequisitionBucket testVendorRequisitionBucket = vendorRequisitionBucketList.get(vendorRequisitionBucketList.size() - 1);
        assertThat(testVendorRequisitionBucket.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVendorRequisitionBucket.getStages()).isEqualTo(UPDATED_STAGES);
        assertThat(testVendorRequisitionBucket.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testVendorRequisitionBucket.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVendorRequisitionBucket.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testVendorRequisitionBucket.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testVendorRequisitionBucket.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingVendorRequisitionBucket() throws Exception {
        int databaseSizeBeforeUpdate = vendorRequisitionBucketRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendorRequisitionBucketMockMvc.perform(put("/api/vendor-requisition-buckets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vendorRequisitionBucket)))
            .andExpect(status().isBadRequest());

        // Validate the VendorRequisitionBucket in the database
        List<VendorRequisitionBucket> vendorRequisitionBucketList = vendorRequisitionBucketRepository.findAll();
        assertThat(vendorRequisitionBucketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVendorRequisitionBucket() throws Exception {
        // Initialize the database
        vendorRequisitionBucketRepository.saveAndFlush(vendorRequisitionBucket);

        int databaseSizeBeforeDelete = vendorRequisitionBucketRepository.findAll().size();

        // Delete the vendorRequisitionBucket
        restVendorRequisitionBucketMockMvc.perform(delete("/api/vendor-requisition-buckets/{id}", vendorRequisitionBucket.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VendorRequisitionBucket> vendorRequisitionBucketList = vendorRequisitionBucketRepository.findAll();
        assertThat(vendorRequisitionBucketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
