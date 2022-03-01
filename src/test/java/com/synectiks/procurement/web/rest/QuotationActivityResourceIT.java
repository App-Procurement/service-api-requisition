package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.QuotationActivity;
import com.synectiks.procurement.repository.QuotationActivityRepository;

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
 * Integration tests for the {@link QuotationActivityResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class QuotationActivityResourceIT {

    private static final String DEFAULT_QUOTATION_NO = "AAAAAAAAAA";
    private static final String UPDATED_QUOTATION_NO = "BBBBBBBBBB";

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

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Long DEFAULT_QUOTATION_ID = 1L;
    private static final Long UPDATED_QUOTATION_ID = 2L;

    @Autowired
    private QuotationActivityRepository quotationActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuotationActivityMockMvc;

    private QuotationActivity quotationActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotationActivity createEntity(EntityManager em) {
        QuotationActivity quotationActivity = new QuotationActivity()
            .quotationNo(DEFAULT_QUOTATION_NO)
            .status(DEFAULT_STATUS)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .dueDate(DEFAULT_DUE_DATE)
            .notes(DEFAULT_NOTES)
            .quotationId(DEFAULT_QUOTATION_ID);
        return quotationActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotationActivity createUpdatedEntity(EntityManager em) {
        QuotationActivity quotationActivity = new QuotationActivity()
            .quotationNo(UPDATED_QUOTATION_NO)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .dueDate(UPDATED_DUE_DATE)
            .notes(UPDATED_NOTES)
            .quotationId(UPDATED_QUOTATION_ID);
        return quotationActivity;
    }

    @BeforeEach
    public void initTest() {
        quotationActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuotationActivity() throws Exception {
        int databaseSizeBeforeCreate = quotationActivityRepository.findAll().size();
        // Create the QuotationActivity
        restQuotationActivityMockMvc.perform(post("/api/quotation-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quotationActivity)))
            .andExpect(status().isCreated());

        // Validate the QuotationActivity in the database
        List<QuotationActivity> quotationActivityList = quotationActivityRepository.findAll();
        assertThat(quotationActivityList).hasSize(databaseSizeBeforeCreate + 1);
        QuotationActivity testQuotationActivity = quotationActivityList.get(quotationActivityList.size() - 1);
        assertThat(testQuotationActivity.getQuotationNo()).isEqualTo(DEFAULT_QUOTATION_NO);
        assertThat(testQuotationActivity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQuotationActivity.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testQuotationActivity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testQuotationActivity.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testQuotationActivity.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testQuotationActivity.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testQuotationActivity.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testQuotationActivity.getQuotationId()).isEqualTo(DEFAULT_QUOTATION_ID);
    }

    @Test
    @Transactional
    public void createQuotationActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quotationActivityRepository.findAll().size();

        // Create the QuotationActivity with an existing ID
        quotationActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationActivityMockMvc.perform(post("/api/quotation-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quotationActivity)))
            .andExpect(status().isBadRequest());

        // Validate the QuotationActivity in the database
        List<QuotationActivity> quotationActivityList = quotationActivityRepository.findAll();
        assertThat(quotationActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllQuotationActivities() throws Exception {
        // Initialize the database
        quotationActivityRepository.saveAndFlush(quotationActivity);

        // Get all the quotationActivityList
        restQuotationActivityMockMvc.perform(get("/api/quotation-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].quotationNo").value(hasItem(DEFAULT_QUOTATION_NO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].quotationId").value(hasItem(DEFAULT_QUOTATION_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getQuotationActivity() throws Exception {
        // Initialize the database
        quotationActivityRepository.saveAndFlush(quotationActivity);

        // Get the quotationActivity
        restQuotationActivityMockMvc.perform(get("/api/quotation-activities/{id}", quotationActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quotationActivity.getId().intValue()))
            .andExpect(jsonPath("$.quotationNo").value(DEFAULT_QUOTATION_NO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.quotationId").value(DEFAULT_QUOTATION_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingQuotationActivity() throws Exception {
        // Get the quotationActivity
        restQuotationActivityMockMvc.perform(get("/api/quotation-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuotationActivity() throws Exception {
        // Initialize the database
        quotationActivityRepository.saveAndFlush(quotationActivity);

        int databaseSizeBeforeUpdate = quotationActivityRepository.findAll().size();

        // Update the quotationActivity
        QuotationActivity updatedQuotationActivity = quotationActivityRepository.findById(quotationActivity.getId()).get();
        // Disconnect from session so that the updates on updatedQuotationActivity are not directly saved in db
        em.detach(updatedQuotationActivity);
        updatedQuotationActivity
            .quotationNo(UPDATED_QUOTATION_NO)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .dueDate(UPDATED_DUE_DATE)
            .notes(UPDATED_NOTES)
            .quotationId(UPDATED_QUOTATION_ID);

        restQuotationActivityMockMvc.perform(put("/api/quotation-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuotationActivity)))
            .andExpect(status().isOk());

        // Validate the QuotationActivity in the database
        List<QuotationActivity> quotationActivityList = quotationActivityRepository.findAll();
        assertThat(quotationActivityList).hasSize(databaseSizeBeforeUpdate);
        QuotationActivity testQuotationActivity = quotationActivityList.get(quotationActivityList.size() - 1);
        assertThat(testQuotationActivity.getQuotationNo()).isEqualTo(UPDATED_QUOTATION_NO);
        assertThat(testQuotationActivity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuotationActivity.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testQuotationActivity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testQuotationActivity.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testQuotationActivity.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testQuotationActivity.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testQuotationActivity.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testQuotationActivity.getQuotationId()).isEqualTo(UPDATED_QUOTATION_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingQuotationActivity() throws Exception {
        int databaseSizeBeforeUpdate = quotationActivityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotationActivityMockMvc.perform(put("/api/quotation-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(quotationActivity)))
            .andExpect(status().isBadRequest());

        // Validate the QuotationActivity in the database
        List<QuotationActivity> quotationActivityList = quotationActivityRepository.findAll();
        assertThat(quotationActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuotationActivity() throws Exception {
        // Initialize the database
        quotationActivityRepository.saveAndFlush(quotationActivity);

        int databaseSizeBeforeDelete = quotationActivityRepository.findAll().size();

        // Delete the quotationActivity
        restQuotationActivityMockMvc.perform(delete("/api/quotation-activities/{id}", quotationActivity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuotationActivity> quotationActivityList = quotationActivityRepository.findAll();
        assertThat(quotationActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
