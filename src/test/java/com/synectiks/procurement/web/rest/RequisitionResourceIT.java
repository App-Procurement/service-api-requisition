package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.repository.RequisitionRepository;

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
 * Integration tests for the {@link RequisitionResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RequisitionResourceIT {

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

    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequisitionMockMvc;

    private Requisition requisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requisition createEntity(EntityManager em) {
        Requisition requisition = new Requisition()
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
            .dueDate(DEFAULT_DUE_DATE);
        return requisition;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requisition createUpdatedEntity(EntityManager em) {
        Requisition requisition = new Requisition()
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
            .dueDate(UPDATED_DUE_DATE);
        return requisition;
    }

    @BeforeEach
    public void initTest() {
        requisition = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequisition() throws Exception {
        int databaseSizeBeforeCreate = requisitionRepository.findAll().size();
        // Create the Requisition
        restRequisitionMockMvc.perform(post("/api/requisitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisition)))
            .andExpect(status().isCreated());

        // Validate the Requisition in the database
        List<Requisition> requisitionList = requisitionRepository.findAll();
        assertThat(requisitionList).hasSize(databaseSizeBeforeCreate + 1);
        Requisition testRequisition = requisitionList.get(requisitionList.size() - 1);
        assertThat(testRequisition.getRequisitionNo()).isEqualTo(DEFAULT_REQUISITION_NO);
        assertThat(testRequisition.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRequisition.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRequisition.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRequisition.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testRequisition.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRequisition.getProgressStage()).isEqualTo(DEFAULT_PROGRESS_STAGE);
        assertThat(testRequisition.getFinancialYear()).isEqualTo(DEFAULT_FINANCIAL_YEAR);
        assertThat(testRequisition.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRequisition.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testRequisition.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testRequisition.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    public void createRequisitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requisitionRepository.findAll().size();

        // Create the Requisition with an existing ID
        requisition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequisitionMockMvc.perform(post("/api/requisitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisition)))
            .andExpect(status().isBadRequest());

        // Validate the Requisition in the database
        List<Requisition> requisitionList = requisitionRepository.findAll();
        assertThat(requisitionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRequisitions() throws Exception {
        // Initialize the database
        requisitionRepository.saveAndFlush(requisition);

        // Get all the requisitionList
        restRequisitionMockMvc.perform(get("/api/requisitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requisition.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRequisition() throws Exception {
        // Initialize the database
        requisitionRepository.saveAndFlush(requisition);

        // Get the requisition
        restRequisitionMockMvc.perform(get("/api/requisitions/{id}", requisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requisition.getId().intValue()))
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
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRequisition() throws Exception {
        // Get the requisition
        restRequisitionMockMvc.perform(get("/api/requisitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequisition() throws Exception {
        // Initialize the database
        requisitionRepository.saveAndFlush(requisition);

        int databaseSizeBeforeUpdate = requisitionRepository.findAll().size();

        // Update the requisition
        Requisition updatedRequisition = requisitionRepository.findById(requisition.getId()).get();
        // Disconnect from session so that the updates on updatedRequisition are not directly saved in db
        em.detach(updatedRequisition);
        updatedRequisition
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
            .dueDate(UPDATED_DUE_DATE);

        restRequisitionMockMvc.perform(put("/api/requisitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequisition)))
            .andExpect(status().isOk());

        // Validate the Requisition in the database
        List<Requisition> requisitionList = requisitionRepository.findAll();
        assertThat(requisitionList).hasSize(databaseSizeBeforeUpdate);
        Requisition testRequisition = requisitionList.get(requisitionList.size() - 1);
        assertThat(testRequisition.getRequisitionNo()).isEqualTo(UPDATED_REQUISITION_NO);
        assertThat(testRequisition.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRequisition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRequisition.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRequisition.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testRequisition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRequisition.getProgressStage()).isEqualTo(UPDATED_PROGRESS_STAGE);
        assertThat(testRequisition.getFinancialYear()).isEqualTo(UPDATED_FINANCIAL_YEAR);
        assertThat(testRequisition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequisition.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testRequisition.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testRequisition.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRequisition() throws Exception {
        int databaseSizeBeforeUpdate = requisitionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequisitionMockMvc.perform(put("/api/requisitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisition)))
            .andExpect(status().isBadRequest());

        // Validate the Requisition in the database
        List<Requisition> requisitionList = requisitionRepository.findAll();
        assertThat(requisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequisition() throws Exception {
        // Initialize the database
        requisitionRepository.saveAndFlush(requisition);

        int databaseSizeBeforeDelete = requisitionRepository.findAll().size();

        // Delete the requisition
        restRequisitionMockMvc.perform(delete("/api/requisitions/{id}", requisition.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Requisition> requisitionList = requisitionRepository.findAll();
        assertThat(requisitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
