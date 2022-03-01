package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.RequisitionLineItemActivity;
import com.synectiks.procurement.repository.RequisitionLineItemActivityRepository;

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
 * Integration tests for the {@link RequisitionLineItemActivityResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RequisitionLineItemActivityResourceIT {

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

    private static final String DEFAULT_ITEM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_QUANTITY = 1;
    private static final Integer UPDATED_ORDER_QUANTITY = 2;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_REQUISITION_LINE_ITEM_ID = 1L;
    private static final Long UPDATED_REQUISITION_LINE_ITEM_ID = 2L;

    @Autowired
    private RequisitionLineItemActivityRepository requisitionLineItemActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequisitionLineItemActivityMockMvc;

    private RequisitionLineItemActivity requisitionLineItemActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequisitionLineItemActivity createEntity(EntityManager em) {
        RequisitionLineItemActivity requisitionLineItemActivity = new RequisitionLineItemActivity()
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .status(DEFAULT_STATUS)
            .progressStage(DEFAULT_PROGRESS_STAGE)
            .itemDescription(DEFAULT_ITEM_DESCRIPTION)
            .orderQuantity(DEFAULT_ORDER_QUANTITY)
            .price(DEFAULT_PRICE)
            .priority(DEFAULT_PRIORITY)
            .notes(DEFAULT_NOTES)
            .dueDate(DEFAULT_DUE_DATE)
            .requisitionLineItemId(DEFAULT_REQUISITION_LINE_ITEM_ID);
        return requisitionLineItemActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequisitionLineItemActivity createUpdatedEntity(EntityManager em) {
        RequisitionLineItemActivity requisitionLineItemActivity = new RequisitionLineItemActivity()
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .status(UPDATED_STATUS)
            .progressStage(UPDATED_PROGRESS_STAGE)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .price(UPDATED_PRICE)
            .priority(UPDATED_PRIORITY)
            .notes(UPDATED_NOTES)
            .dueDate(UPDATED_DUE_DATE)
            .requisitionLineItemId(UPDATED_REQUISITION_LINE_ITEM_ID);
        return requisitionLineItemActivity;
    }

    @BeforeEach
    public void initTest() {
        requisitionLineItemActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequisitionLineItemActivity() throws Exception {
        int databaseSizeBeforeCreate = requisitionLineItemActivityRepository.findAll().size();
        // Create the RequisitionLineItemActivity
        restRequisitionLineItemActivityMockMvc.perform(post("/api/requisition-line-item-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisitionLineItemActivity)))
            .andExpect(status().isCreated());

        // Validate the RequisitionLineItemActivity in the database
        List<RequisitionLineItemActivity> requisitionLineItemActivityList = requisitionLineItemActivityRepository.findAll();
        assertThat(requisitionLineItemActivityList).hasSize(databaseSizeBeforeCreate + 1);
        RequisitionLineItemActivity testRequisitionLineItemActivity = requisitionLineItemActivityList.get(requisitionLineItemActivityList.size() - 1);
        assertThat(testRequisitionLineItemActivity.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRequisitionLineItemActivity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRequisitionLineItemActivity.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRequisitionLineItemActivity.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testRequisitionLineItemActivity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRequisitionLineItemActivity.getProgressStage()).isEqualTo(DEFAULT_PROGRESS_STAGE);
        assertThat(testRequisitionLineItemActivity.getItemDescription()).isEqualTo(DEFAULT_ITEM_DESCRIPTION);
        assertThat(testRequisitionLineItemActivity.getOrderQuantity()).isEqualTo(DEFAULT_ORDER_QUANTITY);
        assertThat(testRequisitionLineItemActivity.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRequisitionLineItemActivity.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testRequisitionLineItemActivity.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testRequisitionLineItemActivity.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testRequisitionLineItemActivity.getRequisitionLineItemId()).isEqualTo(DEFAULT_REQUISITION_LINE_ITEM_ID);
    }

    @Test
    @Transactional
    public void createRequisitionLineItemActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requisitionLineItemActivityRepository.findAll().size();

        // Create the RequisitionLineItemActivity with an existing ID
        requisitionLineItemActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequisitionLineItemActivityMockMvc.perform(post("/api/requisition-line-item-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisitionLineItemActivity)))
            .andExpect(status().isBadRequest());

        // Validate the RequisitionLineItemActivity in the database
        List<RequisitionLineItemActivity> requisitionLineItemActivityList = requisitionLineItemActivityRepository.findAll();
        assertThat(requisitionLineItemActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRequisitionLineItemActivities() throws Exception {
        // Initialize the database
        requisitionLineItemActivityRepository.saveAndFlush(requisitionLineItemActivity);

        // Get all the requisitionLineItemActivityList
        restRequisitionLineItemActivityMockMvc.perform(get("/api/requisition-line-item-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requisitionLineItemActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].progressStage").value(hasItem(DEFAULT_PROGRESS_STAGE)))
            .andExpect(jsonPath("$.[*].itemDescription").value(hasItem(DEFAULT_ITEM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].requisitionLineItemId").value(hasItem(DEFAULT_REQUISITION_LINE_ITEM_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getRequisitionLineItemActivity() throws Exception {
        // Initialize the database
        requisitionLineItemActivityRepository.saveAndFlush(requisitionLineItemActivity);

        // Get the requisitionLineItemActivity
        restRequisitionLineItemActivityMockMvc.perform(get("/api/requisition-line-item-activities/{id}", requisitionLineItemActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requisitionLineItemActivity.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.progressStage").value(DEFAULT_PROGRESS_STAGE))
            .andExpect(jsonPath("$.itemDescription").value(DEFAULT_ITEM_DESCRIPTION))
            .andExpect(jsonPath("$.orderQuantity").value(DEFAULT_ORDER_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.requisitionLineItemId").value(DEFAULT_REQUISITION_LINE_ITEM_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingRequisitionLineItemActivity() throws Exception {
        // Get the requisitionLineItemActivity
        restRequisitionLineItemActivityMockMvc.perform(get("/api/requisition-line-item-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequisitionLineItemActivity() throws Exception {
        // Initialize the database
        requisitionLineItemActivityRepository.saveAndFlush(requisitionLineItemActivity);

        int databaseSizeBeforeUpdate = requisitionLineItemActivityRepository.findAll().size();

        // Update the requisitionLineItemActivity
        RequisitionLineItemActivity updatedRequisitionLineItemActivity = requisitionLineItemActivityRepository.findById(requisitionLineItemActivity.getId()).get();
        // Disconnect from session so that the updates on updatedRequisitionLineItemActivity are not directly saved in db
        em.detach(updatedRequisitionLineItemActivity);
        updatedRequisitionLineItemActivity
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .status(UPDATED_STATUS)
            .progressStage(UPDATED_PROGRESS_STAGE)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .price(UPDATED_PRICE)
            .priority(UPDATED_PRIORITY)
            .notes(UPDATED_NOTES)
            .dueDate(UPDATED_DUE_DATE)
            .requisitionLineItemId(UPDATED_REQUISITION_LINE_ITEM_ID);

        restRequisitionLineItemActivityMockMvc.perform(put("/api/requisition-line-item-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequisitionLineItemActivity)))
            .andExpect(status().isOk());

        // Validate the RequisitionLineItemActivity in the database
        List<RequisitionLineItemActivity> requisitionLineItemActivityList = requisitionLineItemActivityRepository.findAll();
        assertThat(requisitionLineItemActivityList).hasSize(databaseSizeBeforeUpdate);
        RequisitionLineItemActivity testRequisitionLineItemActivity = requisitionLineItemActivityList.get(requisitionLineItemActivityList.size() - 1);
        assertThat(testRequisitionLineItemActivity.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRequisitionLineItemActivity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRequisitionLineItemActivity.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRequisitionLineItemActivity.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testRequisitionLineItemActivity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRequisitionLineItemActivity.getProgressStage()).isEqualTo(UPDATED_PROGRESS_STAGE);
        assertThat(testRequisitionLineItemActivity.getItemDescription()).isEqualTo(UPDATED_ITEM_DESCRIPTION);
        assertThat(testRequisitionLineItemActivity.getOrderQuantity()).isEqualTo(UPDATED_ORDER_QUANTITY);
        assertThat(testRequisitionLineItemActivity.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRequisitionLineItemActivity.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testRequisitionLineItemActivity.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testRequisitionLineItemActivity.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testRequisitionLineItemActivity.getRequisitionLineItemId()).isEqualTo(UPDATED_REQUISITION_LINE_ITEM_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingRequisitionLineItemActivity() throws Exception {
        int databaseSizeBeforeUpdate = requisitionLineItemActivityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequisitionLineItemActivityMockMvc.perform(put("/api/requisition-line-item-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisitionLineItemActivity)))
            .andExpect(status().isBadRequest());

        // Validate the RequisitionLineItemActivity in the database
        List<RequisitionLineItemActivity> requisitionLineItemActivityList = requisitionLineItemActivityRepository.findAll();
        assertThat(requisitionLineItemActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequisitionLineItemActivity() throws Exception {
        // Initialize the database
        requisitionLineItemActivityRepository.saveAndFlush(requisitionLineItemActivity);

        int databaseSizeBeforeDelete = requisitionLineItemActivityRepository.findAll().size();

        // Delete the requisitionLineItemActivity
        restRequisitionLineItemActivityMockMvc.perform(delete("/api/requisition-line-item-activities/{id}", requisitionLineItemActivity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequisitionLineItemActivity> requisitionLineItemActivityList = requisitionLineItemActivityRepository.findAll();
        assertThat(requisitionLineItemActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
