package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.RequisitionActivity;
import com.synectiks.procurement.repository.RequisitionActivityRepository;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RequisitionActivityResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RequisitionActivityResourceIT {

    private static final String DEFAULT_REQUISITION_NO = "AAAAAAAAAA";
    private static final String UPDATED_REQUISITION_NO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRESS_STAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROGRESS_STAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FINANCIAL_YEAR = 1;
    private static final Integer UPDATED_FINANCIAL_YEAR = 2;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_PRICE = 1;
    private static final Integer UPDATED_TOTAL_PRICE = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_REQUISITION_ID = 1L;
    private static final Long UPDATED_REQUISITION_ID = 2L;

    @Autowired
    private RequisitionActivityRepository requisitionActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequisitionActivityMockMvc;

    private RequisitionActivity requisitionActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequisitionActivity createEntity(EntityManager em) {
        RequisitionActivity requisitionActivity = new RequisitionActivity()
            .requisitionNo(DEFAULT_REQUISITION_NO)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .status(DEFAULT_STATUS)
            .progressStage(DEFAULT_PROGRESS_STAGE)
            .financialYear(DEFAULT_FINANCIAL_YEAR)
            .type(DEFAULT_TYPE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .notes(DEFAULT_NOTES)
            .dueDate(DEFAULT_DUE_DATE)
            .requisitionId(DEFAULT_REQUISITION_ID);
        return requisitionActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequisitionActivity createUpdatedEntity(EntityManager em) {
        RequisitionActivity requisitionActivity = new RequisitionActivity()
            .requisitionNo(UPDATED_REQUISITION_NO)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .status(UPDATED_STATUS)
            .progressStage(UPDATED_PROGRESS_STAGE)
            .financialYear(UPDATED_FINANCIAL_YEAR)
            .type(UPDATED_TYPE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .notes(UPDATED_NOTES)
            .dueDate(UPDATED_DUE_DATE)
            .requisitionId(UPDATED_REQUISITION_ID);
        return requisitionActivity;
    }

    @BeforeEach
    public void initTest() {
        requisitionActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequisitionActivity() throws Exception {
        int databaseSizeBeforeCreate = requisitionActivityRepository.findAll().size();
        // Create the RequisitionActivity
        restRequisitionActivityMockMvc.perform(post("/api/requisition-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisitionActivity)))
            .andExpect(status().isCreated());

        // Validate the RequisitionActivity in the database
        List<RequisitionActivity> requisitionActivityList = requisitionActivityRepository.findAll();
        assertThat(requisitionActivityList).hasSize(databaseSizeBeforeCreate + 1);
        RequisitionActivity testRequisitionActivity = requisitionActivityList.get(requisitionActivityList.size() - 1);
        assertThat(testRequisitionActivity.getRequisitionNo()).isEqualTo(DEFAULT_REQUISITION_NO);
        assertThat(testRequisitionActivity.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRequisitionActivity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRequisitionActivity.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRequisitionActivity.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testRequisitionActivity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRequisitionActivity.getProgressStage()).isEqualTo(DEFAULT_PROGRESS_STAGE);
        assertThat(testRequisitionActivity.getFinancialYear()).isEqualTo(DEFAULT_FINANCIAL_YEAR);
        assertThat(testRequisitionActivity.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRequisitionActivity.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testRequisitionActivity.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testRequisitionActivity.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testRequisitionActivity.getRequisitionId()).isEqualTo(DEFAULT_REQUISITION_ID);
    }

    @Test
    @Transactional
    public void createRequisitionActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requisitionActivityRepository.findAll().size();

        // Create the RequisitionActivity with an existing ID
        requisitionActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequisitionActivityMockMvc.perform(post("/api/requisition-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisitionActivity)))
            .andExpect(status().isBadRequest());

        // Validate the RequisitionActivity in the database
        List<RequisitionActivity> requisitionActivityList = requisitionActivityRepository.findAll();
        assertThat(requisitionActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRequisitionActivities() throws Exception {
        // Initialize the database
        requisitionActivityRepository.saveAndFlush(requisitionActivity);

        // Get all the requisitionActivityList
        restRequisitionActivityMockMvc.perform(get("/api/requisition-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requisitionActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionNo").value(hasItem(DEFAULT_REQUISITION_NO)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].progressStage").value(hasItem(DEFAULT_PROGRESS_STAGE)))
            .andExpect(jsonPath("$.[*].financialYear").value(hasItem(DEFAULT_FINANCIAL_YEAR)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].requisitionId").value(hasItem(DEFAULT_REQUISITION_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getRequisitionActivity() throws Exception {
        // Initialize the database
        requisitionActivityRepository.saveAndFlush(requisitionActivity);

        // Get the requisitionActivity
        restRequisitionActivityMockMvc.perform(get("/api/requisition-activities/{id}", requisitionActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requisitionActivity.getId().intValue()))
            .andExpect(jsonPath("$.requisitionNo").value(DEFAULT_REQUISITION_NO))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.progressStage").value(DEFAULT_PROGRESS_STAGE))
            .andExpect(jsonPath("$.financialYear").value(DEFAULT_FINANCIAL_YEAR))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.requisitionId").value(DEFAULT_REQUISITION_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingRequisitionActivity() throws Exception {
        // Get the requisitionActivity
        restRequisitionActivityMockMvc.perform(get("/api/requisition-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequisitionActivity() throws Exception {
        // Initialize the database
        requisitionActivityRepository.saveAndFlush(requisitionActivity);

        int databaseSizeBeforeUpdate = requisitionActivityRepository.findAll().size();

        // Update the requisitionActivity
        RequisitionActivity updatedRequisitionActivity = requisitionActivityRepository.findById(requisitionActivity.getId()).get();
        // Disconnect from session so that the updates on updatedRequisitionActivity are not directly saved in db
        em.detach(updatedRequisitionActivity);
        updatedRequisitionActivity
            .requisitionNo(UPDATED_REQUISITION_NO)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .status(UPDATED_STATUS)
            .progressStage(UPDATED_PROGRESS_STAGE)
            .financialYear(UPDATED_FINANCIAL_YEAR)
            .type(UPDATED_TYPE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .notes(UPDATED_NOTES)
            .dueDate(UPDATED_DUE_DATE)
            .requisitionId(UPDATED_REQUISITION_ID);

        restRequisitionActivityMockMvc.perform(put("/api/requisition-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequisitionActivity)))
            .andExpect(status().isOk());

        // Validate the RequisitionActivity in the database
        List<RequisitionActivity> requisitionActivityList = requisitionActivityRepository.findAll();
        assertThat(requisitionActivityList).hasSize(databaseSizeBeforeUpdate);
        RequisitionActivity testRequisitionActivity = requisitionActivityList.get(requisitionActivityList.size() - 1);
        assertThat(testRequisitionActivity.getRequisitionNo()).isEqualTo(UPDATED_REQUISITION_NO);
        assertThat(testRequisitionActivity.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRequisitionActivity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRequisitionActivity.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRequisitionActivity.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testRequisitionActivity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRequisitionActivity.getProgressStage()).isEqualTo(UPDATED_PROGRESS_STAGE);
        assertThat(testRequisitionActivity.getFinancialYear()).isEqualTo(UPDATED_FINANCIAL_YEAR);
        assertThat(testRequisitionActivity.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequisitionActivity.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testRequisitionActivity.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testRequisitionActivity.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testRequisitionActivity.getRequisitionId()).isEqualTo(UPDATED_REQUISITION_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingRequisitionActivity() throws Exception {
        int databaseSizeBeforeUpdate = requisitionActivityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequisitionActivityMockMvc.perform(put("/api/requisition-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisitionActivity)))
            .andExpect(status().isBadRequest());

        // Validate the RequisitionActivity in the database
        List<RequisitionActivity> requisitionActivityList = requisitionActivityRepository.findAll();
        assertThat(requisitionActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequisitionActivity() throws Exception {
        // Initialize the database
        requisitionActivityRepository.saveAndFlush(requisitionActivity);

        int databaseSizeBeforeDelete = requisitionActivityRepository.findAll().size();

        // Delete the requisitionActivity
        restRequisitionActivityMockMvc.perform(delete("/api/requisition-activities/{id}", requisitionActivity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequisitionActivity> requisitionActivityList = requisitionActivityRepository.findAll();
        assertThat(requisitionActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
